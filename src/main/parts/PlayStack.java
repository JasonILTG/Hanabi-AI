package main.parts;
import java.util.ArrayList;

import main.enums.Color;
import main.enums.Number;

public class PlayStack {
	public final Color color;
	public final int[] dist;
	private ArrayList<Card> cards;
	private Number next;
	
	public PlayStack(Color c) {
		color = c;
		dist = new int[] { 3, 2, 2, 2, 1 };
		cards = new ArrayList<Card>();
		next = Number.ONE;
	}
	
	public PlayStack(Color c, int[] dist) {
		color = c;
		this.dist = dist;
		cards = new ArrayList<Card>();
		next = Number.ONE;
	}
	
	public int getScore() {
		return next.ordinal();
	}
	
	public boolean play(Card card) {
		if (card.color == color && card.number == next) {
			cards.add(card);
			next = next.next();
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Card> reset() {
		ArrayList<Card> toReturn = cards;
		cards = new ArrayList<Card>();
		return toReturn;
	}
}
