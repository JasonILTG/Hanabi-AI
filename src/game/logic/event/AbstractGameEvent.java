package game.logic.event;

import game.logic.HanabiGame;
import game.player.AbstractPlayer;

public abstract class AbstractGameEvent
{
	/** The player who started the action */
	public final AbstractPlayer actor;
	
	/**
	 * @param actor The player who is executing the action
	 */
	public AbstractGameEvent(AbstractPlayer actor)
	{
		this.actor = actor;
	}
}
