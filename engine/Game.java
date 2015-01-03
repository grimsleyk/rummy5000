package engine;

import graphics.PopUps;
import graphics.GameViewer;
import graphics.GraphicController;

import java.awt.Rectangle;
import java.io.ObjectInputStream;
import java.io.Serializable;

import network.DataPacket;
import network.GameServer;
import network.ServerDispatcher;

import resources.CardPile;
import resources.Logger;
import resources.Player;
import resources.PlayingCard;
import resources.ScorePile;
import resources.ScoreSheet;
import resources.Utilities;


/**
 * A class that runs a game of Rummy.
 * 
 */
public class Game implements Serializable
{
	public static int turn;
	public static int round;
	public static boolean P1NextRound;
	public static boolean P1Next;
	public boolean refresh;
	public boolean pickupFromDropPile;
	public int state;
	public int scoreOneButton;	
	public static boolean changeState;		
	public GameViewer gameView;
	public Player p1, p2;
	
	public CardPile deck, dropPile;
	public CardPile scorePileBuffer; // A buffer CardPile while user selects
	                                  // cards to drop for run/trips
	public CardPile resetBuffer;
	public CardPile p1HandCopy, p2HandCopy, dropPileCopy;
	public ScorePile scorePile;
	public ScorePile backupScorePile;
	public ScoreSheet scoreSheet;
	public GameServer gServer;
	
	ServerDispatcher servDispatcher;
	DataPacket packet = new DataPacket();
	ObjectInputStream in;
	
	// Button variables:
	public int discardVal;
	public int scoreRunVal;
	public int scoreSetVal;
	public int scoreAddSetVal;
	public int addRunLowVal;
	public int addRunHighVal;
	
	public static final String  PLAYER_1 = "Player1";
	public static final String  PLAYER_2 = "Player2";
	public static final String  NONE = "none";
	public static final int FINAL_SCORE = 500;

	int test;
	
	/**
	 * Constructor for Game, a class that runs a game of Rummy.
	 * 
	 * @param aInPlayer1 - player one.
	 * @param aInPlatyer2 - player two.
	 */
	public Game(Player aInPlayer1, Player aInPlayer2, GameViewer aInGV, 
			GameServer aInServer) 
	{
		turn = 1;
		round = 1;
		P1NextRound = true; //P1 starts game
		boolean lEndGame = false;
		p1 = aInPlayer1;
		p2 = aInPlayer2;
		gameView = aInGV;
		gServer = aInServer;
		servDispatcher = new ServerDispatcher();
		packet = new DataPacket();
		state = 0;
		scoreOneButton = 0;	
		refresh = false;
		scoreSheet = new ScoreSheet();
		backupScorePile = new ScorePile();
		
		String lWinner = "";

		//Buttons:
		setDiscardVal(-1);
				
		// Until game finishes, loop through each round
		while(!lEndGame)
		{
			lEndGame = round();
		}			
		
		// Handle end of game stuff
		packet.updatePacket(deck, dropPile, scorePile, p1.hand, p2.hand, 
				P1Next, turn, round, "endGame", p1.socket, false, 
				getDiscardVal(), null, scoreSheet, true, lEndGame);	
		
		p2.mClientSender.sendMessageToClient(packet);
		
		if(scoreSheet.getTotalScoreP1() > scoreSheet.getTotalScoreP2())
		{
			lWinner = "Player 1";
		}
		else
		{
			lWinner = "Player 2";
		}
		PopUps.gameOver(scoreSheet.getTotalScoreP1(),
				scoreSheet.getTotalScoreP2(), lWinner);
		
	}
	
