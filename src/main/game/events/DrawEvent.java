package main.game.events;

import main.parts.Card;
import main.players.Player;

public class DrawEvent
		extends PlayerActionEvent
{
	public final Card draw;
	
	public DrawEvent(Player player, Card draw)
	{
		super(player);
		this.draw = draw;
	}
}
