import java.io.*;
import java.util.*;
import cardstatic.*;

public class Hand {

	private ArrayList<Card> cards;

	// Empty hand
	public Hand() {
		cards = new ArrayList<Card>();
	}

	// Hand of n random cards
	public Hand(int n) {
		cards = new ArrayList<Card>();
		for (int i = 0; i < n; i++) {
			Card thisCard = new Card();
			cards.add(thisCard);
		}
	}

	public void add(Card c) {
		cards.add(c);
	}

	public void remove(Card c) {
		cards.remove(c);
	}

	public int size() {
		return cards.size();
	}

	public boolean needsToDraw() {
		if (size() > 6) {
			return true;
		} else {
			return false;
		}
	}

	public int numberToDraw() {
		if (needsToDraw()) {
			return 0; 
		} else {
			return 6 - size();
		}
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	// Here, i represents true zero-based index.

	public Card getCardByIndex(int i) {
		return cards.get(i);
	}

	public Card useCardByIndex(int i) {
		return cards.remove(i);
	}

	@Override
	public String toString() {
		String ret = new String();
		for (Card c : cards) {
			ret += c + "\n";
		}
		return ret;
	}

}