package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import resources.Logger;
import resources.PlayingCard;
import engine.Game;
import engine.GameClient;
import engine.PlayerAction;

//TODO ony made this class because was running out ot time, combine the2 in future.
public class CardMouseActionsP2 implements MouseListener
{
	PlayingCard card;
	int gameState;
	String pileType;
	GameClient game;
	
	public CardMouseActionsP2(PlayingCard aInCard, int aInGameState, 
			String aInPileType, GameClient aInGame)
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
		
		//Working!!
		/*
		game.response = "";		
		if("deck".equals(pileType))
		{
			game.response = "pickupDeck";
		}
		else if("drop".equals(pileType))
		{
			game.response = "dickupDrop";
		}
		game.responseCard = card;
		game.responsePileType = pileType;
		game.setChangeState(true);
		*/
		
		boolean lGameState = false;
			
		game.response = "";		
		if("deck".equals(pileType))
		{
			game.setResponse("pickupDeck");
			lGameState = true;
		}
		else if("drop".equals(pileType))
		{
			game.response = "pickupDrop";
			game.responseCard = card;
			lGameState = true;
		
		}
		else if("hand".equals(pileType))
		{
			//make sure discard button is clicked.
			if(game.discardVal == 1)
			{
				lGameState = true;
				game.setResponse("drop");
			}
			else if(game.getScoreRunVal() == 1)
			{
				pileType = "run";
				game.setResponse("scoreRun");
				lGameState = game.handleScoreRun(card);
			}
			else if(game.getScoreSetVal() == 1)
			{
				pileType = "set";
				game.setResponse("scoreSet");
				lGameState = game.handleScoreSet(card);
			}
			else if(game.getScoreAddSetVal() == 1)
			{
				game.setResponse("addSet");
				lGameState = game.handleAddSet(card);
			}
			else if(game.getAddRunLowVal() == 1)
			{
				game.setResponse("addRunLow");
				lGameState = game.handleAddRunLow(card);
			}
			else if(game.getAddRunHighVal() == 1)
			{
				game.setResponse("addRunHigh");
				lGameState = game.handleAddRunHigh(card);
			}
			else
			{
				lGameState = false;
			}
			
			Logger.logDebug("Card response: " + game.response, 
					this.getClass().toString());
		}
		
		game.responseCard = card;
		game.responsePileType = pileType;
		game.setChangeState(lGameState);
		
		//game.handleMouse(card, pileType, gameState);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void handlePickup()
	{
		
		if("deck".equals(pileType))
		{
			game.setResponse("pickupDeck");
		}
		
		game.setChangeState(true);
	}
	
	public void handlePickupDiscards()
	{
		Logger.logDebug("Card:  " + card.getCardData(), 
				this.getClass().toString());
	}

}