	/**
	 * Executes a Round in Rummy.
	 * 
	 * @return lReturnState - false ends game.
	 */
	public  boolean round()
	{
		// Prepare round
		deck = new CardPile("deck");
		dropPile = new CardPile("drop");
		scorePile = new ScorePile();
		p1.resetHand();
		p2.resetHand();
		boolean lEndRound = false;
		boolean lEndGame = false;
		pickupFromDropPile = false;
		setTurn(1);			
		
		setP1Next(getP1NextRound()); // Set up who starts this round.
		setP1NextRound(!getP1NextRound()); // Alternate who starts next round
		
		
		
		//Set Up Turn
		deck = CardActions.buildDeck(deck);
		deck = CardActions.shuffle(deck);
		CardActions.deal(p1, p2, deck, dropPile);
		p1.hand = CardActions.sortPile(p1.hand);
		p2.hand = CardActions.sortPile(p2.hand);		
		
		
		// While round not over, execute new turn:
		while(!lEndRound)
		{
			lEndRound = turn();
		}
		
		//End round. Add Scores, clean up and increment turn:				
		if(p1.hand.size() == 0)
		{
			Logger.logDebug("round=" + scoreSheet.getRoundScoreP2() +
					"\ntotal=" + scoreSheet.getTotalScoreP2(), "game");
			
			// Player 1 winds, add p1 score and subtract p2.
			scoreSheet.setTotalScoreP1(scoreSheet.getTotalScoreP1() + 
					scoreSheet.getRoundScoreP1());
			scoreSheet.setRoundScoreP1(0);
			scoreSheet.setTotalScoreP2(scoreSheet.getTotalScoreP2() + 
					scoreSheet.getRoundScoreP2() - 
					Utilities.countScore(p2.hand));
			scoreSheet.setRoundScoreP2(0);
		}
		else if(p2.hand.size() == 0)
		{
			scoreSheet.setTotalScoreP2(scoreSheet.getTotalScoreP2() + 
					scoreSheet.getRoundScoreP2());
			scoreSheet.setRoundScoreP2(0);
			scoreSheet.setTotalScoreP1(scoreSheet.getTotalScoreP1() + 
					scoreSheet.getRoundScoreP1() - 
					Utilities.countScore(p1.hand));
			scoreSheet.setRoundScoreP1(0);
		}
		
		PopUps.roundOver(scoreSheet.getRoundScoreP1(),
				scoreSheet.getRoundScoreP2());
				
		//Check if game is over (if a play has reached 500 points)
		if(scoreSheet.getTotalScoreP1() >= FINAL_SCORE || 
				scoreSheet.getTotalScoreP2() >= FINAL_SCORE)
		{
			lEndGame = true;
		}		
		
		return lEndGame;		
	}	
	
	/**
	 * Handles a turn in Rummy.
	 * 
	 * @return
	 */
	public boolean turn( )
	{		
		String lP2Action;
		boolean lEndRound = false;
		resetBuffer = new CardPile("buffer");
		
		// Tools for printing table
		GraphicController lGraphicController = new GraphicController(
				p1, p1, gameView, this, 1);		

		Rectangle lBounds  = gameView.frame.getBounds();//= aInGV.getBounds();
		gameView.closeWindow();
		gameView = new GameViewer(lBounds);
		
		setDiscardVal(-1);
		setScoreRunVal(-1);
		setScoreSetVal(-1);
		setScoreAddSetVal(-1);
		setAddRunLowVal(-1);
		setAddRunHighVal(-1);
		
		backupScorePile = Utilities.copyScorePile(scorePile);
		
		if(P1Next)
		{
			// pickup card(s)
			lP2Action = "view";			
			packet = new DataPacket();
			packet.updatePacket(deck, dropPile, scorePile, p1.hand, p2.hand, 
					P1Next, turn, round, lP2Action, p1.socket, false, 
					getDiscardVal(), null, scoreSheet, lEndRound, false);	
			p2.mClientSender.sendMessageToClient(packet);
			
			state = 1;
			lGraphicController.printGraphicTable(dropPile, getP1Next(), 
					scorePile, scoreSheet, 1);
			
			while(!getChangeState())
			{
				//TODO fix timing issue?
				System.out.println("");
			}
			
			setChangeState(false);						
	
			// Update P2 + Discard
			packet.updatePacket(deck, dropPile, scorePile, p1.hand, p2.hand, 
					P1Next, turn, round, lP2Action, p1.socket, false,
					getDiscardVal(), null, scoreSheet,lEndRound, false);
			
			p2.mClientSender.sendMessageToClient(packet);			
			state = 2;
			refresh = true;
			
			while(!getChangeState())
			{
				//TODO fix timing issue?
				if(refresh)
				{
					lBounds  = gameView.frame.getBounds();
					gameView.closeWindow();
					gameView = new GameViewer(lBounds);		
					lGraphicController.printGraphicTable(dropPile, getP1Next(), 
							scorePile, scoreSheet, 2);
					refresh = false;
				}
				System.out.println("");
			}
			setChangeState(false);	
		}
		else
		{
			//Player 2's turn
			handleP2Turn(lGraphicController, scorePile);
		}	
		
		// Player discarded, check if round is over
		if(p1.hand.size() == 0)
		{
			//Handle P1 win round
			packet.updatePacket(deck, dropPile, scorePile, p1.hand, p2.hand, 
					P1Next, turn, round, "", p1.socket, false,
					getDiscardVal(), null, scoreSheet,lEndRound, false);
			
			p2.mClientSender.sendMessageToClient(packet);
			lEndRound = true;
		}
		else if(p2.hand.size() == 0)
		{
			//Handle P1 win round
			packet.updatePacket(deck, dropPile, scorePile, p1.hand, p2.hand, 
					P1Next, turn, round, "endRound", p1.socket, false,
					getDiscardVal(), null, scoreSheet,lEndRound, false);
			
			p2.mClientSender.sendMessageToClient(packet);
			lEndRound = true;
		}
						
		// Finish turn:
		p1.setPickUpFlag(false);
		p2.setPickUpFlag(false);
		setP1Next(!getP1Next());
		
		turn++;
		
		return lEndRound;
		//return false;
	}
	
