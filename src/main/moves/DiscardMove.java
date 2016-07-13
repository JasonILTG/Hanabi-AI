package main.moves;
import main.moves.Move.MoveType;

public class DiscardMove extends Move {
	public final int pos;
	
	public DiscardMove(int pos) {
		super(MoveType.PLAY);
		this.pos = pos;
	}
}
