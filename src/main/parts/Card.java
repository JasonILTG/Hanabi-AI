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
	
	@Override
	public String toString() {
		return color.ansi() + String.valueOf(number.ordinal() + 1) + Color.NONE.ansi();
	}
	
	@Override
	public boolean equals(Object o) {
		Card c = (Card) o;
		return color == c.color && number == c.number;
	}
}
