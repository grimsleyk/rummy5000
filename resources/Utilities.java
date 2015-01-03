package resources;

import java.util.Arrays;

/*
 * Description:  A class full of usefull methods, like checks.
 *                
 * Author: Kevin Grimsley
 * Last Modification Date: 21/10/2012
 * Version: 1.0  
 */

public class Utilities 
{
	public Utilities()
	{
		
	}
	
	public static boolean isCardString(String aInCardString)
	{
		String lSuite = "";
		char lSuiteChar = aInCardString.charAt(0);
		String lValueString = aInCardString.substring(1);
		String lError = "";
		int lValue = -1;
		int lCounter = 1;
		
		if(lSuiteChar == 'd' || lSuiteChar == 'D')
			lSuite = "Diamonds";
		else if(lSuiteChar == 'h' || lSuiteChar == 'H')
			lSuite = "Hearts";
		else if(lSuiteChar == 'c' || lSuiteChar == 'C')
			lSuite = "Clubs";
		else if(lSuiteChar == 'S' || lSuiteChar == 'S')
			lSuite = "Spades";
		else
			return false;
		
		try
		{
			lValue = Integer.parseInt(lValueString);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		
		if(lValue > 0 || lValue < 14)
			return true;
		else
			return false;
	}
	
	/*
	 * Checks that 3 cards are three of a kind (their values).  Returns true
	 * if they are.
	 * 
	 * @param aInPile = the 3 cards to check.
	 */
	public static boolean isTriplet(CardPile aInPile)
	{
		if (aInPile.size() != 3)  //Ensure proper pile...
		{
			return false;
		}
		
		if (aInPile.get(1).getValue() == aInPile.get(2).getValue() && 
				aInPile.get(2).getValue() == aInPile.get(3).getValue())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*
	 * Checks to ensure 3 cards are a run (same suite, ascending).  Returns 
	 * true if they are.
	 * 
	 * @parm aInPile = the 3 cards to check.
	 */
	public static boolean isRun(CardPile aInPile)
	{
		if (aInPile.size() != 3)  //Ensure proper pile...
		{
			return false;
		}
		
		// Ensure same suite:
		if (!(aInPile.get(1).getSuit().equals(aInPile.get(2).getSuit()))||
				!(aInPile.get(1).getSuit().equals(aInPile.get(3).getSuit())) ||
				!(aInPile.get(2).getSuit().equals(aInPile.get(3).getSuit())))
		{
			return false;
		}
		
		int[] lValues = {
				aInPile.get(1).getValue(), 
				aInPile.get(2).getValue(),
				aInPile.get(3).getValue()};
		
		Arrays.sort(lValues);
		
		// The difference between any card in a run is 1
		if((lValues[1] - lValues[0]) != 1 && (lValues[2] - lValues[1]) != 1) 
		{
			return false;
		}		
		else
		{
			return true;
		}
			
	}
	
	public static int countScore(CardPile aInCardPile)
	{
		int lScore = 0;
		
		PlayingCard lCurrent = aInCardPile.getHead();
		
		while(lCurrent != null)
		{
			if(lCurrent.getValue() == 1)
			{
				lScore += 15;
			}
			else if(lCurrent.getValue() < 10 && lCurrent.getValue() != 0 && lCurrent.getValue() != 0)
			{
				lScore += 5;
			}
			else if(lCurrent.getValue() >= 10)
			{
				lScore += 10;
			}
			
			lCurrent = lCurrent.getNext();			
		}
		
		return lScore;
	}
	
	public static int getValue(PlayingCard aInCard)
	{	
		int value = 0;

		if(aInCard != null && aInCard.getValue() == 1)
		{
			value += 15;
		}
		else if(aInCard != null && aInCard.getValue() < 10 &&
				aInCard.getValue() != 0 && aInCard.getValue() != 0)
		{
			value += 5;
		}
		else if(aInCard != null && aInCard.getValue() >= 10)
		{
			value += 10;
		}		
		
		return value;
	}
	
	public static ScorePile copyScorePile(ScorePile aInScorePile)
	{
		ScorePile lScorePile = new ScorePile();
		int i;
		
		for(i = 0; i < aInScorePile.size(); i++)
		{
			lScorePile.addPile(copyCardPile(aInScorePile.getCardPile(i)));
		}

		return lScorePile;
	}
	
	public static CardPile copyCardPile(CardPile aInCardPile)
	{
		CardPile lCardPile = new CardPile(aInCardPile.getPileType());
		PlayingCard current = aInCardPile.getHead();
		
		while(current.getNext() != null)
		{
			current = current.getNext();
			lCardPile.add(current.getSuit(), current.getValue());
		}
		
		return lCardPile;
	}
	
	public boolean validateIPAddress(String aInAddress)
	{
		return false;
	}
}
