package main.game.events;

import main.event.Event;
import main.players.Player;

public class PlayerActionEvent
		implements Event
{
	/** The player who originated the action */
	public final Player player;
	
	public PlayerActionEvent(Player player)
	{
		this.player = player;
	}
}
