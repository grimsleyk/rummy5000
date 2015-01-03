package test;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HelloWorldGUI extends JPanel implements ActionListener
{
	BufferedImage image;
	JButton b1;
	
	public HelloWorldGUI()
	{
		/*JLabel helloWorldLabel = new JLabel ("Hello World!!!");
		add(helloWorldLabel);
		this.setSize(1000, 700);
		
		
		JButton b1 = new JButton("First Button");
		b1.setVerticalAlignment(AbstractButton.CENTER);
		add(b1);
		
		*/
		//addWindowListener(windowClosing());
		
		//setVisible(true);
				
		b1 = new JButton("Ma First button, bitches!!!");
		b1.setVerticalAlignment(AbstractButton.CENTER);
		b1.setActionCommand("Haha");
		
        b1.addActionListener(this);
		
		//add(b1);
        
        Panel panel = new TestShowPic();
        add(panel);

	}

	
	public void actionPerformed(ActionEvent e) 
	{
				
		try
		{
			System.out.print(".......");
			image = ImageIO.read(new File("/home/kgrims/Desktop/Haha.jpg"));
			System.out.print(".......");
			JLabel imgLabel = new JLabel(new ImageIcon(image));
			//add(imgLabel);
		}
		catch (IOException ioE)
		{
			System.err.println("ON NO");
		}
	}
	
	 @Override
	 public void paintComponent(Graphics g) 
	 {
	     g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters  
		 super.paintComponent(g);
	 }
	
	
}


