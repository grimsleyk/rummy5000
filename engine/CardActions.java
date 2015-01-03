package engine;

import java.util.Random;

import resources.CardPile;
import resources.Logger;
import resources.Player;
import resources.PlayingCard;

/**
 * This class consists of static methods that handle card specific actions.
 *
 */
public class CardActions
{
	/**
	 * Constructor
	 */
	public static Logger logger = new Logger();;
	
	public CardActions()
	{
		
	}
	
	/**
	 * Builds a deck of 52 cards, 4 suits.
	 * 
	 * @param aInPile - a deck that it is modifying.
	 */
	public static CardPile buildDeck(CardPile aInPile)
	{
		int i;
		
		for(i = 1; i <= 13; i++)
		{
			aInPile.add("Hearts", i);
		}
		for(i = 1; i <= 13; i++)
		{
			aInPile.add("Diamonds", i);
		}
		for(i = 1; i <= 13; i++)
		{
			aInPile.add("Spades", i);
		}
		for(i = 1; i <= 13; i++)
		{
			aInPile.add("Clubs", i);
		}
		
		return aInPile;
	}
	
	/**
	 * Deals out a hand of 7 cards to two players.
	 * 
	 * @param aInPlayer1 - player 1.
	 * @param aInPlayer2 - player 2.
	 * @param aInDeck - deck of cards to deal.
	 */
	public static void deal(Player aInPlayer1, Player aInPlayer2, 
			CardPile aInDeck, CardPile aInDropPile)
	{
		PlayingCard lCard = new PlayingCard(null);
		
		// Deal cards to player in correct order.
		for(int i = 0; i < 7; i++)
		{
			lCard = aInDeck.getTop();
			aInPlayer1.hand.add(lCard);
			lCard = aInDeck.getTop();
			aInPlayer2.hand.add(lCard);
		}
		
		//Deal 1 card to 'drop pile':
		lCard = aInDeck.getTop();
		aInDropPile.add(lCard);
	}
	
	/**
	 * Shuffles a pile of cards.
	 * 
	 * @param aInDeck - pile of cards to shuffle.
	 */
	public static CardPile shuffle(CardPile aInDeck)
	{
		CardPile rDeck = new CardPile("deck");
		PlayingCard lCardMove = new PlayingCard(null);
		int i, lIndex;
		

		for(i = aInDeck.size(); i > 0; i--)
		{
			lIndex = getRandomCardIndex(1, i);
	
			lCardMove = aInDeck.get(lIndex);
			rDeck.add(lCardMove.getSuit(), lCardMove.getValue());
			aInDeck.remove(lIndex);
		}
		
		return rDeck;
	}
	
	/**
	 * Random number generator.  Generates a random number in between two
	 * integers.
	 * 
	 * @param aInStart - bottom limit.
	 * @param aInEnd - upper limit.
	 */
	public static int getRandomCardIndex(int aInStart, int aInEnd)
	{
		Random lRandom = new Random();
		
	    long range = (long)aInEnd - (long)aInStart + 1;
	    long fraction = (long)(range * lRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aInStart);  
	    
	    return randomNumber;
	}
	
	/**
	 * Takes a card from a pile, and adds it (to top) of other pile.
	 * 
	 * @param aInIndex - Index of card  to get.
	 * @param aInToPile - Drop pile (pile to insert card in)
	 * @param aInFromPile - Hand (pile to take card from)
	 */
	public static void takeCard(int aInIndex, CardPile aInToPile, 
			CardPile aInFromPile)
	{
		if(aInIndex  < aInFromPile.size()) // Not Top card
		{
			PlayingCard lCopyCard = aInFromPile.get(aInIndex);
			aInFromPile.remove(aInIndex);
			//aInFromPile.setHead(aInFromPile.get(aInIndex));
			lCopyCard.setNext(null);
			aInToPile.add(lCopyCard);			
		}
		else if(aInIndex == aInFromPile.size()) // Top card
		{
			PlayingCard lCopyCard = aInFromPile.getTop();
			aInToPile.add(lCopyCard);			
		}
		else
		{
			logger.log("Could not take and/or drop card");
		}				
	}
	
