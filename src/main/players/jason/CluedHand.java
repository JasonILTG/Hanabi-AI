package main.players.jason;

import java.util.ArrayList;

import main.enums.Color;
import main.enums.Number;
import main.parts.Card;

/**
 * Class for a hand of clued cards.
 */
public class CluedHand {
	private ArrayList<CluedCard> cards;
	
	/**
	 * Constructor.
	 * 
	 * @param cards The ArrayList of cards the hand starts with.
	 */
	public CluedHand(ArrayList<Card> cards) {
		this.cards = new ArrayList<CluedCard>();
		
		for (Card card : cards) {
			this.cards.add(new CluedCard(card));
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
	 * @return Whether the card in the given position is clued
	 */
	public boolean isClued(int pos) {
		return cards.get(pos).isClued();
	}
	
	/**
	 * Adds the given card to the hand.
	 * 
	 * @param card The card to add
	 */
	public void draw(Card card) {
		cards.add(new CluedCard(card));
	}
	
	/**
	 * Remove the card in the given position.
	 * 
	 * @param pos The position of the card to remove
	 */
	public void take(int pos) {
		cards.remove(pos);
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
	 * Gives the specified color clue to all of the cards in the hand.
	 * 
	 * @param color The color to clue
	 */
	public void clue(Color color) {
		for (CluedCard card : cards) {
			if (card.color == color || card.color == Color.MULTI) {
				card.clue(color);
			} else {
				card.antiClue(color);
			}
		}
	}
	
	/**
	 * Gives the specified number clue to all of the cards in the hand.
	 * 
	 * @param number The number to clue
	 */
	public void clue(Number number) {
		for (CluedCard card : cards) {
			if (card.number == number) {
				card.clue(number);
			} else {
				card.antiClue(number);
			}
		}
	}
}
