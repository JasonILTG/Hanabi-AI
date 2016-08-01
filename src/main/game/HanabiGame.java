package main.game;

import main.enums.Color;
import main.enums.GameMode;
import main.enums.Number;
import main.event.Event;
import main.event.EventBus;
import main.event.EventListener;
import main.game.breakpoint.DeathFail;
import main.game.breakpoint.DeckEmpty;
import main.game.breakpoint.FullScore;
import main.game.events.DiscardEvent;
import main.game.events.DrawEvent;
import main.game.events.PlayEvent;
import main.game.utils.CyclicIterator;
import main.moves.DiscardMove;
import main.moves.Move;
import main.moves.PlayMove;
import main.parts.Card;
import main.parts.Deck;
import main.parts.Hand;
import main.players.Human;
import main.players.Player;
import main.players.jason.JasonHanabiAI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Class for all of the game logic.
 */
public class HanabiGame
		implements EventListener
{
	/** A way for the game to provide authentication to read a player's hand */
	public class Authenticator
	{
		private Authenticator()
		{
		}
	}
	
	private Authenticator auth;
	public static final Scanner in = new Scanner(System.in);
	
	/** Hand sizes for different numbers of players */
	public static final int[] HAND_SIZES = { 0, 0, 7, 5, 4, 4 };
	
	public final GameMode mode;
	private final boolean logging;
	
	private int clues;
	private int lives;
	
	private Deck deck;
	/** A map from the colors to the number of cards played for that color */
	private HashMap<Color, Integer> fireworks;
	private ArrayList<Card> discard;
	
	private LinkedList<Player> players;
	private CyclicIterator<Player> playerItr;
	private Player currentPlayer;
	
	Map<Color, Map<Number, Integer>> discardCountMap;
	
	/**
	 * Constructor.
	 *
	 * @param mode    The game mode
	 * @param logging Whether to log the game states and moves
	 */
	public HanabiGame(GameMode mode, boolean logging)
	{
		this.mode = mode;
		this.logging = logging;
		players = new LinkedList<>();
		auth = new Authenticator();
		setup();
		setupListener();
	}
	
	/**
	 * Registers the relevant event association for this game.
	 */
	private void setupListener()
	{
		// Listen to discard events
		EventBus.addListener(DiscardEvent.class, this);
	}
	
	@Override
	public void onEvent(Event event)
	{
		if (event instanceof DiscardEvent) {
			Card card = ((DiscardEvent) event).discard;
			
			// Add the discarded card to the mapping
			Map<Number, Integer> coloredDiscardMap = discardCountMap.get(card.color);
			// If the mapping does not exist yet, create one.
			if (coloredDiscardMap == null) {
				coloredDiscardMap = new HashMap<>();
				discardCountMap.put(card.color, coloredDiscardMap);
			}
			
			Integer currentDiscardCount = coloredDiscardMap.get(card.number);
			// If the mapping does not exist yet, set to 0.
			if (currentDiscardCount == null) {
				currentDiscardCount = 0;
			}
			// Add one to the discard count and save
			coloredDiscardMap.put(card.number, currentDiscardCount + 1);
		}
	}
	
	/**
	 * @return The number of clues left to use
	 */
	public int getClueCount()
	{
		return clues;
	}
	
	/**
	 * @return The number of cards remaining in the deck
	 */
	public int getDeckSize()
	{
		return deck.size();
	}
	
	/**
	 * Adds a player to this game
	 */
	public void addPlayer(Player player)
	{
		players.add(player);
	}
	
	/**
	 * Prepares the game to be ready to play.
	 */
	private void setup()
	{
		// Set up clues and lives
		clues = 8;
		lives = 3;
		
		// Initialize deck
		if (mode == GameMode.NORMAL) {
			deck = new Deck();
		}
		else {
			// Include extra color if appropriate
			deck = new Deck(mode.extraColor, mode.hard);
		}
		deck.shuffle();
		
		// Initialize discard
		discard = new ArrayList<>();
		discardCountMap = new HashMap<>();
		
		// Initialize fireworks
		fireworks = new HashMap<>();
		for (Color color : mode.colors) {
			fireworks.put(color, 0);
		}
	}
	
	/**
	 * Executes the actions to do just before the start of a game.
	 * <br>
	 * The actions include dealing cards and randomizing the first player.
	 */
	private void preStart()
	{
		// Create and build hands
		Hand[] hands = new Hand[players.size()];
		int handSize = HAND_SIZES[players.size()];
		
		for (int index = 0; index < hands.length; index++) {
			hands[index] = new Hand(players.get(index));
			// Draw cards
			for (int i = 0; i < handSize; i++) {
				hands[index].draw(deck.draw());
			}
			// Give hand to player
			players.get(index).setHand(hands[index]);
		}
		
		// Create player iterator
		playerItr = new CyclicIterator<>(players);
		// Go to a random position in the list
		int startPos = new Random().nextInt(players.size());
		for (int i = 0; i < startPos; i++) {
			playerItr.next();
		}
	}
	
	/**
	 * Checks if there should be a significant turn of events in the game.
	 *
	 * @throws DeathFail The players failed the game by losing all their lives
	 * @throws DeckEmpty The last card in the deck was drawn
	 * @throws FullScore The players achieved the highest score possible under their present conditions
	 */
	private void checkGameOver()
			throws DeathFail, DeckEmpty, FullScore
	{
		// If there are no more lives, game ends.
		if (lives == 0) throw new DeathFail();
		
		// If the deck is empty, everyone will take their last turn.
		if (deck.isEmpty()) throw new DeckEmpty();
		
		// Check that max score is reached
		boolean fullScore = true;
		Map<Color, Integer> maxScoreMap = buildMaxScoreMap();
		for (Map.Entry<Color, Integer> maxScoreEntry : maxScoreMap.entrySet())
		{
			if ()
		}
	}
	
	/**
	 * @return A map from each color to the maximum core attainable on that color
	 */
	private Map<Color, Integer> buildMaxScoreMap()
	{
		// Build a map of the number of discards made on each.
		// Variable names are confusing in this section; just trust me that this will work. Probably.
		Map<Color, Integer> maxScoreMap = new HashMap<>();
		
		for (Map.Entry<Color, Map<Number, Integer>> coloredDiscardEntry : discardCountMap.entrySet()) {
			// Initialize to 5
			int maxScore = 5;
			
			// For each Number, check if all the cards were discarded.
			for (Number n : Number.VALUES) {
				Integer colorNumberDiscardCount = coloredDiscardEntry.getValue().get(n);
				if (colorNumberDiscardCount != null && colorNumberDiscardCount.intValue() == n.amount) {
					// All cards of this color and this number has been discarded, set the max score and break.
					maxScore = n.getValue();
					break;
				}
			}
			
			// Record the result
			maxScoreMap.put(coloredDiscardEntry.getKey(), maxScore);
		}
		return maxScoreMap;
	}
	
	/**
	 * Rebuilds the discard count using the discard pile to verify that the information is correct.
	 */
	private void rebuildDiscardCountMap()
	{
		// TODO
	}
	
	/**
	 * Execute a play move the player requested.
	 *
	 * @return Whether the execution was successful
	 */
	private boolean play(PlayMove move)
	{
		// Check play index
		if (move.pos < 0 || move.pos >= currentPlayer.getHand().size()) {
			// Message the player if it is a human
			if (currentPlayer instanceof Human) {
				Human player = (Human) currentPlayer;
				player.message("Invalid card index.");
			}
			return false;
		}
		
		// Remove the card from player's hand and let the player draw a new card
		Card play = currentPlayer.getHand().take(move.pos);
		Card draw = null;
		try {
			draw = deck.draw();
		}
		catch (IndexOutOfBoundsException ex) {} // Happens when there are no cards left.
		
		// Evaluate if the play is successful or not.
		int expectedNumber = fireworks.get(play.color).intValue() + 1;
		PlayEvent playEvent;
		if (play.number.getValue() == expectedNumber) {
			// Play successful.
			fireworks.put(play.color, play.number.getValue());
			// If the players played a 5, increase number of clues by 1.
			if (play.number == Number.FIVE) {
				clues++;
			}
			playEvent = new PlayEvent(currentPlayer, play, true);
		}
		else {
			// Play unsuccessful, the card goes to the discard pile, and players lose one life.
			discard.add(play);
			lives--;
			playEvent = new PlayEvent(currentPlayer, play, false);
		}
		
		// Fire the events
		EventBus.fireEvent(playEvent);
		EventBus.fireEvent(new DrawEvent(currentPlayer, draw));
		
		// Execution successful.
		return true;
	}
	
	/**
	 * Execute a discard move the player requested.
	 *
	 * @return Whether the execution was successful
	 */
	private boolean discard(DiscardMove move)
	{
		// Check play index
		if (move.pos < 0 || move.pos >= currentPlayer.getHand().size()) {
			// Message the player if it is a human
			if (currentPlayer instanceof Human) {
				Human player = (Human) currentPlayer;
				player.message("Invalid card index.");
			}
			return false;
		}
		
		// Remove the card from player's hand and let the player draw a new card
		Card discard = currentPlayer.getHand().take(move.pos);
		Card draw = null;
		try {
			draw = deck.draw();
		}
		catch (IndexOutOfBoundsException ex) {} // Happens when there are no cards left.
		
		// Add to the discard pile
		this.discard.add(discard);
		
		// Fire the events
		EventBus.fireEvent(new DiscardEvent(currentPlayer, discard));
		EventBus.fireEvent(new DrawEvent(currentPlayer, draw));
		
		// Execution successful.
		return true;
	}
	
	/**
	 * Executes a move given by the player.
	 *
	 * @param m The move to execute
	 * @return Whether the execution was successful
	 */
	private boolean executeMove(Move m)
	{
		switch (m.type) {
			case PLAY:
				return play((PlayMove) m);
			case DISCARD:
				break; // TODO execute the discard move
			case CLUE:
				break;// TODO execute the clue move
		}
	}
	
	/**
	 * Progresses the game by one turn.
	 */
	private void doTurn()
	{
		// Keep querying for the next move.
		while (!executeMove(currentPlayer.getNextMove())) ;
		// Advance to the next player.
		currentPlayer = playerItr.next();
	}
	
	/**
	 * @return The current score earned in the game
	 */
	private int getScore()
	{
		int score = 0;
		// Add all the values of the fireworks
		for (Map.Entry<Color, Integer> entry : fireworks.entrySet()) {
			score += entry.getValue();
		}
		
		return score;
	}
	
	/**
	 * Plays the game.
	 *
	 * @return The ending score
	 */
	public int play()
	{
		try {
			while (true) {
				checkGameOver();
				doTurn();
			}
		}
		catch (DeckEmpty deckEmpty) {
			// TODO Action when deck is empty
		}
		catch (DeathFail deathFail) {
			// All lives lost, game failed.
			return 0;
		}
		catch (FullScore fullScore) {
			// Calculate score.
			return getScore();
		}
		
		return getScore();
	}
	
	/**
	 * Resets the game to be played again.
	 */
	private void reset()
	{
		setup();
	}
	
	private void println()
	{
		System.out.println();
	}
	
	private void println(String message)
	{
		System.out.println(message);
	}
	
	/**
	 * If the current player is human, send the player a message.
	 */
	private void message(String msg)
	{
		if (currentPlayer instanceof Human) {
			Human player = (Human) currentPlayer;
			player.message(msg);
		}
	}
	
	/**
	 * Logs the game state.
	 */
	private void stateLog()
	{
		println("--------------------------------------------------------------");
		println("Clues: " + clues + " | Lives: " + lives);
		println();
		
		println("Fireworks: ");
		StringBuilder f = new StringBuilder();
		for (Map.Entry<Color, Integer> e : fireworks.entrySet()) {
			f.append(e.getKey().ansi() + e.getValue());
		}
		println(f.toString());
		println();
		
		for (int i = 0; i < players.size(); i++) {
			println(players.get(i) + "'s hand: " + players.get(i).getHand());
		}
		println("--------------------------------------------------------------");
		println();
	}
	
	public static void main(String[] args)
	{
		
		for (int p = 4; p <= 4; p++) {
			HanabiGame g = new HanabiGame(GameMode.NORMAL, false);
			g.addPlayer(new JasonHanabiAI("Jackie", g));
			g.addPlayer(new JasonHanabiAI("Jason", g));
			g.addPlayer(new JasonHanabiAI("Milan", g));
			g.addPlayer(new JasonHanabiAI("Maya", g));
			g.addPlayer(new JasonHanabiAI("Pesto", g));
			
			int MAX = 100000;
			
			int num = 0;
			int score = 0;
			double total = 0;
			int[] scores = new int[26];
			while (num <= MAX) {
				score = g.play();
				scores[score]++;
				total += score;
				g.reset();
				num++;
				
				if (num % (MAX / 100) == 0) {
					System.out.print(".");
				}
				/*if (num % 10000 == 0) {
					System.out.println("Score distribution at " + num + ": ");
					for (int i = 0; i < scores.length; i++) {
						System.out.println(i + ": " + scores[i]);
					}
					System.out.println("Average over " + num + " games: " + (total / num));
				}*/
			}
			
			System.out.println();
			System.out.println("Final score distribution (" + p + " players): ");
			for (int i = 0; i < scores.length; i++) {
				System.out.println(i + ": " + scores[i]);
			}
			System.out.println("Average over " + num + " games: " + total / num);
		}
	}
}