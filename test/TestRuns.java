package test;

import java.util.*;

public class TestRuns 
{
	public TestRuns()
	{
		testArray1();
	}
	
	public static void testArray1()
	{
		int[] arr = {1, 3, 5, 6, 9, 7};
		
		//Arrays.sort(arr);
		printArr(arr);
	}
	
	public static void printArr(int[] arr)
	{
		System.out.println("\n");
		for(int i = 0; i < arr.length; i++ )
		{
			System.out.print(" " + arr[i] + " ");
		}
	}
}
