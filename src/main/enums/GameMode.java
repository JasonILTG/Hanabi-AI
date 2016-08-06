package main.enums;

/**
 * Enum for game modes of Hanabi.  Each has different options.
 */
public enum GameMode {
	NORMAL(null, Color.STANDARD, false, "Normal"),
	RAINBOW(Color.RAINBOW, Color.WITH_RAINBOW, false, "Rainbow"),
	RAINBOW_HARD(Color.RAINBOW, Color.WITH_RAINBOW, true, "Rainbow Hard"),
	MULTI(Color.MULTI, Color.WITH_MULTI, false, "Multicolor"),
	MULTI_HARD(Color.MULTI, Color.WITH_MULTI, true, "Multicolor Hard");
	
	public final Color extraColor;
	public final Color[] colors;
	public final boolean hard;
	public final String name;
	
	/**
	 * Constructor.
	 * 
	 * @param extra The extra color to add (null if none)
	 * @param hard Whether the extra color only has one of each number
	 */
	GameMode(Color extra, Color[] colors, boolean hard, String name) {
		this.extraColor = extra;
		this.colors = colors;
		this.hard = hard;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}