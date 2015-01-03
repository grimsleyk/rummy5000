package test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class TestShowPic extends Panel
{
	BufferedImage image;
	
	public TestShowPic()
	{
		try
		{
			image = ImageIO.read(new File("/home/kgrims/Desktop/Haha.jpg"));
		}
		catch (IOException ioE)
		{
			
		}
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(image,  0, 0, 60, 120, this);	
	}
	
}
