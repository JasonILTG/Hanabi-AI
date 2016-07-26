package main.players.jason;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;
import main.parts.Card;

/**
 * Class for a card with clues.
 */
public class CluedCard {
	public final Color color;
	public final Number number;
	
	/** The ArrayList of possible colors */
	private ArrayList<Color> pColors;
	/** The ArrayList of possible numbers */
	private ArrayList<Number> pNumbers;
	
	private ArrayList<Card> notPossible;
	
	private boolean isCClued;
	private boolean isNClued;
	
	private Marker mark;
	
	/**
	 * Constructor for an unknown card.
	 * 
	 * @param mode The game mode
	 */
	public CluedCard(GameMode mode) {
		this(null, null, mode);
	}
	
	public CluedCard(GameMode mode, ArrayList<Card> notPossible) {
		this(null, null, mode);
		
		this.notPossible.addAll(notPossible);
	}
	
	/** 
	 * Constructor.
	 * 
	 * @param card The non-clued card to copy data from
	 * @param mode The game mode
	 */
	public CluedCard(Card card, GameMode mode) {
		this(card.color, card.number, mode);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param c The color of the card
	 * @param n The number of the card
	 * @param mode The game mode
	 */
	public CluedCard(Color c, Number n, GameMode mode) {
		color = c;
		number = n;
		isCClued = false;
		isNClued = false;
		mark = Marker.NONE;
		
		pNumbers = new ArrayList<Number>(Arrays.asList(Number.VALUES));
		pColors = new ArrayList<Color>(Arrays.asList(mode.colors));
		
		notPossible = new ArrayList<Card>();
	}
	
	/**
	 * @return Whether the card has been directly clued
	 */
	public boolean isClued() {
		return isCClued || isNClued;
	}
	
	/**
	 * @return Whether the card has been given a number clue
	 */
	public boolean isCClued() {
		return isCClued;
	}
	
	/**
	 * @return Whether the card has been given a color clue
	 */
	public boolean isNClued() {
		return isNClued;
	}
	
	/**
	 * Marks the card with the given marker.
	 * 
	 * @param m The marker
	 */
	public void mark(Marker m) {
		mark = m;
	}
	
	/**
	 * @return The marker on the card
	 */
	public Marker getMark() {
		return mark;
	}
	
	/**
	 * @param color The color
	 * @return Whether the card can possibly be the given color
	 */
	public boolean possible(Color color) {
		return pColors.contains(color);
	}
	
	/**
	 * @param number The number
	 * @return Whether the card can possibly be the given number
	 */
	public boolean possible(Number number) {
		return pNumbers.contains(number);
	}
	
	/**
	 * @param color The color
	 * @param number The number
	 * @return Whether the card can possibly be the given color and number
	 */
	public boolean possible(Color color, Number number) {
		return !notPossible.contains(new Card(color, number));
	}
	
	/**
	 * @return The ArrayList of possible colors given the clues
	 */
	public ArrayList<Color> getPColors() {
		return pColors;
	}
	
	/**
	 * @return The ArrayList of possible numbers given the clues
	 */
	public ArrayList<Number> getPNumbers() {
		return pNumbers;
	}
	
	/**
	 * Clues the card as being the given color.
	 * 
	 * @param color The color clued
	 */
	public void clue(Color color) {
		isCClued = true;
		Iterator<Color> it = pColors.iterator();
		
		while (it.hasNext()) {
			Color next = it.next();
			if (!next.same(color)) {
				it.remove();
			}
		}
	}
	
	/**
	 * Clues the card as being the given number.
	 * 
	 * @param number The number clued
	 */
	public void clue(Number number) {
		isNClued = true;
		Iterator<Number> it = pNumbers.iterator();
		
		while (it.hasNext()) {
			Number next = it.next();
			if (next != number) {
				it.remove();
			}
		}
	}
	
	/**
	 * Clues the card as NOT being the given color.
	 * 
	 * @param color The color anti-clued
	 */
	public void antiClue(Color color) {
		pColors.remove(color);
		pColors.remove(Color.MULTI);
	}
	
	/**
	 * Clues the card as NOT being the given number.
	 * 
	 * @param number The number anti-clued
	 */
	public void antiClue(Number number) {
		pNumbers.remove(number);
	}
	
	/**
	 * Clues the card as NOT being the given card.
	 * 
	 * @param c The card
	 */
	public void antiClue(Card c) {
		notPossible.add(c);
	}
	
	@Override
	public boolean equals(Object o) {
		CluedCard c = (CluedCard) o;
		return c.color == color && c.number == number;
	}
}