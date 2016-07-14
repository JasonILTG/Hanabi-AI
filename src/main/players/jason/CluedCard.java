package main.players.jason;

import java.util.ArrayList;
import java.util.Iterator;

import main.enums.Color;
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
	
	private boolean isClued;
	
	/**
	 * Constructor.
	 * 
	 * @param card The non-clued card to copy data from
	 */
	public CluedCard(Card card) {
		color = card.color;
		number = card.number;
		isClued = false;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param c The color of the card
	 * @param n The number of the card
	 */
	public CluedCard(Color c, Number n) {
		color = c;
		number = n;
		isClued = false;
	}
	
	/**
	 * @return Whether the card has been directly clued
	 */
	public boolean isClued() {
		return isClued;
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
		isClued = true;
		Iterator<Color> it = pColors.iterator();
		
		while (it.hasNext()) {
			Color next = it.next();
			if (next != color && next != Color.MULTI) {
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
		isClued = true;
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
}