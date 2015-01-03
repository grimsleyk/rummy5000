package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import resources.*;

/*
 * Description: A class that executed a players turn.
 * 
 * Author: Kevin Grimsley
 * Last Modification Date: 2/11/2012
 * Version: 1.0
 */

public class PlayerAction 
{	
	static Logger logger = new Logger();
	/*
	 * Constructor.
	 */
	public  PlayerAction()
	{
		
	}
	
	/*
	 * Player must pick up from either deck or dropile.  Mandory 'action'.
	 * TODO: final method type wont be boolean
	 */
	public static void pickup(Player aInPlayer, CardPile aInDeck, 
			CardPile aInCardDropPile)
	{
		String lUserInString = "";
		boolean lInputGood = false;
		int lUserInInt = 0;
		
		System.out.print("Your turn, please pick from deck(1)" +
				" or pile(2): ");
		
		while(!lInputGood)
		{
			lUserInInt = getUserIntKey();
			
			if(lUserInInt == 1)
			{
				//Player picks from deck.
				aInPlayer.hand.add(aInDeck.getTop());
				
				lInputGood = true;
			}
			else if(lUserInInt == 2 )
			{
				lInputGood = pickupPile(aInCardDropPile, aInPlayer);
			}
			else
			{
				System.out.print("\nNot valid option: ");			
			}
		}
	}
	
	/*
	 * Method that handles user picking up pile
	 */
	
	public static boolean pickupPile(CardPile aInCardDropPile, Player aInPlayer)
	{

		//Player picks from drop pile
		int lCardIndex = -1;
		System.out.println("Pick the card you like:");
		
		while(lCardIndex == -1 )
		{
			lCardIndex = getCardUserKey(aInCardDropPile);
			
			if(lCardIndex == -1)
				System.out.print("\nNot valid, choose again:");
			else
			{
				
				aInPlayer.setPickUpFlag(true);						
				PlayingCard tmpCard;
				//TODO: create a tmp hand in case user cant play:
				
				for(int i = aInCardDropPile.size(); 
						i >= lCardIndex; i--)
				{
					 tmpCard = aInCardDropPile.getTop();
					 aInPlayer.hand.add(tmpCard);
					 aInPlayer.pickUp.add(tmpCard);
				}
				
			}
				
		}
		
		return true;
	}
	
	/*
	 * gets a integer from user via keyboard.
	 */
	public static int getUserIntKey()
	{
		String lUserInString = "";
		boolean lInputBad = true;
		int lUserInInt = 0;
		
		BufferedReader lBr = new BufferedReader(new 
				InputStreamReader(System.in));
		
		while(lInputBad)
		{
			try
			{
				lUserInString = lBr.readLine();
			}
			catch(Exception e)
			{
				System.err.println("Well this is unfortunate...");
			}
			
			try
			{
				lUserInInt = Integer.parseInt(lUserInString);
				lInputBad = false;
			}
			catch(NumberFormatException nfe)
			{
				System.out.println("Sorry, that response blows. Do you not " +
						"what an integer is? Try again");
			}			
		}
		
		return lUserInInt;
	}
	
	/*
	 * Gets a users choice of a card from a pile.  Ensures card exists
	 * 
	 * @param aInPile - the pile to search in.
	 * @return - index of the card that was found.
	 */
	public static int getCardUserKey(CardPile aInPile)
	{
		String lUserInString = "";
		boolean lInputBad = true;
		boolean lCardInGood = false;
		int lIndex = -1;

		BufferedReader lBr = new BufferedReader(new 
				InputStreamReader(System.in));
		
		while(!lCardInGood)
		{
			try
			{
				lUserInString = lBr.readLine();
			}
			catch(Exception e)
			{
				System.err.println("Well this is unfortunate...");
			}
			
			if(lUserInString != null)
			{
				lIndex = aInPile.findIndex(lUserInString);
				if(lIndex != -1)
					lCardInGood = true;
				else
					System.out.print("\nSorry, not a card or not found....");
				
			}
			else
				System.out.print("\nSorry, not a card or not found....");
	
		}
		
		return lIndex;
	}
	
