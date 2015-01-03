package engine;

import graphics.GameViewer;
import graphics.GraphicController;
import graphics.PopUps;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import network.DataPacket;
import network.ServerDispatcher;

import resources.CardPile;
import resources.Logger;
import resources.PlayingCard;
import resources.ScorePile;
import resources.ScoreSheet;
import resources.Utilities;

/*
 * Description: a class that runs the rummy game on the client side (Player2).
 */
public class GameClient {
	public GameViewer gameView;

	public CardPile deck, dropPile;
	public CardPile p1Hand, p2Hand;
	public CardPile scorePileBuffer; // A buffer CardPile while user selects
										// cards to drop for run/trips
	public ScorePile scorePile;
	public ScoreSheet scoreSheet;

	public int turn, round;
	public boolean p1Turn;
	public String action;
	public boolean changeState;
	public boolean successMsg;
	public ScorePile backupScorePile;
	public CardPile resetBuffer = new CardPile("buffer");

	public String response;
	public PlayingCard responseCard;
	public String responsePileType;
	public CardPile responsePile;

	// Buttons
	public int discardVal;
	public int scoreRunVal;
	public int scoreSetVal;
	public int scoreAddSetVal;
	public int addRunLowVal;
	public int addRunHighVal;
	public boolean resetVal, returnDropPile;

	Scanner scan = new Scanner(System.in);
	ObjectInputStream in;
	ObjectOutputStream out;
	Socket socket;
	Socket p1Socket;
	ServerDispatcher servDispatcher;

	public GameClient(Socket aInSocket) {
		setChangeState(false);
		socket = aInSocket;
		servDispatcher = new ServerDispatcher();

		String lWinner = "";

		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			PopUps.warningWindow("Can not connect to server.....");
			System.err.println("Can not connect to server.....");
			System.exit(-1);
		}

		gameView = new GameViewer();
		scoreSheet = new ScoreSheet();

