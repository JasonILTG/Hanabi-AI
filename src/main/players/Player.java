package main.players;

import main.event.Event;
import main.event.EventBus;
import main.event.EventListener;
import main.game.HanabiGame;
import main.moves.Move;
import main.parts.Hand;

/**
 * Abstract class for players (AIs or humans).
 */
public abstract class Player
		implements EventListener
{
	private String name;
	protected HanabiGame game;
	protected Hand hand;
	
	/**
	 * Constructor.
	 *
	 * @param name The player's name
	 */
	public Player(String name, HanabiGame game)
	{
		this.name = name;
		this.game = game;
		// Register this as an event listener
		Class<? extends Event>[] eventClasses = getListenableEventClasses();
		if (eventClasses != null) {
			for (Class<? extends Event> eventClass : eventClasses) {
				EventBus.addListener(eventClass, this);
			}
		}
	}
	
	/**
	 * @param hand The cards this player has
	 */
	public void setHand(Hand hand)
	{
		this.hand = hand;
	}
	
	/**
	 * @return The hand this player is holding
	 */
	public Hand getHand()
	{
		return hand;
	}
	
	public abstract Move getNextMove();
	
	/**
	 * @return A list of all the event classes this object can listen to
	 */
	public void message(String message) {}

	protected abstract Class<? extends Event>[] getListenableEventClasses();
	
	@Override
	public String toString()
	{
		return name;
	}
}