	/*
	 * Handles Scoring + endturn
	 * 
	 * returns true if finished dropping cards (inluding card for drop pikle),
	 * other wise false.
	 */
	
	public static boolean dropCards(Player aInPlayer, CardPile aInDropPile,
			ScorePile aInScorePile)
	{
		boolean lEndTurn = false;
		int lCardIndex, lCardIndex1, lCardIndex2, lCardIndex3;
		
		System.out.print("\nChoice: ");
			
		int lUserInInt =  getUserIntKey();
			
		// Handle players decision		
		if (lUserInInt == 1) // Player drop card + end turn.
		{
			System.out.print("\nPlease drop a card (pick one like S12): ");
			 lCardIndex = getCardUserKey(aInPlayer.hand);
			CardActions.takeCard(lCardIndex, aInDropPile, aInPlayer.hand);
			
			return true;
			
		}
		else if (lUserInInt == 2)
		{
			CardPile tmpDrop = new CardPile("temp");
			System.out.print("\nPlease choose 3 cards to drop: Card1:");
			lCardIndex1 = getCardUserKey(aInPlayer.hand);
			CardActions.takeCard(lCardIndex1, tmpDrop, aInPlayer.hand);
			logger.logDebug(tmpDrop.simpleString(), 
					PlayerAction.class.getName());

			System.out.print("\nCard2:");
			lCardIndex2 = getCardUserKey(aInPlayer.hand); 
			CardActions.takeCard(lCardIndex2, tmpDrop, aInPlayer.hand);
			logger.logDebug(tmpDrop.simpleString(), 
					PlayerAction.class.getName());

			System.out.print("\nCard3:");
			lCardIndex3 = getCardUserKey(aInPlayer.hand);
			CardActions.takeCard(lCardIndex3, tmpDrop, aInPlayer.hand);			
			logger.logDebug(tmpDrop.simpleString(), 
					PlayerAction.class.getName());
			
			boolean lPileGood = Utilities.isTriplet(tmpDrop);
			
			if(!lPileGood)
			{
				lPileGood = Utilities.isRun(tmpDrop);
			}
			
			if (lPileGood)
			{
				aInScorePile.addPile(tmpDrop);
				return false;
			}
			else
			{
				//Return the cards, user made a bad choice:
				aInPlayer.hand.add(tmpDrop.getTop());
				aInPlayer.hand.add(tmpDrop.getTop());
				aInPlayer.hand.add(tmpDrop.getTop());			
				
				System.out.print("\nSorry, cant drop that pile.");
				
				return false;
			}
		}
		else if (lUserInInt == 69)
		{
			
		}
		else
		{
			System.out.println("Try again: ");
		}

		return false;		
	}
	
	/*
	 * A temp method to end a turn.
	 */
		public static boolean testEndTurn(Player aInPlayer, CardPile aInDropPile)
	{
		boolean lEndRound = false;
		
		System.out.print("\nEndRound (69=y): ");
		int lUserInInt =  getUserIntKey();
		
		if(lUserInInt == 69) //Testing purposes
		{
			lEndRound = true;
		}		
		else  // Drop card
		{
			System.out.println("Please drop a card (pick one like S12): ");
			int lCardIndex = getCardUserKey(aInPlayer.hand);
			CardActions.takeCard(lCardIndex, aInDropPile, aInPlayer.hand);
		}
		
		return lEndRound;
	}
		
	public static void pickupDeck(Game aInGame)
	{		
		//Uncomment when done testing:
		aInGame.p1.hand.add(aInGame.deck.getTop());		
	}
	
	public static void pickupDeck(GameClient aInGame)
	{
		aInGame.p2Hand.add(aInGame.deck.getTop());
	}
}