	/**
	 * Handles the mouse clicking the table.
	 * 
	 * @param aInCard car that is clicked.
	 * @param aInPileType the type of pile that is clicked.
	 */
	public void handleMouse(PlayingCard aInCard, String aInPileType)
	{
		if(state == 1 && "deck".equals(aInPileType))
		{
			handlePickup(aInPileType);
		}
		else if(state == 1 && "drop".equals(aInPileType))
		{
			handlePickupFromDropPile(aInCard);
		}
		else if(state == 2 && "hand".equals(aInPileType))
		{
			if(getScoreAddSetVal() == 1) // Drop single card
			{
				handleAddSet(aInCard);
			}
			else if(getAddRunLowVal() == 1)
			{
				// true=low, false=high
				handleAddRunLow(aInCard);
			}
			else if(getAddRunHighVal() == 1)
			{
				handleAddRunHigh(aInCard);
			}
			else if (getScoreRunVal() == 1) // Player wants to discard a run.
			{
				handleScoreRun(aInCard);
			}
			else if(getScoreSetVal() == 1)
			{
				handleScoreSet(aInCard);
			}			
			else // Discard card
			{
				handleDiscard(aInCard);
			}
		}
	}
	
	/**
	 * Handles pick up.
	 * 
	 * @param aInPileType
	 */
	public void handlePickup(String aInPileType)
	{
		if("deck".equals(aInPileType))
		{
			PlayerAction.pickupDeck(this);
		}

		setChangeState(true);
	}
	
	/**
	 * Handles picking up from a drop pile.
	 * 
	 * @param aInCard card to be picked up.
	 */
	public void handlePickupFromDropPile(PlayingCard aInCard)
	{
		PlayingCard lTmpCard;
		int lIndex = dropPile.findIndex(
				aInCard.getShortSuit() + aInCard.getValue());
		pickupFromDropPile = true;
		dropPileCopy = Utilities.copyCardPile(dropPile);
				
		if(P1Next)
		{
			p1HandCopy = Utilities.copyCardPile(p1.hand);
			p1.setPickUpFlag(true);
			
			for(int i = dropPile.size(); i >= lIndex; i--)
			{
				 lTmpCard = dropPile.getTop();
				 p1.hand.add(lTmpCard);
				// p1.pickUp.add(lTmpCard);
			}
		}
		else
		{
			p2HandCopy = Utilities.copyCardPile(p2.hand);
			p2.setPickUpFlag(true);
			
			for(int i = dropPile.size(); i >= lIndex; i--)
			{
				 lTmpCard = dropPile.getTop();
				 p2.hand.add(lTmpCard);
			}
		}
		
		setChangeState(true);
	}
	
	/**
	 * Handles player discarding a card.
	 * 
	 * @param aInCard Card to be discarded.
	 */
	public void handleDiscard(PlayingCard aInCard)
	{

		if (getDiscardVal() == 1 || !getP1Next()) 
		{
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();
			if (P1Next) 
			{
				
				int lIndex = p1.hand.findIndex(lSearchString);
				CardActions.takeCard(lIndex, dropPile, p1.hand);
			} 
			else 
			{
				// Already verified
				int lIndex = p2.hand.findIndex(lSearchString);
				CardActions.takeCard(lIndex, dropPile, p2.hand);
			}
			setChangeState(true);
		}
		
	}
	
	/**
	 * Handles scoring a run (of 3 cards).
	 * 
	 * @param aInCard Card to be added to run.
	 */
	public void handleScoreRun(PlayingCard aInCard)
	{
		if(scorePileBuffer.size() < 3)
		{
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();
			if (P1Next) // Make sure P1
			{
				int lIndex = p1.hand.findIndex(lSearchString);
				CardActions.takeCard(lIndex, scorePileBuffer, p1.hand);
				
				if(p1.hand.size() < 1)
				{
					restoreScorePileBuffer("Sorry, hand is to small.");
				}
			} 		
			
			// Check after card is 'dealt' with
			if(scorePileBuffer.size() == 3)
			{
				//Verify and handle scoring:
				if(Utilities.isRun(scorePileBuffer))
				{
					resetBuffer.addCopy(scorePileBuffer);
					scoreSheet.setRoundScoreP1(scoreSheet.getRoundScoreP1() +
							Utilities.countScore(scorePileBuffer));
					addPileToScorePiles("run", scorePileBuffer);
					scorePileBuffer = null;
					refresh = true;
				}
				else
				{
					restoreScorePileBuffer("Sorry, this is not a run.");
				}
			}
		}		
	}
	
