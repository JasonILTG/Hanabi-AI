package game.player;

import game.logic.HanabiGame;
import game.logic.event.AbstractGameEvent;
import game.player.move.AbstractPlayerMove;

public abstract class AbstractPlayer
{
	protected HanabiGame.PlayerInterface playerInterface;
	
	protected String name;
	
	public AbstractPlayer(String name)
	{
		this.name = name;
	}
	
	/**
	 * Notify the player that something has happened in the game.
	 *
	 * @param event The event that took place
	 */
	public abstract void onEvent(AbstractGameEvent event);
	
	/**
	 * Request that the player make a move.
	 *
	 * @return The move that this player wants to make
	 */
	public abstract AbstractPlayerMove getNextMove();
	
	/**
	 * Signals the player that a move has failed to execute. Depending on the implementation, this method may or may not do anything.
	 *
	 * @param move The move that has failed to execute
	 */
	public abstract void onFailedMove(AbstractPlayerMove move);
	
	/**
	 * Signals the player that the game has ended.
	 */
	public abstract void onGameEnd();
	
	public void setPlayerInterface(HanabiGame.PlayerInterface playerInterface)
	{
		this.playerInterface = playerInterface;
	}
}
