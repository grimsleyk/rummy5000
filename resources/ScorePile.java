package resources;

import java.io.Serializable;
import java.util.*;


/*
 * Description: A class holds and handles all card piles played for scoring..
 * 
 * Author: Kevin Grimsley
 * Last Modification Date: 21/11/2012
 * Version: 1.0
 */

public class ScorePile  implements Serializable
{
	ArrayList<CardPile> scorePiles;// = new ArrayList<CardPile>();
	int size;
	/*
	 * Constructor.
	 */
	public ScorePile()
	{
		scorePiles = new ArrayList<CardPile>();
		size = 0; //How many pilesare in it (runs and sets)
	}
	public ScorePile(ScorePile aInScorePile)
	{
		scorePiles = aInScorePile.getScorePiles();
		size = aInScorePile.size();
	}
	
	/*
	 * Adds a pile to the score table.
	 * 
	 * @param aInPile - card pile to be added.
	 */
	public void addPile(CardPile aInPile)
	{
		scorePiles.add(aInPile);
		size++;
	}
	
	/*
	 * Prints the score table.
	 */
	public void printScoreTable()
	{
		CardPile lTempCardPile = new CardPile("temp");
		
		for(int i = 0; i < scorePiles.size(); i++)
		{
			System.out.print(scorePiles.get(i).simpleString() + "   ");
		}
	}
	
	public String printScoreTableString()
	{
		CardPile lTempCardPile = new CardPile("temp");
		String printOut = "";
		
		for(int i = 0; i < scorePiles.size(); i++)
		{
			printOut += scorePiles.get(i).simpleString() + "   \n";
		}
		
		return printOut;
	}
	
	public boolean scorePileExists(PlayingCard aInCard)
	{
		int i, j;
		int lTmpLow, lTmpHigh;
		PlayingCard lCurrent;
		CardPile lPile;
		boolean lSuccess = false;
		
		for(i = 0; i < size(); i++)
		{				
			// Only need to check value of one card in a scored pile
			lCurrent = null;
			lPile = getCardPile(i);
			lCurrent = lPile.getHead().getNext();
			
			if(lCurrent.getValue() == aInCard.getValue() &&
					"set".equals(lPile.getPileType()))
			{
				lSuccess = true;
			}
			else if(lCurrent.getShortSuit() != null && 
					lCurrent.getSuit().equals(aInCard.getSuit())  &&
					"run".equals(lPile.getPileType()))
			{									
				lTmpLow = lPile.get(1).getValue();
				lTmpHigh = lPile.get(1).getValue();
				
				for(j = 1; j <= lPile.size(); j++)
				{
					if(lPile.get(j).getValue() < lTmpLow)
					{
						lTmpLow = lPile.get(j).getValue();
					}
					
					if(lPile.get(j).getValue() > lTmpHigh)
					{
						lTmpHigh = lPile.get(j).getValue();
					}
				}
				
				if(aInCard.getValue() == lTmpLow - 1 || 
						aInCard.getValue() == lTmpHigh + 1)
				{
					lSuccess = true;
				}
			}
		}
		
		return lSuccess;
	}
	/*
	 * Accessor for Score Piles.
	 */
	public ArrayList<CardPile> getScorePiles()
	{
		return scorePiles;
	}
	
	/*
	 * Gets a pile from an index.
	 */
	public CardPile getCardPile(int aInIndex)
	{
		return scorePiles.get(aInIndex);
	}
	
	/*
	 * Returns the amount of piels in score pile (runs and sets).
	 */
	public int size()
	{
		return size;
	}
}
