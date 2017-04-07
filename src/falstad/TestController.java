package falstad;

import generation.Cells;
import generation.MazeConfiguration;
import falstad.Constants.*;

import java.awt.*;

/**
 * Stub class to help test Drivers and Robots
 * Extends and carries out all of the functions of MazeController, but bypasses all graphics
 * Also avoids any settings or other key presses, this will all be set manually in test cases
 */
public class TestController extends MazeController {
    private Robot robot;

    @Override
    public void setDriver(String driver) {
        this.selectDriver(driver);
    }

    public RobotDriver getDriver() {
        return this.driver;
    }

    /**
     * Comparable function to calling keyDown on title screen
     * Calls factory.order and waits for response
     */
    public void generate() {
        //this.setState(Constants.StateGUI.STATE_GENERATING);
        factory.order(this);
        factory.waitTillDelivered();
    }

    /**
     * Overloaded generate function, used to pass in robot for checking driver set and get later
     * @param testRobot - Robot to be checked against later
     */
    public void generate(Robot testRobot) {
        this.robot = testRobot;

        factory.order(this);
        factory.waitTillDelivered();
    }

    @Override
    public void deliver(MazeConfiguration mazeConfig) {
        this.mazeConfig = mazeConfig;

        BasicRobot newRobot;

        // if robot was generated manually, use that robot, otherwise create new
        if (this.robot != null) {
            newRobot = (BasicRobot) this.robot;
        } else {
            newRobot = new BasicRobot();
        }

        newRobot.setMaze(this);

        this.driver.setRobot(newRobot);
        this.driver.setDistance(newRobot.getMaze().getMazeConfiguration().getMazedists());

        this.deliverSetup();

        //this.setState(Constants.StateGUI.STATE_PLAY);
    }

    /**
     * Overridden dummy method to prevent interfacing with graphics from MazeController
     */
    @Override
    protected void slowedDownRedraw() {
    }

    @Override
    public boolean keyDown(KeyPress key) {
        switch (key) {
            case UP:
                // move forward
                walk(1);
                break;
            case LEFT:
                // turn left
                rotate(1);
                break;
            case RIGHT:
                // turn right
                rotate(-1);
                break;
        }
        return true;
    }


    /**
     * Overridden updateProgress method to avoid state reference
     * @param percentage gives the new percentage on a range [0,100]
     */
    /*
    @Override
    public void updateProgress(int percentage) {
        if (this.percentdone < percentage && percentage <= 100) this.percentdone = percentage;
    }
    */

}
