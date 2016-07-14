package main.parts;

import java.util.ArrayList;

import main.enums.Color;
import main.enums.Number;

public class CluedHand {
	private ArrayList<CluedCard> cards;
	
	public CluedHand(ArrayList<Card> cards) {
		this.cards = new ArrayList<CluedCard>();
		
		for (Card card : cards) {
			this.cards.add(new CluedCard(card));
		}
	}
	
	public int size() {
		return cards.size();
	}
	
	public boolean isClued(int pos) {
		return cards.get(pos).isClued();
	}
	
	public void draw(Card card) {
		cards.add(new CluedCard(card));
	}
	
	public void discard(int pos) {
		cards.remove(pos);
	}
	
	public Color getColor(int pos) {
		return cards.get(pos).color;
	}
	
	public Number getNumber(int pos) {
		return cards.get(pos).number;
	}
	
	public void clue(Color color) {
		for (CluedCard card : cards) {
			if (card.color == color || card.color == Color.MULTI) {
				card.clue(color);
			} else {
				card.antiClue(color);
			}
		}
	}
	
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
