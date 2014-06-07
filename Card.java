import java.io.*;
import java.util.*;

public class Card implements Comparable {

	private final String rank;
	private final String suit;
	private final String color; 

	private Random r = new Random();

	// In Durak, the playing cards ranked 2-5 are not used.

	// Rank constants:

	// Rank names
	public static final String[] ranks = {
		"6",
		"7",
		"8",
		"9",
		"10",
		"Jack",
		"Queen",
		"King",
		"Ace"
	};


	// Rank values
	public static final Map<String, Integer> values;
	static {
		Map<String, Integer> valuesMap = new HashMap<String, Integer>();
		valuesMap.put("6", 6);
		valuesMap.put("7", 7);
		valuesMap.put("8", 8);
		valuesMap.put("9", 9);
		valuesMap.put("10", 10);
		valuesMap.put("Jack", 11);
		valuesMap.put("Queen", 12);
		valuesMap.put("King", 13);
		valuesMap.put("Ace", 14);
		values = Collections.unmodifiableMap(valuesMap);
	}

	// Suit constants:
	public static final String[] suits = {
		"Hearts",
		"Diamonds",
		"Clubs",
		"Spades"
	};

	// Colors constants: 
	public static final Map<String, String> colors;
	static {
		Map<String, String> colorsMap = new HashMap<String, String>();
		colorsMap.put("Hearts", "Red");
		colorsMap.put("Diamonds", "Red");
		colorsMap.put("Clubs", "Black");
		colorsMap.put("Spades", "Black");
		colors = Collections.unmodifiableMap(colorsMap);

	// Create a random card
	public Card() {

		int randRankIndex = r.nextInt(9);
		rank = ranks[randRankIndex];

		int randSuitIndex = r.nextInt(4);
		suit = suits[randSuitIndex];

		color = colors.get(suit);

	}

	// Create a card with specified rank, suit
	public Card(String r, String s) {

		if (Arrays.asList(ranks).contains(r) && Arrays.asList(suits).contains(s)) {
			rank = r;
			suit = s;
			color = colors.get(suit);
		} else {
			throw new IllegalArgumentException("Invalid rank or suit");
		}

	}

	@Override
	public String toString() {
		String ret = "<" + rank + ", " + suit + ">";
		return ret;
	}

	@Override
	// Comparison purely by rank
	public int compareTo(Object o) {

		Card otherCard = (Card) o;

		int thisValue = values.get(rank);
		int otherValue = values.get(otherCard.rank);

		return thisValue - otherValue;
	}

	// Comparison accounting for trump
	// Accepts trump parameter (part of game state)
	public int trueCompareTo(Object o, String t) {

		Card otherCard = (Card) o; 

		boolean thisCardIsTrump = this.isTrump(t);
		boolean otherCardIsTrump = otherCard.isTrump(t);

		int valueDifference = this.compareTo(o);

		if (thisCardIsTrump && otherCardIsTrump) {
			return valueDifference;
		} else if (thisCardIsTrump && !otherCardIsTrump) {
			return 1;
		} else if (!thisCardIsTrump && otherCardIsTrump) {
			return -1;
		} else if (sameSuit(o)) {
			return valueDifference;
		} else {
			// Different suit; cannot occur based on game rules
			throw new IllegalArgumentException("Different suit");
		}

	}

	public int trueCompareTo(Object o) {
		return trueCompareTo(o, Durak.TRUMP);
	}

	// Accepts trump parameter (part of game state)
	public boolean isTrump(String t) {

		return suit.equals(t);

	}

	public boolean isTrump() {
		return suit.equals(Durak.TRUMP);
	}

	public boolean sameSuit(Object o) {

		Card otherCard = (Card) o;

		String thisSuit = this.suit;
		String otherSuit = otherCard.suit;

		return thisSuit.equals(otherSuit);
	}

}