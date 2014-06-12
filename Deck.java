import java.io.*;
import java.util.*;
import cardstatic.*;


public class Deck {

	private Stack<Card> cards;

	// Creates a standard shuffled 36 card deck
	public Deck() {
		cards = new Stack<Card>();

		ArrayList<Card> allCards = new ArrayList<Card>();
		for (String rank : Static.ranks) {
			for (String suit : Static.suits) {
				allCards.add(new Card(rank, suit));
				Collections.shuffle(allCards);
			}
		}

		for (Card card : allCards) {
			cards.push(card);
		}
	}

	public Card draw() {
		return cards.pop()
	}

	public int size() {
		return cards.size();
	}

	// Strangely, once a trump is drawn and viewed at the beginning of a game of Durak,
	// it is then reinserted at the bottom of the deck.
	// It is for this single purpose that the intent of the stack will be broken, using the inherited add method from Vector.
	public void reinsert(Card t) {
		cards.add(0, t);
	}

	@Override
	public String toString() {
		String ret = new String("[Bottom]\n");
		for (Card card : cards) {
			ret += card + "\n";
		}
		ret += "[Top]\n";
		return ret;
	}

}