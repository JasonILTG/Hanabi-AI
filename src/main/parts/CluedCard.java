package main.parts;

import java.util.ArrayList;
import java.util.Iterator;

import main.enums.Color;
import main.enums.Number;

public class CluedCard {
	public final Color color;
	public final Number number;
	
	private ArrayList<Color> pColors;
	private ArrayList<Number> pNumbers;
	
	private boolean isClued;
	
	public CluedCard(Card card) {
		color = card.color;
		number = card.number;
		isClued = false;
	}
	
	public boolean isClued() {
		return isClued;
	}
	
	public ArrayList<Color> getPColors() {
		return pColors;
	}
	
	public ArrayList<Number> getPNumbers() {
		return pNumbers;
	}
	
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
	
	public void antiClue(Color color) {
		pColors.remove(color);
		pColors.remove(Color.MULTI);
	}
	
	public void antiClue(Number number) {
		pNumbers.remove(number);
	}
}