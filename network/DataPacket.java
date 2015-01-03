package network;

import java.io.Serializable;
import java.net.Socket;

import resources.Card;
import resources.CardPile;
import resources.PlayingCard;
import resources.ScorePile;
import resources.ScoreSheet;

import engine.Game;
import engine.GameClient;

/*
 * Desciption:  This class holds information passed between player 1 (server) 
 *              and player 2.  The information it (can) holds:
 *              
 *              - deck
 *              - pickup pile
 *              - score piles
 *              - player1 hand
 *              - player2 hand
 */
@SuppressWarnings("serial")
public class DataPacket implements Serializable
{

	public CardPile deck, dropPile;
	public CardPile bufferPile; // Used to hold sets or runs to be scored.
	public CardPile p1Hand, p2Hand;
	public ScorePile scorePile;
	public int turn, round;
	public boolean p1Turn;
	public String action;
	public Socket p1Socket;
	public boolean successMsg;
	
	public String response;
	public PlayingCard responseCard;
	public String responsePileType;
	public CardPile responsePile;
	public ScoreSheet scoreSheet;
	public boolean endRound, endGame;
	
	// Buttons
	public int discardVal;
	
	public DataPacket()
	{

	}
	
	public DataPacket(Card aInCard, CardPile aInPile)
	{
	}
	
	public void updatePacket(Game aInGame)
	{
	}
	
	/*
	 *  Updates packed (to be send).
	 */
	public void updatePacket(CardPile aInDeck, CardPile aInDrop, 
			ScorePile aInScorePile, CardPile aInP1Hand, CardPile aInP2Hand, 
			boolean aInP1Turn,	int aInTurn, int aInRound, String aInAction,
			Socket aInSocket, boolean aInSuccess, int aInDiscardVal, 
			CardPile aInBufferPile, ScoreSheet aInScoreSheet, 
			boolean aInEndRound, boolean aInEndGame)
	{
		deck = aInDeck;
		dropPile = aInDrop;
		scorePile = aInScorePile;
		p1Turn = aInP1Turn;
		turn = aInTurn;
		round = aInRound;
		p1Hand = aInP1Hand;
		p2Hand = aInP2Hand;
		action = aInAction;
		p1Socket = aInSocket;
		successMsg = aInSuccess;
		discardVal = aInDiscardVal;
		bufferPile = aInBufferPile;
		scoreSheet = aInScoreSheet;
		endRound = aInEndRound;
		endGame = aInEndGame;
	}
	
	// Incomplete. finish as needed TODO
	public void updatePacket(GameClient aInClient)
	{
		deck = aInClient.deck;
		dropPile = aInClient.dropPile;
		scorePile = aInClient.scorePile;
		p1Turn = aInClient.p1Turn;
		turn = aInClient.turn;
		round = aInClient.round;
		p1Hand = aInClient.p1Hand;
		p2Hand = aInClient.p2Hand;
		action = aInClient.action;
		scoreSheet = aInClient.scoreSheet;
	}
	
	public void setMouseResponse(String aInResponse, PlayingCard aInCard,
			String aInPileType, CardPile aInResponsePile)
	{
		response = aInResponse;
		responseCard = aInCard;
		responsePileType = aInPileType;
		responsePile = aInResponsePile;
	}
	
	public void updateButtonPacket(int aInDiscardVal)
	{
		discardVal = aInDiscardVal;
	}
	
	public void setBufferPile(CardPile aInBufferPile)
	{
		bufferPile = aInBufferPile;
	}
}
