package main.players.jason;
import java.util.ArrayList;
import java.util.HashMap;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;
import main.moves.ClueMove;
import main.moves.DiscardMove;
import main.moves.Move;
import main.moves.PlayMove;
import main.parts.Card;
import main.parts.Hand;
import main.players.Player;

public class JasonHanabiAI extends Player {
	private GameMode mode;
	private int clues;
	private int lives;
	private HiddenHand ownHand;
	private CluedHand[] hands;
	private HashMap<Color, boolean[]> willBePlayed;
	private HashMap<Color, Integer> fireworks;
	private HashMap<Color, int[]> discard;
	private HashMap<Color, int[]> cardsSeen;
	
	public JasonHanabiAI(String name) {
		super(name);
	}
	
	@Override
	public void init(Hand[] hands, GameMode mode) {
		this.mode = mode;
		this.clues = 8;
		this.lives = 3;
		
		ownHand = new HiddenHand(hands[0].size(), mode);
		
		willBePlayed = new HashMap<Color, boolean[]>();
		fireworks = new HashMap<Color, Integer>();
		discard = new HashMap<Color, int[]>();
		cardsSeen = new HashMap<Color, int[]>();
		for (Color color : mode.colors) {
			willBePlayed.put(color, new boolean[Number.VALUES.length]);
			fireworks.put(color, 0);
			discard.put(color, new int[Number.VALUES.length]);
			cardsSeen.put(color, new int[Number.VALUES.length]);
		}
		
		this.hands = new CluedHand[hands.length];
		for (int i = 0; i < hands.length; i++) {
			this.hands[i] = new CluedHand(hands[i].getCards(), mode);
			for (Card c : hands[i].getCards()) {
				cardsSeen.get(c.color)[c.number.ordinal()]++;
			}
		}
	}

