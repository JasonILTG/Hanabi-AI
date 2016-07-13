package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import main.enums.Color;
import main.enums.Number;
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
	int clues;
	int lives;
	HashMap<Color, PlayStack> stacks;
	Player[] players;
	Hand[] hands;
	Deck deck;
	ArrayList<Card> discard;
	
	int currPlayer;
	boolean lastTurn;
	int lastPlayer;
	
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
		
		for (Player p : players) {
			p.init(this);
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
		int[] move = players[currPlayer].move();
		
		boolean toReturn = interpretMove(move);
		
		currPlayer = (currPlayer + 1) % players.length;
		return toReturn;
	}
	
	private boolean interpretMove(int[] move) {
		return lives > 0 && (!lastTurn || currPlayer != lastPlayer);
	}
	
	private void play(int pos) {
		Card c = hands[currPlayer].discard(pos);
		Card d = deck.draw();
		hands[currPlayer].draw(d);
		
		if (!stacks.get(c.color).play(c)) {
			lives--;
			discard.add(c);
		} else if (c.number == Number.FIVE) {
			clues++;
		}
		
		message("Player " + currPlayer + " has played a " + c + " and drawn a " + d + ".", currPlayer, "You have played a " + c + ".");
	}
	
	private void discard(int pos) {
		Card c = hands[currPlayer].discard(pos);
		Card d = deck.draw();
		hands[currPlayer].draw(d);
		discard.add(c);
		clues++;
		
		message("Player " + currPlayer + " has discarded a " + c + " and drawn a " + d + ".", currPlayer, "You have discarded a " + c + ".");
	}
	
	private void clue(int player, Color color) {
		message("Player " + currPlayer + " has clued all of Player " + player + "'s " + color + "s.");
	}
	
	private void clue(int player, Number number) {
		message("Player " + currPlayer + " has clued all of Player " + player + "'s " + number + "s.");
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
	
	private void message(String message) {
		for (Player p : players) {
			p.message(message);
		}
	}
	
	static enum GameMode {
		NORMAL(null, false),
		RAINBOW(Color.RAINBOW, false),
		RAINBOW_HARD(Color.RAINBOW, true),
		MULTI(Color.MULTI, false),
		MULTI_HARD(Color.MULTI, true);
		
		public final Color extraColor;
		public final boolean hard;
		
		GameMode(Color extra, boolean hard) {
			this.extraColor = extra;
			this.hard = hard;
		}
	}
	
	static enum Action {
		PLAY, DISCARD, CLUE;
	}
}
