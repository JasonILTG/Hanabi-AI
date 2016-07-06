
public class Card {
	public static final int UNKNOWN = 0;
	public static final int WHITE = 1;
	public static final int RED = 2;
	public static final int YELLOW = 3;
	public static final int GREEN = 4;
	public static final int BLUE = 5;
	public static final int RAINBOW = 6;
	public static final int MULTI = 100;
	
	public final int color;
	public final int number;
	
	public Card(int c, int n) {
		color = c;
		number = n;
	}
}
