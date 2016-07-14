package main.moves;
import main.enums.MoveType;

/**
 * Move class for plays.
 */
public class PlayMove extends Move {
	public final int pos;
	
	/**
	 * Constructor.
	 * 
	 * @param pos The position to play from
	 */
	public PlayMove(int pos) {
		super(MoveType.PLAY);
		this.pos = pos;
	}
}
