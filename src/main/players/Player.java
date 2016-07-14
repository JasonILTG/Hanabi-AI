package main.players;

import main.enums.Color;
import main.enums.Number;
import main.enums.GameMode;
import main.moves.Move;
import main.parts.Hand;

/**
 * Abstract class for players (AIs or humans).
 */
public abstract class Player {
	/**
	 * Initializes the player with the hands of the other players and the game mode.
	 * 
	 * IMPORTANT: Indexing for players will be 0 for the player immediately after
	 * this player, and then 1, 2, and so on.  This player will be indexed as -1.
	 * 
	 * @param hands The hands of the other players, in playing order after this player
	 * @param mode The game mode
	 */
	public abstract void init(Hand[] hands, GameMode mode);
	
	/**
	 * Calculates and returns the move this player makes.
	 * 
	 * @return The move the player makes.
	 */
	public abstract Move move();
	
	/**
	 * Informs this player of a play.
	 * 
	 * @param player The position of the player who played
	 * @param pos The position of the card played in the player's hand
	 */
	public abstract void play(int player, int pos);
	
	/**
	 * Informs this player of a discard.
	 * 
	 * @param player The position of the player who discarded
	 * @param pos The position of the card discarded in the player's hand
	 */
	public abstract void discard(int player, int pos);
	
	/**
	 * Informs this player of a color clue.
	 * 
	 * @param fromPlayer The position of the player who gave the clue
	 * @param player The position of the player who received the clue
	 * @param color The color clued
	 */
	public abstract void clue(int fromPlayer, int player, Color color);
	
	/**
	 * Informs this player of a number clue.
	 * 
	 * @param fromPlayer The position of the player who gave the clue
	 * @param player The position of the player who received the clue
	 * @param number The number clued
	 */
	public abstract void clue(int fromPlayer, int player, Number number);
	
	/**
	 * Sends a message to this player.  This is mostly used for human players.
	 * 
	 * @param message The message sent
	 */
	public void message(String message) {}
}