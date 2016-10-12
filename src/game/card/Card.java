package game.card;

public class Card
{
	public enum Color
	{
		RED, YELLOW, GREEN, BLUE, WHITE, /** For the mode where hinting a color does not hint rainbow */RAINBOW, /** For the game mode where hinting a color also hints rainbow */MULTI;
		public static final Color[] MAIN_COLORS = { RED, YELLOW, GREEN, BLUE, WHITE };
	}
	
	public enum Number
	{
		ONE, TWO, THREE, FOUR, FIVE;
		public static final Number[] VALUES = values();
		
		/** The value of this card, which is one of 1, 2, 3, 4, and 5 */
		public final int value;
		
		Number()
		{
			value = ordinal() + 1;
		}
	}
	
	public final Color color;
	public final Number number;
	
	public Card(Color color, Number number)
	{
		this.color = color;
		this.number = number;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		// Compare by color and number
		if (obj == this) {
			return true;
		}
		else if (!(obj instanceof Card)) {
			return false;
		}
		else {
			Card other = (Card) obj;
			return color == other.color && number == other.number;
		}
	}
	
	@Override
	public String toString()
	{
		return color + " " + number.value;
	}
}
