package game.logic.event;

import game.player.AbstractPlayer;

public class GameEndEvent
		extends AbstractGameEvent
{
	public final String REASON;
	
	public GameEndEvent(String reason)
	{
		super(null);
		REASON = reason;
	}
	
	public static final GameEndEvent DEATH = new GameEndEvent("Out of lives");
	public static final GameEndEvent CARD_LIMIT_REACHED = new GameEndEvent("Ran out of cards");
	public static final GameEndEvent MAX_SCORE_REACHED = new GameEndEvent("Maximum score possible reached");
}
