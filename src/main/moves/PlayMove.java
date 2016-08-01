package main.moves;
import main.enums.MoveType;

/**
 * Move class for plays.
 */
public class PlayMove extends Move {
	/** The position of the move; this corresponds to the index of the card in player's hand */
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
