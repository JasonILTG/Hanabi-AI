package game.logic.event;

import game.player.AbstractPlayer;

/**
 * Marks the start of a game (not the initialization of the entire system).
 */
public class GameStartEvent
		extends AbstractGameEvent
{
	public GameStartEvent()
	{
		super(null);
	}
}
