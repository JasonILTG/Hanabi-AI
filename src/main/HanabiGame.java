package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.MoveType;
import main.enums.Number;
import main.moves.ClueMove;
import main.moves.DiscardMove;
import main.moves.Move;
import main.moves.PlayMove;
import main.parts.Card;
import main.parts.Deck;
import main.parts.Hand;
import main.players.Player;
import main.players.jason.JasonHanabiAI;

/**
 * Class for all of the game logic.
 */
public class HanabiGame {
	public static final Scanner in = new Scanner(System.in);
	
	/** Hand sizes for different numbers of players */
	public static final int[] HAND_SIZES = { 0, 0, 7, 5, 4, 4 };
	
	public final GameMode mode;
	private final boolean logging;
	
	private int clues;
	private int lives;
	
	private Deck deck;
	private HashMap<Color, Integer> fireworks;
	private ArrayList<Card> discard;

	private Player[] players;
	private Hand[] hands;
	
	private int currPlayer;
	
	/**
	 * Constructor.
	 * 
	 * @param mode The game mode
	 * @param players The array of players
	 * @param logging Whether to log the game states and moves
	 */
	public HanabiGame(GameMode mode, Player[] players, boolean logging) {
		this.mode = mode;
		this.players = players;
		this.logging = logging;
		
		// Initialize deck
		if (mode == GameMode.NORMAL) {
			deck = new Deck();
		} else {
			// Include extra color if appropriate
			deck = new Deck(mode.extraColor, mode.hard);
		}
		deck.shuffle();
		
		setup();
	}
	
	/**
	 * Resets the game.
	 */
	public void reset() {
		deck.reset();
		
		setup();
	}
	
	/**
	 * Sets up a new game.
	 */
	private void setup() {
		// Set clues and lives
		clues = 8;
		lives = 3;
		
		// Initialize fireworks
		fireworks = new HashMap<Color, Integer>();
		for (Color color : mode.colors) {
			fireworks.put(color, 0);
		}
		
		// Initialize discard
		discard = new ArrayList<Card>();
		
		// Initialize hands
		int numPlayers = players.length;
		hands = new Hand[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			hands[i] = new Hand();
		}
		
		for (int i = 0; i < HAND_SIZES[numPlayers]; i++) {
			for (int j = 0; j < numPlayers; j++) {
				hands[j].draw(deck.draw());
			}
		}
		
		// Randomize who gets to go first and reset the last player to -1
		currPlayer = (new Random()).nextInt(numPlayers);
		
		// Initialize players
		for (int i = 0; i < players.length; i++) {
			Hand[] otherHands = new Hand[players.length - 1];
			for (int j = 1; j < players.length; j++) {
				otherHands[j - 1] = hands[(i + j) % players.length];
			}
			
			players[i].init(otherHands, mode);
		}
	}

	/**
	 * Starts the game.
	 * 
	 * @return The score at the end of the game
	 */
	public int start() {
		message("GAME START: " + players[currPlayer] + " to move.");
		
		// Keep playing while the deck still has cards
		while (!deck.isEmpty()) {
			if (turn()) {
				return gameOver();
			}
		}
		
		// Everyone gets one last turn after the deck runs out
		for (int i = 0; i < players.length; i++) {
			if (turn()) {
				return gameOver();
			}
		}
		
		return gameOver();
	}
	
	/**
	 * Plays out one turn.
	 * 
	 * @return Whether the game is over
	 */
	private boolean turn() {
		if (logging) stateLog();
		
		// Keep querying the player for a move as long as the player does not give a valid one
		while (!interpretMove(players[currPlayer].move()));
		
		// The game is over if there are no lives left
		if (lives == 0) return true;
		
		// Go to the next player
		currPlayer = (currPlayer + 1) % players.length;
		return false;
	}
	
	/**
	 * Logs the game state.
	 */
	private void stateLog() {
		println("--------------------------------------------------------------");
		println("Clues: " + clues + " | Lives: " + lives);
		println();
		
		println("Fireworks: ");
		StringBuilder f = new StringBuilder();
		for (Entry<Color, Integer> e : fireworks.entrySet()) {
			f.append(e.getKey().ansi() + e.getValue());
		}
		println(f.toString());
		println();
		
		for (int i = 0; i < players.length; i++) {
			println(players[i] + "'s hand: " + hands[i]);
		}
		println("--------------------------------------------------------------");
		println();
	}
	
