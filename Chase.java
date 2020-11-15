import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Chase {
	/**
	 * MAIN METHOD
	 * This main method starts the GUI and runs the createMainWindow() method.
     * This method should not be changed.
	 */
	public static void main (String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createMainWindow();
			}
		});
	}


	/**
	 * STATIC VARIABLES AND CONSTANTS
	 * Declare the objects and variables that you want to access across
     * multiple methods.
	 */

	// Holds the frame and its size
	static JFrame frame;
	static final int PANEL_WIDTH = 800;
	static final int PANEL_HEIGHT = 600;

	// Will hold pictures of mario and a goomba
	static JLabel mario;
	static JLabel goomba;

	// The animation timer
	static Timer movementTimer;

	// How far to move the characters for each timer event
	final static int UPDATE_DELAY = 10;
	final static int MARIO_MOVE_DISTANCE = 1;
	final static int GOOMBA_MOVE_DISTANCE = 1;
	static int deltaX = 0;
	static int deltaY = 0;


	/**
	 * CREATE MAIN WINDOW
     * This method is called by the main method to set up the main GUI window.
	 */
	public static void createMainWindow () {
		// Create and set up the window.
		frame = new JFrame("Mouse Location");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a panel to hold the components
		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

		// Add mario
		mario = SwingHelpers.createScaledImage("resources/mario.png", 48, 64);
		mario.setSize(48, 64);
		mario.setLocation(576, 268);
		panel.add(mario);

		// Add the goomba
		goomba = SwingHelpers.createScaledImage("resources/goomba.png", 32, 32);
		goomba.setSize(32, 32);
		goomba.setLocation(184, 284);
		panel.add(goomba);

		// Create the timer that moves the characters
		movementTimer = new Timer(UPDATE_DELAY, new MovementTimerListener());
		movementTimer.setRepeats(true);
		movementTimer.start();

		// Add listener for key presses
		KeyTypedHandler handler = new KeyTypedHandler();
		frame.addKeyListener(handler);

		// Add the panel to the frame
		frame.setContentPane(panel);

		// Show the frame
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
	}


	/**
     * HELPER METHODS
     * Methods that you create to manage repetitive tasks.
     */

	/**
	 * Moves mario by the stored deltas.
	 */
	public static void moveMario () {
		// Get the new x-value and restrict its value to be in the frame
		int newX = mario.getX() + deltaX;
		if (newX < 0) {
			newX = 0;
		}
		else if (newX >= PANEL_WIDTH - 48) {
			newX = PANEL_WIDTH - 48;
		}

		// Get the new y-value and restrict its value to be in the frame
		int newY = mario.getY() + deltaY;
		if (newY < 0) {
			newY = 0;
		}
		else if (newY >= PANEL_HEIGHT - 64) {
			newY = PANEL_HEIGHT - 64;
		}

		// Move mario
		mario.setLocation(newX, newY);
	}

	/** Moves the goomba to follow mario. */
	public static void moveGoomba () {
		// Get the charcter locations
		int marioX = mario.getX();
		int marioY = mario.getY();
		int goombaX = goomba.getX();
		int goombaY = goomba.getY();

		// Get the distances between the characters
		int distanceX = marioX - goombaX;
		int distanceY = marioY - goombaY;

		// If are farther horizontally, move horizontally
		if (Math.abs(distanceX) > Math.abs(distanceY)) {
			// If mario is to the right, move right
			if (distanceX > 0) {
				goomba.setLocation(goombaX + GOOMBA_MOVE_DISTANCE, goombaY);
			}

			// If mario is to the left, move left
			else {
				goomba.setLocation(goombaX - GOOMBA_MOVE_DISTANCE, goombaY);
			}
		}

		// If are farther vertically, move vertically
		else {
			// If mario is below, move down
			if (distanceY > 0) {
				goomba.setLocation(goombaX, goombaY + GOOMBA_MOVE_DISTANCE);
			}

			// If mario is above, move up
			else {
				goomba.setLocation(goombaX, goombaY - GOOMBA_MOVE_DISTANCE);
			}
		}
	}


	/**
     * EVENT LISTENERS
     * Subclasses that handle events (button clicks, mouse clicks and moves,
     * key presses, timer expirations)
     */

	/** Handles when an arrow key is pressed or released. */
	public static class KeyTypedHandler implements KeyListener {
		/** This method does nothing. */
		public void keyTyped (KeyEvent e) {}

		/** When a key is pressed, change the deltas. */
		public void keyPressed (KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				deltaX = -MARIO_MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				deltaX = MARIO_MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				deltaY = -MARIO_MOVE_DISTANCE;
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				deltaY = MARIO_MOVE_DISTANCE;
			}
		}

		/** When a key is released, unchange the deltas. */
		public void keyReleased (KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				deltaX = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				deltaX = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				deltaY = 0;
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				deltaY = 0;
			}
		}
	}

	/** Handles the character movement when keys are down. */
	private static class MovementTimerListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			// Check if the goomba is touching mario
			if (SwingHelpers.isCollision(mario, goomba)) {
				// Stop the animation timer
				movementTimer.stop();

				// Ask to play again
				int answer = JOptionPane.showConfirmDialog(null,
						"Do you want to play again?", "New Game?",
						JOptionPane.YES_NO_OPTION);

				// If they say yes, reset
				if (answer == JOptionPane.YES_OPTION) {
					// Reset the settings and restart the animation timer
					deltaX = 0;
					deltaY = 0;
					mario.setLocation(576, 268);
					goomba.setLocation(184, 284);
					movementTimer.restart();
				}

				// If they say no, end the program
				else {
					System.exit(0);
				}
			}

			// No collision, so move the characters
			else {
				// Move the characters
				moveMario();
				moveGoomba();

				// Repaint and sync the frame for smooth animation
				frame.repaint();
				Toolkit.getDefaultToolkit().sync();
			}
		}
	}
}
