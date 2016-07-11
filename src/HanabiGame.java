import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HanabiGame {
	private static final Random rand = new Random();
	
	public static final int NORMAL = 0;
	public static final int RAINBOW = 1;
	public static final int RAINBOW_HARD = 2;
	public static final int MULTI = 3;
	public static final int MULTI_HARD = 4;
	
	public static final int[] HAND_SIZES = { 0, 0, 5, 5, 4, 4 };
	
	public static final int PLAY = 0;
	public static final int DISCARD = 1;
	public static final int CLUE = 2;
	
	public static final int COLOR = 0;
	
	public final int mode;
	int clues;
	int lives;
	HashMap<Integer, PlayStack> stacks;
	Player[] players;
	Hand[] hands;
	Deck deck;
	ArrayList<Card> discard;
	
	int currPlayer;
	boolean lastTurn;
	int lastPlayer;
	
	public HanabiGame(int mode, Player[] players) {
		this.mode = mode;
		this.players = players;
		
		int addColor = 0;
		if (mode == NORMAL) {
			stacks = new HashMap<Integer, PlayStack>();
			for (int i = 1; i <= 5; i++) {
				stacks.put(i, new PlayStack(i));
			}
		} else if (mode <= RAINBOW_HARD) {
			stacks = new HashMap<Integer, PlayStack>();
			for (int i = 1; i <= 6; i++) {
				stacks.put(i, new PlayStack(i));
			}
			
			addColor = Card.RAINBOW;
		} else {
			stacks = new HashMap<Integer, PlayStack>();
			for (int i = 1; i <= 5; i++) {
				stacks.put(i, new PlayStack(i));
			}
			stacks.put(Card.MULTI, new PlayStack(Card.MULTI));
			
			addColor = Card.MULTI;
		}
		
		if (mode == NORMAL) {
			deck = new Deck();
		} else {
			int[] addNums = mode % 2 == 1 ? Deck.STD_DIST : Deck.HARD_DIST;
			
			Card[] add = new Card[addNums.length];
			for (int i = 0; i < add.length; i++) {
				add[i] = new Card(addColor, addNums[i]);
			}
			
			deck = new Deck(add);
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
		
		while (!deck.empty()) {
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
		if (move[0] == PLAY) {
			play(move[1]);
		} else if (move[0] == DISCARD) {
			discard(move[1]);
		} else {
			if (move[1] == COLOR) {
				clueColor(move[2], move[3]);
			} else {
				clueNumber(move[2], move[3]);
			}
		}
		
		return lives > 0 && (!lastTurn || currPlayer != lastPlayer);
	}
	
	private void play(int pos) {
		Card c = hands[currPlayer].discard(pos);
		Card d = deck.draw();
		hands[currPlayer].draw(d);
		if (!stacks.get(c.color).play(c)) {
			lives--;
			discard.add(c);
		} else if (c.number == 5) {
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
	
	private void clueColor(int player, int color) {
		hands[player].clueColor(color);
		
		message("Player " + currPlayer + " has clued all of Player " + player + "'s " + getColorName(color) + "s.");
	}
	
	private void clueNumber(int player, int number) {
		hands[player].clueNumber(number);
		
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
	
	private String getColorName(int color) {
		if (color == Card.RED) {
			return "red";
		} else if (color == Card.YELLOW) {
			return "yellow";
		} else if (color == Card.GREEN) {
			return "green";
		} else if (color == Card.BLUE) {
			return "blue";
		} else if (color == Card.WHITE) {
			return "white";
		} else {
			return "rainbow";
		}
	}
}