	/**
	 * Interprets and attempts to play out the given move.
	 * 
	 * @param move The move made by the current player
	 * @return Whether the move was valid
	 */
	private boolean interpretMove(Move move) {
		for (int i = 0; i < players.length; i++) {
			if (!players[i].check(hands, i, fireworks)) {
				int j = -1;
				players[j] = null;
			}
		}
		
		if (move.type == MoveType.PLAY) {
			// Play move
			return play(((PlayMove) move).pos);
		} else if (move.type == MoveType.DISCARD) {
			// Discard move
			return discard(((DiscardMove) move).pos);
		} else {
			ClueMove cMove = (ClueMove) move;
			
			if (cMove.number == null) {
				// Color clue
				return clue(unshift(cMove.player, currPlayer), cMove.color);
			} else {
				// Number clue
				return clue(unshift(cMove.player, currPlayer), cMove.number);
			}
		}
	}
	
	/**
	 * Attempts to play the current player's card in the given position.
	 * 
	 * @param pos The position to play from
	 * @return Whether the play is a valid move
	 */
	private boolean play(int pos) {
		// Check if the move is valid
		if (pos >= hands[currPlayer].size()) {
			message(currPlayer, "Invalid card.");
			return false;
		}
		
		String message, altMessage = "";
		
		Card c = hands[currPlayer].take(pos);
		
		if (fireworks.get(c.color) != c.number.ordinal()) {
			// If the play is incorrect, remove a life and discard the card
			lives--;
			discard.add(c);
		} else {
			// Otherwise, play the card
			fireworks.put(c.color, c.number.ordinal() + 1);
			
			// Fives give back a clue
			if (c.number == Number.FIVE) clues++;
		}
		
		Card d = null;
		if (deck.isEmpty()) {
			// If the deck is empty, do not draw
			message = players[currPlayer] + " has played a " + c + ".";
			message = "You have played a " + c + ".";
		} else {
			// Otherwise, draw
			d = deck.draw();
			hands[currPlayer].draw(d);
			
			message = players[currPlayer] + " has played a " + c + " and drawn a " + d + ".";
			altMessage = "You have played a " + c + " and drawn a card.";
		}
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			if (i != currPlayer) {
				players[i].play(shift(currPlayer, i), pos);
				if (d != null) players[i].draw(shift(currPlayer, i), d);
			} else {
				players[i].play(pos, c);
				if (d != null) players[i].draw();
			}
		}
		
