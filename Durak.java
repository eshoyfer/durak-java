import java.io.*;
import java.util.*;
import cardstatic.*;

public class Durak {
	public static String TRUMP;
	private Player one;
	private Player two;
	private Deck deck;
	private int round; // Natural number
	private Player attacker;
	private Player defender;
	private Field currentField;
	private boolean roundInitiated; // If a round is occurring and has completed its initiation stages

	public Scanner sc = new Scanner(System.in);
	public Random r = new Random();

	// Set default trump to hearts
	public Durak() {
		run();
	}
 
	public void run() {
		boolean running = true;

		while (running) {
			setup();
			game();
			System.out.println("The game has ended.");
			System.out.println("Play again? y/n");

			boolean validResponse = false;
			while (!validResponse) {
				String response = sc.nextLine();
				if (response.equals("y")) {
					validResponse = true;
					running = true;
				} else if (response.equals("n")) {
					validResponse = true;
					running = false;
				} else {
					validResponse = false;
				}
			}
		}


	}

	// Setting up for a game instance
	public void setup() {
		System.out.println("Welcome to Durak!");

		System.out.println("\nEnter the name of Player 1...\n");
		String oneName = sc.nextLine();
		System.out.println("\nEnter the name of Player 2...\n");
		String twoName = sc.nextLine();

		System.out.println("\nPlease wait!\n");
		System.out.println("Creating game...\n");

		System.out.println("Shuffling the cards...\n");
		deck = new Deck();

		System.out.println("Dealing the cards...\n");
		one = new Player(deck, oneName);
		two = new Player(deck, twoName);

		System.out.println("Determining trump card...\n");
		Card trumpCard = deck.draw();
		String trumpSuit = trumpCard.getSuit();
		TRUMP = trumpSuit;

		System.out.println("The trump is: " + TRUMP + "!\n");

		System.out.println("Reinserting trump card...\n");
		deck.reinsert(trumpCard);

		System.out.println("Resetting round count...\n");
		round = 1;

		System.out.println("The game is ready.\n");
	}

	// Running a game instance
	public void game() {
		System.out.println("Determining initial attacker...\n");
		if (r.nextInt(2) < 1) {
			setAttacker(one);
			setDefender(two);
		} else {
			setAttacker(two);
			setDefender(one);
		}



		System.out.println("The initial attacker is: " + attacker + ".");
		System.out.println("The initial defender is: " + defender + ".\n\n");

		// Round creation & handling until victoryAchieved()

		boolean gameOver = false;

		while (!gameOver) {

			boolean thisRound = round(); // Run a round

			if (victoryAchieved()) {
				// Victory was achieved by the playing of cards at some point
				// Arrived here after breaking out of the round instantly
				gameOver = true; // Break out of the game loop
			} else { 
				// No victory was achieved
				// Draw cards, last attacker first
				attacker.replenish();
				defender.replenish();
				round++; // Increment round number
				if (thisRound) {
					// Attacker won the round
					// Attacker goes again; no switching occurs
				} else {
					// Defender won the round
					// Roles switch
					switchRoles();
				}
			}
		}

		System.out.println("Game over!\n");
		System.out.println("The winner is " + determineWinner() + "!\n");
		return;
	}

	// Victory check
	public boolean victoryAchieved() {
		return ((one.victoryAchieved() || two.victoryAchieved()));
	}


