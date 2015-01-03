package network;

import java.net.Socket;
import java.util.Vector;

import engine.Game;

import resources.Logger;
import resources.Player;

public class ServerDispatcher extends Thread
{
	// Good if to upgrade to multiple players or server in future
	public Vector mPlayers = new Vector();
	public Vector mDataQueue = new Vector();
	
	public void ServerDispatcher()
	{
		
	}
	
	// Method that adds client to the servers client list:
	public synchronized boolean addClient(Player aInPlayer)
	{
		mPlayers.add(aInPlayer);
		Logger.logInfo("Playyer " + aInPlayer.playerVal + " has entered the " +
				"game.");
		return true;
	}
	
	//Method that deletes client in servers client list:
	public synchronized void deleteClient(Player aInPlayer)
	{
		int clientIndex = mPlayers.indexOf(aInPlayer);
		if(clientIndex != -1)
		{
			mPlayers.removeElementAt(clientIndex);
		}
		System.out.println("Plater " + aInPlayer.playerVal + " has left the " +
				"game");
	}
	
	public int queueSize()
	{
		return mPlayers.size();
	}
	
	public synchronized void dispatchMessage(Player aInPlayer, Game aInGame)
	{
		Socket socket = aInPlayer.socket;
		mDataQueue.add(aInGame);
		
	}
	
	private synchronized Game getNextDataFromQueue() throws
		InterruptedException
	{
		Game lGame;
		
		while(mDataQueue.size() == 0)
		{
			wait();
		}
		
		lGame = (Game) mDataQueue.get(0);
		mDataQueue.remove(0);
		return lGame;
	}
	
	public synchronized void sendMessageToAllClients(Game aInGame)
	{
		int i;
		Game lGame = aInGame;
				
		for(i = 0; i < mPlayers.size(); i++)
		{
			Player lPlayer = (Player) mPlayers.get(i);

				lPlayer.mClientSender.sendMessage(aInGame);
		}
	} //End sendMessageToAllClients()
	
	
	
	public void run()
	{
		try 
		{
			while (true) 
			{
				Game lGame;
				lGame = getNextDataFromQueue();
	            sendMessageToAllClients(lGame);
	        }
		} catch (InterruptedException ie) 
		{
	           // Thread interrupted. Stop its execution
		}
	}
}
