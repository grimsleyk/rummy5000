package test;

import engine.*;
import resources.*;

/*
 * Desctiption:  Test Class. Tests:
 *               
 *              - Can build and Add to Pile.
 *              - Can get cards from pile.
 */
public class TestLinkedList 
{
	public TestLinkedList()
	{
		System.out.println("Packages work, hooray for organization!!!");
		BuildAndPrint();
		//FullDeck();
	}
	
	// Test function that builds a pile of cards (linked list)
	// and displays them, forwards & backwards.
	public void BuildAndPrint()
	{
		String lSuite = "test";
		String lCardValue = "";
		
		//Fisrt Pile
		CardPile lPile = new CardPile("test");
		
		lPile.add("HEAD", 111);
		lPile.add("MID", 222);
		lPile.add("TAIL", 333);	
		lPile.add("XXX", 444);			
		lPile.add("YYY", 555);			
		lPile.add("ZZZ", 666);			


		//Card result = lPile.get(2);
		//lCardValue = result.getCardValue();
		System.out.println("Before:" + lPile.toString() + lPile.size());
		lPile.remove(4);
		System.out.println("Before:" + lPile.toString()+ lPile.size());

		
		//Future add Card type objects to pile:
		/*
		Card c1 = new Card("HEAD", 999);
		Card c2 = new Card("MID", 666);
		Card c3 = new Card("TAIL", 111);
		
		CardPile lPileCard = new CardPile();
		lPileCard.add(c1);
		lPileCard.add(c2);
		lPileCard.add(c3);
		
		System.out.println("Before:" + lPileCard.toString() + lPile.size());
		lPile.remove(2);
		System.out.println("Before:" + lPileCard.toString()+ lPile.size());
		*/
	}
	
	public void FullDeck()
	{
		CardPile deck = new CardPile("test");
		int i;
		
		for(i = 1; i <= 13; i++)
		{
			deck.add("Hearts", i);
		}
		for(i = 1; i <= 13; i++)
		{
			deck.add("Diamonds", i);
		}
		for(i = 1; i <= 13; i++)
		{
			deck.add("Spades", i);
		}
		for(i = 1; i <= 13; i++)
		{
			deck.add("Clubs", i);
		}
		
		System.out.println("The deck is:" + deck.toString());
	}
}
