package main.moves;
import main.enums.MoveType;

/**
 * Move class for discards.
 */
public class DiscardMove extends Move {
	public final int pos;
	
	/**
	 * Constructor.
	 * 
	 * @param pos The position to discard from
	 */
	public DiscardMove(int pos) {
		super(MoveType.PLAY);
		this.pos = pos;
	}
}
