package graphics;

import javax.swing.JOptionPane;

public class PopUps 
{
	
	public PopUps()
	{
		 
	}
	
	public static void gameOver(int aInP1Score, int aInP2Score, 
			String aInWinner)
	{
		JOptionPane.showMessageDialog(null, aInWinner + "Wins!!!\n\n" + 
				"Score:\nPlayer1 = " + aInP1Score + "\nPlayer2 = " + 
				aInP2Score);
		System.exit(1);
	}
	
	public static void roundOver(int aInP1Score, int aInP2Score)
	{
		String lWinner = "";
		String lResult = "";
		
		if(aInP1Score > aInP2Score)
		{
			lWinner = "Player 1";
		}
		else
		{
			lWinner = "Player 2";
		}
		
		lResult = "Round over.  Winner: " + lWinner + "\n\n";
		lResult += "Player1: " + aInP1Score +
				"\nPlayer2: " + aInP2Score;
		
		JOptionPane.showMessageDialog(null, lResult);
	}
	
	public static String player2Start()
	{
		String result = "";
		String lP1Address = "";
		
		lP1Address = JOptionPane.showInputDialog(null, 
	    		  "Please enter Player 1's IP Address: ");
		
		return lP1Address;
	}
	
	public static void exampleWindow()
	{
		 String fullName = " ";
	     String strAge = " ";
	      
	      int age = 0;
	      
	      fullName = JOptionPane.showInputDialog(null, 
	    		  "Enter your full name: ");
	      
	      JOptionPane.showMessageDialog(null, "Your full name is " + fullName);
	      
	      //strAge = JOptionPane.showInputDialog(null, " Enter your age: ");
	      
	      age = Integer.parseInt(strAge);
	      
	      JOptionPane.showConfirmDialog(null, age, "Is this your real age?",
	            JOptionPane.OK_CANCEL_OPTION);
	      
	      JOptionPane.showMessageDialog(null, "Your age is " + age + ".");     
	}
	
	public static void warningWindow(String aInWarning)
	{
		JOptionPane.showMessageDialog(null, aInWarning);
	}
}
