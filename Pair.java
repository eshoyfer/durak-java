import java.io.*;
import java.util.*;
import cardstatic.*;

public class Pair {
	// An attack/defense pair as part of gameplay

	private Card attacker;
	private Card defender;

	private boolean completed;

	public Pair(Card a) {
		attacker = a; 
		completed = false; // Completed means: did it receive a response (does it have a defender?)
	}

	// Defender responds
	public void response(Card d) {
		setDefender(d);
		toggleCompleted();
	}

	public Card getAttacker() {
		return attacker;
	}

	public Card getDefender() {
		return defender;
	}

	public boolean isCompleted() {
		return completed;
	}

	// Attacker not meant to be changed 

	// It's a valid defender if it wins.
	public boolean isValidDefender(Card d) {
		try {
			if (d.trueCompareTo(attacker) > 0) {
				return true;
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		return false;
	}

	// Sanitized setting of defender
	// Only a valid card if it beats the attacker
	public void setDefender(Card d) {
		if (isValidDefender(d)) {
			defender = d;
		} else {
			throw new IllegalArgumentException("Invalid defender");
		}
	}

	public void toggleCompleted() {
		completed = !completed;
	}

	// If the Pair is completed, returns ArrayList with 2 cards (attacker, defender)
	// if the Pair is not completed, returns ArrayList with 1 card (attacker)
	public ArrayList<Card> fetchAllCards() {
		ArrayList<Card> ret = new ArrayList<Card>();
		if (completed) {
			ret.add(attacker);
			ret.add(defender);
		} else {
			ret.add(attacker);
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = new String("{Pair}\n");
		for (Card card : fetchAllCards()) {
			ret += card + "\n";
		}
		ret += "{Pair}\n";
		return ret;
	}
}