package main.enums;

/**
 * Enum for Hanabi colors.
 */
public enum Color {
	NONE("", "\u001B[0m"), RED("Pink", "\u001B[31m"), YELLOW("Yellow", "\u001B[33m"), GREEN("Green", "\u001B[32m"),
	BLUE("Blue", "\u001B[34m"), WHITE("White", "\u001B[37m"), RAINBOW("Rainbow", "\u001B[35m"), MULTI("Multi", "\u001B[35m");

	public static final Color[] STANDARD = { RED, YELLOW, GREEN, BLUE, WHITE };
	public static final Color[] WITH_RAINBOW = { RED, YELLOW, GREEN, BLUE, WHITE, RAINBOW };
	public static final Color[] WITH_MULTI = { RED, YELLOW, GREEN, BLUE, WHITE, MULTI };

	public static final Color[] VALUES = { RED, YELLOW, GREEN, BLUE, WHITE, RAINBOW, MULTI };
	
	private final String name;
	private final String ansi;
	
	/**
	 * Constructor.
	 * 
	 * @param name The name of the color
	 */
	Color(String name, String ansi) {
		this.name = name;
		this.ansi = ansi;
	}
	
	@Override
	public String toString() {
		return ansi + name + Color.NONE.ansi;
	}
	
	/**
	 * @return The ANSI code for the color to color text in the console
	 */
	public String ansi() {
		return ansi;
	}
	
	public boolean same(Color color) {
		return this == MULTI || this == color;
	}
}
