package main.players;
import main.HanabiGame;

public abstract class Player {
	int playerNum;
	
	public Player(int playerNum) {
		this.playerNum = playerNum;
	}
	
	public abstract void init(HanabiGame g);
	
	public abstract int[] move();
	
	public abstract void message(String message);
}