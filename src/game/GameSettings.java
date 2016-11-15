package game;

import game.card.Card;

public class GameSettings
{
	/** Whether this game involves rainbow cards */
	public final boolean isRainbow;
	/** Whether this game involves multi (hintable from every color) cards. Overrides isRainbow. */
	public final boolean isMulti;
	/** Whether the extra color will only have 1 card each */
	public final boolean hardMode;
	
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

	public int evalCardCount(Card c)
	{
		switch (c.color)
		{
			case RED:
			case YELLOW:
			case GREEN:
			case BLUE:
			case WHITE:
				return Config.CARD_COUNT[c.number.value];
			case RAINBOW:
				if (isRainbow && !isMulti)
				{
					return hardMode ? Config.HARD_CARD_COUNT[c.number.ordinal()] : Config.CARD_COUNT[c.number
							.ordinal()];
				}
				else
				{
					// Should not have any card of this type.
					return 0;
				}
			case MULTI:
				if (isMulti)
				{
					return hardMode ? Config.HARD_CARD_COUNT[c.number.ordinal()] : Config.CARD_COUNT[c.number
							.ordinal()];
				}
				else
				{
					// Should not have any card of tis type
					return 0;
				}
			default:
				return 0;
		}
	}

	// Some buuilt-in game modes
	public static final GameSettings NORMAL = new GameSettings();
	public static final GameSettings RAINBOW = new GameSettings(true, false, false);
	public static final GameSettings RAINBOW_HARD = new GameSettings(true, false, true);
	public static final GameSettings MULTI = new GameSettings(false, true, false);
	public static final GameSettings MULTI_HARD = new GameSettings(false, true, true);
}
