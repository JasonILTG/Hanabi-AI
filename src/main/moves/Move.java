package main.moves;

import main.enums.MoveType;

/**
 * Abstract class for moves.
 */
public abstract class Move {
	public final MoveType type;
	
	/**
	 * Constructor.
	 * 
	 * @param type The type of move
	 */
	public Move(MoveType type) {
		this.type = type;
	}
}
