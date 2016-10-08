package game.logic.event;

import game.player.AbstractPlayer;

public abstract class AbstractEvent
{
	/** The player who started the action */
	public final AbstractPlayer actor;
	
	/**
	 * @param actor The player who is executing the action
	 */
	public AbstractEvent(AbstractPlayer actor)
	{
		this.actor = actor;
	}
}
