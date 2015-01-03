package resources;

import java.io.Serializable;

/*
 * Description:  A class taht represents a playing card.  It contains 
 * information like its suite and its value.
 * 
 * Author: Kevin Grimsley
 * Last Modification Date: 21/10/2012
 * Verstion: 1.1
 */

public class Card implements Serializable
{
	public  String suit;
	public  int value;
	
	
	/*
	 * Default constructor.  Default values are no suite and 0;
	 */
	public Card()
	{
		suit = "";
		value = 0;
	}
	
	/*Constructor to build a card with a given suite and value.
	 * 
	 * @param aInSuite - the suite of the card.
	 * @param aInValue - the cards value.
	 */
	public Card(String aInSuite, int aInValue)
	{
		suit = aInSuite;
		value = aInValue;		
	}
	
	/*
	 * Accessor to get the suit.
	 */
	public String getSuit()
	
	{
		return suit;				
	}
	
	/*
	 * Accessor to get the value.
	 */
	public int getValue()
	{
		return value;
	}
	
	// Accessior to get card value
	//TODO finalize this (temp for testing purposes)
	public  String getCardValue()
	{
		return suit + value;
	}
	
	/*
	 * Prints a simple output of a card (ex [S|1]).
	 * 
	 * TODO: test it.
	 */
	public String printCard()
	{
		String rCard = "";
		
		//Something went wrong, end it now and return null.
		if(suit == null)
		{
			return null;
		}
		else if(suit.equals("Hearts"))
		{
			rCard = rCard + "H";
		}
		else if(suit.equals("Diamonds"))
		{
			rCard = rCard + "D";
		}
		else if(suit.equals("Spades"))
		{
			rCard = rCard + "S";
		}
		else if(suit.equals("Clubs"))
		{
			rCard = rCard + "C";
		}
		else
		{
			return null;
		}
		
		rCard = rCard + "|";
		rCard = rCard + value;
		rCard = rCard + "] ";
		
		return rCard;
	}
}
