package main.game.events;

import main.parts.Card;
import main.players.Player;

public class DiscardEvent
		extends PlayerActionEvent
{
	public final Card discard;
	
	public DiscardEvent(Player player, Card discard)
	{
		super(player);
		this.discard = discard;
	}
}
