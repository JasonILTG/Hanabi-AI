package main.moves;
import main.enums.Color;
import main.enums.MoveType;
import main.enums.Number;

/**
 * Move class for clues (both color and number).
 */
public class ClueMove extends Move {
	public final int player;
	public final Color color;
	public final Number number;
	
	/**
	 * Color clue constructor.
	 * 
	 * @param player The player the clue is for
	 * @param color The color given by the clue
	 */
	public ClueMove(int player, Color color) {
		super(MoveType.CLUE);
		this.player = player;
		this.color = color;
		this.number = null;
	}
	
	/**
	 * Number clue constructor.
	 * 
	 * @param player The player the clue is for
	 * @param number The number given by the clue
	 */
	public ClueMove(int player, Number number) {
		super(MoveType.CLUE);
		this.player = player;
		this.color = null;
		this.number = number;
	}
}
