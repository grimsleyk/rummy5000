package resources;

import java.io.Serializable;

/*
 * Description:  A LinkedList data structure that groups cards.  It builds
 *                a pile of them.  Each car points to the next. So the bottom
 *                card refers to the one above it, and so forth to the top. 
 *                Also contains methods to acess/modify it.
 *                
 * Author: Kevin Grimsley
 * Last Modification Date: 21/10/2012
 * Version: 1.0  
 */

public class CardPile implements Serializable
{
	private PlayingCard head;
	private int counter = 0;
	public String pileType;
	
	/*
	 * Constructor for Cardpile, the LinkedList that holds groups of cards.
	 * Pile type must be known.
	 * 
	 * @param aInPileType - what type of pile it is:
	 */
	public CardPile(String aInPileType)
	{
		head = new PlayingCard(null);
		counter = 0;
		pileType = aInPileType;
	}
	
	/*
	 * A method that adds cards into a pile of by suite.
	 * @param aInSuite - suite of card.
	 * @param aInValue - value of card.
	 */
	public void add(String aInSuite, int aInValue)
	{		
		PlayingCard lToAdd = new PlayingCard(aInSuite, aInValue);
		PlayingCard current = head;
		
		while (current.getNext() != null)
		{
			current = current.getNext(); 
		}		

		current.setNext(lToAdd);
		counter++;	
	}
	
	/*
	 * Alternative add function, that adds a node to the linked list.
	 * 
	 * @param aInPlayingCard - the playing card (node) to be added.
	 */
	public void add(PlayingCard aInPCard)
	{		
		PlayingCard current;
		
		if(head != null)
		{
			  current = head;
		}
		else
		{
			head = aInPCard;
			current = head;
		}
		
		
		while (current.getNext() != null)
		{
			current = current.getNext(); 
		}		

		current.setNext(aInPCard);
		counter++;	
	}
	
	/*
	 * Overload  function to add card to group of cards.  
	 * BROKEN, TODO: find way to add objects to pile.
	 * 
	 * @param aInCard = card to add.
	 */
	public void add(Card aInCard)
	{		
		PlayingCard lToAdd = new PlayingCard(aInCard.getSuit(), aInCard.getValue());
		PlayingCard current = head;
		
		while (current.getNext() != null)
		{
			current = current.getNext(); 
		}		

		current.setNext(lToAdd);
		counter++;	
	}
	
	/*
	 * Overload function to add a card to card pile. Adds a card to specific 
	 * location in card pile.	 
	 * BROKEN, TODO: find way to add objects to pile.
	 * 
	 * @param aInCard = card to add.
	 * @param aInIndex = spot to add card to.
	 */
	public void add(Card aInCard, int aInIndex)
	{
		PlayingCard lToAdd = new PlayingCard(aInCard);
		PlayingCard current = head;
		int i;
		
		for(i = 1; i < aInIndex && current.getNext() != null; i++)
		{
			current = current.getNext();
		}
		
		lToAdd.setNext(current.getNext());
		current.setNext(lToAdd);
		counter++;
	}
	
	public void add(CardPile aInCardPile)
	{
		PlayingCard current = aInCardPile.getHead().getNext();

		while (current != null)
		{			
			add(current);
			current = current.getNext(); 
		}
				
		/*
		 * PlayingCard current = head.getNext();
		String output = "";
		while(current != null)
		{
			output += "[" + current.getCardData() + "]";
			current = current.getNext();
		}
		return output;

		 */
	}
	
	/*
	 *  Adds a card pile to a cardpile, but as a copy (so it doesnt point to 
	 *  existing pile memory locations.
	 */
	public void addCopy(CardPile aInCardPile)
	{
		PlayingCard current = aInCardPile.getHead().getNext();
		
		while (current != null)
		{
			
			add(current.getSuit(), current.getValue());
			current = current.getNext(); 
		}
	}
	
	/*
	 * Get the size of the card pile.
	 */
	public int size()
	{
		return counter;
	}
	
	/*
	 * Fetches a specific card from the card pile of cards (linked list)
	 * BROKEN TODO: add Card functionality 
	 * 
	 * @param aInIndex - index of card to fetch.
	 */
	public PlayingCard get(int aInIndex)
	{
		int i;
		
		if(aInIndex <= 0)
		{
			return null;
		}
		
		PlayingCard current = head.getNext();
		
		for(i = 1; i < aInIndex; i++)
		{
			if(current.getNext() == null)
			{
				return null;
			}
			
			current = current.getNext();
		}
		
		return current;		
	}
	
