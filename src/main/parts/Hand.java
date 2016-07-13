package main.parts;
import java.util.ArrayList;

import main.enums.Color;
import main.enums.Number;

public class Hand {
	private ArrayList<Card> cards;
	
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	public int size() {
		return cards.size();
	}
	
	public void draw(Card card) {
		cards.add(card);
	}
	
	public ArrayList<Card> reset() {
		ArrayList<Card> toReturn = new ArrayList<Card>(cards);
		cards = new ArrayList<Card>();
		
		return toReturn;
	}
	
	public Card discard(int pos) {
		Card c = cards.remove(pos);
		return c;
	}
	
	public Color getColor(int pos) {
		return cards.get(pos).color;
	}
	
	public Number getNumber(int pos) {
		return cards.get(pos).number;
	}
	
	public boolean clueValid(Color color) {
		for (Card c : cards) {
			if (c.color == color || c.color == Color.MULTI) {
				return true;
			}
		}
		return false;
	}
	
	public boolean clueValid(Number number) {
		for (Card c : cards) {
			if (c.number == number) {
				return true;
			}
		}
		return false;
	}
}
