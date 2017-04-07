package falstad;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import falstad.Constants.StateGUI;
import generation.Order;

import javax.swing.*;

/**
 * Implements the screens that are displayed whenever the game is not in 
 * the playing state. The screens shown are the title screen, 
 * the generating screen with the progress bar during maze generation,
 * and the final screen when the game finishes.
 * @author pk
 *
 */
public class MazeView extends DefaultViewer {

	// need to know the maze model to check the state 
	// and to provide progress information in the generating state
	private MazeController controller ;

    private JPanel settingsPanel;

    private GameSettings settings;

    // arrays used to populate dropdown input boxes on title screen
    private final String[] skillList = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    private final String[] driverList = {"Manual", "WallFollower", "Pledge", "Wizard", "Explorer"};
    private final String[] generatorList = {"DFS", "Prim", "Eller"};

	public MazeView(MazeController c) {
		super() ;
		controller = c ;
	}

	@Override
	public void redraw(MazePanel panel, StateGUI state, int px, int py, int view_dx,
			int view_dy, int walk_step, int view_offset, RangeSet rset, int ang) {
		//dbg("redraw") ;

        Graphics gc = panel.getBufferGraphics();

		switch (state) {
            case STATE_TITLE:
                redrawTitle(gc);
                break;
		    case STATE_GENERATING:
                redrawGenerating(gc);
                break;
            case STATE_PLAY:
                // skip this one
                break;
            case STATE_FINISH:
                redrawFinish(gc);
                break;
		}
	}
	
	private void dbg(String str) {
		System.out.println("MazeView:" + str);
	}
	
	/**
	 * Helper method for redraw to draw the title screen, screen is hard coded
	 * @param  gc graphics is the off screen image
	 */
	void redrawTitle(Graphics gc) {
        // get main frame
        MazeApplication frame = this.controller.getFrame();

        // set up settings
        settings = new GameSettings();

        settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));

        // set up driver label and input box
        settingsPanel.add(new JLabel("Select a Driver"));

        JComboBox driverInput = new JComboBox(driverList);
        driverInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setDriver((String) driverInput.getSelectedItem());
            }
        });

        settingsPanel.add(driverInput);

        // set up skill label and input box
        settingsPanel.add(new JLabel("Select a Skill Level"));

        JComboBox skillInput = new JComboBox(skillList);
        skillInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setSkill(Integer.parseInt((String) skillInput.getSelectedItem()));
            }
        });

        settingsPanel.add(skillInput);

        // set up generator label and input box
        settingsPanel.add(new JLabel("Select a Generator"));

        JComboBox generatorInput = new JComboBox(generatorList);
        generatorInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setBuilder(Order.Builder.valueOf((String) generatorInput.getSelectedItem()));
            }
        });

        settingsPanel.add(generatorInput);

        // add start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDriver(settings.getDriver());
                controller.setSkillLevel(settings.getSkill());
                controller.setBuilder(settings.getBuilder());
                controller.keyDown(Constants.KeyPress.UP);
            }
        });

        settingsPanel.add(startButton);

        // add new panel to frame, set visibility
        frame.add(settingsPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        // produce white background
		gc.setColor(Color.white);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		gc.setFont(largeBannerFont);
		FontMetrics fm = gc.getFontMetrics();
		gc.setColor(Color.red);
		centerString(gc, fm, "MAZE", 100);
		// write the reference to falstad
		gc.setColor(Color.blue);
		gc.setFont(smallBannerFont);
		fm = gc.getFontMetrics();
		centerString(gc, fm, "by Paul Falstad", 160);
		centerString(gc, fm, "www.falstad.com", 190);
	}

    /**
	 * Helper method for redraw to draw final screen, screen is hard coded
	 * @param gc graphics is the off screen image
	 */
	void redrawFinish(Graphics gc) {
		// produce blue background
		gc.setColor(Color.blue);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		gc.setFont(largeBannerFont);
		FontMetrics fm = gc.getFontMetrics();
		gc.setColor(Color.yellow);
		centerString(gc, fm, "You won!", 100);
		// write some extra blufb
		gc.setColor(Color.orange);
		gc.setFont(smallBannerFont);
		fm = gc.getFontMetrics();
		centerString(gc, fm, "Congratulations!", 160);
		// write the instructions
		gc.setColor(Color.white);
		centerString(gc, fm, "Hit any key to restart", 300);
	}

	/**
	 * Helper method for redraw to draw screen during phase of maze generation, screen is hard coded
	 * only attribute percentdone is dynamic
	 * @param gc graphics is the off screen image
	 */
	void redrawGenerating(Graphics gc) {
        // hide settings panel during game
        settingsPanel.setVisible(false);

		// produce yellow background
		gc.setColor(Color.yellow);
		gc.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		// write the title 
		gc.setFont(largeBannerFont);
		FontMetrics fm = gc.getFontMetrics();
		gc.setColor(Color.red);
		centerString(gc, fm, "Building maze", 150);
		gc.setFont(smallBannerFont);
		fm = gc.getFontMetrics();
		// show progress
		gc.setColor(Color.black);
		if (controller != null)
			centerString(gc, fm, controller.getPercentDone()+"% completed", 200);
		else
			centerString(gc, fm, "Error: no controller, no progress", 200);
		// write the instructions
		centerString(gc, fm, "Hit escape to stop", 300);
	}
	
	private void centerString(Graphics g, FontMetrics fm, String str, int ypos) {
		g.drawString(str, (Constants.VIEW_WIDTH-fm.stringWidth(str))/2, ypos);
	}

	final Font largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);
	final Font smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);

    // helper object to hold options for game
    class GameSettings {
        private Order.Builder builder;
        private String driver;
        private int skill;

        // initialize with default options
        public GameSettings() {
            this.builder = Order.Builder.DFS;
            this.driver = "Manual";
            this.skill = 0;
        }

        public Order.Builder getBuilder() {
            return builder;
        }

        public void setBuilder(Order.Builder builder) {
            this.builder = builder;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public int getSkill() {
            return skill;
        }

        public void setSkill(int skill) {
            this.skill = skill;
        }
    }
}