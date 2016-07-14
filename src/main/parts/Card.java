package main.parts;
import main.enums.Color;
import main.enums.Number;

/**
 * Class for simple cards.
 */
public class Card {
	public final Color color;
	public final Number number;
	
	/**
	 * Constructor.
	 * 
	 * @param c The color
	 * @param n The number
	 */
	public Card(Color c, Number n) {
		color = c;
		number = n;
	}
}
