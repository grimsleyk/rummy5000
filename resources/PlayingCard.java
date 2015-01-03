package resources;

import java.io.Serializable;


/*
 * Description: A class that acts as a playing card.  It is the node in the 
 *              CardPile linked list data structure.  It contains the suit and
 *              the value of a card.  Also points to card above it in pile.
 *              
 * Author: Kevin Grimsley
 * Last Modification Date: 21/10/2012
 * Version 1.0
 */

public class PlayingCard  implements Serializable
{
	Card card;
	PlayingCard next, previous;
	String suit;
	String shortSuit;
	int value;
	
	/*
	 * Constructors.  Builds a playing card from a suite and a value.
	 * 
	 * @param aInSuite - suite of the card
	 * @param aInValue - the value of the card
	 */
	public PlayingCard(String aInSuit, int aInValue)
	{
		next = null;
		suit = aInSuit;
		value = aInValue;
		
		if("Spades".equals(suit))
			shortSuit = "s";
		else if("Clubs".equals(suit))
			shortSuit = "c";
		else if("Hearts".equals(suit))
			shortSuit = "h";
		else
			shortSuit = "d";
		
	}
	
	/*
	 * Alternative constructor, uses Card object to build.
	 * BROKEN TODO: Allow user to add Card to PlayingCard.
	 * 
	 * @param aInCard - card to become node.
	 */
	public PlayingCard(Card aInCard)
	{
		next = null;
		card = aInCard;
	}
	
	/*
	 * Gets Card information.
	 * BROKEN TODO: add Card functionality
	 */
	public  Card getData()
	{
		return card;
	}
	
	
	/*
	 * Gets suite in value of card as one string(suite-value).
	 */
	public String getCardData()
	{
		return suit + value;
	}
	
	/*
	 * Accessor, returns suit of card.
	 */
	public String getSuit()
	{
		return suit;
	}
	
	/*
	 *  Accessor for short suit (first character,as a string);
	 */
	public String getShortSuit()
	{
		return shortSuit;
	}
	
	/*
	 * Accessor, returns card value.
	 */
	public int getValue()
	{
		return value;
	}
	
	/*
	 * Sets the card variable.
	 * BROKEN TODO: add Card functionality 
	 */
	public void setData(Card aInCard)
	{
		card = aInCard;
	}
	
	/*
	 * Sets value of card
	 * 
	 * @param aInValue = value of the vard to set.
	 */
	public void setValue(int aInValue)
	{
		value = aInValue;
	}
	
	
	/*
	 * Retrieves the next card in pile, which its point to.
	 */
	public PlayingCard getNext()
	{
		return next;
	}
	
	
	/*
	 * Sets the card that it points to, which is the card above it.
	 */
	public void setNext(PlayingCard aInPlayCard)
	{
		next = aInPlayCard;
	}
	
}	