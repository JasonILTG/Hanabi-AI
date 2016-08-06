package main.players.jason;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

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
	private static final Random rand = new Random();
	
	private static final boolean CLUE_DISCARD = false;
	private static final boolean FINESSE = true;
	
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
		
		// Then play finessed cards
		for (int i = ownHand.size() - 1; i >= 0; i--) {
			if (ownHand.getMark(i).finesse()) {
				return new PlayMove(i);
			}
		}
		
		// Then play normally clued cards
		for (int i = ownHand.size() - 1; i >= 0; i--) {
			if (ownHand.getMark(i).play()) {
				return new PlayMove(i);
			}
		}
		
		// Prevent dangerous cards from being discarded
		if (CLUE_DISCARD) if (clues == 1) {
			for (int i = 0; i < hands.length; i++) {
				CluedHand h = hands[i];
				int chop = getChop(i);
				if(dangerous(i, chop) && !h.isClued(chop) && numPlays(i) == 0){
					Number number = h.getNumber(chop);
					if(getClued(i, number).size() == 1){
						return new ClueMove(i, number);
					}
				}
			}
		}
		
		
		if (clues > 0) {
			// Then look for good clues
			
			// Sort players by number of playable cards they have
			ArrayList<Integer> players = new ArrayList<Integer>();
			for (int i = 0; i < hands.length; i++) players.add(i);
			players.sort(new PlayerComp());
			
			for (int i : players) {
				CluedHand h = hands[i];
				
				// Get positions of playable cards
				ArrayList<Integer> playable = getPlayable(i);
				if (FINESSE) {
					// Finesse clues
					fSearch:
					for (int pos : playable) {
						// If already marked to play, ignore
						if (h.getMark(pos) == Marker.CLUED_PLAY) continue;
						
						if (h.getNumber(pos).next() != null) {
							// If it's not a 5, look for finesse
							Color color = h.getColor(pos);
							Number number = h.getNumber(pos);
							
							// Make sure the card is correctly indicated by the finesse
							if (finessedPos(i, color, number) != pos) continue;
							
							// Make sure no one has a duplicate card
							for (int o : players) {
								if (i == o) continue;
								
								CluedHand oh = hands[o];
								for (int oPos = oh.size() - 1; oPos >= 0; oPos--) {
									if (oh.getColor(oPos) == color && oh.getNumber(oPos) == number) {
										continue fSearch;
									}
								}
							}
							
							// Look for the next card
							for (int f = i + 1; f < hands.length; f++) {
								CluedHand fh = hands[f];
								
								for (int fPos = fh.size() - 1; fPos >= 0; fPos--) {
									if (fh.getColor(fPos) == color && fh.getNumber(fPos) == number.next()) {
										if (finesseClueIndicates(f, number.next()) == fPos && playClueIndicates(f, number.next()) == -1) {
											return new ClueMove(f, number.next());
										}
										
										if (finesseClueIndicates(f, color) == fPos && playClueIndicates(f, color) == -1) {
											if (color != Color.MULTI) return new ClueMove(f, color);
										}
									}
								}
							}
						}
					}
				}
				
				// Direct play clues
				for (int pos : playable) {
					// If already marked to play, ignore
					if (h.getMark(pos) == Marker.CLUED_PLAY) continue;
					
					if (rand.nextInt(2) == 0 && playClueIndicates(i, h.getNumber(pos)) == pos) {
						return new ClueMove(i, h.getNumber(pos));
					}
					
					if (playClueIndicates(i, h.getColor(pos)) == pos) {
						if (h.getColor(pos) != Color.MULTI) return new ClueMove(i, h.getColor(pos));
					}
					
					if (playClueIndicates(i, h.getNumber(pos)) == pos) {
						return new ClueMove(i, h.getNumber(pos));
					}
				}
			}
		}
		
		// If all else fails, discard
		return new DiscardMove(getChop(true));
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
				int totalAmount = mode.hard && (color == Color.MULTI || color == Color.RAINBOW) ? 1 : number.amount;
				if (fireworks.get(color) <= number.ordinal() && discard.get(color)[number.ordinal()] + 1 == totalAmount) {
					return true;
				}
			}
		}
		return false;
	}

	private int numPlays(int player) {
		int numPlays = 0;
		for (int i = 0; i < hands[player].size(); i++) {
			if (hands[player].getMark(i).play()) numPlays++;
		}
		
		return numPlays;
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
		
		for (Color c : h.getCard(pos).getPColors()) {
			if (fireworks.get(c) < Number.VALUES.length && c.same(color) && h.getCard(pos).getPNumbers().contains(Number.VALUES[fireworks.get(c)])) {
				return true;
			}
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
	
	private boolean possiblyPlayable(int pos, Color color) {
		for (Color c : ownHand.getCard(pos).getPColors()) {
			if (fireworks.get(c) < Number.VALUES.length && c.same(color) && ownHand.getCard(pos).getPNumbers().contains(Number.VALUES[fireworks.get(c)])) {
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
	
	private boolean possiblyFinessable(int player, int pos, Color color) {
		CluedHand h = hands[player];
		for (Color c : h.getCard(pos).getPColors()) {
			if (c.same(color) && fireworks.get(color) < 4 && h.getCard(pos).getPNumbers().contains(Number.VALUES[fireworks.get(color) + 1])) {
				return true;
			}
		}
		return false;
	}

	private boolean possiblyFinessable(int player, int pos, Number number) {
		CluedHand h = hands[player];
		for (Color color : h.getCard(pos).getPColors()) {
			if (number.ordinal() == fireworks.get(color) + 1) {
				return true;
			}
		}
		return false;
	}
	
	private boolean possiblyFinessable(int pos, Color color) {
		for (Color c : ownHand.getCard(pos).getPColors()) {
			if (c.same(color) && fireworks.get(color) < 4 && ownHand.getCard(pos).getPNumbers().contains(Number.VALUES[fireworks.get(color) + 1])) {
				return true;
			}
		}
		return false;
	}
	
	private boolean possiblyFinessable(int pos, Number number) {
		for (Color color : ownHand.getCard(pos).getPColors()) {
			if (fireworks.get(color) + 1 == number.ordinal() && ownHand.getCard(pos).getPNumbers().contains(number)) {
				return true;
			}
		}
		return false;
	}
	
	private int getChop(int player) {
		CluedHand h = hands[player];
		
		for (int i = 0; i < h.size(); i++) {
			if (h.getMark(i) == Marker.DISCARD) {
				return i;
			}
		}
		
		for (int i = 0; i < h.size(); i++) {
			if (!h.isClued(i) && h.getMark(i) != Marker.NOT_DISCARD) {
				return i;
			}
		}
		
		for (int i = 0; i < h.size(); i++) {
			if (h.getMark(i) != Marker.NOT_DISCARD) {
				return i;
			}
		}
		
		return 0;
	}
	
	private int getChop(boolean considerDiscard) {
		if (considerDiscard) for (int i = 0; i < ownHand.size(); i++) {
			if (ownHand.getMark(i) == Marker.DISCARD) {
				return i;
			}
		}
		
		for (int i = 0; i < ownHand.size(); i++) {
			if (!ownHand.isClued(i) && ownHand.getMark(i) != Marker.NOT_DISCARD) {
				return i;
			}
		}
		
		for (int i = 0; i < ownHand.size(); i++) {
			if (ownHand.getMark(i) != Marker.NOT_DISCARD) {
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
	
	private int playClueIndicates(int player, Color color) {
		CluedHand h = hands[player];
		
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i = h.size() - 1; i >= 0; i--) {
			if (h.getColor(i).same(color) && possiblyPlayable(player, i, color) && !h.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!h.isCClued(i) && h.isNClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!h.isCClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}

	private int playClueIndicates(int player, Number number) {
		CluedHand h = hands[player];
		
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i = h.size() - 1; i >= 0; i--) {
			if (h.getNumber(i) == number && possiblyPlayable(player, i, number) && !h.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!h.isNClued(i) && h.isCClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!h.isNClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	private int playClueIndicates(Color color, ArrayList<Integer> pos) {
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i : pos) {
			if (possiblyPlayable(i, color) && !ownHand.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!ownHand.isCClued(i) && ownHand.isNClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!ownHand.isCClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	private int playClueIndicates(Number number, ArrayList<Integer> pos) {
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i : pos) {
			if (possiblyPlayable(i, number) && !ownHand.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!ownHand.isNClued(i) && ownHand.isCClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!ownHand.isNClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	private int finesseClueIndicates(int player, Color color) {
		CluedHand h = hands[player];
		
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i = h.size() - 1; i >= 0; i--) {
			if (h.getColor(i).same(color) && possiblyFinessable(player, i, color) && !h.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!h.isCClued(i) && h.isNClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!h.isCClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}

	private int finesseClueIndicates(int player, Number number) {
		CluedHand h = hands[player];
		
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i = h.size() - 1; i >= 0; i--) {
			if (h.getNumber(i) == number && possiblyFinessable(player, i, number) && !h.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!h.isNClued(i) && h.isCClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!h.isNClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	private int finesseClueIndicates(Color color, ArrayList<Integer> pos) {
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i : pos) {
			if (possiblyFinessable(i, color) && !ownHand.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!ownHand.isCClued(i) && ownHand.isNClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!ownHand.isCClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	private int finesseClueIndicates(Number number, ArrayList<Integer> pos) {
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i : pos) {
			if (possiblyFinessable(i, number) && !ownHand.getMark(i).cluedPlay()) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int i : possible) {
			if (!ownHand.isNClued(i) && ownHand.isCClued(i)) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (!ownHand.isNClued(i)) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	private int finessedPos(int player, Color color, Number number) {
		CluedHand h = hands[player];
		
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int p = h.size() - 1; p >= 0; p--) {
			if (h.getCard(p).possible(color, number)) {
				possible.add(p);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int p : possible) {
			if (h.isCClued(p) && h.isNClued(p)) return p;
		}
		
		for (int p : possible) {
			if (h.isClued(p)) return p;
		}
		
		return possible.get(0);
	}
	
	private int finessedPos(Color color, Number number) {
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int p = ownHand.size() - 1; p >= 0; p--) {
			if (ownHand.getCard(p).possible(color, number)) {
				possible.add(p);
			}
		}
		
		if (possible.size() == 0) return -1;
		
		for (int p : possible) {
			if (ownHand.isCClued(p) && ownHand.isNClued(p)) return p;
		}
		
		for (int p : possible) {
			if (ownHand.isClued(p)) return p;
		}
		
		return possible.get(0);
	}
	
	private void findFinessed(Color fColor, Number fNumber, int fromPlayer, int player) {
		if (fromPlayer < player) {
			// The player being finessed can't be this player
			for (int f = fromPlayer + 1; f < player; f++) {
				CluedHand fh = hands[f];
				int fp = finessedPos(f, fColor, fNumber);
				
				if (fh.getColor(fp) == fColor && fh.getNumber(fp) == fNumber) {
					fh.mark(Marker.FINESSED, fp);
					break;
				}
			}
		} else {
			boolean found = false;
			
			for (int f = fromPlayer + 1; f < hands.length; f++) {
				CluedHand fh = hands[f];
				int fp = finessedPos(f, fColor, fNumber);
				if (fp == -1) continue;
				
				if (fh.getColor(fp) == fColor && fh.getNumber(fp) == fNumber) {
					fh.mark(Marker.FINESSED, fp);
					found = true;
					break;
				}
			}
			
			if (!found) for (int f = 0; f < player; f++) {
				CluedHand fh = hands[f];
				int fp = finessedPos(f, fColor, fNumber);
				if (fp == -1) continue;
				
				if (fh.getColor(fp) == fColor && fh.getNumber(fp) == fNumber) {
					fh.mark(Marker.FINESSED, fp);
					found = true;
					break;
				}
			}
			
			if (!found) {
				int fp = finessedPos(fColor, fNumber);
				if (fp != -1) ownHand.mark(Marker.FINESSED, fp);
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
			discard.get(c.color)[c.number.ordinal()]++;
			lives--;
		}
		
		willBePlayed.get(c.color)[c.number.ordinal()] = true;
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
		
		willBePlayed.get(c.color)[c.number.ordinal()] = true;
		
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
		
		int pos = playClueIndicates(player, color);
		if (pos == -1) {
			// If nothing is indicated to be played, it must be a finesse
			if (FINESSE) {
				// Mark finessed card for the hand receiving the clue
				pos = finesseClueIndicates(player, color);
				hand.mark(Marker.FINESSED, pos);
				
				Color fColor = hand.getColor(pos);
				Number fNumber = Number.VALUES[hand.getNumber(pos).ordinal() - 1];
				willBePlayed.get(fColor)[fNumber.ordinal()] = true;
				willBePlayed.get(fColor)[fNumber.ordinal() + 1] = true;
				
				// Mark the finessed card for the hand being finessed
				findFinessed(fColor, fNumber, fromPlayer, player);
			}
		} else {
			// Mark the clued card to be played
			hand.mark(Marker.CLUED_PLAY, pos);
			willBePlayed.get(hand.getColor(pos))[hand.getNumber(pos).ordinal()] = true;
		}
		
		hand.clue(color);
	}
	
	@Override
	public void clue(int fromPlayer, Color color, ArrayList<Integer> pos) {
		clues--;
		
		// Clue to this player
		int p = playClueIndicates(color, pos);
		if (p == -1) {
			// If nothing is indicated to be played, it must be a finesse
			if (FINESSE) {
				// Mark own hand as finessed
				p = finesseClueIndicates(color, pos);
				ownHand.mark(Marker.FINESSED, p);
			}
		} else {
			// Mark the clued card to be played
			ownHand.mark(Marker.CLUED_PLAY, p);
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
		
		// Check if it's a discard clue
		if (CLUE_DISCARD) if (clued.size() == 1 && clues == 0) {
			int last = clued.get(0);
			if (!hand.isNClued(last) && getChop(player) == last && dangerous(player, last)) {
				hand.mark(Marker.NOT_DISCARD, last);
				
				hand.clue(number);
				return;
			}
		}
		
		int pos = playClueIndicates(player, number);
		if (pos == -1) {
			// If nothing is indicated to be played, it must be a finesse
			if (FINESSE) {
				// Mark finessed card for the hand receiving the clue
				pos = finesseClueIndicates(player, number);
				hand.mark(Marker.FINESSED, pos);
				
				Color fColor = hand.getColor(pos);
				Number fNumber = Number.VALUES[hand.getNumber(pos).ordinal() - 1];
				willBePlayed.get(fColor)[fNumber.ordinal()] = true;
				willBePlayed.get(fColor)[fNumber.ordinal() + 1] = true;
				
				// Mark the finessed card for the hand being finessed
				findFinessed(fColor, fNumber, fromPlayer, player);
			}
		} else {
			// Mark the clued card to be played
			hand.mark(Marker.CLUED_PLAY, pos);
			willBePlayed.get(hand.getColor(pos))[hand.getNumber(pos).ordinal()] = true;
		}
		
		hand.clue(number);
	}
	
	@Override
	public void clue(int fromPlayer, Number number, ArrayList<Integer> pos) {
		clues--;
		
		// Clue to this player
		
		// Check if it's a discard clue
		if (CLUE_DISCARD) if (pos.size() == 1 && clues == 0) {
			int last = pos.get(0);
			if (!ownHand.isNClued(last) && getChop(false) == last && possiblyDangerous(last)) {
				ownHand.mark(Marker.NOT_DISCARD, last);
				
				ownHand.clue(number, pos);
				return;
			}
		}
		
		// Calculate which card is indicated, and mark it
		int p = playClueIndicates(number, pos);
		if (p == -1) {
			if (FINESSE) {
				// Mark own hand as finessed
				p = finesseClueIndicates(number, pos);
				ownHand.mark(Marker.FINESSED, p);
			}
		} else {
			// Mark the clued card to be played
			ownHand.mark(Marker.CLUED_PLAY, p);
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
		int totalAmount = mode.hard && (c.color == Color.MULTI || c.color == Color.RAINBOW) ? 1 : c.number.amount;
		if (cardsSeen.get(c.color)[c.number.ordinal()] == totalAmount) {
			ownHand.antiClueAll(c.color, c.number);
		}
	}
	
	class PlayerComp implements Comparator<Integer> {
		@Override
		public int compare(Integer a, Integer b) {
			int aPlays = numPlays(a);
			int bPlays = numPlays(b);
			
			if (aPlays < bPlays) return -1;
			if (aPlays > bPlays) return 1;
			if (a < b) return -1;
			if (a > b) return 1;
			return 0;
		}
	}
}
