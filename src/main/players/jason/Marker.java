package main.players.jason;

/**
 * Enum to help the AI keep track of info from other players' clues.
 */
public enum Marker {
	NONE(false, false), DISCARD(false, false), NOT_DISCARD(false, false), 
	CLUED_PLAY(true, false), INFERRED_PLAY(true, false), 
	FINESSED(true, true), BLUFF(true, true), FINESSED_UNLESS_BLUFF(true, true);
	
	private final boolean play;
	private final boolean finesse;
	
	Marker(boolean play, boolean finesse) {
		this.play = play;
		this.finesse = finesse;
	}
	
	public boolean play() {
		return play;
	}
	
	public boolean cluedPlay() {
		return play && this != INFERRED_PLAY;
	}
	
	public boolean finesse() {
		return finesse;
	}
}
