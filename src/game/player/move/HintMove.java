package game.player.move;

import game.logic.HanabiGame;

public abstract class HintMove
		extends PlayerMove
{
	public final HanabiGame.PlayerIdentifier targetIdentifier;
	
	public HintMove(HanabiGame.PlayerIdentifier identifier)
	{
		this.targetIdentifier = identifier;
	}
	
	public static class Color
			extends HintMove
	{
		public final Color hintColor;
		
		public Color(HanabiGame.PlayerIdentifier identifier, Color hintColor)
		{
			super(identifier);
			this.hintColor = hintColor;
		}
	}
	
	public static class Number
			extends HintMove
	{
		public final Number hintNumber;
		
		public Number(HanabiGame.PlayerIdentifier identifier, Number hintNumber)
		{
			super(identifier);
			this.hintNumber = hintNumber;
		}
	}
}

