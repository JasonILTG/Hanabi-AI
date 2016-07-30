package main.game;

import main.players.Player;

public class ClueEvent
		extends PlayerActionEvent
{
	private ClueEvent(Player player)
	{
		super(player);
	}
	
	public static class Number
			extends ClueEvent
	{
		public final main.enums.Number number;
		
		public Number(Player player, main.enums.Number number)
		{
			super(player);
			this.number = number;
		}
	}
	
	public static class Color
			extends ClueEvent
	{
		public final main.enums.Color color;
		
		public Color(Player player, main.enums.Color color)
		{
			super(player);
			this.color = color;
		}
	}
}
