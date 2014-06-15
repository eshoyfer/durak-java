import java.io.*;
import java.util.*;
import cardstatic.*;

public class Field {
	// Generated when an attacking phase is initiated

	// ArrayList of Pairs

	private ArrayList<Pair> pairs;
	private ArrayList<String> playedRanks;
	private boolean completed;

	// Empty field
	public Field() {
		pairs = new ArrayList<Pair>();
		playedRanks = new ArrayList<String>();
		completed = false;
	}

	// Field generated from a new attack
	public Field(Card a) {
		pairs = new ArrayList<Pair>();
		playedRanks = new ArrayList<String>();
		completed = false;

		Pair initialPair = new Pair(a);
		pairs.add(initialPair);

		String initialCardRank = a.getRank();
		playedRanks.add(initialCardRank);
		
	}

	// Generates new pair with attacking card, adds it to field.
	public void attack(Card a) {
		if (canAttack() && isValidAttack(a)) {
			Pair newAttackPair = new Pair(a);
		} else {
			throw new IllegalArgumentException("You can't attack.");
		}
	}

	// Respond to the current open pair
	public void respond(Card d) {
		if (anyOpenPairs()) {
			Pair openPair = currentOpenPair();
			openPair.response(d); // Sanitized in Pair class
		}
	}

	// It's a valid attack if the attacking card is one of the playedRanks.
	public boolean isValidAttack(Card a) {
		String thisRank = a.getRank();
		for (String rank : playedRanks) {
			if (thisRank.equals(rank)) {
				return true;
			}
		}
		return false;
	}

	// You can attack if there are no open pairs.
	public boolean canAttack() {
		return !anyOpenPairs();
	}

	public boolean anyOpenPairs() {
		for (Pair pair : pairs) {
			if (!pair.isCompleted()) {
				return true;
			}
		}
		return false;
	}

	public boolean isCompleted() {
		return completed;
	}

	// Returns a reference to the current open pair:
	// Assumes there is a single open pair;
	// Use of this method should be preceded by a call to anyOpenPairs()
	// to determine game logic.
	public Pair currentOpenPair() {
		Pair ret = null;
		for (Pair pair : pairs) {
			if (!pair.isCompleted()) {
				ret = pair;
			}
		}
		if (ret != null) {
			return ret;
		} else {
			throw new IllegalArgumentException("There are no open pairs.");
		}
	}

	public void toggleCompleted() {
		completed = !completed;
	}

	// An end of a round 
	// It's a boolean, not a void:
	// The boolean indicates the result of this round
	// True: attack was successful.
	// False: defense was successful.
	// Should be used in tandem with fetchAllCards() once a round ends

	public boolean endField() {
		toggleCompleted();
		return anyOpenPairs();
	}

	// Returns an ArrayList of all cards associated with this Field.
	// Should be used in tandem with endField() once a round ends
	public ArrayList<Card> fetchAllCards() {
		ArrayList<Card> ret = new ArrayList<Card>();
		for (Pair pair : pairs) {
			ArrayList<Card> pairCards = pair.fetchAllCards();
			for (Card card: pairCards) {
				ret.add(card);
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = new String("+++ Field +++\n\n");
		for (Pair pair : pairs) {
			ret += pair + "\n\n";
		}
		ret += "+++ Field +++\n\n";
		return ret;
	}

}