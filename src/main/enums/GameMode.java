package main.enums;

public enum GameMode {
	NORMAL(null, false),
	RAINBOW(Color.RAINBOW, false),
	RAINBOW_HARD(Color.RAINBOW, true),
	MULTI(Color.MULTI, false),
	MULTI_HARD(Color.MULTI, true);
	
	public final Color extraColor;
	public final boolean hard;
	
	GameMode(Color extra, boolean hard) {
		this.extraColor = extra;
		this.hard = hard;
	}
}