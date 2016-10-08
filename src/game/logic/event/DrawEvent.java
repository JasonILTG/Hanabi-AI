package game.logic.event;

import game.card.Card;
import game.player.AbstractPlayer;

public class DrawEvent
		extends AbstractEvent
{
	public final Card cardDrawn;
	
	public DrawEvent(AbstractPlayer actor, Card cardDrawn)
	{
		super(actor);
		this.cardDrawn = cardDrawn;
	}
}
