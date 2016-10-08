package game.player;

import game.logic.HanabiGame;
import game.logic.event.AbstractEvent;

public abstract class AbstractPlayer
{
	private HanabiGame game;
	private HanabiGame.PlayerIdentifier selfIdentifier;
	
	private String name;
	
	public AbstractPlayer(String name)
	{
		this.name = name;
	}
	
	abstract void onEvent(AbstractEvent evnet);
}
