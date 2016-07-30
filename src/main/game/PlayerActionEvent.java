package main.game;

import main.event.Event;
import main.players.Player;

public class PlayerActionEvent
		implements Event
{
	public final Player player;
	
	public PlayerActionEvent(Player player)
	{
		this.player = player;
	}
}
