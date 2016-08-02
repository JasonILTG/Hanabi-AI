package main.parts;

import main.enums.Color;
import main.enums.Number;

/**
 * Class for simple cards.
 */
public class Card
{
	public final Color color;
	public final Number number;
	private Color colorClue;
	private Number numberClue;
	
	/**
	 * Constructor.
	 * 
	 * @param c The color
	 * @param n The number
	 */
	public Card(Color c, Number n)
	{
		color = c;
		number = n;
		colorClue = null;
		numberClue = null;
	}
	
	@Override
	public String toString()
	{
		return color.ansi() + String.valueOf(number.ordinal() + 1) + Color.NONE.ansi();
	}
	
	@Override
	public boolean equals(Object o)
	{
		Card c = (Card) o;
		return color == c.color && number == c.number;
	}
	
	public void clearHints()
	{
		colorClue = null;
		numberClue = null;
	}
	
	/**
	 * Adds a clue to this card, if applicable. This method will be called every time the hand that this card is in is hinted.
	 */
	public void giveColorClue(Color clue)
	{
		if (color.isClueableBy(clue)) {
			if (color == Color.MULTI && colorClue != clue) {
				// If this is a multi and the new hint is different from the previous hint, mark this one as multi.
				colorClue = Color.MULTI;
			}
			else {
				// Mark the color
				colorClue = clue;
			}
		}
	}
	
	/**
	 * Adds a number clue to this card, if applicable. This method will be called every time the hand that this card is in is hinted.
	 */
	public void giveNumberClue(Number clue)
	{
		if (number == clue) {
			numberClue = clue;
		}
	}
	
	// Getters for the clues
	public Color getColorClue()
	{
		return colorClue;
	}
	
	public Number getNumberClue()
	{
		return numberClue;
	}
	
}
