package game.card;

import game.GameSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * The pile where successful plays go.
 */
public class PlayPile
		implements ICardSink
{
	private GameSettings settings;
	private Map<Card.Color, Integer> scoreMap;
	
	public PlayPile(GameSettings settings)
	{
		scoreMap = new HashMap<>();
		this.settings = settings;
	}
	
	public void initialize()
	{
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
	
	public boolean canPlay(Card c)
	{
		return c.number.value == scoreMap.get(c.color) + 1;
	}
	
	@Override
	public void addCard(Card c)
			throws RuntimeException
	{
		// Check if the move is allowed.
		if (!canPlay(c)) {
			throw new RuntimeException("Cannot play card: " + c + " onto the played cards pile.");
		}
		
		// Add the card to the pile
		scoreMap.put(c.color, c.number.value);
	}
	
	/**
	 * @return The score on that color, i.e. the number on the top card of that color pile
	 */
	public int getScore(Card.Color c)
	{
		return scoreMap.get(c);
	}
	
	/**
	 * @return The current score
	 */
	public int getTotalScore()
	{
		int totalScore = 0;
		
		for (Map.Entry<Card.Color, Integer> entry : scoreMap.entrySet()) {
			totalScore += entry.getValue();
		}
		
		return totalScore;
	}
}
