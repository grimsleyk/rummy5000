package resources;

import java.io.Serializable;

public class ScoreSheet implements Serializable
{
	int totalScoreP1, totalScoreP2;
	int roundScoreP1, roundScoreP2;
	
	public ScoreSheet()
	{
		setTotalScoreP1(0);
		setRoundScoreP1(0);
		setTotalScoreP2(0);
		setRoundScoreP2(0);
	}
	
	public int getTotalScoreP1()
	{
		return totalScoreP1;
	}
	
	public int getTotalScoreP2()
	{
		return totalScoreP2;
	}
	
	public void setTotalScoreP1(int aInScore)
	{
		totalScoreP1 = aInScore;
	}
	
	public void setTotalScoreP2(int aInScore)
	{
		totalScoreP2 = aInScore;
	}
	
	public int getRoundScoreP1()
	{
		return roundScoreP1;
	}
	
	public int getRoundScoreP2()
	{
		return roundScoreP2;
	}
	
	public void setRoundScoreP1(int aInScore)
	{
		roundScoreP1 = aInScore;
	}
	
	public void setRoundScoreP2(int aInScore)
	{
		roundScoreP2 = aInScore;
	}
}