	/**
	 * Handles scoring a set (of 3 cards with the same value).
	 * 
	 * @param aInCard Card to be added to the set.
	 */
	public void handleScoreSet(PlayingCard aInCard)
	{
		if(scorePileBuffer.size() < 3)
		{
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();
			if (P1Next) // Make sure P1
			{
				int lIndex = p1.hand.findIndex(lSearchString);
				CardActions.takeCard(lIndex, scorePileBuffer, p1.hand);
				
				if(p1.hand.size() < 1)
				{
					restoreScorePileBuffer("Sorry, hand is to small.");
				}
			} 		
			
			// Check after card is 'dealt' with
			if(scorePileBuffer.size() == 3)
			{
				//Verify and handle scoring:
				if(Utilities.isTriplet(scorePileBuffer))
				{
					resetBuffer.addCopy(scorePileBuffer);
					scoreSheet.setRoundScoreP1(scoreSheet.getRoundScoreP1() +
							Utilities.countScore(scorePileBuffer));
					addPileToScorePiles("set", scorePileBuffer);
					scorePileBuffer = null;
					refresh = true;
				}
				else
				{
					restoreScorePileBuffer("Sorry, this is not a set");
				}
			}
		}
	}
	
	/**
	 * Handles adding a card to a set.
	 * 
	 * @param aInCard Card to be added to the set.
	 */
	public void handleAddSet(PlayingCard aInCard)
	{
		int i;
		int lPileIndex = 0;
		CardPile lPile;
		PlayingCard lCurrent;
		boolean lSuccess = false;
		
		Logger.logDebug("YES", this.getClass().toString());
		

		if(p1.hand.size() < 2 && P1Next)
		{
			failedAddScoreCard("Hand is too small.");
		}
		else
		{			
			// If P1 verify action, for both players find correct pile.
			for(i = 0; i < scorePile.size(); i++)
			{				
				// Only need to check value of one card in a scored pile
				lCurrent = null;
				lPile = scorePile.getCardPile(i);
				lCurrent = lPile.getHead().getNext();
				
				if(lCurrent.getValue() == aInCard.getValue() &&
						"set".equals(lPile.getPileType()))
				{
					lSuccess = true;
					lPileIndex = i;
				}		
			}		

			if (lSuccess) 
			{
				String lSearchString = aInCard.getShortSuit() + 
						aInCard.getValue();
				
				resetBuffer.add(aInCard.getSuit(), aInCard.getValue());
				if(P1Next)
				{
					scoreSheet.setRoundScoreP1(scoreSheet.getRoundScoreP1() + 
							Utilities.getValue(aInCard));
					int lIndex = p1.hand.findIndex(lSearchString);
					CardActions.takeCard(lIndex, scorePile.getCardPile(lPileIndex),
							p1.hand);
					
				}
				else
				{
					scorePile.getCardPile(lPileIndex).add(aInCard);
				}
				
				refresh = true;
			} 
			else
			{
				failedAddScoreCard("Sorry, could not find appropriate set pile.");
			}
		}
	}
	
	/**
	 * Handles adding card to bottom of  run.
	 * 
	 * @param aInCard Card to be added to run.
	 */
	public void handleAddRunLow(PlayingCard aInCard)
	{
		int i,j;
		int lTmpLow;
		int lPileIndex = 0;
		CardPile lPile;
		PlayingCard lCurrent;
		boolean lSuccess = false;
		
		if(p1.hand.size() < 2 && P1Next)
		{
			failedAddScoreCard("Hand is too small.");
		}
		else
		{						
			//P1 needs verification, P2 still needs to find pile again.
			for(i = 0; i < scorePile.size(); i++)
			{				
				// Only need to check value of one card in a scored pile
				lCurrent = null;
				lPile = scorePile.getCardPile(i);
				lCurrent = lPile.getHead().getNext();

				// If same suit, find lowest card in that suit.
				if(lCurrent.getShortSuit() != null && 
						lCurrent.getSuit().equals(aInCard.getSuit())  &&
						"run".equals(lPile.getPileType()))
				{									
					lTmpLow = lPile.get(1).getValue();
					
					for(j = 1; j <= lPile.size(); j++)
					{
						if(lPile.get(j).getValue() < lTmpLow)
						{
							lTmpLow = lPile.get(j).getValue();
						}
					}
					
					if(aInCard.getValue() == lTmpLow - 1)
					{
						lSuccess = true;
						lPileIndex = i;
					}
				}		
			}
						
			if(lSuccess) 
			{				
				String lSearchString = aInCard.getShortSuit() + 
						aInCard.getValue();
				resetBuffer.add(aInCard.getSuit(), aInCard.getValue());
				
				if(P1Next)
				{
					scoreSheet.setRoundScoreP1(scoreSheet.getRoundScoreP1() + 
							Utilities.getValue(aInCard));
					int lIndex = p1.hand.findIndex(lSearchString);
					CardActions.takeCard(
							lIndex, scorePile.getCardPile(lPileIndex),
							p1.hand);					
				}
				else
				{
					scorePile.getCardPile(lPileIndex).add(aInCard);
				}
				
				refresh = true;
			} 
			else
			{
				failedAddScoreCard("Sorry, could not find appropriate run pile.");
			}
		}
	}
	
