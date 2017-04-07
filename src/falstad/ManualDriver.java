package falstad;

public class ManualDriver extends BasicDriver {
    /**
     * Constructor
     */
    public ManualDriver() {}

    /**
     * Move method, moves robot one tile in the direction it is facing
     */
    public void move() {
        this.robot.move(1, true);

        // figure out pathlength for this
    }

    /**
     * Tells the robot to rotate in the turn enum given
     * @param turn passed form keyboard input
     */
    public void rotate(Robot.Turn turn) {
        this.robot.rotate(turn);
    }
}
