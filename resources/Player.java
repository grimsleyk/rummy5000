package resources;



import graphics.PopUps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import network.ClientListener;
import network.ClientSender;

/*
 * Description: A class that holds the information of a player.
 * 
 * Author: Kevin Grimsley
 * Last Modification Date: 22/10/2012
 * Version: 1.0
 */
public class Player 
{
	public CardPile hand;
	public boolean pickUpFlag = false; // Used if player decides to pick from
										 //pick up from pile.
	public CardPile pickUp; //Temp Pile in case player picks up and cant play.
	public int playerVal; // Player 1, player 2, ...
	
	public Socket mSocket = null;
	public ClientListener mClientListener = null;
	public ClientSender mClientSender = null;
	public static Socket socket;
	public static int PORT = 1234;
//	public static String HOSTNAME = "192.168.1.118";
	public static String HOSTNAME;// = "127.0.0.1";
			
	/*
	 * Player constructor
	 */
	public Player()
	{
		this(0);
	}
	
	public Player(int aInVal)
	{
		hand = new CardPile("hand");
		pickUp = new CardPile("temp");
		pickUp = null;
		playerVal = aInVal;
	}
	
	/*
	 * Mutator for pickUpFlag.
	 */
	public void setPickUpFlag(boolean aInPickUpFlag)
	{
		pickUpFlag = aInPickUpFlag;
	}
	
	public void setClientData()
	{
		try
		{
			//Connect to server:
			Logger.logDebug(HOSTNAME, "Player");
			socket = new Socket(HOSTNAME, PORT);
			Logger.logInfo("Player " + playerVal + " connected to server....");
		} 
		catch (IOException e)
		{
			PopUps.warningWindow("Player " + playerVal + 
					" can not connect to server.....");
			System.exit(-1);
		}
	}
	
	public void resetHand()
	{
		hand = null;
		hand = new CardPile("hand");
	}
}
