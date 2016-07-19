package main.parts;
import java.util.ArrayList;

import main.enums.Color;
import main.enums.Number;

/**
 * Class for hands.
 */
public class Hand {
	private ArrayList<Card> cards;
	
	/**
	 * Constructor.
	 */
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	/**
	 * @return The ArrayList of cards in hand
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	/**
	 * @return The number of cards in hand
	 */
	public int size() {
		return cards.size();
	}
	
	/**
	 * Adds the given card to the hand.
	 * 
	 * @param card The card to add
	 */
	public void draw(Card card) {
		cards.add(card);
	}
	
	/**
	 * Removes and returns the card from the given position.
	 * 
	 * @param pos The position to remove the card from
	 * @return The removed card
	 */
	public Card take(int pos) {
		Card c = cards.remove(pos);
		return c;
	}
	
	/**
	 * @param pos The position
	 * @return The color of the card in that position
	 */
	public Color getColor(int pos) {
		return cards.get(pos).color;
	}
	
	/**
	 * @param pos The position
	 * @return The number of the card in that position
	 */
	public Number getNumber(int pos) {
		return cards.get(pos).number;
	}
	
	/**
	 * @param color The color clued
	 * @return The positions of the colors clued
	 */
	public ArrayList<Integer> cluedCards(Color color) {
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).color == color) {
				clued.add(i);
			}
		}
		
		return clued;
	}
	
	/**
	 * @param number The number clued
	 * @return The positions of the numbers clued
	 */
	public ArrayList<Integer> cluedCards(Number number) {
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).number == number) {
				clued.add(i);
			}
		}
		
		return clued;
	}
	
	/**
	 * Checks whether the given color clue is non-empty for this hand.
	 * 
	 * @param color The color of the clue
	 * @return Whether the color clue is non-empty
	 */
	public boolean clueNonEmpty(Color color) {
		for (Card c : cards) {
			if (c.color.same(color)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the given number clue is non-empty for this hand.
	 * 
	 * @param number The number of the clue
	 * @return Whether the number clue is non-empty
	 */
	public boolean clueNonEmpty(Number number) {
		for (Card c : cards) {
			if (c.number == number) {
				return true;
			}
		}
		return false;
	}
}
