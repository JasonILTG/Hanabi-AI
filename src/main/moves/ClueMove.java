package main.moves;

import main.enums.Color;
import main.enums.MoveType;
import main.enums.Number;
import main.players.Player;

/**
 * Move class for clues (both color and number).
 */
public class ClueMove
		extends Move
{
	public final Player target;
	public final Color color;
	public final Number number;
	
	/**
	 * Color clue constructor.
	 *
	 * @param playerHand The player to give clue to
	 * @param color The color given by the clue
	 */
	public ClueMove(Player player, Color color)
	{
		super(MoveType.CLUE);
		this.target = player;
		this.color = color;
		this.number = null;
	}
	
	/**
	 * Number clue constructor.
	 *
	 * @param player The player to give clue to
	 * @param number The number given by the clue
	 */
	public ClueMove(Player player, Number number)
	{
		super(MoveType.CLUE);
		this.target = player;
		this.color = null;
		this.number = number;
	}
}
