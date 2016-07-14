package main.moves;
import main.enums.Color;
import main.enums.MoveType;
import main.enums.Number;

public class ClueMove extends Move {
	public final int player;
	public final Color color;
	public final Number number;
	
	public ClueMove(int player, Color color) {
		super(MoveType.CLUE);
		this.player = player;
		this.color = color;
		this.number = null;
	}
	
	public ClueMove(int player, Number number) {
		super(MoveType.CLUE);
		this.player = player;
		this.color = null;
		this.number = number;
	}
}