	// Determines the winner.
	// Should only be called if victoryAchieved returns true.
	public Player determineWinner() {
		if (victoryAchieved()) {
			if (one.victoryAchieved()) {
				return one;
			} else {
				return two;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	// Runs a single round, given references to attacker and defender
	// Returns true if attacker succeeded
	// Returns false if defender succeeded
	public boolean round() {
		// Create references to attacker and defender


		// Generate header
		String roundName = "ROUND " + round;
		String headerLine = "==================== " + roundName + " ====================" + "\n";
		String headerContent = "Attacker: " + attacker + " | " + "Defender: " + defender + "\n";
		String header = "\n\n\n" + headerLine + headerContent + headerLine + "\n\n\n";

		roundInitiated = false; // Round is in initial stages

		System.out.println(header);
		System.out.println(roundName + " has begun!\n");

		// Initiation of round and its associated field

		System.out.println(attacker + ", initiate the attack!");

		int initialAttack = playerInput(attacker);
		Card initialAttackCard = attacker.useCard(initialAttack);
		announceCardPlayed(attacker, initialAttackCard);
		//  (CARD PLAYED: Check for victory!)

		if (victoryAchieved()) {
			return true; // Pop out of round (In game() screen, there should be a victoryAchieved() check after each round to immediately proceed from here)
		}

		Field roundField = new Field(initialAttackCard); // Generate a Field
		currentField = roundField;
		roundInitiated = true;

		while (!roundField.isCompleted()) {
			boolean defenderTurn = defenderResponse(roundField);
			if (defenderTurn || victoryAchieved()) {
				// Defender took the cards, ended the round
				// OR as a result of the defender's turn, victory was achieved (CARD PLAYED: Check for victory!)
				roundInitiated = false;
				currentField = null;
				return false; // Pop out of round
			} 

			// Defender responded by playing a card

			boolean attackerTurn = attackerResponse(roundField);
			if (attackerTurn || victoryAchieved()) {
				// Attacker declared the round to be over
				// OR as a result of the attacker's turn, victory was achieved (CARD PLAYED: Check for victory!)
				roundInitiated = false;
				currentField = null;
				return true; // Pop out of round
			}
		}

		return true; // Satisfy Java
	}

	public void announceCardPlayed(Player p, Card c) {
		if (p.isAttacker()) {
			System.out.println("\n\n" + p + " has played " + c + ", initiating a new pair!");
		} else {
			System.out.println("\n\n" + p + " has played " + c + " in response!");
		}
	}

	// Action: prints turn selection dialogue to console
	public void turnPrompt(Player player) {
		boolean isAttacker = player.isAttacker();

		String prompt = new String("\n\n"); // StartHeaderline + Precontent + Content + Tail + EndHeaderline
		String promptStartLine = "\n============ PROMPT ============\n\n";
		String promptEndLine = "\n========== END PROMPT ==========\n\n";
		String preContent = new String("CURRENT TRUMP: " + TRUMP + "\n\n");
		preContent += "CARDS REMAINING IN DECK: " + deck.size() + "\n\n";
		preContent += "CARDS REMAINING IN HAND: " + player.cardsInHand() + "\n\n";
		String fieldString;
		String content = new String();
		String tail = new String("=== MESSAGE ===\n\n");

		// Card selection component (same for everyone)
		content += player.cardList(); 
		content += "=== OTHER OPTIONS ===\n\n";

		if (isAttacker) {
			// Attacker (split)
			if (roundInitiated) {
				// Attacker in midst of round
				preContent += "# ATTACK CONTINUATION #\n\n";
				content += "0 | Beaten\n\n";
			} else {
				// Attacker initiating round
				preContent += "# ATTACK INITIATION #\n\n";
				content += "<none>\n\n";
			}
			tail += player + ", you're attacking!\n";
		} else {
			// Defender at any time
			preContent += "# DEFENSE #\n\n";
			content += "0 | Take\n\n";
			tail += player + ", you're defending!\n";
		}

		tail += "Make your move by hitting the corresponding key.\n";

		// Combining

		if (currentField == null) {
			fieldString = "<Empty Field>\n\n";
		} else {
			fieldString = "" + currentField;
		}

		prompt += promptStartLine + fieldString + preContent + content + tail + promptEndLine;

		System.out.println(prompt);
	}

	// Prints player options and accepts input
	// Handles improper input (not in terms of playable cards, but if it was available as a selection numerically)
	public int playerInput(Player p) {
		boolean isAttacker = p.isAttacker();

		turnPrompt(p);

		int playerSelection = -1;
		boolean properInput = false;
		while (!properInput) {
			playerSelection = sc.nextInt();
			if (isAttacker) {
				if (roundInitiated) {
					// Valid input: 0 through and including hand size
					properInput = ((playerSelection >= 0) && (playerSelection <= p.cardsInHand()));
				} else {
					// Valid input: 1 through and including hand size
					properInput = ((playerSelection >= 1) && (playerSelection <= p.cardsInHand()));
				}
			} else {
				// Valid input: 0 through and including hand size
				properInput = ((playerSelection >= 0) && (playerSelection <= p.cardsInHand()));
			}
			if (!properInput) {
				System.out.println("Invalid input. Please enter an acceptable value.");
			}
		}

		return playerSelection;
	}


	// Returns a boolean.
	// Indicates whether or not round was ended by defender.
	// If ended: returns true. Field has been closed. Defender has taken cards.
	// If not ended: returns false. A card has been played in response.
	public boolean defenderResponse(Field f) {
			int defenderResponse = -1;
			boolean properDefenderResponse = false;
			while (!properDefenderResponse) {
				try { // Is this a valid defender? Try to use it.
					defenderResponse = playerInput(defender);
					if (defenderResponse != 0) {
						// This is a card.
						Card defenderResponseCard = defender.getCard(defenderResponse); // getCard() is like peeking
						f.respond(defenderResponseCard);
						properDefenderResponse = true;
						defender.useCard(defenderResponse); // useCard() is like committing to the card
						announceCardPlayed(defender, defenderResponseCard);
						return false;
					} else {
						// This is not a card.
						// This is a request to take all cards and end the round (field).
						properDefenderResponse = true;
						System.out.println("\n\n" + defender + " has chosen to take all cards in the field and end the round!");
						ArrayList<Card> takenCards = f.fetchAllCards();
						for (Card card : takenCards) {
							defender.takeCard(card);
						}
						f.endField();
						return true;
					}
				} catch (IllegalArgumentException e) { // Invalid defender, another one will be solicited
					System.out.println("\n\nInvalid defender!");
					properDefenderResponse = false;
				}
			}
			return true; // Satisfy Java
	}

	// Returns a boolean.
	// Indicates whether or not round was ended by attacker.
	// If ended: returns true. Field has been closed. Round over. No one took cards.
	// If not ended: returns false. A card has been played in the field by the attacker.
	public boolean attackerResponse(Field f) {
			int attackerResponse = -1;
			boolean properAttackerResponse = false;
			while (!properAttackerResponse) {
				try { // Is this a valid move? Try to use it.
					attackerResponse = playerInput(attacker);
					if (attackerResponse != 0) {
						// This is a card.
						Card attackerResponseCard = attacker.getCard(attackerResponse);
						f.attack(attackerResponseCard);
						properAttackerResponse = true;
						attacker.useCard(attackerResponse);
						announceCardPlayed(attacker, attackerResponseCard);
						return false;
					} else {
						// This is not a card.
						// This is a request to end the round. No one takes any cards. The field closes.
						System.out.println("\n\n" + attacker + " has chosen to end the round!");
						properAttackerResponse = true;
						f.endField();
						return true;
					}
				} catch (IllegalArgumentException e) { // Invalid attack card, another one will be solicited
					System.out.println("Invalid attack card!");
					properAttackerResponse = false;
				}
			}
			return true; // Satisfy Java
	}

	// Determines current attacker once game is initiated
	// If true: one is attacker, two is defender
	// If false: one is defender, two is attacker
	public boolean whichAttacker() {
		return one.isAttacker();
	}

	public void setAttacker(Player p) {
		attacker = p;
		p.makeAttacker();
	}

	public void setDefender(Player p) {
		defender = p;
		p.makeDefender();
	}

	public void switchRoles() {
		Player temp = attacker;
		attacker = defender;
		defender = temp;
		one.switchRole();
		two.switchRole();
	}

	public Player getAttacker() {
		return attacker;
	}

	public Player getDefender() {
		return defender;
	}

	public boolean roundInitiated() {
		return roundInitiated;
	}
}