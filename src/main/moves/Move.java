package main.moves;

public abstract class Move {
	public final MoveType type;
	
	public Move(MoveType type) {
		this.type = type;
	}
	
	static enum MoveType {
		PLAY, DISCARD, CLUE;
	}
}
