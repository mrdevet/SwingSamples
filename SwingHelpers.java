
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class SwingHelpers {

	/** Creates an image label scaled to the given size. */
	public static JLabel createScaledImage (String filename, int width, int height) {
		Image originalImage = new ImageIcon(filename).getImage();
		Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(scaledImage));
	}


	/** Checks to see if two components collide. */
	public static boolean isCollision (JComponent comp1, JComponent comp2) {
		return comp1.getX() + comp1.getWidth() > comp2.getX() &&
				comp1.getX() < comp2.getX() + comp2.getWidth() &&
				comp1.getY() + comp1.getHeight() > comp2.getY() &&
				comp1.getY() < comp2.getY() + comp2.getHeight();
	}


	/** Randomly shuffle the order of elements in an array of objects. */
	public static void shuffle (Object [] array) {
		Random rand = new Random();
		for (int index = 0; index < array.length; index++) {
			int randomIndex = rand.nextInt(array.length);
			Object temp = array[randomIndex];
			array[randomIndex] = array[index];
			array[index] = temp;
		}
	}

	/** Randomly shuffle the order of elements in an array of integers. */
	public static void shuffle (int [] array) {
		Random rand = new Random();
		for (int index = 0; index < array.length; index++) {
			int randomIndex = rand.nextInt(array.length);
			int temp = array[randomIndex];
			array[randomIndex] = array[index];
			array[index] = temp;
		}
	}
}
