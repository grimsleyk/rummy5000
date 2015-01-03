package graphics;

import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

public class Table extends  WindowAdapter 
{
	public static JFrame table;
	public Table()
	{
		// Set up frame:
		table = new JFrame("Learnen GUI");
		table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		table.setSize(1500, 750);
	}
}
