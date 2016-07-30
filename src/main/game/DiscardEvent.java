package main.game;

import main.parts.Card;
import main.players.Player;

public class DiscardEvent
		extends PlayerActionEvent
{
	public final Card discard;
	public final Card draw;
	
	public DiscardEvent(Player player, Card discard, Card draw)
	{
		super(player);
		this.discard = discard;
		this.draw = draw;
	}
}
