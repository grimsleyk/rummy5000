package test;

import resources.*;

public class TestScoreTable 
{
	public TestScoreTable()
	{
		runSTTest();	
	}
	
	public static void runSTTest()
	{
		CardPile p1 = new CardPile("test");
		p1.add("Spades", 1);
		p1.add("Spades", 2);
		p1.add("Spades", 3);
		p1.add("Spades", 4);
		
		CardPile p2 = new CardPile("test");
		p2.add("Hearts", 5);
		p2.add("Hearts", 6);
		p2.add("Hearts", 7);
		p2.add("Hearts", 8);
		
		CardPile p3 = new CardPile("test");
		p3.add("Clubs", 9);
		p3.add("Clubs", 10);
		p3.add("Clubs", 11);
		p3.add("Clubs", 12);
		
		ScorePile sP = new ScorePile();
		sP.addPile(p1);
		sP.addPile(p2);
		sP.addPile(p3);
		
		sP.printScoreTable();
		
	}

}
