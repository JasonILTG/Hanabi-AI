package game.player;

import game.logic.HanabiGame;
import game.logic.event.AbstractEvent;
import game.player.move.PlayerMove;

public abstract class AbstractPlayer
{
	private HanabiGame game;
	private HanabiGame.PlayerIdentifier selfIdentifier;
	
	private String name;
	
	public AbstractPlayer(String name)
	{
		this.name = name;
	}
	
	/**
	 * Notify the player that something has happened in the game.
	 *
	 * @param event The event that took place
	 */
	abstract void onEvent(AbstractEvent event);
	
	/**
	 * Request that the player make a move.
	 *
	 * @return The move that this player wants to make
	 */
	abstract PlayerMove getNextMove();
}
