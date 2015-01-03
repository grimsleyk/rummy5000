package graphics;



import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import engine.Game;
import engine.GameClient;

import resources.CardPile;
import resources.Logger;
import resources.Player;
import resources.PlayingCard;
import resources.ScorePile;
import resources.ScoreSheet;

public class GraphicController extends JPanel
{
	public static final String HTML_TAG = "<HTML>";
	public static final String END_HTML_TAG = "</HTML>";
	public static final String END_FONT_TAG = "</FONT>";
	public static final String SCORE_BOARD_FONT = "<FONT COLOR=BLUE>";
	public static final String SCORE_VALUE_FONT = "<FONT COLOR=RED>";
	public static final String LINE_BREAK = "<BR>";
	public static final String END_LINE_BREAK = "</BR>";
	public static final String INDENT = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + 
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	public static final String PAD = "&nbsp;&nbsp;&nbsp;";
	public static final String NBSP = "&nbsp;";

	public static final int CARD_WIDTH = 73;
	public static final int CARD_LENGTH = 97;
	public static final String PLAYER_1 = "p1";
	public static final String PLAYER_2 = "p2";
	
	public Player p1;
	public Player p2;
	public GameViewer gameView;
	public Game game;	
	public GameClient gameClient;
	public int player;
	public int state;
	
	public static int xCoord, yCoord; // Coordinates for print cards;
	
	public JPanel panel;
	
	public GraphicController()
	{
		
	}
	
	public GraphicController(Player aInP1, Player aInP2, GameViewer aInGV,
			Game aInGame, int aInPlayer) 
	{
		super(true);
		
		p1 = aInP1;
		p2 = aInP2;
		gameView = aInGV;
		game = aInGame;
		player = aInPlayer;
		panel = new JPanel();
	}
	
	public GraphicController(CardPile aInP1Hand, CardPile aInP2Hand,
			GameViewer aInGV, GameClient aInGame, int aInPlayer)
	{
		super (true);
		
		gameClient = aInGame;
		player = aInPlayer;
		panel = new JPanel();
	}
	
	/*
	 * Method that prints table (TODO for player1 only????).
	 * 
	 * @param aInP1 - Player1
	 * @param aInP2 - Player2
	 * @param aInDropPile - pile where players drop cards
	 * @param aInP1Next - whos turn it is.
	 * @param aInScorePile - all the piles of scored games.
	 * @param aInGV - the game viewer which handles the graphics.
	 * 
	 * @param aInGameState - handles what state the game is in:
	 *                        * 1 = pickup cards
	 *                        * 2 =
	 */
	/*
	public  void printGraphicTable(Player aInP1, Player aInP2, 
			CardPile aInDropPile, boolean aInP1Next, ScorePile aInScorePile,
			GameViewer aInGV, int aInGameState, Game aInGame)
	{						
		//TODO: once network capabilities function, just focus on users stuff:
		// Print pickup piles:
		state = aInGameState;
		printButtons(gameView, player1, aInGame.getP1Next());
		printPickUpPiles(aInDropPile, aInGV);
		printHand(aInP1.hand, aInGV);
	}
	*/

	
	//Player 1 only???? TODO
	public  void printGraphicTable(CardPile aInDropPile, boolean aInP1Next, 
			ScorePile aInScorePile, ScoreSheet aInScoreSheet, int aInGameState)
	{						
		//TODO: once network capabilities function, just focus on users stuff:
			// Print pickup piles:
		    state = aInGameState;
		    initializeCoordinates();
			printScorePiles(aInScorePile, gameView);
			//Set coordinates for pickup pile + scoreboard.
			xCoord = 50;
			yCoord = yCoord + 25 + CARD_LENGTH;
			printScoreBoard(gameView, aInScoreSheet);
			printPickUpPiles(aInDropPile, gameView);
			xCoord = 50;
			yCoord = yCoord + 25 + CARD_LENGTH;
			printHand(p1.hand, gameView);
			xCoord = 50;
			yCoord = yCoord + 25 + CARD_LENGTH;
			printButtons(gameView, PLAYER_1, aInP1Next);			
	}
	
	
	// Just for p2
	public void PrintPlayer2(CardPile aInP1Hand, CardPile aInP2Hand, 
			CardPile aInDropPile, boolean aInP1Next, ScorePile aInScorePile, 
			ScoreSheet aInScoreSheet, int aInGameState)
	{
		state = aInGameState;
	    initializeCoordinates();
		printScorePiles(aInScorePile, gameView);
		//Set coordinates for pickup pile + scoreboard.
		xCoord = 50;
		yCoord = yCoord + 25 + CARD_LENGTH;
		printScoreBoard(gameView, aInScoreSheet);
		printPickUpPiles(aInDropPile, gameView);
		xCoord = 50;
		yCoord = yCoord + 25 + CARD_LENGTH;
		printHand(aInP2Hand, gameView);
		xCoord = 50;
		yCoord = yCoord + 25 + CARD_LENGTH;
		printButtons(gameView, PLAYER_2, aInP1Next);
	}
	
