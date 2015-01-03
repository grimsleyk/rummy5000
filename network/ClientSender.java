package network;

import java.io.*;
import java.net.*;
import java.util.*;

import engine.Game;

import resources.Logger;
import resources.Player;
 
public class ClientSender extends Thread
{
	private Vector mMessageQueue = new Vector();
	 
    private ServerDispatcher mServerDispatcher;
    private Player mPlayer;
    private PrintWriter mOut;
    ObjectOutputStream out;
	ObjectInputStream in;

    // Constructor
    public ClientSender(Player aInPlayer, ServerDispatcher aServerDispatcher) 
    		throws IOException
    {
    	 mPlayer = aInPlayer;
         mServerDispatcher = aServerDispatcher;
         Socket socket = aInPlayer.mSocket;
         out = new ObjectOutputStream(socket.getOutputStream());
		 in = new ObjectInputStream(socket.getInputStream());

    }
    
    // Method that adds message to queue that must be sent
    // Also sends notify interrupt
    public synchronized void sendMessage(Game aInGame)
    {
        mMessageQueue.add(aInGame);
        notify();
    }
    
 
    
    
    // Method that retrieves the next message in the queue
    private synchronized DataPacket getNextDataFromQueue() throws InterruptedException
    {
        while (mMessageQueue.size()==0)
           wait();
        DataPacket mGame  = (DataPacket) mMessageQueue.get(0);
        mMessageQueue.removeElementAt(0);
        return mGame;
    }
    
    // Method that sends message to client to print 
    //public void sendMessageToClient(Game aInGame)
    public void sendMessageToClient(DataPacket aInGame)
    {
       try 
       {
		out.writeObject(aInGame);
		out.reset(); // For serialization
    	out.flush();
       } 
       catch (IOException e) 
       {
		// TODO Auto-generated catch block
    	   Logger.log("Couldnt send game...");
       }
       
    }
    
	public DataPacket clientListen()
	{
		DataPacket packet = new DataPacket();
		
		try
		{
			Logger.logDebug("Listening...", this.getClass().toString());
			packet = (DataPacket) in.readObject();
			Logger.logDebug(packet.response, this.getClass().toString());
		} 
		catch (IOException e)
		{
			Logger.log("connection broken... \nClosing game...");
			System.exit(0);
		} 
		catch (ClassNotFoundException e) 
		{
			Logger.log("Class not found...");
		}
		
		return packet;
	}
    
    public void run()
    {
        try 
        {
           while (!isInterrupted()) 
           {
              DataPacket message = getNextDataFromQueue();
              sendMessageToClient(message);
           }
        } catch (Exception e) 
        {
           // Commuication problem
        }
 
        // Communication is broken. Interrupt both listener and sender threads
        mPlayer.mClientListener.interrupt();
        mServerDispatcher.deleteClient(mPlayer);
       
        
    }
    
    
}