		// Message all players
		message(message, currPlayer, altMessage);
		return true;
	}
	
	/**
	 * Attempts to discard the current player's card in the given position.
	 * 
	 * @param pos The position to discard from
	 * @return Whether the discard is a valid move
	 */
	private boolean discard(int pos) {
		// Check whether the move is valid
		if (pos >= hands[currPlayer].size()) {
			message(currPlayer, "Invalid card.");
			return false;
		}
		
		String message, altMessage = "";
		
		// Discard the card and give back a clue
		Card c = hands[currPlayer].take(pos);
		discard.add(c);
		clues++;
		
		Card d = null;
		if (deck.isEmpty()) {
			// If the deck is empty, do not draw
			message = players[currPlayer] + " has discarded a " + c + ".";
			message = "You have discarded a " + c + ".";
		} else {
			// Otherwise, draw
			d = deck.draw();
			hands[currPlayer].draw(d);
			
			message = players[currPlayer] + " has discarded a " + c + " and drawn a " + d + ".";
			altMessage = "You have discarded a " + c + " and drawn a card.";
		}
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			if (i != currPlayer) {
				players[i].discard(shift(currPlayer, i), pos);
				if (d != null) players[i].draw(shift(currPlayer, i), d);
			} else {
				players[i].discard(pos, c);
				if (d != null) players[i].draw();
			}
		}
		
		// Message all players
		message(message, currPlayer, altMessage);
		return true;
	}
	
	/**
	 * Gives a clue of the given color to the given player from the current player.
	 * 
	 * @param player The player to clue
	 * @param color The color to clue
	 * @return Whether the clue is a valid move
	 */
	private boolean clue(int player, Color color) {
		// Check if there are no clues left or if the clue is empty
		if (clues == 0 || !hands[player].clueNonEmpty(color)) {
			message(currPlayer, "Invalid clue (" + color + " to " + players[player] + ").");
			return false;
		}
		
		
		// Use up a clue
		clues--;
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			if (i != player) {
				players[i].clue(shift(currPlayer, i), shift(player, i), color);
			} else {
				players[i].clue(shift(player, i), color, hands[player].cluedCards(color));
			}
		}
		
		// Message all players
		message(players[currPlayer] + " has clued all of " + players[player] + "'s " + color + "s.");
		return true;
	}
	
	/**
	 * Gives a clue of the given number to the given player from the current player.
	 * 
	 * @param player The player to clue
	 * @param number The number to clue
	 * @return Whether the clue is a valid move
	 */
	private boolean clue(int player, Number number) {
		// Check if there are no clues left or if the clue is empty
		if (clues == 0 || !hands[player].clueNonEmpty(number)) {
			message(currPlayer, "Invalid clue (" + number + " to " + players[player] + ").");
			return false;
		}
		
		// Use up a clue
		clues--;
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			if (i != player) {
				players[i].clue(shift(currPlayer, i), shift(player, i), number);
			} else {
				players[i].clue(shift(player, i), number, hands[player].cluedCards(number));
			}
		}
		
		// Message all players
		message(players[currPlayer] + " has clued all of " + players[player] + "'s " + number + "s.");
		return true;
	}
	
	/**
	 * Calculates the score once the game is over.
	 * 
	 * @return The score
	 */
	private int gameOver() {
		int score = 0;
		for (int s : fireworks.values()) {
			score += s;
		}
		
		if (logging) {
			stateLog();
			message("Game over!  Final score: " + score);
		}
		return score;
	}
	
	/**
	 * Messages all but one player one message, and the excluded player a different message.
	 * This is mainly used to not reveal information about card draws.
	 * 
	 * @param message The main message
	 * @param alt The player who receives an alternate message
	 * @param altMessage The alternate message
	 */
	private void message(String message, int alt, String altMessage) {
		for (int i = 0; i < players.length; i++) {
			if (i != alt) {
				players[i].message(message);
			} else {
				players[alt].message(altMessage);
			}
		}
		
		if (logging) {
			println(message);
			println("Enter anything to continue.");
			in.nextLine();
		}
	}
	
	/**
	 * Messages the given player.
	 * 
	 * @param to The player to message
	 * @param message The message
	 */
	private void message(int to, String message) {
		players[to].message(message);
		
		if (logging) {
			println("[" + players[to] + "] " + message);
			println("Enter anything to continue.");
			in.nextLine();
		}
	}
	
	/**
	 * Messages all players.
	 * 
	 * @param message The message
	 */
	private void message(String message) {
		for (Player p : players) {
			p.message(message);
		}
		
		if (logging) {
			println(message);
			println("Enter anything to continue.");
			in.nextLine();
		}
	}
	
	/**
	 * Shifts the given player index to the given POV. 
	 * If player == POV, this will return -1.
	 * 
	 * @param player The player index to shift
	 * @param pov The POV to shift to
	 * @return The shifted player index
	 */
	private int shift(int player, int pov) {
		return ((player - pov + players.length) % players.length) - 1;
	}
	
	private int unshift(int player, int pov) {
		return (player + pov + 1) % players.length;
	}

	public void println() {
		System.out.println();
	}
	
	public void println(String s) {
		System.out.println(Color.NONE + "[" + System.currentTimeMillis() + "] " + s + Color.NONE);
	}

	public static void main(String[] args) {
		Player[] players = new Player[4];
		players[0] = new JasonHanabiAI("Jackie");
		players[1] = new JasonHanabiAI("Jason");
		players[2] = new JasonHanabiAI("Milan");
		players[3] = new JasonHanabiAI("Maya");
		
		HanabiGame g = new HanabiGame(GameMode.NORMAL, players, false);
		
		int num = 0;
		int score = 0;
		double total = 0;
		int[] scores = new int[26];
		while(score != 25) {
			score = g.start();
			scores[score]++;
			total += score;
			g.reset();
			num++;
			
			if (num % 10000 == 0) {
				System.out.println("Score distribution at " + num + ": ");
				for (int i = 0; i < scores.length; i++) {
					System.out.println(i + ": " + scores[i]);
				}
			}
		}
		
		System.out.println("Final score distribution: ");
		for (int i = 0; i < scores.length; i++) {
			System.out.println(i + ": " + scores[i]);
		}
		System.out.println("Average over " + num + " games: " + total / num);
	}
}