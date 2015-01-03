package resources;

import java.io.*;
import java.sql.Date;
import java.util.*;

import java.util.Calendar;
import java.text.SimpleDateFormat;

/*
 * Description: A class that logs things like errors for Rummy5000.
 *              
 * Author: Kevin Grimsley
 * Last Modification Date: 21/10/2012
 * Version 1.0
 */
public class Logger 
{
	static Calendar currentDate = Calendar.getInstance();
	static SimpleDateFormat formatter= 
			new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
	String dateTime;
	
	public Logger()
	{		
		
		dateTime = ""; 	
	}
	
	public void log(String aInMessage)
	{
		if(aInMessage == null)
			aInMessage = "";
		try
		{
			FileWriter fstream = new FileWriter("rummyError.log", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.append("[" + formatter.format(currentDate.getTime()) 
					+ "]ERROR: " + aInMessage +"\n");
			out.close();
		}
		catch (Exception e)
		{	//Catch exception if any
			System.err.println("Error: " + e.getMessage() + "\n");
		}
	}
	
	public static void logDebug(String aInMessage, String aInClassName)
	{
		if(aInMessage == null)
			aInMessage = "";
		try
		{
			FileWriter fstream = new FileWriter("rummyError.log", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.append("[" + formatter.format(currentDate.getTime()) 
					+ " " + aInClassName + " ] DEBUG: " + aInMessage + "\n");
			out.close();
		}
		catch (Exception e)
		{	//Catch exception if any
			System.err.println("Error: " + e.getMessage() + "\n");
		}
	}
	
	public static void logInfo(String aInMessage)
	{
		if(aInMessage == null)
			aInMessage = "";
		try
		{
			FileWriter fstream = new FileWriter("rummyError.log", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.append("[" + formatter.format(currentDate.getTime()) 
					+"] INFO: " + aInMessage + "\n");
			out.close();
		}
		catch (Exception e)
		{	//Catch exception if any
			System.err.println("Error: " + e.getMessage() + "\n");
		}
	}
}
