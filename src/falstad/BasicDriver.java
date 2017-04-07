package falstad;

import generation.Distance;

/**
 * Basic driver upon which all other drivers are based
 * Contains the methods that are common to all driver classes
 * Uses manual implementation for its drive2Exit
 */
public class BasicDriver implements RobotDriver {

    protected Robot robot;

    private int width;
    private int height;

    private Distance distance;

    private float startingEnergy;
    protected int pathLength;

    public BasicDriver() {
        this.pathLength = 0;
    }

    // implements manual drive2Exit method before being overriden.
    public boolean drive2Exit() throws Exception {
        throw new Exception("This driver is not automatic.");
    }

    public Robot getRobot() {
        return this.robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
        this.startingEnergy = this.robot.getBatteryLevel();
    }

    public int[] getDimensions() {
        return new int[] {this.width, this.height};
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Distance getDistance() {
        return this.distance;
    }

    public void setDistance(Distance newDistance) {
        this.distance = newDistance;

        // check that the set distance is equal to the robot's distance object
        // disabled due to inheritance issues
        //BasicRobot testRobot = (BasicRobot) this.robot;
        //assert(this.distance == testRobot.getMaze().getMazeConfiguration().getMazedists());
    }

    public float getStartingEnergy() {
        return this.startingEnergy;
    }

    public void setStartingEnergy(float startingEnergy) {
        this.startingEnergy = startingEnergy;
    }

    public int getPathLength() {
        return this.pathLength;
    }

    public void setPathLength(int newPathLength) {
        this.pathLength = newPathLength;
    }

    public float getEnergyConsumption() {
        return this.startingEnergy - this.robot.getBatteryLevel();
    }
}
