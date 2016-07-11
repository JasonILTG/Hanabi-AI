import java.util.ArrayList;

public class PlayStack {
	public final int color;
	public final int[] dist;
	private ArrayList<Card> cards;
	private int next;
	
	public PlayStack(int c) {
		color = c;
		dist = new int[] { 3, 2, 2, 2, 1 };
		cards = new ArrayList<Card>();
		next = 1;
	}
	
	public PlayStack(int c, int[] dist) {
		color = c;
		this.dist = dist;
		cards = new ArrayList<Card>();
		next = 1;
	}
	
	public int getScore() {
		return next - 1;
	}
	
	public boolean play(Card card) {
		if (card.color == color && card.number == next) {
			cards.add(card);
			next++;
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
