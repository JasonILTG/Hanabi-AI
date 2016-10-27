package game.card;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiscardPile
{
	private List<Card> discards;
	
	public DiscardPile()
	{
		discards = new LinkedList<>();
	}
	
	public void addCard(Card c)
	{
		discards.add(c);
	}
	
	public Map<Card, Integer> getDiscardCount()
	{
		Map<Card, Integer> discardMap = new HashMap<>();
		
		// Go through the cards and add to the discard count
		for (Card c : discards)
		{
			// Make sure there is an entry
			if (discardMap.get(c) == null)
			{
				discardMap.put(c, 1);
			}
			else
			{
				discardMap.put(c, discardMap.get(c) + 1);
			}
		}
		
		return discardMap;
	}
}
