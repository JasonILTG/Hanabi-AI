package main.game.events;

import main.players.Player;

public class ClueEvent
		extends PlayerActionEvent
{
	/** The player that this clue targets */
	public final Player target;
	
	private ClueEvent(Player player, Player target)
	{
		super(player);
		this.target = target;
	}
	
	public static class Number
			extends ClueEvent
	{
		public final main.enums.Number number;
		
		public Number(Player player, Player target, main.enums.Number number)
		{
			super(player, target);
			this.number = number;
		}
	}
	
	public static class Color
			extends ClueEvent
	{
		public final main.enums.Color color;
		
		public Color(Player player, Player target, main.enums.Color color)
		{
			super(player, target);
			this.color = color;
		}
	}
}
