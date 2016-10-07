package game.card;

import game.Config;
import game.GameSettings;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck
{
	private GameSettings settings;
	private List<Card> cards;
	
	public Deck(GameSettings settings)
	{
		this.settings = settings;
	}
	
	public void setGameSettings(GameSettings settings)
	{
		this.settings = settings;
	}
	
	/**
	 * Initializes the deck according to the game settings specifications given.
	 * <p>
	 * This method automatically shuffles the deck.
	 */
	public void initialize()
	{
		cards = new LinkedList<>();
		
		// Add the main deck of cards
		for (Card.Color c : Card.Color.MAIN_COLORS) {
			for (int numberIndex = 0; numberIndex < 5; numberIndex++) {
				// Add the number of cards as indicated
				for (int j = 0; j < Config.CARD_COUNT[numberIndex]; j++) {
					cards.add(new Card(c, Card.Number.VALUES[numberIndex]));
				}
			}
		}
		
		// If game mode has multi or rainbow, add those cards
		if (settings.isMulti) {
			// Get frequency list base on whether the game is hard mode or not
			int[] freqList = settings.hardMode ? Config.HARD_CARD_COUNT : Config.CARD_COUNT;
			// Add the number of cards as indicated
			for (int numberIndex = 0; numberIndex < 5; numberIndex++) {
				cards.add(new Card(Card.Color.MULTI, Card.Number.VALUES[numberIndex]));
			}
		}
		else if (settings.isRainbow) {
			// Get frequency list base on whether the game is hard mode or not
			int[] freqList = settings.hardMode ? Config.HARD_CARD_COUNT : Config.CARD_COUNT;
			// Add the number of cards as indicated
			for (int numberIndex = 0; numberIndex < 5; numberIndex++) {
				cards.add(new Card(Card.Color.RAINBOW, Card.Number.VALUES[numberIndex]));
			}
		}
		
		// Shuffle the deck
		shuffle();
	}
	
	public void shuffle()
	{
		Collections.shuffle(cards);
	}
	
	/**
	 * Deals a card from the deck; this operation will remove one card from the deck and return it.
	 */
	public Card dealCard()
	{
		return cards.remove(0);
	}
}
