import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	public static final int[] STD_DIST = new int[] { 1, 1, 1, 2, 2, 3, 3, 4, 4, 5 };
	public static final int[] HARD_DIST = new int[] { 1, 2, 3, 4, 5 };
	
	private final ArrayList<Card> cardSet;
	private ArrayList<Card> cards;
	
	public Deck() {
		cardSet = new ArrayList<Card>();
		cards = new ArrayList<Card>();
		
		for (int c = 1; c <= 5; c++) {
			for (int n : STD_DIST) {
				cards.add(new Card(c, n));
				cardSet.add(new Card(c, n));
			}
		}
	}
	
	public Deck(Card[] additional) {
		this();
		
		for (Card c : additional) {
			cards.add(c);
			cardSet.add(c);
		}
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public boolean empty() {
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