	public void initializeCoordinates ()
	{
		xCoord = 50;
		yCoord = 50;
	}
	/*
	 * Prints piles (overlapped)
	 * 
	 * @param aInPile - card pile to be printed
	 */
	public void printPile(CardPile aInPile)
	{
		
	}
	
	/*
	 * Prints a card based off given dimmensions.
	 * 
	 * @param aInPlayingCard - card to be printed (if null is deck...)
	 * @param aInX - x coordanate.
	 * @param aInY - y cooradnate.
	 */
	public  void PrintCard(GameViewer aInGV, PlayingCard aInCard, int aInX, 
			int aInY, boolean lIsLast, String aInPileType)
	{
		JLabel lCardLabel = new JLabel();
		String lPath = "cards/";
		ImageIcon lImg = new ImageIcon();
		
		try
		{
			// Get Image.
			if(aInCard == null) // Print deck
			{
				//String lFileName = "b.gif";
				String lFileName = "b.jpg";
				String lImgLocation = lPath + lFileName;
									
				lImg = new ImageIcon(ImageIO.read(this.getClass().
						getResourceAsStream(lImgLocation)));				
			}
			else
			{
				String  lFileName = aInCard.getValue() + 
						aInCard.getShortSuit() + ".gif"; 
				String lImgLocation = lPath + lFileName;
						
				if(lIsLast)
				{
					lImg = new ImageIcon(ImageIO.read(this.getClass().
							getResourceAsStream(lImgLocation)).
							getSubimage(0, 0, CARD_WIDTH, CARD_LENGTH));			
				}
				else
				{
					lImg = new ImageIcon(ImageIO.read(this.getClass().
							getResourceAsStream(lImgLocation)).
							getSubimage(0, 0, 25, CARD_LENGTH));		
				}
			}
		}
		catch (Exception e)
		{
			Logger.log("Could not find file...");
		}
				
		// Display it:
		lCardLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		lCardLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		lCardLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lCardLabel.setIcon(lImg);
		lCardLabel.setOpaque(true);
		aInGV.frame.getContentPane().add(lCardLabel);
		
		
		// Handle printing last card in pile:
		if(lIsLast)
		{
			lCardLabel.setBounds(aInX, aInY, CARD_WIDTH, CARD_LENGTH);
		}
		else
		{
			lCardLabel.setBounds(aInX, aInY, 25, CARD_LENGTH);
		}
				
		// TODO: handle more advanced mouse actions?
		if(player == 1 && state != 0)
		{
			lCardLabel.addMouseListener(new CardMouseActions(aInCard, 
					state, aInPileType, game));
		}
		else if(player == 2 && state != 0)
		{
			lCardLabel.addMouseListener(new CardMouseActionsP2(aInCard, 
					state, aInPileType, gameClient));
		}
	}
	
	public void printLogo(GameViewer aInGV)
	{
		String lFileName = "firstLogo.xcf";
		ImageIcon lImg = new ImageIcon();
		JLabel lLogo = new JLabel();

		try
		{
			lImg = new ImageIcon(ImageIO.read(this.getClass().
					getResourceAsStream(lFileName)));
		}
		catch(Exception e)
		{
			lLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			lLogo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			lLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			lLogo.setIcon(lImg);
			lLogo.setOpaque(true);
			aInGV.frame.getContentPane().add(lLogo);
		}
	}
	
	/*
	 * prints pickup piles (i.e deck and drop pile)
	 * 
	 * @param aInDropPile - pile that users discard their cards to.
	 * @param aInGV - frame
	 */
	public void printPickUpPiles(CardPile aInDropPile, GameViewer aInGV)
	{
		//int dropPilePosX = 140;
		//int dropPilePosY = 250;
				
		PlayingCard lCurrent = null;
		yCoord = yCoord + 26;
		
		//Print Deck:
		PrintCard(aInGV, null, 50, yCoord, true,"deck");
		
		if(aInDropPile != null && aInDropPile.getHead() != null)
		{
			lCurrent = aInDropPile.getHead().getNext();
		}
		
		xCoord = 140;
		while(lCurrent != null)
		{			
			PrintCard(aInGV, lCurrent, xCoord, yCoord, 
					lCurrent.getNext() == null, aInDropPile.getPileType());
			lCurrent = lCurrent.getNext();
			xCoord = xCoord + 25;									
		}
		
		yCoord = yCoord + 26;
	}
	
