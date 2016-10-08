package game.logic.event;

import game.card.Card;
import game.player.AbstractPlayer;

public class DiscardEvent
		extends AbstractEvent
{
	public final Card cardDiscarded;
	
	public DiscardEvent(AbstractPlayer actor, Card cardDiscarded)
	{
		super(actor);
		this.cardDiscarded = cardDiscarded;
	}
}
