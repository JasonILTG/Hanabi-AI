package game.logic.event;

import game.logic.HanabiGame;

public abstract class AbstractGameEvent
{
	/** The player who started the action */
	public final HanabiGame.PlayerIdentifier actor;
	
	/**
	 * @param actor The player who is executing the action
	 */
	public AbstractGameEvent(HanabiGame.PlayerIdentifier actor)
	{
		this.actor = actor;
	}
}
