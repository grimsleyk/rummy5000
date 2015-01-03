package test;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;
import java.awt.*;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class TestGUI extends  WindowAdapter
{
	 public TestGUI()
	 {
		 // Set up frame:
		 JFrame frame = new JFrame("Learnen GUI");
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setSize(1500, 750);
		 
		//Create and set up the content pane.
	    HelloWorldGUI newContentPane = new HelloWorldGUI();
	    newContentPane.setOpaque(true); //content panes must be opaque
	    frame.setContentPane(newContentPane);
	    
	    Panel panel = new TestShowPic();
	    //panel.setSize(100, 100);
	    //frame.getContentPane().add(panel);
	    
	    // Display window:
        frame.setVisible(true);
	 }
}
