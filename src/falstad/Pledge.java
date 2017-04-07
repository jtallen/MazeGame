package falstad;

import falstad.Robot.*;

/**
 * RobotDriver class that executes the pledge maze-solving algorithm
 * Uses a counter to help navigate obstacles
 * Written by Turner Allen
 * Uses a robot to interface with the maze, called by the MazeController class.
 */
public class Pledge extends BasicDriver {

    public Pledge() {}

    @Override
    public boolean drive2Exit () throws Exception {
        if (this.robot == null) throw new Exception();

        int pledgeCounter = 0;

        // loops until robot reaches exit
        while (!this.robot.isAtExit()) {

            // if pledgecounter = 0 and nothing in front, move forward (obstacle-less movement)
            if (pledgeCounter == 0) {
                if (this.robot.distanceToObstacle(Direction.FORWARD) != 0) {
                    this.robot.move(1, false);
                    this.pathLength++;
                // rotate if front path is blocked
                } else {
                    this.robot.rotate(Turn.RIGHT);
                    pledgeCounter--;
                }
            // if not facing main direction, go into pledge algorithm
            } else {
                if (this.robot.distanceToObstacle(Direction.LEFT) != 0) {
                    this.robot.rotate(Turn.LEFT);
                    this.robot.move(1, false);
                    this.pathLength++;
                    pledgeCounter++;
                } else if (this.robot.distanceToObstacle(Direction.FORWARD) != 0) {
                    this.robot.move(1, false);
                    this.pathLength++;
                } else {
                    this.robot.rotate(Turn.RIGHT);
                    pledgeCounter--;
                }
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
}
