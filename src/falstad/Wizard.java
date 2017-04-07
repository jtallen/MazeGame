package falstad;

import generation.CardinalDirection;
import falstad.Robot.*;

/**
 * RobotDriver class that executes the Wizard maze-solving algorithm.
 * Uses magic (the "getNeighborCloserToExit" method) to navigate the maze.
 * Written by Turner Allen
 * Uses a robot to interface with the maze, called by the MazeController class.
 */
public class Wizard extends BasicDriver {
    // overloads robot in BasicDriver for use in Wizard
    BasicRobot robot;

    public Wizard() {}

    @Override
    public boolean drive2Exit() throws Exception {

        while(!this.robot.isAtExit()) {

            // get neighbor
            int[] currentPosition = this.robot.getMaze().getCurrentPosition();
            int[] neighbor = this.robot.getMaze().getMazeConfiguration().getNeighborCloserToExit(currentPosition[0], currentPosition[1]);

            // direction has to be flipped in the Y position due to flipped issues
            int[] direction = {neighbor[0] - currentPosition[0], neighbor[1] - currentPosition[1]};

            CardinalDirection neighborDirection = CardinalDirection.getDirection(direction[0], direction[1]);

            CardinalDirection facingDirection = this.robot.getMaze().getCurrentDirection();

            if (facingDirection == neighborDirection.oppositeDirection()) this.robot.rotate(Turn.AROUND);

            else if (facingDirection == neighborDirection.rotateClockwise()) this.robot.rotate(Turn.RIGHT);

            else if (facingDirection == neighborDirection.rotateCounterclockwise()) this.robot.rotate(Turn.LEFT);

            if (facingDirection == neighborDirection) {
                robot.move(1, false);
                this.pathLength++;
            }

            // end driving if robot stops or runs out of battery
            if (this.robot.hasStopped() || this.robot.getBatteryLevel() == 0) return false;
        }

        // final block to help exit maze/push state to end
        if (robot.canSeeExit(Direction.LEFT)) this.robot.rotate(Turn.LEFT);
        else if (robot.canSeeExit(Direction.RIGHT)) this.robot.rotate(Turn.RIGHT);
        else if (robot.canSeeExit(Direction.BACKWARD)) this.robot.rotate(Turn.AROUND);

        // end driving if robot stops or runs out of battery
        if (this.robot.hasStopped() || this.robot.getBatteryLevel() == 0) return false;

        robot.move(1, false);
        this.pathLength++;

        return true;
    }

    @Override
    public void setRobot(Robot robot) {
        this.robot = (BasicRobot) robot;
        setStartingEnergy(this.robot.getBatteryLevel());
    }

    @Override
    public Robot getRobot() {
        return this.robot;
    }

    @Override
    public float getEnergyConsumption() {
        return this.getStartingEnergy() - this.robot.getBatteryLevel();
    }
}
