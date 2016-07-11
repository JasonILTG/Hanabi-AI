public abstract class Player {
	private int playerNum;
	private HanabiGame game;
	
	public Player(int playerNum) {
		this.playerNum = playerNum;
	}
	
	public void init(HanabiGame g) {
		game = g;
	}
	
	public abstract int[] move();
	
	public abstract void message(String message);
}