package game.logic.event;

import game.card.Card;
import game.player.AbstractPlayer;

public class PlayEvent
		extends AbstractEvent
{
	public final Card cardPlayed;
	public final boolean success;
	
	public PlayEvent(AbstractPlayer actor, Card cardPlayed, boolean success)
	{
		super(actor);
		this.cardPlayed = cardPlayed;
		this.success = success;
	}
}
