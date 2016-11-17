package game.logic;

import game.Config;
import game.GameSettings;
import game.card.Card;
import game.card.Deck;
import game.card.DiscardPile;
import game.card.Hand;
import game.card.HintedCard;
import game.card.PlayPile;
import game.logic.event.AbstractGameEvent;
import game.logic.event.ClueEvent;
import game.logic.event.DiscardEvent;
import game.logic.event.DrawEvent;
import game.logic.event.GameEndEvent;
import game.logic.event.PlayEvent;
import game.player.AbstractPlayer;
import game.player.move.AbstractPlayerMove;
import game.player.move.ClueMove;
import game.player.move.DiscardMove;
import game.player.move.PlayMove;
import utils.CyclicIterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HanabiGame
{
	/**
	 * The class that will handle all the requests from players and return information on the current game status.
	 * One game interface will be assigned to each player.
	 */
	public class PlayerInterface
	{
		private final AbstractPlayer requester;
		
		public PlayerInterface(AbstractPlayer requester)
		{
			this.requester = requester;
		}
		
		public GameSettings getSettings()
		{
			return settings;
		}
		
		public int getLives()
		{
			return lives;
		}
		
		public int getClues()
		{
			return clues;
		}
		
		public boolean isCountdown()
		{
			return countdown;
		}
		
		public int getTurnsRemaining()
		{
			return turnsRemaining;
		}
		
		public int getDeckSize()
		{
			return deck.size();
		}
		
		public PlayPile getPlayed()
		{
			return played;
		}
		
		public DiscardPile getDiscards()
		{
			return discards;
		}
		
		/**
		 * Will automatically hide the cards if the player is getting its own hand.
		 *
		 * @return The hand of the player requested
		 */
		public Hand getHand(AbstractPlayer player)
		{
			return handMap.get(player).getProtected(player == requester);
		}
		
		public AbstractPlayer getCurrentPlayer()
		{
			return currentPlayer;
		}
		
		/**
		 * @return An iterator containing the order in which the players take turn, starting at the player who made the request.
		 */
		public CyclicIterator<AbstractPlayer> getPlayerOrder()
		{
			return new CyclicIterator<AbstractPlayer>(orderedPlayers, orderedPlayers.indexOf(requester));
		}
	}
	
	/**
	 * A map that links the players to their hands
	 */
	private Map<AbstractPlayer, Hand> handMap;
	/**
	 * The order in which players will have their turn
	 */
	private List<AbstractPlayer> orderedPlayers;
	private CyclicIterator<AbstractPlayer> playerIterator;
	private AbstractPlayer currentPlayer;
	
	// Fields that keep track of the game status
	private GameSettings settings;
	private boolean gameInProgress;
	
	private int lives;
	private int clues;
	/**
	 * Once all cards are dealt, every player will take one more turn
	 */
	private int turnsRemaining;
	private boolean countdown;
	private Deck deck;
	private PlayPile played;
	private DiscardPile discards;
	
	public HanabiGame(GameSettings settings)
	{
		this.settings = settings;
		gameInProgress = false;
	}
	
	private int getDeckSize()
	{
		return deck.size();
	}
	
	private Hand getHand(AbstractPlayer player)
	{
		return handMap.get(player);
	}
	
	/**
	 * Constructs the lives, clues, deck, play and discards, and player hands.
	 * <p>
	 * Also generates and shuffles the deck.
	 * <p>
	 * This should be done once per execution.
	 */
	public void initialize()
	{
		lives = 3;
		clues = 8;
		turnsRemaining = orderedPlayers.size();
		countdown = false;
		deck = new Deck(settings);
		deck.initialize();
		played = new PlayPile(settings);
		discards = new DiscardPile();
		handMap = new HashMap<>();
		orderedPlayers = new LinkedList<>();
	}
	
	/**
	 * Calls to this method should be done between games.
	 */
	public void addPlayer(AbstractPlayer player)
	{
		if (gameInProgress)
		{
			throw new RuntimeException("Cannot add player while game is in progress!");
		}
		
		// Add player to the game
		orderedPlayers.add(player);
		
		// Have player join the game and receive an interface
		player.setPlayerInterface(new PlayerInterface(player));
	}
	
	/**
	 * Scrambles players and assigns Hands to players.
	 */
	public void startGame()
	{
		gameInProgress = true;
		
		// Shuffle
		Collections.shuffle(orderedPlayers);
		// Set up iterator
		playerIterator = new CyclicIterator<>(orderedPlayers);
		
		// Assigns hands
		int cardCount = Config.HAND_SIZES[orderedPlayers.size()];
		for (AbstractPlayer player : orderedPlayers)
		{
			Hand playerHand = new Hand();
			// Add the appropriate number of cards
			for (int i = 0; i < cardCount; i++)
			{
				playerHand.giveCard(deck.dealCard());
			}
			
			handMap.put(player, playerHand);
		}
	}
	
	/**
	 * Sends an event to all players.
	 */
	private void fireEvent(AbstractGameEvent event)
	{
		for (AbstractPlayer player : orderedPlayers)
		{
			player.onEvent(event);
		}
	}
	
	/**
	 * Executes turn for the players.
	 */
	private void doTurn()
	{
		// Go to the next player
		currentPlayer = playerIterator.next();
		
		// Execute the next player's move
		AbstractPlayerMove nextMove;
		do
		{
			nextMove = currentPlayer.getNextMove();
		}
		while (!tryExecuteMove(nextMove));
		
		// If the game is in countdown then decrease the number of turns remaining
		if (countdown)
		{
			turnsRemaining--;
		}
		else
		{
			// If there are no cards left in deck then start countdown
			if (deck.size() <= 0)
			{
				countdown = true;
			}
		}
		
		// Game ending checks executed between turns.
	}
	
	/**
	 * Tries to execute the move given by player.
	 *
	 * @return true if the execution is successful (including a failed play), and false if the move is impossible to execute
	 */
	private boolean tryExecuteMove(AbstractPlayerMove move)
	{
		// TODO check that this works
		
		if (move instanceof DiscardMove)
		{
			DiscardMove discardMove = (DiscardMove) move;
			
			// Discard from the player's hand
			Card cardDiscarded = getHand(currentPlayer).removeCard(discardMove.index).card;
			discards.addCard(cardDiscarded);
			
			// Players gain 1 extra clue
			clues++;
			
			// Fire the on discard event
			fireEvent(new DiscardEvent(currentPlayer, discardMove.index, cardDiscarded));
			
			// Draw from the deck, if there is more.
			tryDrawCard();
			
			return true;
		}
		else if (move instanceof ClueMove)
		{
			ClueMove clueMove = (ClueMove) move;
			
			// Check if there are enough hints left in the pool
			if (clues <= 0)
			{
				currentPlayer.onFailedMove(clueMove);
				return false;
			}
			
			// Use up 1 clue and gives the player a clue
			clues--;
			if (clueMove instanceof ClueMove.Color)
			{
				ClueMove.Color colorClue = (ClueMove.Color) clueMove;
				getHand(clueMove.target).applyClue(colorClue.clueColor);
				
				// Fire the event
				fireEvent(new ClueEvent.Color(currentPlayer, clueMove.target, colorClue.clueColor));
			}
			else if (clueMove instanceof ClueMove.Number)
			{
				ClueMove.Number numberClue = (ClueMove.Number) clueMove;
				getHand(currentPlayer).applyClue(numberClue.clueNumber);
				
				// Fire the event
				fireEvent(new ClueEvent.Number(currentPlayer, clueMove.target, numberClue.clueNumber));
			}
			
			// Draw a card to compensate the
			
			return true;
		}
		else if (move instanceof PlayMove)
		{
			PlayMove playMove = (PlayMove) move;
			HintedCard cardPlayed = getHand(currentPlayer).removeCard(playMove.index);
			
			if (played.canPlay(cardPlayed.card))
			{
				// Successful play
				played.addCard(cardPlayed.card);
				// If played a 5, gain 1 clue
				if (cardPlayed.card.number == Card.Number.FIVE)
				{
					clues++;
				}
				
				fireEvent(new PlayEvent(currentPlayer, cardPlayed, true));
			}
			else
			{
				// Failed play, card goes into discard
				discards.addCard(cardPlayed.card);
				// Lose 1 life
				lives--;
				
				fireEvent(new PlayEvent(currentPlayer, cardPlayed, false));
			}
			
			// Draw from the deck, if there is more.
			tryDrawCard();
			
			return true;
		}
		
		// What the heck is going on? You are not supposed to be here.
		return false;
	}
	
	/**
	 * Tries to have the current player draw a card.
	 */
	private void tryDrawCard()
	{
		if (getDeckSize() > 0)
		{
			Card cardDrawn = deck.dealCard();
			getHand(currentPlayer).giveCard(cardDrawn);
			
			// Fire the draw card event
			fireEvent(new DrawEvent(currentPlayer, cardDrawn));
		}
	}
	
	/**
	 * @return True if the game will end this turn, and false if it will not.
	 */
	private GameEndEvent checkGameEnd()
	{
		// Check if all lives are lost
		if (lives <= 0)
		{
			return GameEndEvent.DEATH;
		}
		
		// Check if the countdown has ended
		if (turnsRemaining <= 0)
		{
			return GameEndEvent.CARD_LIMIT_REACHED;
		}
		
		// Check if max score is reached
		
		if (calcMaxScore() <= played.getTotalScore())
		{
			// Double check.
			discards.recalculateDiscardMap();
			
			if (calcMaxScore() <= played.getTotalScore())
			{
				return GameEndEvent.MAX_SCORE_REACHED;
			}
		}
		
		return null;
	}
	
	/**
	 * @return The maximum score that can be achieved at this stage of the game
	 */
	private int calcMaxScore()
	{
		Map<Card.Color, Integer> maxScoreMap = new HashMap<>();
		for (Card.Color c : Card.Color.values())
		{
			// All colors can go up to 5.
			maxScoreMap.put(c, 5);
		}
		
		for (Map.Entry<Card, Integer> entry : discards.getDiscardCount().entrySet())
		{
			// Get the number of cards available overall for that card
			int cardCount = settings.evalCardCount(entry.getKey());
			if (cardCount <= entry.getValue())
			{
				// The cards run out at this point, then the max score is 1 below this card's value.
				maxScoreMap.put(entry.getKey().color, entry.getKey().number.value - 1);
			}
		}
		
		// Sum up the values
		int maxScore = 0;
		for (Integer num : maxScoreMap.values())
		{
			maxScore += num;
		}
		
		return maxScore;
	}
	
	/**
	 * Actions to execute when the game ends.
	 */
	private void onGameEnd(GameEndEvent gameEndEvent)
	{
		gameInProgress = false;
		for (AbstractPlayer player : orderedPlayers)
		{
			player.onGameEnd();
		}
	}
	
	private void play()
	{
		startGame();
		
		GameEndEvent gameEnd = null;
		do
		{
			doTurn();
			gameEnd = checkGameEnd();
		}
		while (gameEnd == null);
		
		onGameEnd(gameEnd);
	}
	
	/**
	 * Plays the game once and calculates the final score.
	 *
	 * @return The final score in the game
	 */
	public int playAndGetScore()
	{
		play();
		
		return played.getTotalScore();
	}
}
