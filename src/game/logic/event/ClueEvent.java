package game.logic.event;

import game.card.Card;
import game.logic.HanabiGame;
import game.player.AbstractPlayer;

public abstract class ClueEvent
		extends AbstractGameEvent
{
	public final AbstractPlayer target;
	
	public ClueEvent(AbstractPlayer actor, AbstractPlayer target)
	{
		super(actor);
		this.target = target;
	}
	
	public static class Color
			extends ClueEvent
	{
		public final Card.Color clueColor;
		
		public Color(AbstractPlayer actor, AbstractPlayer target, Card.Color clueColor)
		{
			super(actor, target);
			this.clueColor = clueColor;
		}
	}
	
	public static class Number
			extends ClueEvent
	{
		public final Card.Number clueNumber;
		
		public Number(AbstractPlayer actor, AbstractPlayer target, Card.Number clueNumber)
		{
			super(actor, target);
			this.clueNumber = clueNumber;
		}
	}
}
