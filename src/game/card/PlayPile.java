package game.card;

import game.GameSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * The pile where successful plays go.
 */
public class PlayPile
{
	private Map<Card.Color, Integer> scoreMap;
	
	public PlayPile(GameSettings settings)
	{
		scoreMap = new HashMap<>();
		
		// Add universal colors
		for (Card.Color c : Card.Color.MAIN_COLORS) {
			scoreMap.put(c, 0);
		}
		
		// Add additional colors based on the game difficulty
		if (settings.isMulti) {
			scoreMap.put(Card.Color.MULTI, 0);
		}
		else if (settings.isRainbow) {
			scoreMap.put(Card.Color.RAINBOW, 0);
		}
	}
}
