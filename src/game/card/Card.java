package game.card;

public class Card
{
	public enum Color
	{
		RED, YELLOW, GREEN, BLUE, WHITE, /** For the mode where hinting a color does not hint rainbow */RAINBOW, /** For the game mode where hinting a color also hints rainbow */MULTI
	}
	
	public enum Number
	{
		ONE, TWO, THREE, FOUR, FIVE;
		public static final Number[] VALUES = values();
	}
	
	public final Color color;
	public final Number number;
	
	public Card(Color color, Number number)
	{
		this.color = color;
		this.number = number;
	}
}