	/*
	 * Removes a card from card pile.
	 * 
	 * @param aInIndex - location of card to remove from pile.
	 */
	public boolean remove(int aInIndex)
	{
		int i;
		
		if(aInIndex < 1 || aInIndex > size())
		{
			return false;
		}
		
		PlayingCard current = head;
		for(i = 1; i < aInIndex; i++)
		{
			if(current.getNext() == null)
			{
				return false;
			}
			current = current.getNext();
		}
		
		current.setNext(current.getNext().getNext());
		counter--;
		
		return true;
	}	
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 
	 * Returns contents of sting.  Good for testing.
	 */
	public String toString()
	{
		PlayingCard current = head.getNext();
		String output = "";
		while(current != null)
		{
			output += "[" + current.getCardData() + "]";
			current = current.getNext();
		}
		return output;
	}
	
	/*
	 * Returns pile 
	 */
	public String simpleString()
	{
		String rPile = "";
		String lTmpSuit = "";
		PlayingCard current = null;
		
		if ( head != null)
		{			
			current = head.getNext();
		}
				
		while(current != null)
		{
			rPile = rPile + "[";
			lTmpSuit = current.getSuit();
			
			//Something went wrong, end it now and return null.
			if(lTmpSuit == null)
			{
				return null;
			}
			else if(lTmpSuit.equals("Hearts"))
			{
				rPile = rPile + "H";
			}
			else if(lTmpSuit.equals("Diamonds"))
			{
				rPile = rPile + "D";
			}
			else if(lTmpSuit.equals("Spades"))
			{
				rPile = rPile + "S";
			}
			else if(lTmpSuit.equals("Clubs"))
			{
				rPile = rPile + "C";
			}
			else
			{
				return null;
			}
			
			rPile = rPile + "|";
			rPile = rPile + current.getValue();
			rPile = rPile + "] ";
			
			current = current.getNext();
		}
		
		return rPile;
	}
	
	/*
	 * Accessor to get head of pile.
	 */
	public PlayingCard getHead()
	{
		return head;
	}
	
	/*
	 * Mutator for head of pile
	 */
	public void setHead( PlayingCard aInHead)
	{
		head = aInHead;
	}
	
	/*
	 * Gets top card in pile, and returns it.  Also removies it from the
	 * pile.
	 */
	public PlayingCard getTop()
	{
		PlayingCard returnCard = new PlayingCard(null);
		
		returnCard = get(counter);
		remove(counter);	
		return returnCard;
	}
	
	/*
	 * Accessor, returns pile type.
	 */
	public String getPileType()
	{
		return pileType;
	}
	
	public int findIndex(String aInCardString)
	{
		String lSuit = "";
		char lSuitChar;// aInCardString.charAt(0);
		String lValueString;// = aInCardString.substring(1);
		String lError = "";
		int lValue = -1;
		int lCounter = 0; //For search and replace purposes, begin at 0:
			
		
		if (aInCardString == null || aInCardString.length() < 2)
		{
			return -1;
		}
		else
		{
			lSuitChar = aInCardString.charAt(0);
			lValueString = aInCardString.substring(1);
		}
		
		if(lSuitChar == 'd' || lSuitChar == 'D')
			lSuit = "Diamonds";
		else if(lSuitChar == 'h' || lSuitChar == 'H')
			lSuit = "Hearts";
		else if(lSuitChar == 'c' || lSuitChar == 'C')
			lSuit = "Clubs";
		else if(lSuitChar == 's' || lSuitChar == 'S')
			lSuit = "Spades";
		else
			return -1;
		
		try
		{
			lValue = Integer.parseInt(lValueString);
		}
		catch(NumberFormatException nfe)
		{
			return -1;
		}
			
		PlayingCard current = head;
		while(current != null)
		{
			if(current.getValue() == lValue)
			{
				if(current.getSuit().equals(lSuit))
				{
					return lCounter;
				}
			}
			
			if(current.getNext() == null)
			{
					return -1;
			}
				
			current = current.getNext();			
			lCounter++;
		}
		
		return -1;
	}

}
