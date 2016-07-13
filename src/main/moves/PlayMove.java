package main.moves;
import main.moves.Move.MoveType;

public class PlayMove extends Move {
	public final int pos;
	
	public PlayMove(int pos) {
		super(MoveType.PLAY);
		this.pos = pos;
	}
}