	/**
	 * Handles adding card to top of run.
	 * 
	 * @param aInCard Card to be added to run.
	 */
	public void handleAddRunHigh(PlayingCard aInCard)
	{
		int i,j;
		int lTmpHigh;
		int lPileIndex = 0;
		CardPile lPile;
		PlayingCard lCurrent;
		boolean lSuccess = false;
		
		if(p1.hand.size() < 2 && P1Next)
		{
			failedAddScoreCard("Hand is too small.");
		}
		else
		{						
			//P1 needs verification, P2 still needs to find pile again.
			for(i = 0; i < scorePile.size(); i++)
			{				
				// Only need to check value of one card in a scored pile
				lCurrent = null;
				lPile = scorePile.getCardPile(i);
				lCurrent = lPile.getHead().getNext();

				// If same suit, find lowest card in that suit.
				if(lCurrent.getShortSuit() != null && 
						lCurrent.getSuit().equals(aInCard.getSuit())  &&
						"run".equals(lPile.getPileType()))
				{								
					lTmpHigh = lPile.get(1).getValue();
					
					for(j = 1; j <= lPile.size(); j++)
					{
						if(lPile.get(j).getValue() > lTmpHigh)
						{
							lTmpHigh = lPile.get(j).getValue();
						}
					}
					
					if(aInCard.getValue() == lTmpHigh + 1)
					{
						lSuccess = true;
						lPileIndex = i;
					}
				}		
			}
						
			if(lSuccess) 
			{			
				String lSearchString = aInCard.getShortSuit() + 
						aInCard.getValue();
				
				resetBuffer.add(aInCard.getSuit(), aInCard.getValue());
				if(P1Next)
				{
					scoreSheet.setRoundScoreP1(scoreSheet.getRoundScoreP1() + 
							Utilities.getValue(aInCard));
					int lIndex = p1.hand.findIndex(lSearchString);
					CardActions.takeCard(lIndex, scorePile.getCardPile(lPileIndex),
							p1.hand);					
				}
				else
				{
					scorePile.getCardPile(lPileIndex).add(aInCard);
				}
				
				refresh = true;
			} 
			else
			{
				failedAddScoreCard("Sorry, could not find appropriate run pile.");
			}
		}
	}
	
	/**
	 * Adds a pile of cards to all the score piles.
	 * 
	 * @param aInType Type of pile to be added.
	 * @param aInPile Card pile to be aded.
	 */
	public void addPileToScorePiles(String aInType, CardPile aInPile)
	{
		aInPile.pileType = aInType;
		scorePile.addPile(aInPile);
	}
	
	/**
	 * Handles restoring after a card could not be played
	 * 
	 */
	public void failedAddScoreCard()
	{
		// Turn off all other buttons
		resetAllButtonVals();
	}
	
	/**
	 * Handles restoring after a card could not be played
	 * 
	 */
	public void failedAddScoreCard(String aInMsg)
	{
		// Turn off all other buttons
		PopUps.warningWindow(aInMsg);
		resetAllButtonVals();
	}
	
	/**
	 * Restores buffer to score pile.
	 * 
	 * @param aInWarning
	 */
	public void restoreScorePileBuffer(String aInWarning)
	{
		// Return cards back to hand
		int tmp = scorePileBuffer.size();
		for(int i = 1; i <= tmp; i++)
		{
			if (P1Next) 
			{
				CardActions.takeCard(i, p1.hand, scorePileBuffer);
			} 
			else 
			{
				CardActions.takeCard(i, p2.hand, scorePileBuffer);
			}
		}
		
		if(aInWarning != null || "".equals(aInWarning))
		{
			PopUps.warningWindow(aInWarning);
		}
		resetAllButtonVals();
	}
	
