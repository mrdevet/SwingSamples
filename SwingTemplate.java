// Import the GUI libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingTemplate {
    
    /////////////////////////////// MAIN METHOD ////////////////////////////////
    // This main method starts the GUI and runs the createMainWindow() method.
    // This method should not be changed.
    ////////////////////////////////////////////////////////////////////////////
    
    public static void main (String [] args) {
        javax.swing.SwingUtilities.invokeLater (new Runnable () {
            public void run () {
                createMainWindow ();
            }
        });
    }
    

    ////////////////////// STATIC VARIABLES AND CONSTANTS //////////////////////
    // Declare the objects and variables that you want to access across
    // multiple methods.
    ////////////////////////////////////////////////////////////////////////////



    
    //////////////////////////// CREATE MAIN WINDOW ////////////////////////////
    // Inside of the method below, you will set up the main window.  Add
    // components and/or layouts to the contentPane.
    ////////////////////////////////////////////////////////////////////////////
    
    /** This method is called by main() to set up the main GUI window. */
    private static void createMainWindow () {
        // Create and set up the window.
        JFrame frame = new JFrame ("Frame Title");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setResizable (false);

        // The panel that will hold the components in the frame.
        JPanel contentPane = new JPanel();

        // TODO: Add components to the content pane panel


        // Add the panel to the frame
        frame.setContentPane(contentPane);

        //size the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    ////////////////////////////// HELPER METHODS //////////////////////////////
    // Methods that you create to manage repetitive tasks.
    ////////////////////////////////////////////////////////////////////////////


    

    ///////////////////////////// EVENT LISTENERS //////////////////////////////
    // Subclasses that handle events (button clicks, mouse clicks and moves,
    // key presses, timer expirations)
    ////////////////////////////////////////////////////////////////////////////
    

}