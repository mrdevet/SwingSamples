// Import the GUI libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfiniteScroller {
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

	// Stores the frame and its size.
	static JFrame frame;
	static final int MAX_X = 1280;
	static final int MAX_Y = 720;

	// Store the background images and their current position.
	static JLabel leftBackground;
	static JLabel rightBackground;
	static int backgroundOffset;

	// The penguin and the penguins default position
	static JLabel penguin;
	static final int PENGUIN_X = 604;
	static final int PENGUIN_Y = 528;

	// The penguin images that could be put into the penguin label
	static ImageIcon [] images;
	static ImageIcon [] imagesFlipped;

	// Stores the state and direction of the penguin
	static boolean isWalking = false;
	static boolean isFacingRight = true;

	// Stores which of the arrow keys are currently down
	static boolean leftDown = false;
	static boolean rightDown = false;

	// The frame delay
	static final int UPDATE_DELAY = 10;

	// The frame counter and a maximum for the count for each state.
	static int count = 0;
	static final int MAX_COUNT = 80;


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
		contentPane.setPreferredSize(new Dimension(MAX_X, MAX_Y));

        // Add the backgrounds
		leftBackground = SwingHelpers.createScaledImage("resources/backgrounds/snow/snow.png", MAX_X, MAX_Y);
		rightBackground = SwingHelpers.createScaledImage("resources/backgrounds/snow/snow.png", MAX_X, MAX_Y);
		leftBackground.setSize(MAX_X, MAX_Y);
		rightBackground.setSize(MAX_X, MAX_Y);
		rightBackground.setLocation(MAX_X, 0);
		contentPane.add(leftBackground);
		contentPane.add(rightBackground);
		contentPane.setLayer(leftBackground, -100);
		contentPane.setLayer(rightBackground, -100);
		backgroundOffset = 0;

		// Add the walking penguin images
		images = new ImageIcon [4];
		imagesFlipped = new ImageIcon[4];
		for (int index = 0; index < 4; index++) {
			images[index] = new ImageIcon("resources/penguins/penguin_walk0" + (index + 1) + ".png");
			imagesFlipped[index] = new ImageIcon("resources/penguins/penguin_walk0" + (index + 1) + "_flip.png");
		}

		// Add the penguin label
		penguin = new JLabel(images[0]);
		penguin.setSize(72, 64);
		penguin.setLocation(PENGUIN_X, PENGUIN_Y);
		contentPane.add(penguin);

		// Start the update timer
		Timer updateTimer = new Timer(UPDATE_DELAY, new UpdateTimerHandler());
		updateTimer.start();

		// Add the key listener
		frame.addKeyListener(new KeyHandler());

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

	/** Timer listener that handles animations. */
	private static class UpdateTimerHandler implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			// The penguin is walking
			if (isWalking) {
				// Walk to the right
				if (isFacingRight) {
					backgroundOffset++;
					if (backgroundOffset >= MAX_X) {
						backgroundOffset -= MAX_X;
					}
					leftBackground.setLocation(-backgroundOffset, 0);
					rightBackground.setLocation(MAX_X - backgroundOffset, 0);
					penguin.setIcon(images[count / 20]);
				}

				// Walk to the left
				else {
					backgroundOffset--;
					if (backgroundOffset < 0) {
						backgroundOffset += MAX_X;
					}
					leftBackground.setLocation(-backgroundOffset, 0);
					rightBackground.setLocation(MAX_X - backgroundOffset, 0);
					penguin.setIcon(imagesFlipped[count / 20]);
				}

				// Increase the counter
				count++;

				// If the count reaches the max, go back to 0
				if (count == MAX_COUNT) {
					count = 0;
				}
			}

			// The penguin is standing
			else {
				// Show the standing image
				if (isFacingRight) {
					penguin.setIcon(images[0]);
				}
				else {
					penguin.setIcon(imagesFlipped[0]);
				}
			}

			// Repaint and sync the frame for smooth animation
			frame.repaint();
			Toolkit.getDefaultToolkit().sync();
		}
	}


	/** Key listener that detects changes in the arrow keys. */
    private static class KeyHandler implements KeyListener {
        public void keyTyped (KeyEvent event) {
        }

        public void keyPressed (KeyEvent event) {
			int code = event.getKeyCode();

			// The left arrow is pressed
			if (code == KeyEvent.VK_LEFT && !leftDown) {
				leftDown = true;
				isWalking = true;
				isFacingRight = false;
				count = 0;
			}

			// The right arrow is pressed
			else if (code == KeyEvent.VK_RIGHT && !rightDown) {
				rightDown = true;
				isWalking = true;
				isFacingRight = true;
				count = 0;
			}
        }

        public void keyReleased (KeyEvent event) {
			int code = event.getKeyCode();

			// The left arrow is released
			if (code == KeyEvent.VK_LEFT) {
				leftDown = false;

				// If the right arrow is still down, stay walking right
				if (rightDown) {
					isFacingRight = true;
				}

				// Otherwise stop walking
				else {
					isWalking = false;
				}
			}

			// The right arrow is released
			else if (code == KeyEvent.VK_RIGHT) {
				rightDown = false;

				// If the left arrow is still down, stay walking left
				if (leftDown) {
					isFacingRight = false;
				}

				// Otherwise stop walking
				else {
					isWalking = false;
				}
			}
        }
    }
}
