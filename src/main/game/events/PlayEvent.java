package main.game.events;

import main.parts.Card;
import main.players.Player;

public class PlayEvent
		extends PlayerActionEvent
{
	public final Card play;
	public final boolean success;
	
	public PlayEvent(Player player, Card play, boolean success)
	{
		super(player);
		this.play = play;
		this.success = success;
	}
}
