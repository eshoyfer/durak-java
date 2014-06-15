import java.io.*;
import java.util.*;
import cardstatic.*;

public class Player {

	private Hand hand;
	private String name;
	private final int id;
	private Deck deck;
	private static int count = 0;
	private boolean attacker;

	// Create a new Player with an empty hand and set to draw from a default deck (does not draw).
	public Player() {
		count++;
		id = count;
		name = "Player " + id;
		hand = new Hand();
		deck = new Deck();
		attacker = false;
	}

	// Create a new Player and draw five cards from given deck.
	public Player(Deck d) {
		count++; 
		id = count; 
		name = "Player " + id;
		hand = new Hand();
		deck = d;
		drawCards(6);
		attacker = false;
		
	}

	// Create a new Player and draw five cards from given deck, with given name.
	public Player(Deck d, String n) {
		count++; 
		id = count; 
		name = n;
		hand = new Hand();
		deck = d;
		drawCards(6);
		attacker = false;
	}

	// Draws a card from given deck.
	// If empty, nothing happens.
	public void draw(Deck d) {
			if (!d.isEmpty()) {
				Card thisCard = d.draw();
				hand.add(thisCard);
			}
	}

	// Draws card from associated deck (instance variable).
	public void draw() {
		draw(deck);
	}

	// Draw n cards from given deck.
	public void drawCards(Deck d, int n) {
		for (int i = 0; i < n; i++) {
			draw(deck);
		}
	}

	// Draw n cards from associated deck (instance variable).
	public void drawCards(int n) {
		for (int i = 0; i < n; i++) {
			draw();
		}
	}

	public void takeCard(Card c) {
		hand.add(c);
	}

	// Discards a given card from Player's hand.
	public void discard(Card c) {
		hand.remove(c);
	}

	public int cardsInHand() {
		return hand.size();
	}

	// Draw cards until reaching 6
	public void replenish() {
		int toDraw = hand.numberToDraw();
		drawCards(toDraw);
	}

	// Victory check
	public boolean victoryAchieved() {
		return ((hand.size() <= 0) && (deck.isEmpty()));
	}

	public boolean isAttacker() {
		return attacker;
	}

	public void makeAttacker() {
		attacker = true;
	}

	public void makeDefender() {
		attacker = false;
	}

	public void switchRole() {
		attacker = !attacker;
	}

	@Override
	public String toString() {
		return name;
	}
	public String cardList() {
		String ret = "\n=== YOUR CARDS ===\n";
		ArrayList<Card> cards = hand.getCards();
		int i = 1;
		for (Card card : cards) {
			ret += i + " ~ " + card + "\n";
			i += 1;
		}
		ret += "\n\n";
		return ret;
	}

}