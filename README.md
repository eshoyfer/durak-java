Durak
=======

### Elvin Shoyfer
### AP CS - Mr. Zamansky Period 3 - Final Project
### Spring 2014

#### Introduction

Durak ("fool" in English) is a Russian card game that is commonly played in the post-Soviet states.

Read more: http://en.wikipedia.org/wiki/Durak

#### Basic Rules

Durak is played with a 36 card deck (numbers 2 through 5 that would normally be found in a 52 card deck are not included).
After the deck is shuffled, each player recieves six cards. 
A card is drawn from the top of the deck to determine the trump suit, then placed at the bottom of the deck in a visible manner (it can still be drawn).

In this implementation, there are two players.

Players take turns alternating between attacking and defending.
The attacker initiates his attack with a card of his choice placed face up on the board. 
His opponent has the option of taking the cards (surrendering) or defending.

Defense works as follows:
- Trump suit cards can only be defeated by higher ranked trump suit cards
- A non-trump suit card can be defeated by any trump suit card
- All cards can be defeated by a card of same suit and higher rank. 

After a round of defense, the attacker may declare his attack over (defeated) or may continue adding more cards.
The rule for adding additional cards is that they must have the same rank as a card already in play for that round.

After a round is over:

The attacker draws cards to replenish his deck first, up to six. This is then followed by the defender.
The new attacker is the winner of the previous round.

Play proceeds in this manner until the deck runs out and players fight to shed all cards.

Example of an attack/defense round:

TRUMP SUIT: Hearts

Attacker plays 6 of Spades.

The defender can choose to take the card or to defend.
The defender plays a 9 of Spades to counter this card [The defender had the option of playing any Spades card greater than 6, or any trump card].

This pair is completed. The attacker may continue attacking by initiating a new attack/defense pair or declare the end of his attack.

The attack chooses to keep going. 
His choices in attacking now are limited to only what is in play [A 6 or a 9 of any suit]. 

He plays a 6 of Hearts (Trump), initiating a new attack/defense pair.

The defender can choose to take all cards played so far this round or to defend.
In order to successfully defend, the defender must play any Hearts card greater than 6 (as Hearts is the trump suit).
He fails, and takes all cards in play. Since the defender lost, the attacker will attack again next round. 

Prior to the start of the next round, the attacker replenishes his cards first, followed by the defender.

#### Structure

Data was modelled from the ground up in order of increasing complexity.

Classes include `Card`, `Deck`, `Hand`, `Player`, `Pair`, and `Field`.

There is a `Static` class under package 'cardstatic' that governs the behavior of cards.

`Durak` contains the components that regulate gameplay.
`Driver` runs the game by instantiating the Durak class, triggering the start of the game.

Examples of 2nd term data structures: `HashMap` in `Static`, `Stack` in `Deck`

#### Running

Compile all files with `javac *.java` and run `java Driver`.

The game will initially prompt you for names of the players followed by numerical input for actual gameplay.