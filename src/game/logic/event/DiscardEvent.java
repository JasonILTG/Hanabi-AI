package game.logic.event;

import game.card.Card;
import game.logic.HanabiGame;
import game.player.AbstractPlayer;

public class DiscardEvent
		extends AbstractGameEvent
{
	public final int cardIndex;
	public final Card cardDiscarded;
	
	public DiscardEvent(AbstractPlayer actor, int cardIndex, Card cardDiscarded)
	{
		super(actor);
		this.cardIndex = cardIndex;
		this.cardDiscarded = cardDiscarded;
	}
}
