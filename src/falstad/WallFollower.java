package falstad;

import falstad.Robot.*;

/**
 * RobotDriver class that executes the Wall Follower (i.e. left-hand/right-hand)
 * algorithm. Keeps a wall on the left at all times to help navigate a maze.
 * Written by Turner Allen
 * Uses a robot to interface with the maze, called by the MazeController class.
 */
public class WallFollower extends BasicDriver {

    public WallFollower() {}

    @Override
    public boolean drive2Exit () throws Exception {
        if (this.robot == null) throw new Exception();

        // if at exit, returns true (do we need to do more than this? move forward 1?)
        while (!this.robot.isAtExit()) {

            // check if wall on left go left if not
            if (this.robot.distanceToObstacle(Robot.Direction.LEFT) != 0) {
                this.robot.rotate(Robot.Turn.LEFT);
                this.robot.move(1, false);
                this.pathLength++;
            }

            // check if wall in front, move forward if not
            else if (this.robot.distanceToObstacle(Robot.Direction.FORWARD) != 0) {
                this.robot.move(1, false);
                this.pathLength++;
            }

            // rotate to the right if wall in front and to the left
             else this.robot.rotate(Robot.Turn.RIGHT);

            // end driving if robot stops or runs out of battery
            if (this.robot.hasStopped() || this.robot.getBatteryLevel() == 0) {
                System.out.println("Robot Battery Level: " + this.robot.getBatteryLevel());
                return false;
            }
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
