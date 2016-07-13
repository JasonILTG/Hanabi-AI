package main.parts;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import main.enums.Color;
import main.enums.Number;

public class Deck {
	public static final int[] STD_DIST = new int[] { 1, 1, 1, 2, 2, 3, 3, 4, 4, 5 };
	public static final int[] HARD_DIST = new int[] { 1, 2, 3, 4, 5 };
	
	private final ArrayList<Card> cardSet;
	private ArrayList<Card> cards;
	
	public Deck() {
		cardSet = new ArrayList<Card>();
		cards = new ArrayList<Card>();
		
		for (Color color : Color.STANDARD) {
			for (Number number : Number.VALUES) {
				for (int i = 0; i < number.amount(false); i++) {
					Card c = new Card(color, number);
					cardSet.add(c);
					cards.add(c);
				}
			}
		}
	}
	
	public Deck(Color extraColor, boolean hard) {
		this();
		
		for (Number number : Number.VALUES) {
			for (int i = 0; i < number.amount(hard); i++) {
				Card c = new Card(extraColor, number);
				cardSet.add(c);
				cards.add(c);
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public boolean isEmpty() {
		return cards.size() == 0;
	}
	
	public Card draw() {
		return cards.remove(0);
	}
	
	public void reset() {
		cards = new ArrayList<Card>(cardSet);
		shuffle();
	}
}
