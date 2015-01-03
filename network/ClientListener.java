package network;

import java.io.*;
import java.net.*;

import resources.Player;

public class ClientListener extends Thread
{
	//This is a thread that actively listens to clients sending message
	private ServerDispatcher mServerDispatcher;
	private BufferedReader mIn;
	private boolean loggedIn = false; // Initially client is not logged in
	private Player player;
	
	public ClientListener(Player aInPlayer, ServerDispatcher aServerDispatcher)
			throws IOException
	{
		player = aInPlayer;
		mServerDispatcher = aServerDispatcher;
		Socket socket = player.mSocket;
		mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
	}
	
	public void run()
	{
		/*
		try
		{
			while(!isInterrupted())
			{
				if(loggedIn)//Only listen for messages to be sent if user is logged in
				{
					String message = mIn.readLine();
					if(message == null)
						break;
					//mServerDispatcher.dispatchMessage(player, message);
				}
				else //This is where the 'login actions' should take place
				{
					String message = mIn.readLine();
					if(message == null)
						break;
					///System.out.println(message);
					loggedIn = true;
				}
			}
		}catch (IOException e)
		{
			//problem reading from socket
		}
		
		// Communication is broken. Interrupt both listener and sender threads
        player.mClientSender.interrupt();
        mServerDispatcher.deleteClient(player);
        */
	}
	
	
}
