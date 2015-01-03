package network;

import engine.Game;
import graphics.GameViewer;
import graphics.GraphicController;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import resources.*;

/*
 * Description: A class that runs a game of rummy.  Does so by acting as a 
 * 				server.  It is a seperate thread run by Player 1.
 * 
 * Author: Kevin Grimsley
 * Last Modification Date: 22/10/2012
 * Version: 1.0
 */

public class GameServer
{
	//Might be able to not need these, if game class still functional:
	public static int turn;
	public static int round;
	public static boolean P1NextRound;
	public static boolean P1Next;
	
	public static boolean changeState;
	
	public static JFrame frame;
	public static JPanel p1cards;
	
	public GameViewer gameView;
	public Player p1, p2;
	public CardPile deck;
	
	public static final int PORT = 1234;
	ServerDispatcher servDispatcher = new ServerDispatcher();

	
	public GameServer()
	{
		boolean foundOpponent = false;
		// Set server socket to listen:
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(PORT);
			Logger.logInfo("Server started listening...");
		}
		catch (IOException e)
		{
			Logger.log("Port " + PORT + " is busy...");
			System.exit(-1);
		}
		
		servDispatcher.start();
	
		//TODO: Add player one automatically 
		
		// Find opponent (Player 2)
		while(!foundOpponent)
		{
			try
			{
				Socket socket = serverSocket.accept();
				p2 = new Player(2);
				p2.mSocket = socket;
				ClientListener clientListener = new ClientListener
						(p2, servDispatcher);
				ClientSender clientSender = new ClientSender
						(p2, servDispatcher);				
				p2.mClientListener = clientListener;
				p2.mClientSender = clientSender;
				clientListener.start();
				clientSender.start();
				foundOpponent = servDispatcher.addClient(p2);					
			}
			catch(IOException e)
			{
				Logger.log("Died trying to find opponent...");
			}
		}
		
		
		p1 = new Player(1);
		GameViewer gv = new GameViewer();
		Game game = new Game(p1, p2, gv, this);
	
	}
	
}
