// Import the GUI libraries 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Pirates {
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
	// Score and its label
	static int score = 0;
	static JLabel scoreLabel;

	// Pirate ship image
	static JLabel pirateShip;

	// Generates random values for the ship's location
	static Random randomGenerator = new Random();

	// The speed of the ship.  This is the number of milliseconds between ship moves
	static final int INITIAL_SPEED = 2000;
	static final int SPEED_CHANGE = 50;
	static int speed = INITIAL_SPEED;
	static Timer shipMoveTimer;

	// Music toggle button
	static JButton musicButton;

	// Object for the background music
	static MP3Player music;


	/**
	 * CREATE MAIN WINDOW
	 * This method is called by the main method to set up the main GUI window.
	 */
	private static void createMainWindow () {
		// Create and set up the window.
		JFrame frame = new JFrame ("Pirates");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.setResizable (false);

		// The panel that will hold the components in the frame.
		JPanel contentPane = new JPanel ();
		contentPane.setPreferredSize(new Dimension(950, 400));

		// Making the content pane use BorderLayout
		contentPane.setLayout(new BorderLayout());

		// Make the side panel
		JPanel sideBar = new JPanel();
		sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));
		sideBar.setPreferredSize(new Dimension(175, 300));
		sideBar.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.add(sideBar, BorderLayout.EAST);

		// Make the score title
		JLabel scoreTitle = new JLabel("Score");
		scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		sideBar.add(scoreTitle);
		sideBar.add(Box.createRigidArea(new Dimension(135, 10)));

		// Make the score label
		scoreLabel = new JLabel("0");
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sideBar.add(scoreLabel);

		// Add the filler "glue"
		sideBar.add(Box.createVerticalGlue());

		// Make sidebar title
		JLabel actionsLabel = new JLabel("Actions");
		actionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		actionsLabel.setHorizontalAlignment(JLabel.CENTER);
		actionsLabel.setVerticalAlignment(JLabel.CENTER);
		sideBar.add(actionsLabel);
		sideBar.add(Box.createRigidArea(new Dimension(135, 10)));

		// Make sidebar buttons
		JButton newGameButton = new JButton("New Game");
		newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGameButton.addActionListener(new NewGameButtonListener());
		sideBar.add(newGameButton);
		sideBar.add(Box.createRigidArea(new Dimension(135, 10)));

		musicButton = new JButton("Music Off");
		musicButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		musicButton.addActionListener(new MusicButtonListener());
		sideBar.add(musicButton);
		sideBar.add(Box.createRigidArea(new Dimension(135, 10)));

		JButton quitButton = new JButton("Quit");
		quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitButton.addActionListener(new QuitButtonListener());
		sideBar.add(quitButton);

		// Make the center "map" panel
		JLayeredPane mapPanel = new JLayeredPane();
		contentPane.add(mapPanel, BorderLayout.CENTER);

		// Get the map image
		JLabel mapImage = new JLabel(new ImageIcon("resources/world-map-animals.jpg"));
		mapPanel.add(mapImage, Integer.valueOf(-300));
		mapImage.setSize(775, 400);
		mapImage.setLocation(0, 0);

		// Create the pirate ship
		pirateShip = SwingHelpers.createScaledImage("resources/pirate-ship.png", 40, 40);
		pirateShip.setSize(40, 40);
		pirateShip.addMouseListener(new ShipClickListener());
		mapPanel.add(pirateShip);
		changePirateShipLocation();

		// Create the timer that moves the ship
		shipMoveTimer = new Timer(speed, new ShipTimerListener());
		shipMoveTimer.setRepeats(true);
		shipMoveTimer.start();

		// Start the music
		music = new MP3Player("resources/the-buccaneers-haul.mp3");
		music.setRepeats(true);
		music.play();

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

	/** Randomly change the ship's location. */
	private static void changePirateShipLocation () {
		int pirateX = randomGenerator.nextInt(735);
		int pirateY = randomGenerator.nextInt(360);
		pirateShip.setLocation(pirateX, pirateY);
		pirateShip.setVisible(true);
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
				score = 0;
				scoreLabel.setText("0");
				speed = INITIAL_SPEED;
				shipMoveTimer.setDelay(speed);
			}
		}
	}

	/** Handles clicks on the music button. */
	private static class MusicButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			if (music.isPlaying()) {
				music.stop();
				musicButton.setText("Music On");
			}
			else {
				music.play();
				musicButton.setText("Music Off");
			}
		}
	}

	/** Handles clicks on the ship image. */
	private static class ShipClickListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			score++;
			scoreLabel.setText(String.valueOf(score));
			speed -= SPEED_CHANGE;
			shipMoveTimer.setDelay(speed);
			pirateShip.setVisible(false);
		}
	}

	/** Handles timer expiration on ship move timer. */
	private static class ShipTimerListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			changePirateShipLocation();
		}
	}
}
