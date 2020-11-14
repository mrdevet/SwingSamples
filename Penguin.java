// Import the GUI libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Penguin {
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

	// Enumerated type that used to track the state the penguin is in.
	private enum State {
		STANDING,
		WALKING,
		SLIDING,
		JUMPING
	}

	// Enumerated type used to store directions, both for movement and facing
	private enum Direction {
		NONE,
		RIGHT,
		LEFT
	}

	// Stores the frame and its size.
	static JFrame frame;
	static final int MAX_X = 1280;
	static final int MAX_Y = 720;

	// Store the background images, their current position and the speeds that they move.
	static final int NUM_BACKGROUNDS = 8;
	static JLabel [] leftBackgrounds;
	static JLabel [] rightBackgrounds;
	static int [] backgroundOffsets;
	static int [] backgroundSpeeds = {1, 4, 5, 10, 5, 10, 0, 0};

	// The penguin and the penguins default position
	static JLabel penguin;
	static final int PENGUIN_X = 604;
	static final int PENGUIN_Y = 528;

	// The penguin images that could be put into the penguin label
	static ImageIcon standingImage;
	static ImageIcon standingImageFlipped;
	static ImageIcon [] walkingImages;
	static ImageIcon [] walkingImagesFlipped;
	static ImageIcon slidingImage;
	static ImageIcon slidingImageFlipped;
	static ImageIcon [] jumpingImages;
	static ImageIcon [] jumpingImagesFlipped;

	// Stores the state and direction of the penguin
	static State state = State.STANDING;
	static Direction faceDirection = Direction.RIGHT;
	static Direction moveDirection = Direction.NONE;

	// Stores which of the arrow keys are currently down
	static boolean leftDown = false;
	static boolean rightDown = false;
	static boolean upDown = false;
	static boolean downDown = false;

	// The frame delay
	static final int UPDATE_DELAY = 10;

	// The frame counter and a maximum for the count for each state.
	static int count = 0;
	static final int STANDING_MAX_COUNT = 20;
	static final int WALKING_MAX_COUNT = 80;
	static final int SLIDING_MAX_COUNT = 200;
	static final int JUMPING_MAX_COUNT = 200;


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
		leftBackgrounds = new JLabel [NUM_BACKGROUNDS];
		rightBackgrounds = new JLabel [NUM_BACKGROUNDS];
		for (int index = 0; index < NUM_BACKGROUNDS; index++) {
			leftBackgrounds[index] = SwingHelpers.createScaledImage("resources/backgrounds/snow/layers/l" + (8 - index) + ".png", MAX_X, MAX_Y);
			rightBackgrounds[index] = SwingHelpers.createScaledImage("resources/backgrounds/snow/layers/l" + (8 - index) + ".png", MAX_X, MAX_Y);
			leftBackgrounds[index].setSize(MAX_X, MAX_Y);
			rightBackgrounds[index].setSize(MAX_X, MAX_Y);
			rightBackgrounds[index].setLocation(MAX_X, 0);
			contentPane.add(leftBackgrounds[index]);
			contentPane.add(rightBackgrounds[index]);
			contentPane.setLayer(leftBackgrounds[index], -100 - index);
			contentPane.setLayer(rightBackgrounds[index], -100 - index);
		}
		backgroundOffsets = new int [NUM_BACKGROUNDS];

		// Add the standing penguin images
		standingImage = new ImageIcon("resources/penguins/penguin_walk01.png");
		standingImageFlipped = new ImageIcon("resources/penguins/penguin_walk01_flip.png");

		// Add the walking penguin images
		walkingImages = new ImageIcon [4];
		walkingImagesFlipped = new ImageIcon[4];
		for (int index = 0; index < 3; index++) {
			walkingImages[index] = new ImageIcon("resources/penguins/penguin_walk0" + (index + 2) + ".png");
			walkingImagesFlipped[index] = new ImageIcon("resources/penguins/penguin_walk0" + (index + 2) + "_flip.png");
		}
		walkingImages[3] = new ImageIcon("resources/penguins/penguin_walk01.png");
		walkingImagesFlipped[3] = new ImageIcon("resources/penguins/penguin_walk01_flip.png");

		// Add the sliding penguin images
		slidingImage = new ImageIcon("resources/penguins/penguin_slide02.png");
		slidingImageFlipped = new ImageIcon("resources/penguins/penguin_slide02_flip.png");

		// Add the jumping penguin images
		jumpingImages = new ImageIcon [3];
		jumpingImagesFlipped = new ImageIcon[3];
		for (int index = 0; index < 3; index++) {
			jumpingImages[index] = new ImageIcon("resources/penguins/penguin_jump0" + (index + 1) + ".png");
			jumpingImagesFlipped[index] = new ImageIcon("resources/penguins/penguin_jump0" + (index + 1) + "_flip.png");
		}

		// Add the penguin label
		penguin = new JLabel(standingImage);
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
			// Move the backgrounds
			for (int index = 0; index < 6; index++) {
				// Only move is the count is division by the background speed
				if (count % backgroundSpeeds[index] == 0) {
					// Move right
					if (moveDirection == Direction.RIGHT) {
						backgroundOffsets[index]++;
						if (backgroundOffsets[index] >= MAX_X) {
							backgroundOffsets[index] -= MAX_X;
						}
						leftBackgrounds[index].setLocation(-backgroundOffsets[index], 0);
						rightBackgrounds[index].setLocation(MAX_X - backgroundOffsets[index], 0);
					}

					// Move left
					else if (moveDirection == Direction.LEFT) {
						backgroundOffsets[index]--;
						if (backgroundOffsets[index] < 0) {
							backgroundOffsets[index] += MAX_X;
						}
						leftBackgrounds[index].setLocation(-backgroundOffsets[index], 0);
						rightBackgrounds[index].setLocation(MAX_X - backgroundOffsets[index], 0);
					}
				}
			}

			// The penguin is jumping
			if (state == State.JUMPING) {
				// Set the height of the penguin
				int y = (int) (0.0128 * count * (count - JUMPING_MAX_COUNT) + PENGUIN_Y);
				penguin.setLocation(PENGUIN_X, y);

				// If the down key is pressed, show the sliding image
				if (downDown) {
					if (faceDirection == Direction.RIGHT) {
						penguin.setIcon(slidingImage);
					}
					else {
						penguin.setIcon(slidingImageFlipped);
					}
				}

				// Othersize, show one of the jumping images
				else {
					// Show different image for first 10% and last 10% of jump
					int imageIndex = 1;
					if (count < 0.1 * JUMPING_MAX_COUNT) {
						imageIndex = 0;
					}
					else if (count >= 0.9 * JUMPING_MAX_COUNT) {
						imageIndex = 2;
					}

					if (faceDirection == Direction.RIGHT) {
						penguin.setIcon(jumpingImages[imageIndex]);
					}
					else {
						penguin.setIcon(jumpingImagesFlipped[imageIndex]);
					}
				}

				// Increase the counter
				count++;

				// If the counter reaches the max, change to an appropriate state
				if (count == JUMPING_MAX_COUNT) {
					if (downDown) {
						state = State.SLIDING;
					}
					else if (leftDown || rightDown) {
						state = State.WALKING;
						moveDirection = faceDirection;
					}
					else {
						state = State.STANDING;
						moveDirection = Direction.NONE;
					}
					count = 0;
				}
			}

			// The penguin is sliding
			else if (state == State.SLIDING) {
				// Show the sliding image
				if (faceDirection == Direction.RIGHT) {
					penguin.setIcon(slidingImage);
				}
				else {
					penguin.setIcon(slidingImageFlipped);
				}

				// Increase the counter
				count++;

				// If the count reaches the max, stop moving.
				if (count == SLIDING_MAX_COUNT) {
					moveDirection = Direction.NONE;
					count = 0;
				}
			}

			// The penguin is walking
			else if (state == State.WALKING) {
				// Show one of the walking images
				if (faceDirection == Direction.RIGHT) {
					penguin.setIcon(walkingImages[count / 20]);
				}
				else {
					penguin.setIcon(walkingImagesFlipped[count / 20]);
				}

				// Increase the counter
				count++;

				// If the count reaches the max, go back to 0
				if (count == WALKING_MAX_COUNT) {
					count = 0;
				}
			}

			// The penguin is standing
			else if (state == State.STANDING) {
				// Show the standing image
				if (faceDirection == Direction.RIGHT) {
					penguin.setIcon(standingImage);
				}
				else {
					penguin.setIcon(standingImageFlipped);
				}

				// Increase the counter
				count++;

				// If the count reaches the max, go back to 0
				if (count == STANDING_MAX_COUNT) {
					count = 0;
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

				// Face left
				faceDirection = Direction.LEFT;

				// If appropriate, change state to walking
				if (state == State.STANDING || state == State.WALKING) {
					state = State.WALKING;
					moveDirection = Direction.LEFT;
					count = 0;
				}
			}

			// The right arrow is pressed
			else if (code == KeyEvent.VK_RIGHT && !rightDown) {
				rightDown = true;

				// Face right
				faceDirection = Direction.RIGHT;

				// If appropriate, change state to walking
				if (state == State.STANDING || state == State.WALKING) {
					state = State.WALKING;
					moveDirection = Direction.RIGHT;
					count = 0;
				}
			}

			// The up arrow is pressed
			else if (code == KeyEvent.VK_UP && !upDown) {
				upDown = true;

				// If not sliding or already jumping, change state to jumping
				if (state != State.JUMPING && !downDown) {
					state = State.JUMPING;
					count = 0;
				}
			}

			// The down arrow is pressed
			else if (code == KeyEvent.VK_DOWN && !downDown) {
				downDown = true;

				// If not jumping, change state the sliding
				if (state != State.JUMPING) {
					state = State.SLIDING;
					count = 0;
				}
			}
        }

        public void keyReleased (KeyEvent event) {
			int code = event.getKeyCode();

			// The left arrow is released
			if (code == KeyEvent.VK_LEFT) {
				leftDown = false;

				// If the right arrow is still down, change facing direction
				if (rightDown) {
					faceDirection = Direction.RIGHT;
				}
			}

			// The right arrow is released
			else if (code == KeyEvent.VK_RIGHT) {
				rightDown = false;

				// If the left arrow is still down, change facing direction
				if (leftDown) {
					faceDirection = Direction.LEFT;
				}
			}

			// The up arrow is released
			else if (code == KeyEvent.VK_UP) {
				upDown = false;
			}

			// The down arrow is released
			else if (code == KeyEvent.VK_DOWN) {
				downDown = false;
			}

			// If the penguin is jumping, don't change the state
			if (state == State.JUMPING) {
			}

			// If the down arrow is still pressed, change state to sliding
			else if (downDown) {
				if (state != state.SLIDING) {
					state = State.SLIDING;
					count = 0;
				}
			}

			// If the left or right arrows are still pressed, change state to walking
			else if (leftDown || rightDown) {
				state = State.WALKING;
				moveDirection = faceDirection;
				count = 0;
			}

			// If no arrows are still pressed, change state to standing
			else {
				state = State.STANDING;
				moveDirection = Direction.NONE;
				count = 0;
			}
        }
    }
}