	/*
	 * Prints users hand;
	 */
	public void printHand(CardPile aInHand, GameViewer aInGV)
	{
		//int pilePosX = 50;
		//int pilePosY = 450;
		
		PlayingCard lCurrent = null;
		
		if(aInHand.getHead() != null)
		{
			lCurrent = aInHand.getHead().getNext();
		}
		
		while(lCurrent != null)
		{		
			PrintCard(aInGV, lCurrent, xCoord, yCoord, 
					lCurrent.getNext() == null, aInHand.getPileType());
			lCurrent = lCurrent.getNext();
			xCoord = xCoord + 25;
		}
	}
	
	/*
	 * Prints the cards that have been played (scored).
	 */
	public void printScorePiles(ScorePile aInScorePile, GameViewer aInGV)
	{
		// Initial positions
		//int pilePosX = 50;
		//int pilePosY = 50;
		int i;
		int lSpaceLeft = 750;
		CardPile lPile;
		PlayingCard lCurrent;
				
		// Print each Card Pile	
		for(i = 0; i < aInScorePile.size(); i++)
		{			
			lCurrent = null;
			lPile = aInScorePile.getCardPile(i);
			
			if(lPile.size() * 25 > lSpaceLeft)
			{
				lSpaceLeft = 750;
				xCoord = 50;
				yCoord = yCoord + 25 + CARD_LENGTH;
			}
			
			if(lPile.getHead() != null)
			{
				lCurrent = lPile.getHead().getNext();
			}
			
			while(lCurrent != null)
			{		
				PrintCard(aInGV, lCurrent, xCoord, yCoord, 
						lCurrent.getNext() == null, lPile.getPileType());
				lCurrent = lCurrent.getNext();
				xCoord = xCoord + 25;
				lSpaceLeft= lSpaceLeft - 25;
			}
			
			xCoord += 75;
		}							
	}
	
	/*
	 * Prints the score board.
	 */
	public void printScoreBoard(GameViewer aInGV, ScoreSheet aInScoreSheet)
	{
		// Build score board values as string:
		String lScoreBoardString= "";
				
		lScoreBoardString += HTML_TAG  + SCORE_BOARD_FONT; 
		lScoreBoardString += PAD + "TotalScoreP1:" + END_FONT_TAG;		
		lScoreBoardString += buildSpacer(25 - 
				String.valueOf(aInScoreSheet.getTotalScoreP2()).length());
		lScoreBoardString += SCORE_VALUE_FONT;
		lScoreBoardString += aInScoreSheet.getTotalScoreP1() + END_FONT_TAG;
		lScoreBoardString += LINE_BREAK + SCORE_BOARD_FONT;
		lScoreBoardString += PAD + "TotalScoreP2:" + END_FONT_TAG;
		lScoreBoardString += buildSpacer(25 - 
				String.valueOf(aInScoreSheet.getTotalScoreP2()).length());
		lScoreBoardString += SCORE_VALUE_FONT;
		lScoreBoardString += aInScoreSheet.getTotalScoreP2() + END_FONT_TAG;
		lScoreBoardString += LINE_BREAK + SCORE_BOARD_FONT;
		lScoreBoardString += LINE_BREAK;
		lScoreBoardString +=  PAD + "RoundScoreP1:" + END_FONT_TAG;
		lScoreBoardString += buildSpacer(23 - 
				String.valueOf(aInScoreSheet.getRoundScoreP1()).length());
		lScoreBoardString += SCORE_VALUE_FONT;
		lScoreBoardString += aInScoreSheet.getRoundScoreP1() + END_FONT_TAG;
		lScoreBoardString += LINE_BREAK + SCORE_BOARD_FONT;
		lScoreBoardString +=  PAD + "RoundScoreP2:" + END_FONT_TAG;
		lScoreBoardString += buildSpacer(23 - 
				String.valueOf(aInScoreSheet.getTotalScoreP2()).length());
		lScoreBoardString += SCORE_VALUE_FONT;
		lScoreBoardString += aInScoreSheet.getRoundScoreP2() + END_FONT_TAG;
		lScoreBoardString += LINE_BREAK + SCORE_BOARD_FONT;
		lScoreBoardString += END_FONT_TAG + END_HTML_TAG; 
				
		JLabel scoreBoardLabel = new JLabel(lScoreBoardString);
		// Display it:
		scoreBoardLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		scoreBoardLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		scoreBoardLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
		scoreBoardLabel.setBackground(java.awt.Color.LIGHT_GRAY);
		scoreBoardLabel.setOpaque(true);
		aInGV.frame.getContentPane().add(scoreBoardLabel);
		aInGV.frame.setVisible(true);
		
		//scoreBoardLabel.setBounds(1200, 225, 280, 150);
		scoreBoardLabel.setBounds(1200, yCoord, 280, 150);
	}
	
