package game.card;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiscardPile
{
	private List<Card> discards;
	private Map<Card, Integer> discardMap;
	
	public DiscardPile()
	{
		discards = new LinkedList<>();
		discardMap = new HashMap<>();
	}
	
	public void addCard(Card c)
	{
		discards.add(c);

		if (!discardMap.containsKey(c))
		{
			discardMap.put(c, 1);
		}
		else
		{
			discardMap.put(c, discardMap.get(c) + 1);
		}
	}
	
	public Map<Card, Integer> getDiscardCount()
	{
		return discardMap;
	}

	/**
	 * Re-evaluates the discard map to ensure that the map is indeed correct.
	 */
	public void recalculateDiscardMap()
	{
		discardMap.clear();
		
		for (Card c : discards)
		{
			if (!discardMap.containsKey(c))
			{
				discardMap.put(c, 1);
			}
			else
			{
				discardMap.put(c, discardMap.get(c) + 1);
			}
		}
	}
}
