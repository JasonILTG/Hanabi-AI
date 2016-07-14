package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import main.parts.Firework;
import main.players.Player;

/**
 * Class for all of the game logic.
 */
public class HanabiGame {
	/** Hand sizes for different numbers of players */
	public static final int[] HAND_SIZES = { 0, 0, 7, 5, 4, 4 };
	
	public final GameMode mode;
	
	private int clues;
	private int lives;
	
	private Deck deck;
	private HashMap<Color, Firework> fireworks;
	private ArrayList<Card> discard;

	private Player[] players;
	private Hand[] hands;
	
	private int currPlayer;
	
	/**
	 * Constructor.
	 * 
	 * @param mode The game mode
	 * @param players The array of players
	 */
	public HanabiGame(GameMode mode, Player[] players) {
		this.mode = mode;
		this.players = players;
		
		// Initialize fireworks
		fireworks = new HashMap<Color, Firework>();
		for (Color color : Color.STANDARD) {
			fireworks.put(color, new Firework(color));
		}
		
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
		
		// Initialize discard
		discard = new ArrayList<Card>();
		
		// Initialize hands
		int numPlayers = players.length;
		hands = new Hand[numPlayers];
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
		// Keep querying the player for a move as long as the player does not give a valid one
		while (!interpretMove(players[currPlayer].move()));
		
		// The game is over if there are no lives left
		if (lives == 0) return true;
		
		// Go to the next player
		currPlayer = (currPlayer + 1) % players.length;
		return false;
	}
	
	/**
	 * Interprets and attempts to play out the given move.
	 * 
	 * @param move The move made by the current player
	 * @return Whether the move was valid
	 */
	private boolean interpretMove(Move move) {
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
				return clue(cMove.player, cMove.color);
			} else {
				// Number clue
				return clue(cMove.player, cMove.number);
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
		
		// Check if the play is correct
		if (!fireworks.get(c.color).play(c)) {
			lives--;
			discard.add(c);
		} else if (c.number == Number.FIVE) {
			// Fives give back a clue
			clues++;
		}
		
		if (deck.isEmpty()) {
			// If the deck is empty, do not draw
			message = players[currPlayer] + " has played a " + c + ".";
			message = "You have played a " + c + ".";
		} else {
			// Otherwise, draw
			Card d = deck.draw();
			hands[currPlayer].draw(d);
			
			message = players[currPlayer] + " has played a " + c + " and drawn a " + d + ".";
			altMessage = "You have played a " + c + " and drawn a card.";
		}
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			players[i].play(shift(currPlayer, i), pos);
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
		
		if (deck.isEmpty()) {
			// If the deck is empty, do not draw
			message = players[currPlayer] + " has discarded a " + c + ".";
			message = "You have discarded a " + c + ".";
		} else {
			// Otherwise, draw
			Card d = deck.draw();
			hands[currPlayer].draw(d);
			
			message = players[currPlayer] + " has discarded a " + c + " and drawn a " + d + ".";
			altMessage = "You have discarded a " + c + " and drawn a card.";
		}
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			players[i].discard(shift(currPlayer, i), pos);
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
			message(currPlayer, "Invalid clue.");
			return false;
		}
		
		
		// Use up a clue
		clues--;
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			players[i].clue(shift(currPlayer, i), shift(player, i), color);
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
			message(currPlayer, "Invalid clue.");
			return false;
		}
		
		// Use up a clue
		clues--;
		
		// Update all players
		for (int i = 0; i < players.length; i++) {
			players[i].clue(shift(currPlayer, i), shift(player, i), number);
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
		for (Firework s : fireworks.values()) {
			score += s.getScore();
		}
		
		message("Game over!  Final score: " + score);
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
	}
	
	/**
	 * Messages the given player.
	 * 
	 * @param to The player to message
	 * @param message The message
	 */
	private void message(int to, String message) {
		players[to].message(message);
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
}