package main.players.jason;

/**
 * Enum to help the AI keep track of info from other players' clues.
 */
public enum Marker {
	NONE(false), DISCARD(false), NOT_DISCARD(false), CLUED_PLAY(true), INFERRED_PLAY(true), FINESSED(true), FINESSED_UNLESS_BLUFF(true);
	
	private final boolean play;
	
	Marker(boolean play) {
		this.play = play;
	}
	
	public boolean play() {
		return play;
	}
}
