package falstad;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import falstad.Constants.*;

/**
 * Class implements a wrapper for the user input handled by the Maze class. 
 * The MazeApplication attaches the listener to the GUI, such that user keyboard input
 * flows from GUI to the listener.keyPressed to the MazeController.keyDown method.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class SimpleKeyListener implements KeyListener {

	private Container parent;
	private MazeController controller;

	private ManualDriver driver;
	
	SimpleKeyListener(Container parent, MazeController controller){
		this.parent = parent ;
		this.controller = controller ;
        this.driver = new ManualDriver();
	}
	/**
	 * Translates keyboard input to the corresponding characters for the Maze.keyDown method.
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
        KeyPress keyDown = KeyPress.ESC;

		int key = arg0.getKeyChar() ;
		int code = arg0.getKeyCode() ;

		if (KeyEvent.CHAR_UNDEFINED == key)
		{
			if ((KeyEvent.VK_0 <= code && code <= KeyEvent.VK_9)||(KeyEvent.VK_A <= code && code <= KeyEvent.VK_Z))
				key = code ;
			if (KeyEvent.VK_ESCAPE == code)
				key = Constants.ESCAPE ; // use internal encoding for escape signal
			if (KeyEvent.VK_UP == code)
				key = 'k' ; // reduce duplicate encodings
			if (KeyEvent.VK_DOWN == code)
				key = 'j' ; // reduce duplicate encodings
			if (KeyEvent.VK_LEFT == code)
				key = 'h' ; // reduce duplicate encodings
			if (KeyEvent.VK_RIGHT == code)
				key = 'l' ; // reduce duplicate encodings
		}

        switch(key) {
            case 'k':
                driver.move();
                parent.repaint();
                return;
            case 'j':
                return;
            case 'h':
                driver.rotate(Robot.Turn.LEFT);
                parent.repaint();
                return;
            case 'l':
                driver.rotate(Robot.Turn.RIGHT);
                parent.repaint();
                return;
            case Constants.ESCAPE: case 65385:
                keyDown = KeyPress.ESC;
                break;
            case ('w' & 0x1f):
                keyDown = KeyPress.WALL_PHASE;
                break;
            case '\t': case 'm':
                keyDown = KeyPress.FULL_MAZE;
                break;
            case 'z':
                keyDown = KeyPress.SHOW_MAZE;
                break;
            case 's':
                keyDown = KeyPress.SHOW_SOLUTION;
                break;
            case '+': case '=':
                keyDown = KeyPress.ZOOM_IN;
                break;
            case '-':
                keyDown = KeyPress.ZOOM_OUT;
                break;
            default:
                break;
        }

		// possible inputs for key: unicode char value, 0-9, A-Z, Escape, 'k','j','h','l' plus CTRL-W
		controller.keyDown(keyDown);
		parent.repaint();
	
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// nothing to do
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// NOTE FOR THIS TYPE OF EVENT IS getKeyCode always 0, so Escape etc is not recognized	
		// this is why we work with keyPressed
	}

    /**
     * Setter for a Manual Driver to use to move a robot through the maze manually
     * @param driver accepts a manual driver to send robot-related key presses to
     */
    public void setDriver(ManualDriver driver) {
        this.driver = driver;
    }

}
