package game.card;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the hand of cards of a player.
 * <p>
 * In the internal array, 0 represents the left side of the hand.
 */
public class Hand
{
	protected List<HintedCard> cards;
	
	public Hand()
	{
		cards = new LinkedList<>();
	}
	
	public void giveCard(Card c)
	{
		cards.add(0, new HintedCard(c));
	}
	
	public List<HintedCard> getCards()
	{
		return cards;
	}
	
	public HintedCard getCard(int pos)
	{
		return cards.get(pos);
	}
	
	public HintedCard removeCard(int pos)
	{
		try
		{
			return cards.remove(pos);
		}
		catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}
	
	/**
	 * A class used to protect the content of the Hand.
	 */
	private static class ProtectedHand
			extends Hand
	{
		private ProtectedHand(List<HintedCard> cards, boolean hideCard)
		{
			for (HintedCard c : cards)
			{
				this.cards.add(c.getProtected(hideCard));
			}
		}
	}
	
	public Hand getProtected(boolean hideCard)
	{
		return new ProtectedHand(cards, hideCard);
	}
	
	public void applyClue(Card.Color clue)
	{
		for (HintedCard c : cards)
		{
			c.applyClue(clue);
		}
	}
	
	public void applyClue(Card.Number clue)
	{
		for (HintedCard c : cards)
		{
			c.applyClue(clue);
		}
	}
}
