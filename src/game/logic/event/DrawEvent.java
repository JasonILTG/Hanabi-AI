package game.logic.event;

import game.card.Card;
import game.logic.HanabiGame;
import game.player.AbstractPlayer;

public class DrawEvent
		extends AbstractGameEvent
{
	public final Card cardDrawn;
	
	public DrawEvent(AbstractPlayer actor, Card cardDrawn)
	{
		super(actor);
		this.cardDrawn = cardDrawn;
	}
}
