package game.card;

public class Card
{
	public enum Color
	{
		RED, GREEN, YELLOW, BLUE, WHITE, RAINBOW
	}
	
	public enum Number
	{
		ONE, TWO, THREE, FOUR, FIVE
	}
	
	public final Color color;
	public final Number number;
	
	public Card(Color color, Number number)
	{
		this.color = color;
		this.number = number;
	}
}
