package main.players;

import main.enums.Color;
import main.enums.Number;
import main.enums.GameMode;
import main.moves.Move;
import main.parts.Hand;

public abstract class Player {
	public int playerNum;
	
	public Player(int playerNum) {
		this.playerNum = playerNum;
	}
	
	public abstract void init(Hand[] g, GameMode mode);
	
	public abstract Move move();
	
	public abstract void play(int player, int pos);
	
	public abstract void discard(int player, int pos);
	
	public abstract void clue(int fromPlayer, int player, Color color);
	
	public abstract void clue(int fromPlayer, int player, Number number);
	
	public abstract void message(String message);
}