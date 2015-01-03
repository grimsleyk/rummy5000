package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import engine.CardActions;
import engine.Game;
import engine.PlayerAction;

import resources.Card;
import resources.CardPile;
import resources.Player;
import resources.ScorePile;
import test.HelloWorldGUI;

public class GameViewer extends JFrame implements ActionListener
{
	// GUI stuff:
	public JFrame frame;
	public static int dropPilePosX, dropPilePosY;
	
	// Gameplay stuff:
	public static int turn;
	public static int round;
	public static boolean P1NextRound;
	public static boolean P1Next;
	public static final String BACKGROUND_COLOUR = "#31B94D";  
	
	public GameViewer()
	{	// For Test purposes, 'bbb' is good:
		//this(new Rectangle(150, 150, 1500, 750)); // bbb
		this(new Rectangle(2500, 150, 1500, 750));
	}
	
	public GameViewer(Rectangle aInBounds)
	{
		// Set up Initial Positions:
		dropPilePosX = 125;
		dropPilePosY = 250;
				
		// Set up frame:
		frame = new JFrame ("Rummy Table");
		//frame.setContentPane(new GraphicController());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(1500, 750);
		buildWindow();
		//Create and set up the content pane.
		//frame.getContentPane().setBackground(Color.decode("#488214"));
		frame.getContentPane().setBackground(
				Color.decode(BACKGROUND_COLOUR));					    
			    
		// Display window:
		frame.setVisible(true);
		frame.setBounds(aInBounds);
	}
	
	public void refreshWindow()
	{
		frame.repaint();
		frame.removeAll();
	}
	
	public void buildWindow()
	{
		
		//Create and set up the content pane.
		frame.getContentPane().setLayout(null);
		//frame.getContentPane().setBackground(java.awt.Color.GREEN);		
		//frame.getContentPane().setBackground(Color.decode("#488214"));
		frame.getContentPane().setBackground(Color.decode("#31B94D"));
			    
		
		//JScrollBar hbar = new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 300);
		JScrollBar hbar = new JScrollBar(JScrollBar.HORIZONTAL);
		hbar.setUnitIncrement(2);
		hbar.setBlockIncrement(1);	    
		//frame.getContentPane().add(hbar);
		//JScrollPane scrollPane = new JScrollPane(frame);
		//scrollPane.add(hbar);			
				
		// Display window:
		frame.setVisible(true);
		
		
		 //JFrame.setDefaultLookAndFeelDecorated(true);
	     //   JFrame frame = new JFrame("[=] Embrace of the JScrollPane [=]");

	     //   frame.setContentPane(createContentPane());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public boolean update(boolean t)
	{	
		return !t;
	}
	
	public void updateColour()
	{
		frame.getContentPane().setBackground(java.awt.Color.BLACK);
	}
	
	public void closeWindow()
	{
		frame.setVisible(false);
		frame.dispose();
	}
	
	public JPanel createContentPane (){

        // As usual, we create our bottom-level panel.
        JPanel totalGUI = new JPanel();
        
        // This is the story we took from Wikipedia.
        String story = "The Internet Foundation Classes (IFC) were a graphics "+
                       "library for Java originally developed by Netscape Communications "+
                       "Corporation and first released on December 16, 1996.\n\n"+
                       "On April 2, 1997, Sun Microsystems and Netscape Communications"+
                       " Corporation announced their intention to combine IFC with other"+
                       " technologies to form the Java Foundation Classes. In addition "+
                       "to the components originally provided by IFC, Swing introduced "+
                       "a mechanism that allowed the look and feel of every component "+
                       "in an application to be altered without making substantial "+
                       "changes to the application code. The introduction of support "+
                       "for a pluggable look and feel allowed Swing components to "+
                       "emulate the appearance of native components while still "+
                       "retaining the benefits of platform independence. This feature "+
                       "also makes it easy to have an individual application's appearance "+
                       "look very different from other native programs.\n\n"+
                       "Originally distributed as a separately downloadable library, "+
                       "Swing has been included as part of the Java Standard Edition "+
                       "since release 1.2. The Swing classes are contained in the "+
                       "javax.swing package hierarchy.";
        
        // We create the TextArea and pass the story in as an argument.
        // We also set it to be non-editable, and the line and word wraps set to true.
        //JTextArea storyArea = new JTextArea(story);
       // storyArea.setEditable(false);
      //  storyArea.setLineWrap(true);
      //  storyArea.setWrapStyleWord(true);
        
        // We create the ScrollPane and instantiate it with the TextArea as an argument
        // along with two constants that define the behaviour of the scrollbars.
      //  JScrollPane area = new JScrollPane(storyArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        //                                   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // We then set the preferred size of the scrollpane.
      //  area.setPreferredSize(new Dimension(300, 200));
        
        // and add it to the GUI.
      //  totalGUI.add(area);
        JScrollPane scrollPane = new JScrollPane(frame, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        totalGUI.add(scrollPane);
        
        totalGUI.setOpaque(true);
        return totalGUI;
    }
}

