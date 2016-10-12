package game.logic;

import game.GameSettings;
import game.card.Deck;
import game.card.DiscardPile;
import game.card.Hand;
import game.card.PlayPile;
import game.player.AbstractPlayer;

import java.util.HashMap;
import java.util.Map;

public class HanabiGame
{
	public static class PlayerIdentifier {}
	
	/** A map that links the players to their hands */
	private Map<AbstractPlayer, Hand> handMap;
	/** A  map that links identifiers to players */
	private Map<PlayerIdentifier, AbstractPlayer> playerIdentifierMap;
	
	// Fields that keep track of the game status
	private GameSettings settings;
	private int lives;
	private int clues;
	private Deck deck;
	private PlayPile played;
	private DiscardPile discards;
	
	public HanabiGame(GameSettings settings)
	{
		this.settings = settings;
		handMap = new HashMap<>();
		playerIdentifierMap = new HashMap<>();
	}
	
	public void initialize()
	{
		lives = 3;
		clues = 8;
		deck = new Deck(settings);
		played = new PlayPile(settings);
		discards = new DiscardPile();
	}
	
	public GameSettings getSettings()
	{
		return settings;
	}
	
	public int getLives()
	{
		return lives;
	}
	
	public int getClues()
	{
		return clues;
	}
	
	public PlayPile getPlayed()
	{
		return played;
	}
	
	public DiscardPile getDiscards()
	{
		return discards;
	}
	
	/**
	 * @param requester  The player who is making the request
	 * @param identifier The identifier of the player to get hand of
	 * @return The hand of the target player; obscured if the player is trying to get his/her own hand
	 */
	public Hand getHand(AbstractPlayer requester, PlayerIdentifier identifier)
	{
		AbstractPlayer player = playerIdentifierMap.get(identifier);
		Hand playerHand = handMap.get(player);
		// The output should be obscured
		return playerHand.getProtected(player == requester);
	}
}
