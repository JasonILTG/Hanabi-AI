import java.util.ArrayList;
import java.util.Random;

public class Deck {
	public static final int[] STD_DIST = new int[] { 1, 1, 1, 2, 2, 3, 3, 4, 4, 5 };
	
	private ArrayList<Card> cards;
	
	public Deck() {
		cards = new ArrayList<Card>();
		
		for (int c = 1; c <= 5; c++) {
			for (int n : STD_DIST) {
				cards.add(new Card(c, n));
			}
		}
		
		shuffle();
	}
	
	public Deck(Card[] additional) {
		this();
		
		for (Card c : additional) {
			cards.add(c);
		}
		
		shuffle();
	}
	
	public void shuffle() {
		ArrayList<Card> shuffled = new ArrayList<Card>();
		Random rand = new Random();
		
		while (cards.size() > 0) {
			shuffled.add(cards.remove(rand.nextInt(cards.size())));
		}
		
		cards = shuffled;
	}
	
	public Card draw() {
		return cards.remove(0);
	}
}
