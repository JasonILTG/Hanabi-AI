package main.players;
import java.util.HashMap;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;
import main.moves.Move;
import main.parts.CluedHand;
import main.parts.Hand;

public class HanabiAI extends Player {
	private GameMode mode;
	private CluedHand[] hands;
	private HashMap<Color, Integer> stacks;
	private HashMap<Color, int[]> discard;
	
	public HanabiAI(int playerNum) {
		super(playerNum);
	}
	
	@Override
	public void init(Hand[] hands, GameMode mode) {
		this.mode = mode;
		this.hands = new CluedHand[hands.length];
		
		for (int i = 0; i < hands.length; i++) {
			this.hands[i] = new CluedHand(hands[i].getCards());
		}
		
		stacks = new HashMap<Color, Integer>();
		for (Color color : Color.VALUES) {
			stacks.put(color, 1);
		}
	}

	@Override
	public Move move() {
		return null;
	}
	
	private Move checkChop() {
		for (int i = 1; i < hands.length; i++) {
			int pos = (i + playerNum) % hands.length;
			
			int chopPos = getChop(hands[pos]);
			Number chopNumber = hands[pos].getNumber(chopPos);
			Color chopColor = hands[pos].getColor(chopPos);
			int stack = stacks.get(chopColor);
			
			if (chopNumber.ordinal() >= stack) {
				int totalAmount = chopNumber.amount(mode.hard && (chopColor == Color.MULTI || chopColor == Color.RAINBOW));
				if (discard.get(chopColor)[chopNumber.ordinal()] + 1 == totalAmount) {
					
				}
			}
		}
		
		return null;
	}
	
	private int getChop(CluedHand hand) {
		for (int i = 0; i < hand.size(); i++) {
			if (!hand.isClued(i)) {
				return i;
			}
		}
		
		return 0;
	}

	@Override
	public void message(String message) {}

	@Override
	public void play(int player, int pos) {
		
	}

	@Override
	public void discard(int player, int pos) {
		
	}

	@Override
	public void clue(int fromPlayer, int player, Color color) {
		
	}

	@Override
	public void clue(int fromPlayer, int player, Number number) {
		
	}
}
