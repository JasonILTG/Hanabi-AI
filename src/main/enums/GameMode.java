package main.enums;

/**
 * Enum for game modes of Hanabi.  Each has different options.
 */
public enum GameMode {
	NORMAL(null, false),
	RAINBOW(Color.RAINBOW, false),
	RAINBOW_HARD(Color.RAINBOW, true),
	MULTI(Color.MULTI, false),
	MULTI_HARD(Color.MULTI, true);
	
	public final Color extraColor;
	public final boolean hard;
	
	/**
	 * Constructor.
	 * 
	 * @param extra The extra color to add (null if none)
	 * @param hard Whether the extra color only has one of each number
	 */
	GameMode(Color extra, boolean hard) {
		this.extraColor = extra;
		this.hard = hard;
	}
}