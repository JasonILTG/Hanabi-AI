package main.enums;

public enum Number implements Comparable<Number> {
	ONE(3), TWO(2), THREE(2), FOUR(2), FIVE(1);
	
	public static final Number[] VALUES = { ONE, TWO, THREE, FOUR, FIVE };
	
	private final int amount;
	
	Number(int amount) {
		this.amount = amount;
	}
	
	public Number next() {
		return this == FIVE ? null : VALUES[this.ordinal() + 1];
	}
	
	public int amount(boolean hard) {
		return hard ? 1 : amount;
	}
	
	public String toString() {
		return String.valueOf(ordinal() + 1);
	}
}
