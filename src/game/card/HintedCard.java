package game.card;

/**
 * A wrapper for Card that overlays the card with hints. The hints are processed automatically through the two applyHint methods.
 */
public class HintedCard
{
	public Card card;
	
	/** The currently known hint for the card's color; null for unknown */
	private Card.Color hintedColor;
	/** The currently known hint for the card's number; null for unknown */
	private Card.Number hintedNumber;
	
	public HintedCard(Card card)
	{
		this(card, null, null);
	}
	
	public HintedCard(Card card, Card.Color hintedColor, Card.Number hintedNumber)
	{
		this.card = card;
		this.hintedColor = hintedColor;
		this.hintedNumber = hintedNumber;
	}
	
	public void applyHint(Card.Color hint)
	{
		// Special case for multi mode
		if (card.color == Card.Color.MULTI) {
			if (hintedColor == null || hintedColor == hint) {
				hintedColor = hint;
			}
			else {
				hintedColor = Card.Color.MULTI;
			}
		}
		
		// General case
		if (card.color == hint) {
			hintedColor = hint;
		}
	}
	
	public void applyHint(Card.Number hint)
	{
		if (card.number == hint) {
			hintedNumber = hint;
		}
	}
	
	public Card.Color getHintedColor()
	{
		return hintedColor;
	}
	
	public Card.Number getHintedNumber()
	{
		return hintedNumber;
	}
	
	/**
	 * A class used to protect the content of the HintedCard.
	 */
	private static class Protected
			extends HintedCard
	{
		public Protected(Card card, Card.Color hintedColor, Card.Number hintedNumber)
		{
			super(card, hintedColor, hintedNumber);
		}
	}
	
	/**
	 * Adds protection to the hinted card so that this can be sent to players safely.
	 *
	 * @param hideCard Whether to hide the card wrapped or not
	 * @return A precessed version of the hint wrapper that disables hinting and possible has the actual card hidden
	 */
	public HintedCard getProtected(boolean hideCard)
	{
		return new Protected(hideCard ? null : card, hintedColor, hintedNumber);
	}
}
