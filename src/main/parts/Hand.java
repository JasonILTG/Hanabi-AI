package main.parts;

import com.sun.istack.internal.Nullable;
import main.enums.Color;
import main.enums.Number;
import main.game.HanabiGame;
import main.players.Player;

import java.util.ArrayList;

/**
 * Class for hands.
 */
public class Hand
{
	/** The hand remembers its owner so that the owner cannot access its own hand */
	private Player owner;
	private ArrayList<Card> cards;
	
	/**
	 * Constructor.
	 */
	public Hand(Player owner)
	{
		this.owner = owner;
		cards = new ArrayList<Card>();
	}
	
	/**
	 * A secure way for the game to retrieve the hand of a player.
	 *
	 * @return The ArrayList of cards in hand
	 */
	public ArrayList<Card> getCards(HanabiGame.Authenticator auth)
	{
		return cards;
	}
	
	/**
	 * A safe way for players to retrieve the hand of a player without worrying about accidentally revealing the player's own hand
	 *
	 * @return The ArrayList of cards in hand
	 */
	@Nullable
	public ArrayList<Card> getCards(Player request)
	{
		return request == owner ? null : cards;
	}
	
	/**
	 * @return The number of cards in hand
	 */
	public int size()
	{
		return cards.size();
	}
	
	/**
	 * Adds the given card to the hand.
	 *
	 * @param card The card to add
	 */
	public void draw(Card card)
	{
		cards.add(card);
	}
	
	/**
	 * Removes and returns the card from the given position.
	 *
	 * @param pos The position to remove the card from
	 * @return The removed card
	 */
	public Card take(int pos)
	{
		Card c = cards.remove(pos);
		return c;
	}
	
	/**
	 * A secure way for the game to get information about the hand of a player.
	 *
	 * @param pos The position
	 * @return The color of the card in that position
	 */
	public Color getColor(HanabiGame.Authenticator auth, int pos)
	{
		return cards.get(pos).color;
	}
	
	/**
	 * A safe way for players to get information about a player's hand without worrying about accidentally revealing the player's own hand
	 *
	 * @param pos The position
	 * @return The color of the card in that position
	 */
	public Color getColor(Player request, int pos)
	{
		return request == owner ? null : cards.get(pos).color;
	}
	
	/**
	 * A secure way for the game to retrieve the hand of a player.
	 *
	 * @param pos The position
	 * @return The number of the card in that position
	 */
	public Number getNumber(HanabiGame.Authenticator auth, int pos)
	{
		return cards.get(pos).number;
	}
	
	/**
	 * A safe way for players to get information about a player's hand without worrying about accidentally revealing the player's own hand
	 *
	 * @param pos The position
	 * @return The number of the card in that position
	 */
	public Number getNumber(Player request, int pos)
	{
		return request == owner ? null : cards.get(pos).number;
	}
	
	/**
	 * @param color The color clued
	 * @return The positions of the colors clued
	 */
	public ArrayList<Integer> cluedCards(Color color)
	{
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = cards.size() - 1; i >= 0; i--) {
			if (cards.get(i).color == color) {
				clued.add(i);
			}
		}
		
		return clued;
	}
	
	/**
	 * @param number The number clued
	 * @return The positions of the numbers clued
	 */
	public ArrayList<Integer> cluedCards(Number number)
	{
		ArrayList<Integer> clued = new ArrayList<Integer>();
		for (int i = cards.size() - 1; i >= 0; i--) {
			if (cards.get(i).number == number) {
				clued.add(i);
			}
		}
		
		return clued;
	}
	
	/**
	 * Checks whether the given color clue is non-empty for this hand.
	 *
	 * @param color The color of the clue
	 * @return Whether the color clue is non-empty
	 */
	public boolean clueNonEmpty(Color color)
	{
		for (Card c : cards) {
			if (c.color.same(color)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the given number clue is non-empty for this hand.
	 *
	 * @param number The number of the clue
	 * @return Whether the number clue is non-empty
	 */
	public boolean clueNonEmpty(Number number)
	{
		for (Card c : cards) {
			if (c.number == number) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuilder handBuilder = new StringBuilder();
		for (Card c : cards) {
			handBuilder.append(c.toString());
		}
		return handBuilder.toString();
	}
}
