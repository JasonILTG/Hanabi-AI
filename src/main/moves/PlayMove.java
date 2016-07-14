package main.moves;
import main.enums.MoveType;

public class PlayMove extends Move {
	public final int pos;
	
	public PlayMove(int pos) {
		super(MoveType.PLAY);
		this.pos = pos;
	}
}
