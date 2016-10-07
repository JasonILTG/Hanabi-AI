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
	private List<HintedCard> cards;
	
	public Hand()
	{
		cards = new LinkedList<>();
	}
	
	public void giveCard(Card c)
	{
		cards.add(0, new HintedCard(c));
	}
	
	public List<HintedCard> viewCards()
	{
		return cards;
	}
	
	public HintedCard viewCard(int pos)
	{
		return cards.get(pos);
	}
	
	public HintedCard removeCard(int pos)
	{
		return cards.remove(pos);
	}
}
