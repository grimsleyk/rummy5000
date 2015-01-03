package engine;

import graphics.PopUps;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Logger;
import resources.Player;

//Just a class that soley handles starting the game.
public class StartMenu extends JFrame implements ActionListener
{
	public static JFrame frame ;
	public static JButton JButtonP1, JButtonP2;
	public static Player  lPlayer = new Player();
	boolean validIP; //Ensures user can find server if second player.
	public static boolean start;	
	
	public static final String LOGO_DIRECTORY = "pics/";
	
	public StartMenu() throws InterruptedException
	{
		startIntro();
		startScreen();		
	}
	
	public  void startIntro() throws InterruptedException
	{		
		String lImgLocation = "";
		String lFileName = "";
		
		lFileName = "Scene1.jpg";
		lImgLocation = LOGO_DIRECTORY + lFileName;
		printScene(lImgLocation, 2000, new Rectangle(75, 150, 600, 300));
		
		printDedication();
		
		//lFileName = "Scene2.jpg";
		//lImgLocation = LOGO_DIRECTORY + lFileName;
		//printScene(lImgLocation, 1500, new Rectangle(75, 150, 600, 300));	
		
		//lFileName = "dedicationPic2.jpg";
		//lImgLocation = LOGO_DIRECTORY + lFileName;
		//printScene(lImgLocation, 3000, new Rectangle(75, 150, 441, 480));

		lFileName = "Scene4.jpg";
		lImgLocation = LOGO_DIRECTORY + lFileName;
		printScene(lImgLocation, 1500, new Rectangle(75, 150, 600, 300));
		
		lFileName = "firstLogo2.jpg";
		lImgLocation = LOGO_DIRECTORY + lFileName;
		printScene(lImgLocation, 2000, new Rectangle(75, 50, 640, 640));	
	}
	
	public void startScreen()
	{
		String lImgLocation = "";
		start = false;
		
		frame = new JFrame("Start Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create and set up the content pane.
		frame.getContentPane().setLayout(null);
		//frame.getContentPane().setBackground(Color.decode("#488214"));
		frame.getContentPane().setBackground(Color.decode("#31B94D"));
		frame.setBounds(600, 180, 700, 500);
		
		//Print Rummy5000Logo
		ImageIcon lImg = new ImageIcon();
		JLabel lLogo = new JLabel();
		lImgLocation = LOGO_DIRECTORY + "GameLogo.jpg";
		try
		{			
			lImg = new ImageIcon(ImageIO.read(this.getClass().
					getResourceAsStream(lImgLocation)));
		}
		catch(Exception e)
		{
			System.err.print(e);
		}
		
		lLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		lLogo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		lLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lLogo.setIcon(lImg);
		lLogo.setOpaque(true);
		frame.getContentPane().add(lLogo);	
		frame.setVisible(true);		
		lLogo.setBounds(75, 50, 600, 300);
		
		SymAction lSymAction = new SymAction();
		
		JButtonP1 = new JButton();
		JButtonP1.setBounds(100, 250, 200, 50);
		JButtonP1.setText("Player 1");
		JButtonP1.addActionListener(lSymAction);
		frame.getContentPane().add(JButtonP1);
		
		JButtonP2 = new JButton();
		JButtonP2.setBounds(400, 250, 200, 50);
		JButtonP2.setText("Player 2");
		JButtonP2.addActionListener(lSymAction);
		frame.getContentPane().add(JButtonP2);
		
		frame.setVisible(true);
	}
	
	public  void printScene(String aInImgLocation, int aInLength,
			Rectangle aInDimensions ) throws
	InterruptedException
	{
		frame = new JFrame("Intro");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.decode("#31B94D"));
		//frame.setBounds(2500, 300, 800, 800);
		frame.setBounds(600, 180, 800, 800);
		
		printPic(aInImgLocation, aInDimensions);
		
		Thread.sleep(aInLength);
		frame.dispose();
	}

	public  void printDedication() throws
	InterruptedException
	{
		String aInImgLocation1 = LOGO_DIRECTORY +  "Scene2.jpg";
		String aInImgLocation2 = LOGO_DIRECTORY + "dedicationPic2.jpg";
		String aInImgLocation3 = LOGO_DIRECTORY + "Name_Steffani.jpg";
		Rectangle aInDimensions1 = new Rectangle(50, 50, 600, 300); 
		Rectangle aInDimensions2 = new Rectangle(175, 150, 441, 480);
		Rectangle aInDimensions3 = new Rectangle(450, 650, 210, 122);;
		frame = new JFrame("Intro");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.decode("#31B94D"));
		frame.setBounds(600, 180, 800, 800);
		
		printPic(aInImgLocation1, aInDimensions1);
		Thread.sleep(1500);
		printPic(aInImgLocation2, aInDimensions2);
		Thread.sleep(1500);
		printPic(aInImgLocation3, aInDimensions3);
		Thread.sleep(2000);
		
		//Thread.sleep(aInLength);
		frame.dispose();
	}
	
	// prints a pic to THIS frame...
	public  void printPic(String aInImgLocation, 
			Rectangle aInDimensions)
	{		
		ImageIcon lImg = new ImageIcon();
		JLabel lLogo = new JLabel();
		
		try
		{			
			lImg = new ImageIcon(ImageIO.read(this.getClass().
					getResourceAsStream(aInImgLocation)));
		}
		catch(Exception e)
		{
			System.err.print(e);
		}
		
		lLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		lLogo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		lLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lLogo.setIcon(lImg);
		lLogo.setOpaque(true);
		frame.getContentPane().add(lLogo);	
		frame.setVisible(true);		
		//lLogo.setBounds(75, 50, 640, 640);
		lLogo.setBounds(aInDimensions);
	}
	
	public static Player startGame()
	{	
		while(!start)
		{
			System.out.println("");
		}
		
		return lPlayer;
	}


	
	public static void firstPlayerAction()
	{
		lPlayer = new Player(1);
		frame.dispose();
		start = true;
	}
	
	public static void secondPlayerAction()
	{
		
		lPlayer = new Player(2);
		lPlayer.HOSTNAME = PopUps.player2Start();
		// TODO: check if IP is valid/can connect.
		frame.dispose();
		start = true;
	}
	
	public void reCreateMenu()
	{
		
	}
	
	class SymAction implements java.awt.event.ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			Object button = event.getSource();
			
			if(button == JButtonP1)
				firstPlayerAction();
			else if(button == JButtonP2)
				secondPlayerAction();
		}
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}