	@Override
	public Move move() {
		// Check own hand for playable or discardable cards
		ownHand.check(fireworks);
		
		// Then play cards
		for (int i = ownHand.size() - 1; i >= 0; i--) {
			if (ownHand.getMark(i).play()) {
				return new PlayMove(i);
			}
		}
		
		if (clues > 0) {
			// Then look for good clues
			
			// Direct play clues
			for (int i = 0; i < hands.length; i++) {
				CluedHand h = hands[i];
				
				// Get positions of playable cards
				ArrayList<Integer> playable = getPlayable(i);
				
				search:
				for (int pos : playable) {
					// If already marked to play, ignore
					if (h.getMark(pos) == Marker.CLUED_PLAY) continue;
					
					if (!h.isNClued(pos)) {
						// If not already number clued, check if number clue will work
						ArrayList<Integer> clued = getClued(i, h.getNumber(pos));
						for (int c : clued) {
							if (c == pos) break;
							
							// If an earlier card is not number clued, continue the search
							if (!h.isNClued(c)) continue search;
						}
						
						return new ClueMove(i, h.getNumber(pos));
					}
					
					if (!h.isCClued(pos)) {
						// If not already color clued, check if color clue will work
						ArrayList<Integer> clued = getClued(i, h.getColor(pos));
						for (int c : clued) {
							if (c == pos) break;
							
							// If an earlier card is not color clued, continue the search
							if (!h.isCClued(c)) continue search;
						}
						
						return new ClueMove(i, h.getColor(pos));
					}
				}
			}
		}
		
		// Then discard any confirmed discardable cards
		for (int i = 0; i < ownHand.size(); i++) {
			if (ownHand.getMark(i) == Marker.DISCARD) {
				return new DiscardMove(i);
			}
		}
		
		// If all else fails, discard chop
		return new DiscardMove(getChop());
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
	
	private boolean possiblyDangerous(int pos) {
		for (Color color : ownHand.getCard(pos).getPColors()) {
			for (Number number : ownHand.getCard(pos).getPNumbers()) {
				if (fireworks.get(color) <= number.ordinal() && discard.get(color)[number.ordinal()] + 1 == number.amount) {
					return true;
				}
			}
		}
		return false;
	}
	
	private ArrayList<Integer> getPlayable(int player) {
		ArrayList<Integer> playable = new ArrayList<Integer>();
		for (int i = hands[player].size() - 1; i >= 0; i--) {
			if (playable(player, i)) {
				playable.add(i);
			}
		}
		
		return playable;
	}
	
	private boolean playable(int player, int pos) {
		Color color = hands[player].getColor(pos);
		Number number = hands[player].getNumber(pos);
		
		return number.ordinal() == fireworks.get(color) && !willBePlayed.get(color)[number.ordinal()];
	}
	
	private boolean possiblyPlayable(int player, int pos, Color color) {
		CluedHand h = hands[player];
		ArrayList<Number> pNumbers = h.getCard(pos).getPNumbers();
		if (pNumbers.contains(fireworks.get(color))) {
			return true;
		} else if (h.getCard(pos).getPColors().contains(Color.MULTI) && pNumbers.contains(fireworks.get(Color.MULTI))) {
			return true;
		}
		return false;
	}
	
	private boolean possiblyPlayable(int player, int pos, Number number) {
		CluedHand h = hands[player];
		for (Color color : h.getCard(pos).getPColors()) {
			if (number.ordinal() == fireworks.get(color)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean possiblyPlayable(int player, int pos) {
		CluedHand h = hands[player];
		for (Color color : h.getCard(pos).getPColors()) {
			if (h.getCard(pos).getPNumbers().contains(fireworks.get(color))) {
				return true;
			}
		}
		return false;
	}

	private boolean possiblyPlayable(int pos, Color color) {
		for (Color c : ownHand.getCard(pos).getPColors()) {
			if (c.same(color) && ownHand.getCard(pos).getPNumbers().contains(fireworks.get(color))) {
				return true;
			}
		}
		return false;
	}

	private boolean possiblyPlayable(int pos, Number number) {
		for (Color color : ownHand.getCard(pos).getPColors()) {
			if (fireworks.get(color) == number.ordinal() && ownHand.getCard(pos).getPNumbers().contains(number)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean possiblyPlayable(int pos) {
		for (Color color : ownHand.getCard(pos).getPColors()) {
			if (ownHand.getCard(pos).getPNumbers().contains(fireworks.get(color))) {
				return true;
			}
		}
		return false;
	}
	
	private Move checkChop() {
		for (int i = 0; i < hands.length; i++) {
			int chopPos = getChop(i);
			
			if (dangerous(i, chopPos)) {
				return new ClueMove(i, hands[i].getNumber(chopPos));
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
		for (int i = 0; i < ownHand.size(); i++) {
			if (ownHand.getMark(i) == Marker.DISCARD) {
				return i;
			}
		}
		
		for (int i = 0; i < ownHand.size(); i++) {
			if (!ownHand.isClued(i)) {
				return i;
			}
		}
		
		for (int i = 0; i < ownHand.size(); i++) {
			if (ownHand.getMark(i) != Marker.CLUED_PLAY) {
				return i;
			}
		}
		
		return 0;
	}

	private ArrayList<Integer> getClued(int player, Color color) {
		CluedHand hand = hands[player];
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = hand.size() - 1; i >= 0; i--) {
			if (hand.getColor(i).same(color)) {
				clued.add(i);
			}
		}
		return clued;
	}
	
	private ArrayList<Integer> getClued(int player, Number number) {
		CluedHand hand = hands[player];
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = hand.size() - 1; i >= 0; i--) {
			if (hand.getNumber(i) == number) {
				clued.add(i);
			}
		}
		return clued;
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
			discard.get(c.color)[c.number.ordinal()]++;
			lives--;
		}
	}

	@Override
	public void play(int pos, Card c) {
		ownHand.take(pos);
		
		if (fireworks.get(c.color) == c.number.ordinal()) {
			fireworks.put(c.color, c.number.ordinal() + 1);
			
			if (c.number == Number.FIVE) {
				clues++;
			}
		} else {
			discard.get(c.color)[c.number.ordinal()]++;
			lives--;
		}
		
		seeCard(c);
	}
	
	@Override
	public void discard(int player, int pos) {
		CluedCard c = hands[player].take(pos);

		discard.get(c.color)[c.number.ordinal()]++;
		clues++;
	}

	@Override
	public void discard(int pos, Card c) {
		ownHand.take(pos);

		discard.get(c.color)[c.number.ordinal()]++;
		clues++;
		
		seeCard(c);
	}

	@Override
	public void clue(int fromPlayer, int player, Color color) {
		clues--;
		
		// Clue to other player
		CluedHand hand = hands[player];
		
		// Calculate which cards are clued
		ArrayList<Integer> clued = getClued(player, color);
		for (int p : clued) {
			if (!hand.isCClued(p) && hand.getMark(p) != Marker.CLUED_PLAY
					&& possiblyPlayable(player, p, color)) {
				hand.mark(Marker.CLUED_PLAY, p);
				willBePlayed.get(hand.getColor(p))[hand.getNumber(p).ordinal()] = true;
				break;
			}
			
		}
		
		hand.clue(color);
	}

	@Override
	public void clue(int fromPlayer, Color color, ArrayList<Integer> pos) {
		clues--;
		
		// Clue to this player
		for (int p : pos) {
			if (!ownHand.isCClued(p) && ownHand.getMark(p) != Marker.CLUED_PLAY
					&& possiblyPlayable(p, color)) {
				ownHand.mark(Marker.CLUED_PLAY, p);
				break;
			}
			
		}
		
		ownHand.clue(color, pos);
	}

	@Override
	public void clue(int fromPlayer, int player, Number number) {
		clues--;
		
		// Clue to other player
		CluedHand hand = hands[player];
		
		// Calculate which cards are clued
		ArrayList<Integer> clued = getClued(player, number);
		for (int p : clued) {
			if (!hand.isNClued(p) && hand.getMark(p) != Marker.CLUED_PLAY
					&& possiblyPlayable(player, p, number)) {
				hand.mark(Marker.CLUED_PLAY, p);
				willBePlayed.get(hand.getColor(p))[hand.getNumber(p).ordinal()] = true;
				break;
			}
			
		}
		
		hand.clue(number);
	}
	
	@Override
	public void clue(int fromPlayer, Number number, ArrayList<Integer> pos) {
		clues--;
		
		// Clue to this player
		for (int p : pos) {
			if (!ownHand.isNClued(p) && ownHand.getMark(p) != Marker.CLUED_PLAY
					&& possiblyPlayable(p, number)) {
				ownHand.mark(Marker.CLUED_PLAY, p);
				break;
			}
			
		}
		
		ownHand.clue(number, pos);
	}

	@Override
	public void draw(int player, Card c) {
		hands[player].draw(c);
		
		seeCard(c);
	}
	
	@Override
	public void draw() {
		ownHand.draw();
	}

	private void seeCard(Card c) {
		cardsSeen.get(c.color)[c.number.ordinal()]++;
		if (cardsSeen.get(c.color)[c.number.ordinal()] == c.number.amount) {
			ownHand.antiClueAll(c.color, c.number);
		}
	}
	
	public boolean check(Hand[] h, int playerNum, HashMap<Color, Integer> fireworks) {
		for (int i = 0; i < hands.length; i++) {
			int j = (i + playerNum + 1) % h.length;
			
			for (int c = 0; c < h[j].size(); c++) {
				if (h[j].getColor(c) != hands[i].getColor(c)) {
					System.out.println("Hand error: " + h[j] + " " + hands[i]);
					return false;
				}
				if (h[j].getNumber(c) != hands[i].getNumber(c)) {
					System.out.println("Hand error: " + h[j] + " " + hands[i]);
					return false;
				}
			}
		}
		
		for (Color color : mode.colors) {
			if (fireworks.get(color) != this.fireworks.get(color)) {
				System.out.println("Firework error: " + color);
				return false;
			}
		}
		
		return true;
	}
}
