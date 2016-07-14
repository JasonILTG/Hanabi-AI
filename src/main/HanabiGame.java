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
import main.parts.PlayStack;
import main.players.Player;

public class HanabiGame {
	private static final Random rand = new Random();
	
	public static final int[] HAND_SIZES = { 0, 0, 7, 5, 4, 4 };
	
	public static final int PLAY = 0;
	public static final int DISCARD = 1;
	public static final int CLUE = 2;
	
	public static final int COLOR = 0;
	
	public final GameMode mode;
	private int clues;
	private int lives;
	private HashMap<Color, PlayStack> stacks;
	private Player[] players;
	private Hand[] hands;
	private Deck deck;
	private ArrayList<Card> discard;
	
	private int currPlayer;
	private int lastPlayer;
	
	public HanabiGame(GameMode mode, Player[] players) {
		this.mode = mode;
		this.players = players;
		
		stacks = new HashMap<Color, PlayStack>();
		for (Color color : Color.STANDARD) {
			stacks.put(color, new PlayStack(color));
		}
		
		if (mode == GameMode.NORMAL) {
			deck = new Deck();
		} else {
			deck = new Deck(mode.extraColor, mode.hard);
		}
		
		deck.shuffle();
		
		setup();
	}
	
	public void reset() {
		deck.reset();
		
		setup();
	}
	
	private void setup() {
		clues = 8;
		lives = 3;
		
		discard = new ArrayList<Card>();
		
		int numPlayers = players.length;
		hands = new Hand[numPlayers];
		for (int i = 0; i < HAND_SIZES[numPlayers]; i++) {
			for (int j = 0; j < numPlayers; j++) {
				hands[j].draw(deck.draw());
			}
		}
		
		currPlayer = rand.nextInt(numPlayers);
		lastPlayer = -1;
		
		for (Player p : players) {
			p.init(hands, mode);
		}
	}

	public int start() {
		message("GAME START: Player " + currPlayer + " to move.");
		
		while (!deck.isEmpty()) {
			if (!turn()) {
				return gameOver();
			}
		}
		
		for (int i = 0; i < players.length; i++) {
			if (!turn()) {
				return gameOver();
			}
		}
		
		return gameOver();
	}
	
	private boolean turn() {
		while (!interpretMove(players[currPlayer].move()));
		
		currPlayer = (currPlayer + 1) % players.length;
		return lives > 0 && currPlayer != lastPlayer;
	}
	
	private boolean interpretMove(Move move) {
		if (move.type == MoveType.PLAY) {
			return play(((PlayMove) move).pos);
		} else if (move.type == MoveType.DISCARD) {
			return discard(((DiscardMove) move).pos);
		} else {
			ClueMove cMove = (ClueMove) move;
			
			if (cMove.number == null) {
				return clue(cMove.player, cMove.color);
			} else {
				return clue(cMove.player, cMove.number);
			}
		}
	}
	
	private boolean play(int pos) {
		if (pos >= hands[currPlayer].size()) {
			message(currPlayer, "Invalid card.");
			return false;
		}
		
		String message, altMessage = "";
		
		Card c = hands[currPlayer].discard(pos);
		if (!stacks.get(c.color).play(c)) {
			lives--;
			discard.add(c);
		} else if (c.number == Number.FIVE) {
			clues++;
		}

		if (deck.isEmpty()) {
			if (lastPlayer == -1) {
				lastPlayer = currPlayer;
			}
			
			message = "Player " + currPlayer + " has played a " + c + ".";
			message = "You have played a " + c + ".";
		} else {
			Card d = deck.draw();
			hands[currPlayer].draw(d);
			
			message = "Player " + currPlayer + " has played a " + c + " and drawn a " + d + ".";
			altMessage = "You have played a " + c + " and drawn a card.";
		}
		
		for (Player p : players) {
			p.play(currPlayer, pos);
		}
		
		message(message, currPlayer, altMessage);
		return true;
	}
	
	private boolean discard(int pos) {
		if (pos >= hands[currPlayer].size()) return false;
		
		String message, altMessage = "";
		
		Card c = hands[currPlayer].discard(pos);
		discard.add(c);
		clues++;
		
		if (deck.isEmpty()) {
			if (lastPlayer == -1) {
				lastPlayer = currPlayer;
			}
			
			message = "Player " + currPlayer + " has discarded a " + c + ".";
			message = "You have discarded a " + c + ".";
		} else {
			Card d = deck.draw();
			hands[currPlayer].draw(d);
			
			message = "Player " + currPlayer + " has discarded a " + c + " and drawn a " + d + ".";
			altMessage = "You have discarded a " + c + " and drawn a card.";
		}
		
		for (Player p : players) {
			p.play(currPlayer, pos);
		}
		
		message(message, currPlayer, altMessage);
		return true;
	}
	
	private boolean clue(int player, Color color) {
		if (clues == 0 || !hands[player].clueValid(color)) return false;
		
		clues--;
		
		for (Player p : players) {
			p.clue(currPlayer, player, color);
		}
		
		message("Player " + currPlayer + " has clued all of Player " + player + "'s " + color + "s.");
		return true;
	}
	
	private boolean clue(int player, Number number) {
		if (clues == 0 || !hands[player].clueValid(number)) return false;
		
		clues--;
		
		for (Player p : players) {
			p.clue(currPlayer, player, number);
		}
		
		message("Player " + currPlayer + " has clued all of Player " + player + "'s " + number + "s.");
		return true;
	}
	
	private int gameOver() {
		int score = 0;
		for (PlayStack s : stacks.values()) {
			score += s.getScore();
		}
		
		message("Game over!  Final score: " + score);
		return score;
	}
	
	private void message(String message, int alt, String altMessage) {
		for (int i = 0; i < players.length; i++) {
			if (i != alt) {
				players[i].message(message);
			} else {
				players[alt].message(altMessage);
			}
		}
	}
	
	private void message(int to, String message) {
		players[to].message(message);
	}
	
	private void message(String message) {
		for (Player p : players) {
			p.message(message);
		}
	}
}
