package main.enums;

/**
 * Enum for game modes of Hanabi.  Each has different options.
 */
public enum GameMode {
	NORMAL(null, Color.VALUES, false),
	RAINBOW(Color.RAINBOW, Color.WITH_RAINBOW, false),
	RAINBOW_HARD(Color.RAINBOW, Color.WITH_RAINBOW, true),
	MULTI(Color.MULTI, Color.WITH_MULTI, false),
	MULTI_HARD(Color.MULTI, Color.WITH_MULTI, true);
	
	public final Color extraColor;
	public final Color[] colors;
	public final boolean hard;
	
	/**
	 * Constructor.
	 * 
	 * @param extra The extra color to add (null if none)
	 * @param hard Whether the extra color only has one of each number
	 */
	GameMode(Color extra, Color[] colors, boolean hard) {
		this.extraColor = extra;
		this.colors = colors;
		this.hard = hard;
	}
}