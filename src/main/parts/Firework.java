package main.parts;
import main.enums.Color;
import main.enums.Number;

/**
 * Class for a firework (stack of cards of one color in play).
 */
public class Firework {
	public final Color color;
	
	/** The next number needed */
	private Number next;
	
	/**
	 * Constructor.
	 * 
	 * @param c The color of the firework
	 */
	public Firework(Color c) {
		color = c;
		next = Number.ONE;
	}
	
	/**
	 * @return The current score of the firework
	 */
	public int getScore() {
		return next.ordinal();
	}
	
	/**
	 * Attempts to play the given card on the firework.
	 * 
	 * @param card The card to play
	 * @return Whether it is a valid play
	 */
	public boolean play(Card card) {
		if (card.color == color && card.number == next) {
			next = next.next();
			return true;
		}
		
		return false;
	}
}
