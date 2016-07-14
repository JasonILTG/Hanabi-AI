package main.enums;

/**
 * Enum for numbers in Hanabi (1-5).
 */
public enum Number implements Comparable<Number> {
	ONE(3), TWO(2), THREE(2), FOUR(2), FIVE(1);
	
	public static final Number[] VALUES = { ONE, TWO, THREE, FOUR, FIVE };
	
	public final int amount;
	
	/**
	 * Constructor.
	 * 
	 * @param amount The amount each number in the deck in a normal distribution
	 */
	Number(int amount) {
		this.amount = amount;
	}
	
	/**
	 * @return The next number
	 */
	public Number next() {
		return this == FIVE ? null : VALUES[this.ordinal() + 1];
	}
	
	public String toString() {
		return String.valueOf(ordinal() + 1);
	}
}
