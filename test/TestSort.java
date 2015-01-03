package test;

import engine.CardActions;
import resources.CardPile;

public class TestSort 
{
	public TestSort()
	{
		//testAlgorithm();
		testFeature();
	}
	
	public static void testAlgorithm()

	{
		int[] a = {1, 5, 3, 2, 8, 24, 44, 11, 2, 99, 5, 101};
		int tmp;
		boolean swapped;
		
		for(int i = a.length - 1; i >= 0; i--)
		{
			swapped = false;
			
			for(int j = 0; j < i ; j++)
			{
				if(a[j] > a[j + 1])
				{
					tmp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = tmp;
					System.out.println ("swapp" + i );
					swapped = true;
				}
			}
			//if(!swapped)
				//return;
		}
		
		for(int i = 0; i < a.length; i ++)
		{
			System.out.println("aaa" + a[i]);
		}
	}
	
	public static void testFeature()
	{
		CardPile p2 = new CardPile("test");
		p2.add("Hearts", 5);
		p2.add("Spades", 7);
		p2.add("Clubs", 9);
		p2.add("Hearts", 8);
		p2.add("Diamonds", 1);
		p2.add("Hearts", 3);
		p2.add("Clubs", 2);
		p2.add("Hearts", 4);
		
		System.out.println(p2.simpleString());
		p2  = CardActions.sortPile(p2);
		System.out.println(p2.simpleString());
	}
}
