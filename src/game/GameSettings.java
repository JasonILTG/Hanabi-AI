package game;

public class GameSettings
{
	/** Whether this game involves rainbow cards */
	public boolean isRainbow;
	/** Whether this game involves multi (hintable from every color) cards. Overrides isRainbow. */
	public boolean isMulti;
	/** Whether the extra color will only have 1 card each */
	public boolean hardMode;
	
	public GameSettings()
	{
		// By default on easiest settings
		isRainbow = false;
		isMulti = false;
		hardMode = false;
	}
	
	public GameSettings(boolean isRainbow, boolean isMulti, boolean hardMode)
	{
		this.isRainbow = isRainbow;
		this.isMulti = isMulti;
		this.hardMode = hardMode;
	}
}
