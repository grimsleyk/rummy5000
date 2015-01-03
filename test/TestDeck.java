package test;

import engine.CardActions;
import resources.CardPile;
import resources.PlayingCard;

public class TestDeck 
{
	public TestDeck()
	{
		System.out.println("-----------------------------------");
		System.out.println("Beginning tests QA tests on deck of cards:\n");
		
		buildDeckTest();
		
		System.out.println("-----------------------------------");

		
	}
	
	public void buildDeckTest()
	{
		CardPile deck = new CardPile("test");
		
		deck = CardActions.buildDeck(deck);
		System.out.println("Deck is: " + deck.toString());
		deck = chooseSuit(deck, "Hearts");
		//System.out.println("Now it is: " + deck.toString());
		
	}
	
	public CardPile chooseSuit(CardPile aInPile, String aInSuit)
	{
		CardPile lSol = new CardPile("test");
		
	    PlayingCard current = aInPile.getHead();
		
		while (current.getNext() != null)
		{
			if(current.getData().getSuit().equals(aInSuit))
			{
				lSol.add(aInSuit, current.getData().getValue());
			}
			current = current.getNext(); 
		}		
		
		return lSol;
	}
}
