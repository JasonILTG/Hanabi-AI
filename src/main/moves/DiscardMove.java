package main.moves;
import main.enums.MoveType;

public class DiscardMove extends Move {
	public final int pos;
	
	public DiscardMove(int pos) {
		super(MoveType.PLAY);
		this.pos = pos;
	}
}
