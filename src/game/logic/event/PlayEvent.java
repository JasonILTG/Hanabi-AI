package game.logic.event;

import game.card.HintedCard;
import game.logic.HanabiGame;
import game.player.AbstractPlayer;

public class PlayEvent
		extends AbstractGameEvent
{
	public final HintedCard cardPlayed;
	public final boolean success;
	
	public PlayEvent(AbstractPlayer actor, HintedCard cardPlayed, boolean success)
	{
		super(actor);
		this.cardPlayed = cardPlayed;
		this.success = success;
	}
}
