package main.moves;
import main.moves.Move.MoveType;

public class ClueMove extends Move {
	public final int pos;
	
	public ClueMove(int pos) {
		super(MoveType.CLUE);
		this.pos = pos;
	}
}
