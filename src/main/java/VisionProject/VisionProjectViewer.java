package VisionProject;

import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class VisionProjectViewer{
	
	String pictureFileName;
	
	
	public VisionProjectViewer() throws IOException
	{
		BufferedImage img = ImageIO.read(new File("D:\\Work\\EclipseWorkspace\\VisionProject\\Data\\Tej.jpg"));
		ImageIcon icon = new ImageIcon(img);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(img.getWidth(), img.getHeight());
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) throws IOException
	{
		@SuppressWarnings("unused")
		VisionProjectViewer viewer = new VisionProjectViewer();
		
	}
}
