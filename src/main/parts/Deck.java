package main.parts;
import java.util.ArrayList;
import java.util.Collections;

import main.enums.Color;
import main.enums.Number;

/**
 * Class for the draw deck.
 */
public class Deck {
	/** The ArrayList of all of the cards in the set being used */
	private final ArrayList<Card> cardSet;
	/** The ArrayList of all of the cards currently in the deck */
	private ArrayList<Card> cards;
	
	/**
	 * Default constructor.
	 */
	public Deck() {
		cardSet = new ArrayList<Card>();
		cards = new ArrayList<Card>();
		
		for (Color color : Color.STANDARD) {
			for (Number number : Number.VALUES) {
				for (int i = 0; i < number.amount; i++) {
					Card c = new Card(color, number);
					cardSet.add(c);
					cards.add(c);
				}
			}
		}
	}
	
	/**
	 * Constructor for decks with an extra color.
	 * 
	 * @param extraColor The extra color
	 * @param hard Whether there is only one of each number in that color
	 */
	public Deck(Color extraColor, boolean hard) {
		this();
		
		for (Number number : Number.VALUES) {
			for (int i = 0; i < (hard ? 1 : number.amount); i++) {
				Card c = new Card(extraColor, number);
				cardSet.add(c);
				cards.add(c);
			}
		}
	}
	
	/**
	 * Shuffles the cards in the deck.
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
	 * @return Whether the deck is empty
	 */
	public boolean isEmpty() {
		return cards.size() == 0;
	}
	
	/**
	 * Removes a card from the deck.
	 * 
	 * @return The card removed
	 */
	public Card draw() {
		return cards.remove(0);
	}
	
	/**
	 * Resets the deck to include all of the cards in the set, and shuffles it.
	 */
	public void reset() {
		cards = new ArrayList<Card>(cardSet);
		shuffle();
	}
}