	/*
	 * Prints buttons used by user.  Buttons contain:
	 * 
	 * - Triple Drop - Used when dropping new set/run.
	 * - Discard
	 * 
	 */
	public void printButtons(GameViewer aInGV, String aInPlayerView, 
			boolean aInP1Next)
	{
		JButton discard = new JButton("Discard");
		JButton scoreRun = new JButton("Start Run");
		JButton scoreTrips = new JButton("Start Set");
		JButton addSet = new JButton("Add to Set");
		JButton addRunLow = new JButton("Add to Run (low)");
		JButton addRunHigh = new JButton("Add to Run (high)");
		JButton reset = new JButton("Reset");
		JButton returnScorePile = new JButton("Return ScorePile");

		
				
		Logger.logDebug("Good...", "graphicControler");
		// Only enable scoring buttons for p1 if p1s turn
		if((aInP1Next && PLAYER_1.equals(aInPlayerView)) ||
				(!aInP1Next && PLAYER_2.equals(aInPlayerView)))
		{
			Logger.logDebug("great...", "graphicControler");
			discard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			discard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			discard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			discard.setBounds(50, yCoord, 180, 30);
			discard.setVisible(true);
			
			scoreRun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			scoreRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			scoreRun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			scoreRun.setBounds(250, yCoord, 180, 30);
			scoreRun.setVisible(true);		
			
			scoreTrips.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			scoreTrips.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			scoreTrips.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			scoreTrips.setBounds(450, yCoord, 180, 30);
			scoreTrips.setVisible(true);
			
			addRunLow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			addRunLow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			addRunLow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			addRunLow.setBounds(50, yCoord + 40, 180, 30);
			addRunLow.setVisible(true);
			
			addRunHigh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			addRunHigh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			addRunHigh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			addRunHigh.setBounds(250, yCoord + 40, 180, 30);
			addRunHigh.setVisible(true);
			
			addSet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			addSet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			addSet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			addSet.setBounds(450, yCoord + 40, 180, 30);
			addSet.setVisible(true);	
			
			reset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			reset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			reset.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			reset.setBounds(650, yCoord, 180, 30);
			reset.setVisible(true);
			
			returnScorePile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			returnScorePile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			returnScorePile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			returnScorePile.setBounds(650, yCoord + 40, 180, 30);
			returnScorePile.setVisible(true);
			
			//TODO Seperate this cleaner
			if(player == 1)
			{
				
				aInGV.frame.getContentPane().add(discard);
				discard.addMouseListener(new ButtonMouseActions(game, 1));					
				aInGV.frame.getContentPane().add(addSet);
				addSet.addMouseListener(new ButtonMouseActions(game, 2));
				aInGV.frame.getContentPane().add(addRunLow);
				addRunLow.addMouseListener(new ButtonMouseActions(game, 3));
				aInGV.frame.getContentPane().add(addRunHigh);
				addRunHigh.addMouseListener(new ButtonMouseActions(game, 4));
				aInGV.frame.getContentPane().add(scoreRun);
				scoreRun.addMouseListener(new ButtonMouseActions(game, 5));
				aInGV.frame.getContentPane().add(scoreTrips);
				scoreTrips.addMouseListener(new ButtonMouseActions(game, 6));
				aInGV.frame.getContentPane().add(reset);
				reset.addMouseListener(new ButtonMouseActions(game, 7));
				aInGV.frame.getContentPane().add(returnScorePile);
				returnScorePile.addMouseListener(new ButtonMouseActions(game, 8));				
			}
			else if(player == 2)
			{
				Logger.logDebug("reall fucked up...", "graphicControler");

				aInGV.frame.getContentPane().add(discard);
				discard.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 1));				
				aInGV.frame.getContentPane().add(addSet);
				addSet.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 2));				
				aInGV.frame.getContentPane().add(addRunLow);
				addRunLow.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 3));	
				aInGV.frame.getContentPane().add(addRunHigh);
				addRunHigh.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 4));
				aInGV.frame.getContentPane().add(scoreRun);
				scoreRun.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 5));
				aInGV.frame.getContentPane().add(scoreTrips);
				scoreTrips.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 6));
				aInGV.frame.getContentPane().add(reset);
				reset.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 7));
				aInGV.frame.getContentPane().add(returnScorePile);
				returnScorePile.addMouseListener(new ButtonMouseActionsP2(
						gameClient, 8));
			}
		}	
		
	}
	
	public void printScrollBars(GameViewer aInGV)
	{	
		
	}
	
	public static String buildSpacer(int aInSpaceSize)
	{
		String lSpacer = "";
		
		for(int i = 0; i < aInSpaceSize; i++)
		{
			lSpacer += NBSP;
		}
		
		return lSpacer;
	}
	

}