	/**
	 * Method that sorts a card pile.  First by suite (hearts - spades - clubs -
	 * diamonds) then by ascending value (1 - 13).
	 * 
	 * @param aInPile - card pile to sort.
	 */
	public static CardPile sortPile(CardPile aInPile)
	{
		CardPile lHearts = new CardPile("temp");
		CardPile lSpades = new CardPile("temp");
		CardPile lClubs = new CardPile("temp");
		CardPile lDiamonds = new CardPile("temp");
		
		PlayingCard tmpCard;
		String tmpSuite;
		int tmpVal;
		
		int i, j;
		
		//Organize suites:
		for(i =1 ;i <= aInPile.size(); i++)
		{
			 tmpCard = aInPile.get(i);
			 
			 if(tmpCard.getSuit().equals("Hearts"))
			 {
				 lHearts.add("Hearts", tmpCard.getValue());
			 }
			 else if(tmpCard.getSuit().equals("Spades"))
			 {
				 lSpades.add("Spades", tmpCard.getValue());
			 }
			 else if(tmpCard.getSuit().equals("Clubs"))
			 {
				 lClubs.add("Clubs", tmpCard.getValue());
			 }
			 else if(tmpCard.getSuit().equals("Diamonds"))
			 {
				 lDiamonds.add("Diamonds", tmpCard.getValue());
			 }
		}
		
		// Nuke it for the re-organized version.
		aInPile = new CardPile(aInPile.getPileType()); 
		
		// Put cards back together:		
		//For now bubble sort. TODO: improve efficiency:
		for(i = lHearts.size(); i >= 1; i--)
		{
			for(j = 1; j < i ; j++)
			{
				if(lHearts.get(j).getValue() > lHearts.get(j + 1).getValue())
				{
					tmpVal = lHearts.get(j).getValue();
					lHearts.get(j).setValue(lHearts.get(j + 1).getValue());
					lHearts.get(j + 1).setValue(tmpVal);
				}
			}
		}		
		for(i = lSpades.size(); i >= 1; i--)
		{
			for(j = 1; j < i ; j++)
			{
				if(lSpades.get(j).getValue() > lSpades.get(j + 1).getValue())
				{
					tmpVal = lSpades.get(j).getValue();
					lSpades.get(j).setValue(lSpades.get(j + 1).getValue());
					lSpades.get(j + 1).setValue(tmpVal);
				}
			}
		}		
		for(i = lClubs.size(); i >= 1; i--)
		{
			for(j = 1; j < i; j++)
			{
				if(lClubs.get(j).getValue() > lClubs.get(j + 1).getValue())
				{
					tmpVal = lClubs.get(j).getValue();
					lClubs.get(j).setValue(lClubs.get(j + 1).getValue());
					lClubs.get(j + 1).setValue(tmpVal);
				}
			}
		}		
		for(i = lDiamonds.size(); i >= 1; i--)
		{
			for(j = 1; j < i; j++)
			{
				if(lDiamonds.get(j).getValue() > lDiamonds.get(j + 1).getValue())
				{
					tmpVal = lDiamonds.get(j).getValue();
					lDiamonds.get(j).setValue(lDiamonds.get(j + 1).getValue());
					lDiamonds.get(j + 1).setValue(tmpVal);
				}
			}
		}
		
		// Put  back together:
		for(i = 1; i <= lHearts.size(); i++)
		{
			aInPile.add(lHearts.get(i).getSuit(), lHearts.get(i).getValue());			
		}
		for(i = 1; i <= lSpades.size(); i++)
		{
			aInPile.add(lSpades.get(i).getSuit(), lSpades.get(i).getValue());			
		}
		for(i = 1; i <= lClubs.size(); i++)
		{
			aInPile.add(lClubs.get(i).getSuit(), lClubs.get(i).getValue());			
		}		
		for(i = 1; i <= lDiamonds.size(); i++)
		{
			aInPile.add(lDiamonds.get(i).getSuit(), lDiamonds.get(i).getValue());			
		}
		
		return aInPile;
		
	}
}
