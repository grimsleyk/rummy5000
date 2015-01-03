package engine;

import network.GameServer;
import engine.*;
import graphics.GameViewer;
import resources.*;
import test.*;

public class RunRummy 
{
	public static void main(String[] args) throws InterruptedException
	{
		// Run Tests:
		//System.out.println("\nRunning tests...");
		//System.out.println("------------------------------------");
		//TestLinkedList lTest = new TestLinkedList();	
		//TestDeck tDeck = new TestDeck();
		//TestSort tSort = new TestSort();
		//TestScoreTable t = new TestScoreTable();
		//TestRuns t = new TestRuns();
		  
		/*
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
            	TestGUI hwGUI = new TestGUI();
            }
        });
		*/
		
		//System.out.println("\n------------------------------------");
		// End Tests.
		
		// GUI:
		/*
		javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
            	Player p1 = new Player();
        		Player p2 = new Player();
            	GameViewer gameViewer = new GameViewer(p1, p2);
            }
        });
        */
		
		//The Game:
		
		/*
		Player p1 = new Player();
		Player p2 = new Player();
		GameViewer gameViewer = new GameViewer(p1, p2);
		Game lGame = new Game(p1, p2, gameViewer);
		*/
		
		
		StartMenu sMenu = new StartMenu();
		Player player = sMenu.startGame(); 
		
		
		
		//If player1, player runs server:
		if(player.playerVal == 1)
		{
			// Player 1 is server
			//player.HOSTNAME = "127.0.0.1";
			//player.setClientData();
			GameServer gServer = new GameServer();		
		}
		else if(player.playerVal == 2) // Player 2 must connect to player 1
		{
			player.setClientData();
			GameClient gClient = new GameClient(player.socket);
		}
						
	}
}
