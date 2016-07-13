package main.parts;
import main.enums.Color;
import main.enums.Number;

public class Card {
	public final Color color;
	public final Number number;
	
	public Card(Color c, Number n) {
		color = c;
		number = n;
	}
}
