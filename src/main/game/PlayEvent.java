package main.game;

import main.parts.Card;
import main.players.Player;

public class PlayEvent
		extends PlayerActionEvent
{
	public final Card cardPlayed;
	public final Card draw;
	public final boolean success;
	
	public PlayEvent(Player player, Card cardPlayed, Card draw, boolean success)
	{
		super(player);
		this.cardPlayed = cardPlayed;
		this.draw = draw;
		this.success = success;
	}
}