	/**
	 * Drops a single card.
	 * 
	 * @param aInCard card to be dropped.
	 */
	public void dropSingleScoreCard(PlayingCard aInCard)
	{
		String lSearchString = aInCard.getShortSuit() + aInCard.getValue();
		
		if(P1Next)
		{
			int lIndex = p1.hand.findIndex(lSearchString);	
			p1.hand.remove(lIndex);
		}
		else
		{
			int lIndex = p2.hand.findIndex(lSearchString);	
			p2.hand.remove(lIndex);
		}
		
		refresh = true;
	}
	
	
	/**
	 * Quick method to print text based version of table.
	 * 
	 * @param aInP1  Player 1.
	 * @param aInP2  Plater 2.
	 * @param aInDropPile  pile of dropped cards
	 * @param aInP1Turn  holds whether or not its Player 1's turn.
	 * @param aInScorePile  Piles of cards that where played for points.
	 */
	public static void printTable(Player aInP1, Player aInP2, 
			CardPile aInDropPile, boolean aInP1Turn, ScorePile aInScorePile)
	{
		int i;
		System.out.println("ROUND1: " + round + "\tTURN: " + turn);
		
		if(getP1Next()) //Show Player1's perspective
		{
			System.out.println("Player2's Cards (" + aInP2.hand.size() +
					"):    ");
			for(i = aInP2.hand.size(); i > 0; i--)
			{
				System.out.print("[x|x] ");
			}
			
			System.out.print("\n\n\n\n\n\n\n\n\n");
			System.out.print("[DECK]  " + aInDropPile.simpleString());
			System.out.print("\n\n\n");		
			
			if(aInScorePile.getScorePiles().size() > 0)
			{
				aInScorePile.printScoreTable();
			}
			
			System.out.println("\n\n\n\n\n");
			
			System.out.print("Your Cards (" + aInP2.hand.size() + "):  \n " + 
					aInP1.hand.simpleString());
			System.out.print("\n\n");

		}
		else //Reverse perspective (its P2's turn...)
		{
			System.out.println("Player1's Cards (" + aInP1.hand.size() +
					"):    ");
			for(i = aInP1.hand.size(); i > 0; i--)
			{
				System.out.print("[x|x] ");
			}
			
			System.out.print("\n\n\n\n\n\n\n\n\n");
			System.out.print("[DECK]  " + aInDropPile.simpleString());
			System.out.print("\n\n\n\n\n\n\n\n\n");
			
			System.out.print("Your Cards (" + aInP2.hand.size() + "):  \n " + 
					aInP2.hand.simpleString());
			System.out.print("\n\n");
		}
	}
	
	/**
	 * Handles Player 2's turn (from Player 1s perspective).
	 * 
	 * @param aInGraphicController Controls the graphics
	 * @param aInScorePile The score pile.
	 */
	public void handleP2Turn(GraphicController aInGraphicController, 
			ScorePile aInScorePile)
	{
		String lP2Action = "";
		
		//P2 picks up
		lP2Action = "pickup";
		resetBuffer = new CardPile("buffer");
		backupScorePile = Utilities.copyScorePile(scorePile);
		
		aInGraphicController.printGraphicTable(dropPile, getP1Next(), 
				scorePile, scoreSheet, 0);
		
		packet = new DataPacket();
		packet.updatePacket(deck, dropPile, scorePile, p1.hand, p2.hand, 
				P1Next, turn, round, lP2Action, p1.socket, false, 
				getDiscardVal(), null, scoreSheet, false, false);
		
		p2.mClientSender.sendMessageToClient(packet);		
		
		packet = p2.mClientSender.clientListen();
		
		if("pickupDeck".equals(packet.response))
		{
			p2.hand.add(deck.getTop());
			packet.updatePacket(deck, dropPile, scorePile, p1.hand,
					p2.hand, P1Next, turn, round, lP2Action, p1.socket,
					true, getDiscardVal(), null, scoreSheet, false, false);

			p2.mClientSender.sendMessageToClient(packet);
		}
		else if("pickupDrop".equals(packet.response))
		{
			handlePickupFromDropPile(packet.responseCard);
			packet.updatePacket(deck, dropPile, scorePile, p1.hand,
					p2.hand, P1Next, turn, round, lP2Action, p1.socket,
					true, getDiscardVal(), null, scoreSheet, false, false);

			p2.mClientSender.sendMessageToClient(packet);
		}
		
		//Loop through different plays, exit of droping.
		while(!"drop".equals(packet.response))
		{
			packet = p2.mClientSender.clientListen();
			scoreSheet.setRoundScoreP2(packet.scoreSheet.getRoundScoreP2());
			
				
			if("drop".equals(packet.response))
			{
				handleDiscard(packet.responseCard);
				state = 1;
			}
			else if("scoreRun".equals(packet.response))
			{
				p2.hand = packet.p2Hand;
				resetBuffer.addCopy(packet.responsePile);
				addPileToScorePiles("run", packet.responsePile);
			}
			else if("scoreSet".equals(packet.response))
			{
				p2.hand = packet.p2Hand;
				resetBuffer.addCopy(packet.responsePile);
				addPileToScorePiles("set", packet.responsePile);
			}
			else if("addSet".equals(packet.response))
			{
				p2.hand = packet.p2Hand;
				handleAddSet(packet.responseCard);
			}
			else if("addRunLow".equals(packet.response))
			{
				p2.hand = packet.p2Hand;
				handleAddRunLow(packet.responseCard);
			}
			else if("addRunHigh".equals(packet.response))
			{
				p2.hand = packet.p2Hand;
				handleAddRunHigh(packet.responseCard);
			}
			else if("reset".equals(packet.response))
			{
				handleScoreReset();
				
				packet.updatePacket(deck, dropPile, scorePile, p1.hand,
						p2.hand, P1Next, turn, round, lP2Action, p1.socket,
						true, getDiscardVal(), null, scoreSheet, false, false);
				p2.mClientSender.sendMessageToClient(packet);
			}
			else if("returnScorePile".equals(packet.response))
			{
				handleReturnDropPile();
				
				packet.updatePacket(deck, dropPile, scorePile, p1.hand,
						p2.hand, P1Next, turn, round, lP2Action, p1.socket,
						true, getDiscardVal(), null, scoreSheet, false, false);
				p2.mClientSender.sendMessageToClient(packet);
			}
		}
		setChangeState(false);		
	}
	
