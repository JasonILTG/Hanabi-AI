package game.player.move;

import game.card.Card;
import game.logic.HanabiGame;
import game.player.AbstractPlayer;

public abstract class ClueMove
		extends AbstractPlayerMove
{
	public final AbstractPlayer target;
	
	public ClueMove(AbstractPlayer target)
	{
		this.target = target;
	}
	
	public static class Color
			extends ClueMove
	{
		public final Card.Color clueColor;
		
		public Color(AbstractPlayer target, Card.Color clueColor)
		{
			super(target);
			this.clueColor = clueColor;
		}
	}
	
	public static class Number
			extends ClueMove
	{
		public final Card.Number clueNumber;
		
		public Number(AbstractPlayer target, Card.Number clueNumber)
		{
			super(target);
			this.clueNumber = clueNumber;
		}
	}
}

