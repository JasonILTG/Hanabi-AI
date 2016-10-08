package game.logic;

import game.card.Hand;
import game.player.AbstractPlayer;

import java.util.Map;

public class HanabiGame
{
	public static class PlayerIdentifier {}
	
	/** A map that links the players to their hands */
	private Map<AbstractPlayer, Hand> handMap;
	/** A  map that links identifiers to players */
	private Map<PlayerIdentifier, AbstractPlayer> playerIdentifierMap;
}