		runP2Game();

		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			PopUps.warningWindow("Can not connect to server.....");
			System.err.println("Can close connection to server.....");
			System.exit(-1);
		}

		if (scoreSheet.getTotalScoreP1() > scoreSheet.getTotalScoreP2()) {
			lWinner = "Player 1";
		} else {
			lWinner = "Player 2";
		}
		PopUps.gameOver(scoreSheet.getTotalScoreP1(),
				scoreSheet.getTotalScoreP2(), lWinner);

	}

	public void runP2Game() {
		DataPacket lPacket = new DataPacket();
		boolean lEndGame = false;

		while (!lEndGame) {
			lPacket = clientListen();
			lEndGame = lPacket.endGame;

			if ("endRound".equals(lPacket.action)) {
				PopUps.roundOver(lPacket.scoreSheet.getRoundScoreP1(),
						lPacket.scoreSheet.getRoundScoreP2());
			} else if (!lPacket.endRound && !lEndGame) {
				if (p1Turn) {
					printP2Table(); // hand begins

					// P1 picks up

					// P1 plays

					// P2 Discards

				} else {
					handleP2Turn(lPacket);
				}
			} else {
				printP2Table();
			}
		}
	}

	public void handleP2Turn(DataPacket aInPacket) {
		action = "pickup";
		scorePile = aInPacket.scorePile;
		resetBuffer = new CardPile("buffer");
		// backupScorePile = Utilities.copyScorePile(scorePile);

		printP2Table(); // hand begins
		// boolean success = false;
		setScoreRunVal(-1);
		setScoreSetVal(-1);
		setScoreAddSetVal(-1);
		setAddRunLowVal(-1);
		setAddRunHighVal(-1);
		resetVal = false;
		returnDropPile = false;

		// P2 to pickup:

		while (!getChangeState()) {
			// TODO fix timing issue?
			System.out.println("");
		}
		aInPacket.setMouseResponse(response, responseCard, responsePileType,
				responsePile);
		aInPacket.updateButtonPacket(discardVal);
		sendMessageToClient(aInPacket);
		clientListen();
		// success = successMsg;

		while (!"drop".equals(response)) {
			action = "drop";
			printP2Table();

			while (!getChangeState()) {
				// TODO fix timing issue?
				System.out.println("");
			}

			// setResponse("drop");
			if (resetVal) {
				setChangeState(false);
				resetVal = false;

				aInPacket.setMouseResponse("reset", responseCard,
						responsePileType, responsePile);
				aInPacket.updatePacket(this);
				sendMessageToClient(aInPacket);
				clientListen();
			} else if (returnDropPile) {
				setChangeState(false);
				returnDropPile = false;

				aInPacket.setMouseResponse("returnScorePile", responseCard,
						responsePileType, responsePile);
				aInPacket.updatePacket(this);
				sendMessageToClient(aInPacket);
				clientListen();
			} else {
				Logger.logDebug("Starting logs: " + response, this.getClass()
						.toString());
				aInPacket.setMouseResponse(this.response, responseCard,
						responsePileType, responsePile);
				aInPacket.updatePacket(this);
				sendMessageToClient(aInPacket);
			}

		}
	}

	public void printP2Table() {
		int state = 0;

		// Tools for printing table
		GraphicController lGraphicController = new GraphicController(p1Hand,
				p2Hand, gameView, this, 2);
		Rectangle lBounds = gameView.frame.getBounds();// = aInGV.getBounds();
		gameView.closeWindow();
		gameView = new GameViewer(lBounds);

		if ("view".equals(action)) {
			setChangeState(true);
			state = 0;
		} else if ("pickup".equals(action)) {
			setChangeState(false);
			state = 1;
		} else if ("drop".equals(action)) {
			setChangeState(false);
			state = 2;
		}

		lGraphicController.PrintPlayer2(p1Hand, p2Hand, dropPile, p1Turn,
				scorePile, scoreSheet, state);

	}

	public DataPacket clientListen() {
		DataPacket packet = new DataPacket();

		try 
		{
			packet = (DataPacket) in.readObject();
			updateGame(packet);
		} 
		catch (IOException e)
		{
			PopUps.warningWindow("connection broken... \nClosing game...");
			System.exit(0);
		} 
		catch (ClassNotFoundException e) 
		{
			PopUps.warningWindow("Class not found...");
		}
		catch (Exception e) 
		{
			PopUps.warningWindow("An unexpected error has occured...");
		}

		return packet;
	}

	public void sendMessageToClient(DataPacket aInGame) {
		try {
			out.writeObject(aInGame);
			out.reset(); // For serialization
			out.flush();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			PopUps.warningWindow("Couldnt send game...");
		}

	}

	public void updateGame(DataPacket aInPacket) {
		deck = aInPacket.deck;
		dropPile = aInPacket.dropPile;
		scorePile = aInPacket.scorePile;
		p1Turn = aInPacket.p1Turn;
		turn = aInPacket.turn;
		round = aInPacket.round;
		p1Hand = aInPacket.p1Hand;
		p2Hand = aInPacket.p2Hand;
		action = aInPacket.action;
		p1Socket = aInPacket.p1Socket;
		successMsg = aInPacket.successMsg;
		discardVal = aInPacket.discardVal;
		scoreSheet = aInPacket.scoreSheet;
	}

	public boolean handleScoreRun(PlayingCard aInCard) {

		if (scorePileBuffer.size() < 3) {
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();

			int lIndex = p2Hand.findIndex(lSearchString);
			CardActions.takeCard(lIndex, scorePileBuffer, p2Hand);

			if (p2Hand.size() < 1) {
				restoreScorePileBuffer("Sorry, hand is to small.");
			}
		}

		// Check after card is 'dealt' with
		if (scorePileBuffer.size() == 3) {
			// Verify and handle scoring:
			if (Utilities.isRun(scorePileBuffer)) {
				resetBuffer.addCopy(scorePileBuffer);
				scoreSheet.setRoundScoreP2(scoreSheet.getRoundScoreP2()
						+ Utilities.countScore(scorePileBuffer));
				responsePile = scorePileBuffer;
				scorePile.addPile(scorePileBuffer);
				scorePileBuffer = null;
				return true;
			} else {
				restoreScorePileBuffer("Sorry, this is not a run.");
			}
		}

		return false;
	}

	public boolean handleScoreSet(PlayingCard aInCard) {
		if (scorePileBuffer.size() < 3) {
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();

			int lIndex = p2Hand.findIndex(lSearchString);
			CardActions.takeCard(lIndex, scorePileBuffer, p2Hand);

			if (p2Hand.size() < 1) {
				restoreScorePileBuffer("Sorry, hand is to small.");
			}
		}

		// Check after card is 'dealt' with
		if (scorePileBuffer.size() == 3) {
			// Verify and handle scoring:
			if (Utilities.isTriplet(scorePileBuffer)) {
				resetBuffer.addCopy(scorePileBuffer);
				scoreSheet.setRoundScoreP2(scoreSheet.getRoundScoreP2()
						+ Utilities.countScore(scorePileBuffer));
				responsePile = scorePileBuffer;
				scorePile.addPile(scorePileBuffer);
				scorePileBuffer = null;
				return true;
			} else {
				restoreScorePileBuffer("Sorry, this is not a set");
			}
		}

		return false;
	}

	public boolean handleAddSet(PlayingCard aInCard) {
		int i;
		int lPileIndex = 0;
		CardPile lPile;
		PlayingCard lCurrent;
		boolean lSuccess = false;

		if (p2Hand.size() < 2) {
			failedAddScoreCard("Hand is too small.");
		} else {
			for (i = 0; i < scorePile.size(); i++) {
				// Only need to check value of one card in a scored pile
				lCurrent = null;
				lPile = scorePile.getCardPile(i);
				lCurrent = lPile.getHead().getNext();

				if (lCurrent.getValue() == aInCard.getValue()
						&& "set".equals(scorePile.getCardPile(i).getPileType())) {
					lSuccess = true;
					lPileIndex = i;
				}
			}

			if (lSuccess) {
				scoreSheet.setRoundScoreP2(scoreSheet.getRoundScoreP2()
						+ Utilities.getValue(aInCard));
				String lSearchString = aInCard.getShortSuit()
						+ aInCard.getValue();
				int lIndex = p2Hand.findIndex(lSearchString);
				CardActions.takeCard(lIndex, scorePile.getCardPile(lPileIndex),
						p2Hand);
				return true;
			} else {
				failedAddScoreCard("Sorry, could not find appropriate set pile.");
			}
		}

		return false;
	}

	public boolean handleAddRunLow(PlayingCard aInCard) {
		int i, j;
		int lTmpLow;
		int lPileIndex = 0;
		PlayingCard lCurrent;
		CardPile lPile;
		boolean lSuccess = false;

		if (p2Hand.size() < 2) {
			failedAddScoreCard("Hand is too small.");
		} else {
			for (i = 0; i < scorePile.size(); i++) {
				// Only need to check value of one card in a scored pile
				lCurrent = null;
				lPile = scorePile.getCardPile(i);
				lCurrent = lPile.getHead().getNext();

				// If same suit, find lowest card in that suit.
				if (lCurrent.getShortSuit() != null
						&& lCurrent.getSuit().equals(aInCard.getSuit())
						&& "run".equals(lPile.getPileType())) {
					lTmpLow = lPile.get(1).getValue();

					for (j = 1; j <= lPile.size(); j++) {
						if (lPile.get(j).getValue() < lTmpLow) {
							lTmpLow = lPile.get(j).getValue();
						}
					}

					if (aInCard.getValue() == lTmpLow - 1) {
						lSuccess = true;
						lPileIndex = i;
					}
				}
			}
		}

		if (lSuccess) {
			scoreSheet.setRoundScoreP2(scoreSheet.getRoundScoreP2()
					+ Utilities.getValue(aInCard));
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();
			int lIndex = p2Hand.findIndex(lSearchString);
			CardActions.takeCard(lIndex, scorePile.getCardPile(lPileIndex),
					p2Hand);
			return true;
		} else {
			failedAddScoreCard("Sorry, could not find appropriate run pile.");
		}

		return false;
	}

	public boolean handleAddRunHigh(PlayingCard aInCard) {
		int i, j;
		int lTmpHigh;
		int lPileIndex = 0;
		PlayingCard lCurrent;
		CardPile lPile;
		boolean lSuccess = false;

		if (p2Hand.size() < 2) {
			failedAddScoreCard("Hand is too small.");
		} else {
			for (i = 0; i < scorePile.size(); i++) {
				// Only need to check value of one card in a scored pile
				lCurrent = null;
				lPile = scorePile.getCardPile(i);
				lCurrent = lPile.getHead().getNext();

				// If same suit, find lowest card in that suit.
				if (lCurrent.getShortSuit() != null
						&& lCurrent.getSuit().equals(aInCard.getSuit())
						&& "run".equals(lPile.getPileType())) {
					lTmpHigh = lPile.get(1).getValue();

					for (j = 1; j <= lPile.size(); j++) {
						if (lPile.get(j).getValue() > lTmpHigh) {
							lTmpHigh = lPile.get(j).getValue();
						}
					}

					if (aInCard.getValue() == lTmpHigh + 1) {
						lSuccess = true;
						lPileIndex = i;
					}
				}
			}
		}

		if (lSuccess) {
			scoreSheet.setRoundScoreP2(scoreSheet.getRoundScoreP2()
					+ Utilities.getValue(aInCard));
			String lSearchString = aInCard.getShortSuit() + aInCard.getValue();
			int lIndex = p2Hand.findIndex(lSearchString);
			CardActions.takeCard(lIndex, scorePile.getCardPile(lPileIndex),
					p2Hand);
			return true;
		} else {
			failedAddScoreCard("Sorry, could not find appropriate run pile.");
		}

		return false;
	}

	public void restoreScorePileBuffer(String aInWarning) {
		// Return cards back to hand
		int tmp = scorePileBuffer.size();

		for (int i = 1; i <= tmp; i++) {
			CardActions.takeCard(i, p2Hand, scorePileBuffer);
		}

		if (aInWarning != null || "".equals(aInWarning)) {
			PopUps.warningWindow(aInWarning);
		}
		setScoreRunVal(-1);
	}

	/*
	 * Handles restoring after a card could not be played
	 */
	public void failedAddScoreCard() {
		// Turn off all other buttons
		resetAllButtonVals();
	}

	/*
	 * Handles restoring after a card could not be played
	 */
	public void failedAddScoreCard(String aInMsg) {
		// Turn off all other buttons
		PopUps.warningWindow(aInMsg);
		resetAllButtonVals();
	}

	public boolean getChangeState() {
		return changeState;
	}

	public void setChangeState(boolean newState) {
		changeState = newState;
	}

	public void setResponse(String aInResponse) {
		response = aInResponse;
	}

	public void handleMouse(PlayingCard aInCard, String aInPileType,
			int aInGameState) {
		Logger.logDebug("pileType: " + aInPileType, "GameClient");

		if (aInGameState == 1 && "deck".equals(aInPileType)) {
			handlePickup(aInPileType);
		} else if (aInGameState == 2 && "hand".equals(aInPileType)) {
			// handleDiscard(aInCard);
		}
	}

	public void handlePickup(String aInPileType) {
		if ("deck".equals(aInPileType)) {
			PlayerAction.pickupDeck(this);
		}

		setChangeState(true);
	}

	public void setDiscardVal(int aInValue) {
		discardVal = aInValue;
	}

	public int getDiscardVal() {
		return discardVal;
	}

	public void setScoreRunVal(int aInValue) {
		scoreRunVal = aInValue;
	}

	public int getScoreRunVal() {
		return scoreRunVal;
	}

	public void setScoreSetVal(int aInValue) {
		scoreSetVal = aInValue;
	}

	public int getScoreSetVal() {
		return scoreSetVal;
	}

	public void setScoreAddSetVal(int aInValue) {
		scoreAddSetVal = aInValue;
	}

	public int getScoreAddSetVal() {
		return scoreAddSetVal;
	}

	public void setAddRunLowVal(int aInValue) {
		addRunLowVal = aInValue;
	}

	public int getAddRunLowVal() {
		return addRunLowVal;
	}

	public void setAddRunHighVal(int aInValue) {
		addRunHighVal = aInValue;
	}

	public int getAddRunHighVal() {
		return addRunHighVal;
	}

	public ScoreSheet getScoreSheet() {
		return scoreSheet;
	}

	public void setScoreSheet(ScoreSheet aInScoreSheet) {
		scoreSheet = aInScoreSheet;
	}

	public void resetAllButtonVals() {
		discardVal = -1;
		scoreRunVal = -1;
		scoreSetVal = -1;
		scoreAddSetVal = -1;
		addRunLowVal = -1;
		addRunHighVal = -1;

		// In Case the buffer was used:
		if (scorePileBuffer != null && scorePileBuffer.size() > 0) {
			restoreScorePileBuffer("");
		}
	}

	public void handleScoreReset() {
		changeState = true;
		resetVal = true;
	}

	public void handleReturnDropPile() {
		changeState = true;
		returnDropPile = true;
	}
}
