import java.util.ArrayList;

public class Hand {
	private final ArrayList<CluedCard> cards;
	
	public Hand() {
		cards = new ArrayList<CluedCard>();
	}
	
	public void draw(Card card) {
		cards.add(new CluedCard(card));
	}
	
	public Card discard(int pos) {
		return cards.remove(pos).getCard();
	}
	
	public int getColor(int pos) {
		return cards.get(pos).getColor();
	}
	
	public int getNumber(int pos) {
		return cards.get(pos).getNumber();
	}
	
	public int getCluedColor(int pos) {
		return cards.get(pos).getCluedColor();
	}
	
	public int getCluedNumber(int pos) {
		return cards.get(pos).getCluedNumber();
	}
	
	public boolean clueColor(int color) {
		boolean valid = false;
		
		for (CluedCard c : cards) {
			if (c.getColor() == color || c.getColor() == Card.MULTI) {
				c.clueColor(color);
				valid = true;
			}
		}
		return valid;
	}
	
	public boolean clueNumber(int number) {
		boolean valid = false;
		for (CluedCard c : cards) {
			if (c.getNumber() == number) {
				c.clueNumber(number);
				valid = true;
			}
		}
		return valid;
	}
	
	static class CluedCard {
		private final Card card;
		private int colorClued;
		private int numberClued;
		
		public CluedCard(Card c) {
			card = c;
		}
		
		public Card getCard() {
			return card;
		}
		
		public int getColor() {
			return card.color;
		}
		
		public int getNumber() {
			return card.number;
		}
		
		public int getCluedColor() {
			return colorClued;
		}
		
		public int getCluedNumber() {
			return numberClued;
		}
		
		public void clueColor(int color) {
			if (colorClued == -1) {
				colorClued = color;
			} else if (colorClued != color) {
				colorClued = Card.MULTI;
			}
		}
		
		public void clueNumber(int number) {
			numberClued = number;
		}
	}
}
