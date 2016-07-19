package main.players.jason;

import java.util.ArrayList;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;

/**
 * Class for a hand of clued cards.
 */
public class HiddenHand {
	private final GameMode mode;
	
	private ArrayList<CluedCard> cards;
	
	/**
	 * Constructor for a hand of unknown cards.
	 * 
	 * @param size The number of cards
	 */
	public HiddenHand(int size, GameMode mode) {
		this.mode = mode;
		this.cards = new ArrayList<CluedCard>();
		
		for (int i = 0; i < size; i++) {
			cards.add(new CluedCard(mode));
		}
	}
	
	/**
	 * @return The number of cards in hand
	 */
	public int size() {
		return cards.size();
	}
	
	/**
	 * @param pos The position
	 * @return The card at the given position
	 */
	public CluedCard getCard(int pos) {
		return cards.get(pos);
	}
	
	/**
	 * @param pos The position
	 * @return Whether the card in the given position is clued
	 */
	public boolean isClued(int pos) {
		return cards.get(pos).isClued();
	}
	
	/**
	 * @param pos The position
	 * @return Whether the card in the given position is clued
	 */
	public boolean isCClued(int pos) {
		return cards.get(pos).isCClued();
	}
	
	/**
	 * @param pos The position
	 * @return Whether the card in the given position is clued
	 */
	public boolean isNClued(int pos) {
		return cards.get(pos).isNClued();
	}
	
	/**
	 * Marks the card at the given position with the given marker.
	 * 
	 * @param m The marker
	 * @param pos The position
	 */
	public void mark(Marker m, int pos) {
		cards.get(pos).mark(m);
	}
	
	/**
	 * @param pos The position
	 * @return The mark on the card at the given position
	 */
	public Marker getMark(int pos) {
		return cards.get(pos).getMark();
	}
	
	/**
	 * @param c The clued card to find
	 * @return Whether the hand contains the clued card
	 */
	public boolean contains(CluedCard c) {
		return cards.contains(c);
	}
	
	/**
	 * @param color The color
	 * @param number The number
	 * @return The most playable card possibly of the given color and number
	 */
	public int mostPlayable(Color color, Number number) {
		ArrayList<Integer> possible = new ArrayList<Integer>();
		for (int i = cards.size() - 1; i >= 0; i--) {
			if (cards.get(i).possible(color) && cards.get(i).possible(number)) {
				possible.add(i);
			}
		}
		
		if (possible.size() == 0) {
			return -1;
		}
		
		for (int i : possible) {
			if (cards.get(i).isCClued() && cards.get(i).isNClued()) {
				return i;
			}
		}
		
		for (int i : possible) {
			if (cards.get(i).isClued()) {
				return i;
			}
		}
		
		return possible.get(0);
	}
	
	/**
	 * Adds the given card to the hand.
	 * 
	 * @param card The card to add
	 */
	public void draw() {
		cards.add(new CluedCard(mode));
	}
	
	/**
	 * Remove the card in the given position.
	 * 
	 * @param pos The position of the card to remove
	 * @return The removed card
	 */
	public CluedCard take(int pos) {
		return cards.remove(pos);
	}
	
	/**
	 * @param pos The position
	 * @return The color of the card at the given position
	 */
	public Color getColor(int pos) {
		return cards.get(pos).color;
	}
	
	/**
	 * @param pos The position
	 * @return The number of the card at the given position
	 */
	public Number getNumber(int pos) {
		return cards.get(pos).number;
	}
	
	/**
	 * Gives the specified color clue to the specified cards in the hand.
	 * 
	 * @param color The color to clue
	 * @param pos The positions of the cards to clue
	 */
	public void clue(Color color, ArrayList<Integer> pos) {
		for (int i = 0; i < cards.size(); i++) {
			if (pos.contains(i)) {
				cards.get(i).clue(color);
			} else {
				cards.get(i).antiClue(color);
			}
		}
	}
	
	/**
	 * Gives the specified number clue to all of the cards in the hand.
	 * 
	 * @param number The number to clue
	 * @param pos The positions of the cards to clue
	 */
	public void clue(Number number, ArrayList<Integer> pos) {
		for (int i = 0; i < cards.size(); i++) {
			if (pos.contains(i)) {
				cards.get(i).clue(number);
			} else {
				cards.get(i).antiClue(number);
			}
		}
	}
	
	
	@Override
	public String toString() {
		return "";
	}
}
