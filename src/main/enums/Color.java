package main.enums;

public enum Color {
	NONE("None"), RED("Red"), YELLOW("Yellow"), GREEN("Green"),
	BLUE("Blue"), WHITE("White"), RAINBOW("Rainbow"), MULTI("Multi");
	
	public static final Color[] STANDARD = { RED, YELLOW, GREEN, BLUE, WHITE };
	public static final Color[] WITH_RAINBOW = { RED, YELLOW, GREEN, BLUE, WHITE, RAINBOW };
	public static final Color[] WITH_MULTI = { RED, YELLOW, GREEN, BLUE, WHITE, MULTI };

	private final String name;
	
	Color(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