	/**
	 * Mutator for next rounds first turn.
	 * 
	 * @param aInP1NextTurn.
	 */
	public static void setP1Next(boolean aInP1NextTurn)
	{
		P1Next = aInP1NextTurn;
	}
	
	/**
	 * Accessor for next rounds first turn.
	 * 
	 * @return if player 1 is next.
	 */
	public static boolean getP1Next()
	{
		return P1Next;
	}
	
	/**
	 * Mutator for next rounds first turn.
	 * 
	 * @param aInP1NextTurn.
	 */
	public static void setP1NextRound(boolean aInP1NextRound)
	{
		P1NextRound = aInP1NextRound;
	}
	
	/**
	 * Accessor for next rounds first turn.
	 * 
	 * @return if player one is first next round.
	 */
	public static boolean getP1NextRound()
	{
		return P1NextRound;
	}
	
	/**
	 * Mutator for turn.
	 * 
	 * @param aInTurn  new turn.
	 */
	public static void setTurn(int aInTurn)
	{
		turn = aInTurn;
	}
	
	/**
	 * Accessor for turn.
	 * 
	 * @return the turn
	 */
	public int getTurn()
	{
		return turn;
	}
	
	/**
	 * Accessor for round.
	 * 
	 * @return returns round of game.
	 */
	public int getRound()
	{
		return round;
	}
	
	/**
	 * Mutator for round.
	 * 
	 * @param round number
	 */
	public void setRound(int aInRound)
	{
		round = aInRound;
	}	
	
	/**
	 * Mutator for changeState.
	 * 
	 * @param newState if state has changed
	 */
	public void setChangeState(boolean newState)
	{
		changeState = newState;
	}
	
	public boolean getChangeState()
	{
		return changeState;
	}
	
	public void setDiscardVal(int aInValue)
	{
		discardVal = aInValue;
	}
	
	public int getDiscardVal()
	{
		return discardVal;
	}
	
	public void setScoreOneButton(int aInValue)
	{
		scoreOneButton = aInValue;
	}
	
	public int getScoreOneButton()
	{
		return scoreOneButton;
	}
	
	public void setScoreRunVal(int aInValue)
	{
		scoreRunVal = aInValue;
	}
	
	public int getScoreRunVal()
	{
		return scoreRunVal;
	}
	
	public void setScoreSetVal(int aInValue)
	{
		scoreSetVal = aInValue;
	}
	
	public int getScoreSetVal()
	{
		return scoreSetVal;
	}
	
	public void setScoreAddSetVal(int aInValue)
	{
		scoreAddSetVal = aInValue;
	}
	
	public int getScoreAddSetVal()
	{
		return scoreAddSetVal;
	}
	
	public void setAddRunLowVal(int aInValue)
	{
		addRunLowVal = aInValue;
	}
	
	public int getAddRunLowVal()
	{
		return addRunLowVal;
	}
	
