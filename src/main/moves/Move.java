package main.moves;

import main.enums.MoveType;

public abstract class Move {
	public final MoveType type;
	
	public Move(MoveType type) {
		this.type = type;
	}
}
