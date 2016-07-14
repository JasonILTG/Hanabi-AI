package main.enums;

/**
 * Enum for Hanabi colors.
 */
public enum Color {
	NONE("None"), RED("Pink"), YELLOW("Yellow"), GREEN("Green"),
	BLUE("Blue"), WHITE("White"), RAINBOW("Rainbow"), MULTI("Multi");
	
	public static final Color[] STANDARD = { RED, YELLOW, GREEN, BLUE, WHITE };
	public static final Color[] WITH_RAINBOW = { RED, YELLOW, GREEN, BLUE, WHITE, RAINBOW };
	public static final Color[] WITH_MULTI = { RED, YELLOW, GREEN, BLUE, WHITE, MULTI };

	public static final Color[] VALUES = { RED, YELLOW, GREEN, BLUE, WHITE, RAINBOW, MULTI };
	
	private final String name;
	
	/**
	 * Constructor.
	 * 
	 * @param name The name of the color
	 */
	Color(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
