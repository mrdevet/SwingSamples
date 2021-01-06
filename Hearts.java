// Import the GUI libraries
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// Import other libraries
import java.util.Arrays;
import java.util.Random;

public class Hearts {
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
	static JLayeredPane tablePanel;

	static Random generator = new Random();

	static String [] suits = {"C", "D", "S", "H"};
	static String [] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

	static ImageIcon [] cardImages = new ImageIcon [52];
	static String [] cardSuits = new String [52];
	static String [] cardValues = new String [52];
	static int [] cardPoints = new int [52];

	static ImageIcon cardBack;
	static RotatedIcon cardBackRotated;

	static final int NUM_PLAYERS = 4;
	static final int HAND_SIZE = 13;

	static String [] names = {"You", "Alice", "Bob", "Charlie"};
	static JLabel [] nameLabels = new JLabel [NUM_PLAYERS];
	static int [] scores = new int [NUM_PLAYERS];
	static JLabel [] scoreLabels = new JLabel [NUM_PLAYERS];

	static JLabel [][] hands = new JLabel [NUM_PLAYERS][HAND_SIZE]];
	static int [] handSizes = new int [NUM_PLAYERS];
	static int [] points = new int [NUM_PLAYERS];
	static boolean heartsAllowed = false;
	static int [] trick = new int [NUM_PLAYERS];


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
		JPanel contentPane = new JPanel ();
		contentPane.setPreferredSize(new Dimension(1000, 600));

		// Making the content pane use BorderLayout
		contentPane.setLayout(new BorderLayout());

		// Make the side panel
		JPanel sideBar = new JPanel();
		sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));
		sideBar.setPreferredSize(new Dimension(200, 300));
		sideBar.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.add(sideBar, BorderLayout.EAST);

		// Make the score labels
		for (int index = 0; index < NUM_PLAYERS; index++) {
			nameLabels[index] = new JLabel(names[index]);
			nameLabels[index].setAlignmentX(Component.CENTER_ALIGNMENT);
			sideBar.add(nameLabels[index]);
			sideBar.add(Box.createRigidArea(new Dimension(160, 5)));
			scoreLabels[index] = new JLabel(String.valueOf(scores[index]));
			scoreLabels[index].setAlignmentX(Component.CENTER_ALIGNMENT);
			sideBar.add(scoreLabels[index]);
			sideBar.add(Box.createRigidArea(new Dimension(160, 20)));
		}

		// Add the filler "glue"
		sideBar.add(Box.createVerticalGlue());

		// Make sidebar buttons
		JButton newGameButton = new JButton("New Game");
		newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGameButton.addActionListener(new NewGameButtonListener());
		sideBar.add(newGameButton);
		sideBar.add(Box.createRigidArea(new Dimension(160, 10)));

		JButton optionsButton = new JButton("Options");
		optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sideBar.add(optionsButton);
		sideBar.add(Box.createRigidArea(new Dimension(160, 10)));

		JButton quitButton = new JButton("Quit");
		quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitButton.addActionListener(new QuitButtonListener());
		sideBar.add(quitButton);

		// Make the center "table" panel
		tablePanel = new JLayeredPane();
		tablePanel.setBackground(new Color(7, 99, 36));
		tablePanel.setOpaque(true);
		contentPane.add(tablePanel, BorderLayout.CENTER);

		// Create the cards
		for (int sindex = 0; sindex < suits.length; sindex++) {
			for (int vindex = 0; vindex < values.length; vindex++) {
				int index = values.length * sindex + vindex;
				cardSuits[index] = suits[sindex];
				cardValues[index] = values[vindex];
				cardImages[index] = new ImageIcon("resources/cards/" + values[vindex] + suits[sindex] + "_small.png");
				cardImages[index].getImage().flush();
				if (values[vindex].equals("Q") && suits[sindex].equals("S")) {
					cardPoints[index] = 13;
				}
				else if (suits[sindex].equals("H")) {
					cardPoints[index] = 1;
				}
				else {
					cardPoints[index] = 0;
				}
			}
		}
		cardBack = new ImageIcon("resources/cards/red_back_mini.png");
		cardBack.getImage().flush();
		cardBackRotated = new RotatedIcon(cardBack, RotatedIcon.Rotate.DOWN);

		// Create the hand
		for (int pindex = 0; pindex < NUM_PLAYERS; pindex++) {
			for (int cindex = 0; cindex < HAND_SIZE; cindex++) {
				hands[pindex][cindex] = new JLabel();
				if (pindex == 0) {
					hands[pindex][cindex].setSize(100, 153);
				}
				else {
					hands[pindex][cindex].setSize(75, 115);
				}
				tablePanel.add(hand[pindex][cindex]);
			}
		}

		newGame();
		drawHand();

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

	/** Randomly shuffle the order of the cards */
 	public static void shuffle () {
 		for (int index = 0; index < 52; index++) {
 			int randomIndex = generator.nextInt(52);
 			ImageIcon tempImage = cardImages[randomIndex];
 			cardImages[randomIndex] = cardImages[index];
 			cardImages[index] = tempImage;
			String tempSuit = cardSuits[randomIndex];
 			cardSuits[randomIndex] = cardSuits[index];
 			cardSuits[index] = tempSuit;
			String tempValue = cardValues[randomIndex];
 			cardValues[randomIndex] = cardValues[index];
 			cardValues[index] = tempValue;
			int tempPoints = cardPoints[randomIndex];
			cardPoints[randomIndex] = cardPoints[index];
 			cardPoints[index] = tempPoints;
 		}
 	}


	public static void drawHand (int playerIndex) {
		for (int index = 0; index < handSizes[playerIndex]; index++) {
			if (playerIndex == 0) {
				// cardImages[4 * index + playerIndex].getImage().flush();
				hands[playerIndex][index].setIcon(cardImages[4 * index]);
				hands[playerIndex][index].setLocation(360 - 12 * handSizes[playerIndex] + 24 * index, 420);
			}
			else if (playerIndex == 1) {
				hands[playerIndex][index].setIcon(cardBack);
				hands[playerIndex][index].setLocation(-35, 250 - 9 * handSizes[playerIndex]);
			}
			hands[playerIndex][index].setVisible(true);
			tablePanel.add(hand[index]);
			tablePanel.moveToFront(hand[index]);
		}
		for (int index = handSizes[playerIndex]; index < HAND_SIZE; index++) {
			hand[index].setVisible(false);
		}

		// Repaint and sync the frame for smooth animation
		frame.repaint();
		Toolkit.getDefaultToolkit().sync();
	}


	public static void drawHands () {
		for (int pindex = 0; pindex < NUM_PLAYERS; pindex++) {
			drawHand(pindex);
		}
	}


	public static void newHand () {
		shuffle();
		Arrays.fill(handSizes, HAND_SIZE);
		Arrays.fill(points, 0);
		heartsAllowed = false;
		Arrays.fill(trick, -1);
		drawHands();
	}


	public static void newGame () {
		Arrays.fill(scores, 0);
		for (int index = 0; index < NUM_PLAYERS; index++) {
			scoreLabels[index].setText("0");
		}
		newHand();
	}


    /**
     * EVENT LISTENERS
     * Subclasses that handle events (button clicks, mouse clicks and moves,
     * key presses, timer expirations)
     */

	/** Handles clicks on the quit button. */
	private static class QuitButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			int answer = JOptionPane.showConfirmDialog(null, "Are you sure your want to quit?",
					"Quit?", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	/** Handles clicks on the new game button. */
	private static class NewGameButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			int answer = JOptionPane.showConfirmDialog(null,
					"Are you sure your want to start a new game?", "New Game?",
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				newGame();
			}
		}
	}
}
