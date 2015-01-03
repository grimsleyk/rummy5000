package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import engine.CardActions;
import engine.Game;
import engine.PlayerAction;

import resources.*;


public class CardMouseActions implements MouseListener
{
	PlayingCard card;
	int gameState;
	String pileType;
	Game game;
	
	public CardMouseActions(PlayingCard aInCard, int aInGameState, 
			String aInPileType, Game aInGame)
	{
		card = aInCard;
		gameState = aInGameState;
		pileType = aInPileType;
		game = aInGame;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		/*
		if(gameState == 1 && "deck".equals(pileType))
		{
			handlePickup();
		}
		else if(gameState == 2 && "hand".equals(pileType))
		{
			handleDiscard();
		}*/
		
		game.handleMouse(card, pileType);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void handlePickup()
	{
		if("deck".equals(pileType))
		{
			PlayerAction.pickupDeck(game);
		}
		
		game.setChangeState(true);
	}
	
	public void handleDiscard()
	{
		String lSearchString = card.getShortSuit() + card.getValue();
		int lIndex = game.p1.hand.findIndex(lSearchString);
		
		CardActions.takeCard(lIndex, game.dropPile, game.p1.hand);
		game.setChangeState(true);
	}

}
