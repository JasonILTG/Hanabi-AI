package game.logic;

public enum FailMoveReason
{
	UNKNOWN,
	/**
	 * The card retrieved at the indicated index is null
	 */INVALID_INDEX,
	/**
	 * There are no clues available for use
	 */NO_CLUES
}
