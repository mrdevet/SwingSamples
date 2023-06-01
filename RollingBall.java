// Import the GUI libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RollingBall {
	/**
	 * MAIN METHOD
	 * This main method starts the GUI and runs the createMainWindow() method.
     * This method should not be changed.
	 */
	public static void main (String [] args) {
		javax.swing.SwingUtilities.invokeLater (new Runnable () {
			public void run () {
				createMainWindow ();
			}
		});
	}


	/**
	 * STATIC VARIABLES AND CONSTANTS
	 * Declare the objects and variables that you want to access across
     * multiple methods.
	 */
	
	static JFrame frame;

	static JLabel ball;

	static final int NUMBER_OF_BALLS = 6;
	static ImageIcon [] ballImages;
	static int ballIndex;

	static int ballX;
	static int ballY;

	static final int MAX_BALL_X = 750;
	static final int MAX_BALL_Y = 550;

	static final int DELAY = 10;  // updates every 10 ms (100 frames/second)
	static final int MOVE_SPEED = 2;  // in pixels per frame
	static int moveX = MOVE_SPEED;
	static int moveY = MOVE_SPEED;


	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	private static void createMainWindow () {
		// Create and set up the window.
		frame = new JFrame ("Frame Title");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.setResizable (false);

		// The panel that will hold the components in the frame.
		JLayeredPane contentPane = new JLayeredPane ();
		contentPane.setPreferredSize(new Dimension(800, 600));

        // Load the ball images for the animation.
		ballImages = new ImageIcon [NUMBER_OF_BALLS];
		for (int index = 0; index < NUMBER_OF_BALLS; index++) {
			Image image = new ImageIcon("resources/sball" + (5 - index) + ".png").getImage();
			Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			ballImages[index] = new ImageIcon(scaledImage);
		}
		ballIndex = 0;
		ball = new JLabel(ballImages[0]);
		ball.setSize(50, 50);
		ball.setLocation(MAX_BALL_X / 2, MAX_BALL_Y / 2);
		contentPane.add(ball);

		// Create animation timer
		Timer animateTimer = new Timer(DELAY, new AnimationTimerHandler());
		animateTimer.start();

		// Add the panel to the frame
		frame.setContentPane(contentPane);

		//size the window.
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


    /**
     * HELPER METHODS
     * Methods that you create to manage repetitive tasks.
     */



    /**
     * EVENT LISTENERS
     * Subclasses that handle events (button clicks, mouse clicks and moves,
     * key presses, timer expirations)
     */

	/**  */
	private static class AnimationTimerHandler implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			// If the new x is too small or too big switch directions
			int newX = ballX + moveX;
			if (newX < 0 || newX >= MAX_BALL_X) {
				moveX = -moveX;
				newX = ballX + moveX;
			}

			// If the new x is too small or too big switch directions
			int newY = ballY + moveY;
			if (newY < 0 || newY >= MAX_BALL_Y) {
				moveY = -moveY;
				newY = ballY + moveY;
			}

			// Move the ball
			ballX = newX;
			ballY = newY;
			ball.setLocation(ballX, ballY);

			// Change the ball image
			ballIndex = ballX / 10 % 6;
			ball.setIcon(ballImages[ballIndex]);
			
			// Repaint the frame (handles any stuttering)
			frame.repaint();
			frame.getToolkit().sync();
		}
	}

}
