package main.players;
import main.HanabiGame;
import main.enums.Color;
import main.enums.Number;
import main.parts.Card;
import main.parts.Hand;
import main.parts.PlayStack;

public class HanabiAI extends Player {
	private HanabiGame g;
	
	public HanabiAI(int playerNum) {
		super(playerNum);
	}
	
	@Override
	public void init(HanabiGame g) {
		this.g = g;
	}

	@Override
	public int[] move() {
		if (g.clues > 0) {
			int[] chopClue = checkChop();
			if (chopClue != null) {
				return chopClue;
			}
		}
		
		
		
		return null;
	}
	
	private int[] checkChop() {
		for (int i = 0; i < g.hands.length; i++) {
			if (i == playerNum) {
				continue;
			}
			
			int chopPos = getChop(g.hands[i]);
			Number chopNumber = g.hands[i].getNumber(chopPos);
			Color chopColor = g.hands[i].getColor(chopPos);
			PlayStack stack = g.stacks.get(chopColor);
			
			int inDiscard = 0;
			if (chopNumber.ordinal() >= stack.getScore()) {
				for (Card c : g.discard) {
					if (c.color == chopColor && c.number == chopNumber) {
						inDiscard++;
					}
				}
			}
			
			if (inDiscard + 1 == stack.dist[chopNumber.ordinal()]) {
				
			}
		}
		
		return null;
	}
	
	private int getChop(Hand hand) {
		for (int i = 0; i < hand.size(); i++) {
			if (!hand.isClued(i)) {
				return i;
			}
		}
		
		return 0;
	}

	@Override
	public void message(String message) {}
}
