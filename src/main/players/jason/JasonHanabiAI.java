package main.players.jason;
import java.util.ArrayList;
import java.util.HashMap;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;
import main.moves.Move;
import main.parts.Card;
import main.parts.Hand;
import main.players.Player;

public class JasonHanabiAI extends Player {
	private GameMode mode;
	private int clues;
	private int lives;
	private HiddenHand ownHand;
	private CluedHand[] hands;
	private HashMap<Color, Integer> fireworks;
	private HashMap<Color, int[]> discard;
	
	public JasonHanabiAI(String name) {
		super(name);
	}
	
	@Override
	public void init(Hand[] hands, GameMode mode) {
		this.mode = mode;
		this.clues = 8;
		this.lives = 3;
		
		ownHand = new HiddenHand(hands[0].size(), mode);
		
		this.hands = new CluedHand[hands.length];
		for (int i = 0; i < hands.length; i++) {
			this.hands[i] = new CluedHand(hands[i].getCards(), mode);
		}
		
		fireworks = new HashMap<Color, Integer>();
		for (Color color : Color.VALUES) {
			fireworks.put(color, 1);
		}
	}

	@Override
	public Move move() {
		if (clues > 0) {
			Move chopClue = checkChop();
			if (chopClue != null) return chopClue;
		}
		
		return null;
	}
	
	private boolean dangerous(int player, int pos) {
		Color color = hands[player].getColor(pos);
		Number number = hands[player].getNumber(pos);
		
		int stack = fireworks.get(color);
		if (number.ordinal() >= stack) {
			int totalAmount = mode.hard && (color == Color.MULTI || color == Color.RAINBOW) ? 1 : number.amount;
			return discard.get(color)[number.ordinal()] + 1 == totalAmount;
		}
		return false;
	}
	
	private boolean playable(int player, int pos) {
		Color color = hands[player].getColor(pos);
		Number number = hands[player].getNumber(pos);
		
		return number.ordinal() == fireworks.get(color);
	}
	
	private Card finessed(int player, int pos) {
		Color color = hands[player].getColor(pos);
		Number number = hands[player].getNumber(pos);
		
		return number.ordinal() > fireworks.get(color) ? new Card(color, Number.VALUES[fireworks.get(color)]) : null;
	}
	
	private Move checkChop() {
		for (int i = 0; i < hands.length; i++) {
			int chopPos = getChop(i);
			
			if (dangerous(i, chopPos)) {
				
			}
		}
		return null;
	}
	
	private int getChop(int player) {
		for (int j = 0; j < hands[player].size(); j++) {
			if (!hands[player].isClued(j)) {
				return j;
			}
		}
		
		return 0;
	}
	
	private int getChop() {
		for (int j = 0; j < ownHand.size(); j++) {
			if (!ownHand.isClued(j)) {
				return j;
			}
		}
		
		return 0;
	}

	@Override
	public void play(int player, int pos) {
		
	}

	@Override
	public void play(int pos, Card c) {
		
	}
	
	@Override
	public void discard(int player, int pos) {
		
	}

	@Override
	public void discard(int pos, Card c) {
		
	}

	@Override
	public void clue(int fromPlayer, int player, Color color) {
		// Clue to other player
		hands[player].clue(color);
		parseClue(fromPlayer, player, color);
	}

	@Override
	public void clue(int fromPlayer, Color color, ArrayList<Integer> pos) {
		
	}

	private void parseClue(int fromPlayer, int player, Color color) {
		CluedHand hand = hands[player];
		int chopPos = getChop(player);
		
		if (hand.getColor(chopPos).same(color) && dangerous(player, chopPos)) {
			// Chop clue
			return;
		}
		
		// Calculate which cards are clued
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = hand.size() - 1; i >= 0; i--) {
			if (hand.getColor(i).same(color)) {
				clued.add(i);
			}
		}
		
		if (playable(player, clued.get(0))) {
			// Play clue
			hand.mark(Marker.PLAY, clued.get(0));
			return;
		}
		
		Card finessed = finessed(player, clued.get(0));
		if (finessed != null) {
			// Finesse clue
			for (int p = 0; p < hands.length; p++) {
				if (p == player || p == fromPlayer) {
					continue;
				}
				
				int finessePos = hands[p].mostPlayable(finessed.color, finessed.number);
				if (finessePos == -1) continue;
				
				if (hands[p].getColor(finessePos) == finessed.color && hands[p].getNumber(finessePos) == finessed.number) {
					// Finessed card found.
					hands[p].mark(Marker.FINESSED, finessePos);
					return;
				}
			}
			
			// Assume finesse on self unless bluff
			int finessePos = ownHand.mostPlayable(finessed.color, finessed.number);
			if (finessePos == -1) {
				// Definite bluff
				return;
			}
			
			ownHand.mark(Marker.FINESSED_UNLESS_BLUFF, finessePos);
			return;
		}
	}
	
	private void parseClue(int fromPlayer, Color color) {
		
	}

	@Override
	public void clue(int fromPlayer, int player, Number number) {
		
	}
	
	@Override
	public void clue(int fromPlayer, Number number, ArrayList<Integer> pos) {
		
	}
	
	@Override
	public void draw(int player, Card c) {
		
	}
	
	@Override
	public void draw() {
		
	}
}
