package main.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;
import main.moves.ClueMove;
import main.moves.DiscardMove;
import main.moves.Move;
import main.moves.PlayMove;
import main.parts.Card;
import main.parts.Hand;
import main.players.jason.CluedCard;
import main.players.jason.CluedHand;
import main.players.jason.HiddenHand;

public class Human extends Player {
	public static final Scanner in = new Scanner(System.in);
	
	private GameMode mode;
	
	private int clues;
	private int lives;
	
	private HiddenHand ownHand;
	private CluedHand[] hands;
	
	private HashMap<Color, Integer> fireworks;
	private HashMap<Color, int[]> discard;

	public Human(String name) {
		super(name);
	}
	
	@Override
	public void init(Hand[] hands, GameMode mode) {
		clues = 8;
		lives = 3;
		
		this.mode = mode;
		
		this.hands = new CluedHand[hands.length];
		for (int i = 0; i < hands.length; i++) {
			this.hands[i] = new CluedHand(hands[i].getCards(), mode);
		}
		
		ownHand = new HiddenHand(hands[0].size(), mode);
		
		fireworks = new HashMap<Color, Integer>();
		for (Color color : Color.STANDARD) {
			fireworks.put(color, 0);
		}
		
		if (mode.extraColor != null) {
			fireworks.put(mode.extraColor, 0);
		}
		
		discard = new HashMap<Color, int[]>();
		for (Color color : mode.colors) {
			discard.put(color, new int[5]);
		}
	}

	@Override
	public Move move() {
		println("------------------------");
		println(clues + " clues, " + lives + " lives left.");
		
		println();
		println("Fireworks:");
		for (Entry<Color, Integer> e : fireworks.entrySet()) {
			System.out.print(e.getKey().ansi() + e.getValue());
		}
		println();

		println();
		println("Hands:");
		for (CluedHand h : hands) {
			println(h.toString());
		}

		println();
		println("Your Move:");
		if (clues > 0) {
			println("(0) Play (1) Discard (2) Clue");
		} else {
			println("(0) Play (1) Discard");
		}
		
		int choice = in.nextInt();

		println();
		if (choice == 0) {
			println("Choose a card to play from your hand (0-" + (ownHand.size() - 1) + "):");
			
			return new PlayMove(in.nextInt());
		} else if (choice == 1) {
			println("Choose a card to discard from your hand (0-" + (ownHand.size() - 1) + "):");
			for (int i = 0; i < ownHand.size(); i++) {
				println("(" + i + ") " + ownHand.getCard(i));
			}
			
			return new DiscardMove(in.nextInt());
		} else {
			println("Choose someone to hint:");
			for (int i = 0; i < hands.length; i++) {
				println("(" + i + ") " + hands[i]);
			}
			
			int to = in.nextInt();

			println();
			println("Color (0) or number (1)?");
			choice = in.nextInt();

			println();
			if (choice == 0) {
				println("Choose a color:");
				for (int i = 0; i < Color.STANDARD.length; i++) {
					println("(" + i + ") " + Color.STANDARD[i]);
				}
				
				if (mode.extraColor != null) {
					println("(5) " + mode.extraColor);
				}
				
				choice = in.nextInt();
				Color color = choice < 5 ? Color.STANDARD[choice] : mode.extraColor;
				
				return new ClueMove(to, color);
			} else {
				println("Choose a number:");
				for (int i = 0; i < Number.VALUES.length; i++) {
					println("(" + i + ") " + (i + 1));
				}
				
				Number number = Number.VALUES[in.nextInt()];
				
				return new ClueMove(to, number);
			}
		}
	}

	@Override
	public void play(int player, int pos) {
		CluedCard c = hands[player].take(pos);
		if (fireworks.get(c.color) == c.number.ordinal()) {
			fireworks.put(c.color, c.number.ordinal() + 1);
			
			if (c.number == Number.FIVE) {
				clues++;
			}
		} else {
			lives--;
		}
	}

	@Override
	public void play(int pos, Card c) {
		ownHand.take(pos);
		if (fireworks.get(c.color) == c.number.ordinal()) {
			fireworks.put(c.color, fireworks.get(c.color) + 1);
		} else {
			discard.get(c.color)[c.number.ordinal()]++;
			lives--;
		}
	}

	@Override
	public void discard(int player, int pos) {
		CluedCard c = hands[player].take(pos);
		discard.get(c.color)[c.number.ordinal()]++;
	}

	@Override
	public void discard(int pos, Card c) {
		ownHand.take(pos);
		discard.get(c.color)[c.number.ordinal()]++;
	}

	@Override
	public void clue(int fromPlayer, int player, Color color) {
		hands[player].clue(color);
		clues--;
	}

	@Override
	public void clue(int fromPlayer, Color color, ArrayList<Integer> pos) {
		ownHand.clue(color, pos);
		clues--;
	}

	@Override
	public void clue(int fromPlayer, int player, Number number) {
		hands[player].clue(number);
		clues--;
	}

	@Override
	public void clue(int fromPlayer, Number number, ArrayList<Integer> pos) {
		ownHand.clue(number, pos);
		clues--;
	}
	
	@Override
	public void draw(int player, Card c) {
		hands[player].draw(c);
	}
	
	@Override
	public void draw() {
		ownHand.draw();
	}

	public void println() {
		System.out.println();
	}
	
	public void println(String s) {
		System.out.println(Color.NONE + s + Color.NONE);
	}

	@Override
	public void message(String message) {
		System.out.println("[" + System.currentTimeMillis() + "] " + message);
	}
	
	@Override
	public boolean check(Hand[] h, int playerNum, HashMap<Color, Integer> fireworks) {
		return true;
	}
}
