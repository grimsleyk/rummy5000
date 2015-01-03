package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import resources.CardPile;
import resources.Logger;

import engine.Game;

public class ButtonMouseActions implements MouseListener
{
	Game game;
	int buttonVal;

	public ButtonMouseActions(Game aInGame, int aInButton)
	{
		game = aInGame;
		buttonVal = aInButton;
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(buttonVal == 1)
		{
			//handleScore1();
			handleDiscard();
		}
		else if(buttonVal == 2)
		{
			handleAddSet();
		}
		else if(buttonVal == 3)
		{
			handleAddRunLow();
		}
		else if(buttonVal == 4)
		{
			handleAddRunHigh();
		}
		else if(buttonVal == 5)
		{
			handleDropRun();
		}	
		else if(buttonVal == 6)
		{
			handleDropSet();
		}
		else if(buttonVal == 7)
		{
			handleScoreReset();
		}
		else if(buttonVal == 8)
		{
			handleReturnDropPile();
		}
	}
	
	public void handleDiscard()
	{
		if(game.getDiscardVal() > -1)
		{
			game.setDiscardVal(-1);
		}
		else
		{
			// Turn off all other buttons
			game.resetAllButtonVals();
			game.setDiscardVal(1);
		}
		
	}
	
	public void handleDropRun()
	{		
		if(game.getScoreRunVal() > -1)
		{
			game.setScoreRunVal(-1);
			// Clear score pile buffer
		}
		else
		{
			// Turn off all other buttons
			game.resetAllButtonVals();
			game.setScoreRunVal(1);	
			
			//Initiate scorePileBugger
			game.scorePileBuffer = new CardPile("buffer");
		}
	}
	
	public void handleDropSet()
	{		
		if(game.getScoreSetVal() > -1)
		{
			game.setScoreSetVal(-1);
		}
		else
		{	
			//Turn off all other buttons
			game.resetAllButtonVals();		
			game.setScoreSetVal(1);
			
			//Initiate scorePileBugger
			game.scorePileBuffer = new CardPile("buffer");
		}
	}
	
	public void handleAddSet()
	{
		if(game.getScoreSetVal() > -1)
		{
			game.setScoreAddSetVal(-1);
		}
		else
		{
			// Turn off all other buttons
			game.resetAllButtonVals();
			game.setScoreAddSetVal(1);
			
			//Initiate scorePileBugger
			game.scorePileBuffer = new CardPile("buffer");
		}
	}
	
	public void handleAddRunLow()
	{
		if(game.getAddRunLowVal() > -1)
		{
			game.setAddRunLowVal(-1);
		}
		else
		{
			// Turn off all other buttons
			game.resetAllButtonVals();
			game.setAddRunLowVal(1);
			
			//Initiate scorePileBugger
			game.scorePileBuffer = new CardPile("buffer");
		}
	}
	
	public void handleAddRunHigh()
	{
		if(game.getAddRunHighVal() > -1)
		{
			game.setAddRunHighVal(-1);
		}
		else
		{
			// Turn off all other buttons
			game.resetAllButtonVals();
			game.setAddRunHighVal(1);
			
			//Initiate scorePileBugger
			game.scorePileBuffer = new CardPile("buffer");
		}
	}
	
	public void handleScoreReset()
	{
		game.handleScoreReset();
	}
	
	public void handleReturnDropPile()
	{
		game.handleReturnDropPile();
	}
}