	public void setAddRunHighVal(int aInValue)
	{
		addRunHighVal = aInValue;
	}
	
	public int getAddRunHighVal()
	{
		return addRunHighVal;
	}
	
	/**
	 * Resets all button values to original state.
	 */
	public void resetAllButtonVals()
	{
		//TODO use mutators?
		discardVal = -1;
		scoreRunVal = -1;
		scoreSetVal = -1;
		scoreAddSetVal = -1;
		addRunLowVal = -1;
		addRunHighVal = -1;
		
		//In Case the buffer was used:		
		if(scorePileBuffer != null && scorePileBuffer.size() > 0)
		{
			restoreScorePileBuffer("");
		}
	}
	
	/**
	 * Handles reseting the score.
	 */
	public void handleScoreReset()
	{
		scorePile = Utilities.copyScorePile(backupScorePile);
		
		if(resetBuffer != null && P1Next)
		{
			scoreSheet.setRoundScoreP1(scoreSheet.getRoundScoreP1() - 
					Utilities.countScore(resetBuffer));
			p1.hand.addCopy(resetBuffer);
			refresh = true;
		}
		else if(resetBuffer != null)
		{
			scoreSheet.setRoundScoreP2(scoreSheet.getRoundScoreP2() - 
					Utilities.countScore(resetBuffer));
			
			p2.hand.addCopy(resetBuffer);
		}
		
		resetBuffer = new CardPile("buffer");
	}
	
	/**
	 * Handles returning the dropped pile.
	 */
	public void handleReturnDropPile()
	{
		// Ensure the droppile was picked up
		if(pickupFromDropPile)
		{
			dropPile = Utilities.copyCardPile(dropPileCopy);
			handleScoreReset();
			if(P1Next)
			{
				p1.hand = Utilities.copyCardPile(p1HandCopy);
				handlePickup("deck");
				setChangeState(false);
			}
			else
			{
				p2.hand = Utilities.copyCardPile(p2HandCopy);
				p2.hand.add(deck.getTop());
			}
			
			pickupFromDropPile = false;
		}
	}
		
	/**
	 * Sets up all tests.
	 */
	public void setUpTests()
	{
		int i;
		PlayingCard tmp;
		
		//Test scoring
		scoreSheet.setTotalScoreP2(495);
		scoreSheet.setTotalScoreP1(495);
		
		// Testing, ScorePile:
		CardPile pile1 = new CardPile("run");		
//		pile1.add("Spades", 2);
		pile1.add("Spades", 5);
		pile1.add("Spades", 3);
		pile1.add("Spades", 4);
//		pile1.add("Spades", 1);
		
		CardPile pile2 = new CardPile("run");
		pile2.add("Hearts", 5);
		pile2.add("Hearts", 6);
		pile2.add("Hearts", 7);
		pile2.add("Hearts", 8);
				
		CardPile pile3 = new CardPile("run");
		pile3.add("Clubs", 9);
		pile3.add("Clubs", 10);
		pile3.add("Clubs", 11);
		pile3.add("Clubs", 12);
		
		// Testing, Sets ScorePile
		CardPile pile4 = new CardPile("set");
		pile4.add("Clubs", 6);
		pile4.add("Spades", 6);
		pile4.add("Hearts", 6);

		// Score Piles
		scorePile.addPile(pile1);
		scorePile.addPile(pile2);
		for(i = 0; i < 14; i ++)
		{
			scorePile.addPile(pile3);
		}
		scorePile.addPile(pile4);

		
		// Testing, runs P1:
		p1.hand.add("Spades", 1);
		p1.hand.add("Spades", 2);
		p1.hand.add("Spades", 3);
		p1.hand.add("Spades", 4);
		
		// testing, triplet P1:
		p1.hand.add("Hearts", 1);
		p1.hand.add("Clubs", 1);
		
		// Testing, runs P1:
		
		p2.hand.add("Spades", 1);
		p2.hand.add("Spades", 2);
		p2.hand.add("Spades", 3);
		p2.hand.add("Spades", 4);
		
		// testing, triplet P2:
		p2.hand.add("Hearts", 1);
		p2.hand.add("Clubs", 1);
		
		// Testing, add set P1, P2
		p1.hand.add("Diamonds", 6);
		p2.hand.add("Diamonds", 6);
		
		// Testing, add High Run, P1, P2
		p1.hand.add("Hearts", 9);
		p2.hand.add("Hearts", 9);
		
		//Test End run
		p1.hand = new CardPile("hand");
		p1.hand.add("Spades", 6);
		p1.hand.add("Spades", 1);
		p1.hand.add("Spades", 12);
		
		p2.hand = new CardPile("hand");
		p2.hand.add("Spades", 6);
		
	}	
}
