package falstad;

import generation.CardinalDirection;
import falstad.Constants.*;

import java.util.HashMap;
import java.util.Map;

public class BasicRobot implements Robot {

    // Battery cost constants
    private static final float senseEnergy = 1;
    private static final float rotateEnergy = 3;
    private static final float stepEnergy = 5;

    // Variable initialization
    private CardinalDirection facingDirection;

    private MazeController mazeController;

    private boolean isStopped;

    // robot's current position in maze
    private int[] currentPosition;

    // sensor booleans
    private boolean distanceSensor;
    private boolean roomSensor;

    // battery level
    private float batteryLevel;

    /**
     * Constructor
     */
    public BasicRobot() {
        this.facingDirection = CardinalDirection.East;
        this.batteryLevel = 2500;
        this.distanceSensor = true;
        this.roomSensor = true;
    }

    // note: mazeController.getCurrentDirection() will always be flipped for north/south of this.facingDirection
    public void rotate(Turn turn) {
        switch (turn) {
            case LEFT:
                this.facingDirection = this.facingDirection.rotateCounterclockwise();
                mazeController.keyDown(KeyPress.LEFT);
                this.batteryLevel = Math.max(0, this.batteryLevel - 3);
                break;
            case RIGHT:
                this.facingDirection = this.facingDirection.rotateClockwise();
                mazeController.keyDown(KeyPress.RIGHT);
                this.batteryLevel = Math.max(0, this.batteryLevel - 3);
                break;
            case AROUND:
                this.facingDirection = this.facingDirection.oppositeDirection();
                mazeController.keyDown(KeyPress.LEFT);
                mazeController.keyDown(KeyPress.LEFT);
                this.batteryLevel = Math.max(0, this.batteryLevel - 6);
                break;
            default:
                throw new RuntimeException("Inconsistent enum type");
        }

        // stop robot if battery runs out of juice
        if (this.batteryLevel == 0) {
            this.isStopped = true;
        }
    }

    public void move(int distance, boolean manual) {

        // check if maze state is finished, return simple key press if so
        if (this.mazeController.getState() == StateGUI.STATE_FINISH) {
            mazeController.keyDown(KeyPress.UP);
            return;
        }

        this.currentPosition = this.mazeController.getCurrentPosition();
        this.facingDirection = this.mazeController.getCurrentDirection();

        // reverse y-axis to accommodate graphics disparity
        // CardinalDirection wallDirection = CardinalDirection.getDirection(this.facingDirection.getDirection()[0], this.facingDirection.getDirection()[1]);

        while (distance > 0) {
            // stop robot and end movement if attempted move direction has wall
            if (this.mazeController.getMazeConfiguration().getMazecells().hasWall(this.currentPosition[0], this.currentPosition[1], facingDirection)) {
                if(!manual) this.isStopped = true;
                System.out.println("It thinks there's a wall there!");
                return;
            }

            mazeController.keyDown(KeyPress.UP);

            this.batteryLevel = Math.max(0, this.batteryLevel - 5);
            // stop robot and end movement if robot runs out of battery
            if (this.batteryLevel == 0) {
                this.isStopped = true;
                return;
            }

            distance--;
        }

    }

    public int[] getCurrentPosition() throws Exception {
        int width = this.mazeController.getMazeConfiguration().getWidth();
        int height = this.mazeController.getMazeConfiguration().getHeight();

        this.currentPosition = this.mazeController.getCurrentPosition();

        // throw exception if robot's current position is out of bounds of the maze
        if (this.currentPosition[0] < 0 || this.currentPosition[1] < 0 ||
            this.currentPosition[0] >= width || this.currentPosition[1] >= height) throw new Exception("Robot is not in maze!");

        return this.currentPosition;
    }

    public void setMaze(MazeController maze) {
        this.mazeController = maze;
        this.currentPosition = this.mazeController.getCurrentPosition();
    }

    public MazeController getMaze() {
        return this.mazeController;
    }

    public boolean isAtExit() {
        this.currentPosition = this.mazeController.getCurrentPosition();
        return this.mazeController.getMazeConfiguration().getMazecells().isExitPosition(this.currentPosition[0], this.currentPosition[1]);
    }

    public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
        return this.distanceToObstacle(direction) == Integer.MAX_VALUE;
    }

    public boolean isInsideRoom() throws UnsupportedOperationException {
        if (!this.hasRoomSensor()) throw new UnsupportedOperationException("This robot does not have a room sensor");

        // update current position
        this.currentPosition = this.mazeController.getCurrentPosition();

        return this.mazeController.getMazeConfiguration().getMazecells().isInRoom(this.currentPosition[0], this.currentPosition[1]);
    }

    public boolean hasRoomSensor() {
        return this.roomSensor;
    }

    public void addRoomSensor() {
        this.roomSensor = true;
    }

    public void removeRoomSensor() {
        this.roomSensor = false;
    }

    public boolean hasDistanceSensor(Direction direction) {
        return this.distanceSensor;
    }

    public void addDistanceSensor() {
        this.distanceSensor = true;
    }

    public void removeDistanceSensor() {
        this.distanceSensor = false;
    }

    public CardinalDirection getCurrentDirection() {
        return this.facingDirection;
    }

    // standard getters and setters - P5
    public float getBatteryLevel() {
        return this.batteryLevel;
    }

    public void setBatteryLevel(float newLevel) {
        this.batteryLevel = newLevel;
    }

    public CardinalDirection getFacingDirection() {
        return this.facingDirection;
    }

    public void setFacingDirection(CardinalDirection newDirection) {
        this.facingDirection = newDirection;
    }



    public float getEnergyForFullRotation() {
        return rotateEnergy * 4;
    }

    public float getEnergyForStepForward() {
        return stepEnergy;
    }

    public boolean hasStopped() {
        return this.isStopped;
    }

    public void stop() {
        this.isStopped = true;
    }

    public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
        if (!this.distanceSensor) throw new UnsupportedOperationException("This robot does not have a distance sensor");

        // update current position
        this.currentPosition = this.mazeController.getCurrentPosition();
        this.facingDirection = this.mazeController.getCurrentDirection();

        Map<Direction, CardinalDirection> directionMap = this.mapDirections();

        // find desired cardinal direction for sensing
        CardinalDirection sensorDirection = directionMap.get(direction);

        // sensor direction == south

        int distance = 0;

        // loop through cells in direction of sensor until hitting a wall, increment distance each time
        while (!this.mazeController.getMazeConfiguration().getMazecells().hasWall(currentPosition[0] + (sensorDirection.getDirection()[0] * distance), currentPosition[1] + (sensorDirection.getDirection()[1] * distance), sensorDirection)) {
            // checks if tile is exit position and direction leads out of maze
            boolean isExitTile = this.mazeController.getMazeConfiguration().getMazecells().isExitPosition(currentPosition[0] + (sensorDirection.getDirection()[0] * distance), currentPosition[1] + (sensorDirection.getDirection()[1] * distance));
            boolean leadsToExit =
                    currentPosition[0] + (sensorDirection.getDirection()[0] * (distance + 1)) < 0 ||
                    currentPosition[0] + (sensorDirection.getDirection()[0] * (distance + 1)) >= this.mazeController.getMazeConfiguration().getWidth() ||
                    currentPosition[1] + (sensorDirection.getDirection()[1] * (distance + 1)) < 0 ||
                    currentPosition[1] + (sensorDirection.getDirection()[1] * (distance + 1)) >= this.mazeController.getMazeConfiguration().getHeight();
            if (isExitTile && leadsToExit) {
                distance = Integer.MAX_VALUE;
                break;
            }
            distance++;
        }

        this.batteryLevel = Math.max(0, this.batteryLevel - 1);

        // stop robot if battery runs out of juice
        if (this.batteryLevel == 0) {
            this.isStopped = true;
        }

        return distance;
    }

    /**
     * Helper method to create a map from the robots directions to its current
     * Cardinal Directions.
     * @return a Map of Directions synced with robot's currently facing Cardinal Direction
     */
    private Map<Direction, CardinalDirection> mapDirections() {
        Map<Direction, CardinalDirection> directions = new HashMap<>();

        directions.put(Direction.FORWARD, this.facingDirection);
        directions.put(Direction.RIGHT, this.facingDirection.rotateCounterclockwise());
        directions.put(Direction.LEFT, this.facingDirection.rotateClockwise());
        directions.put(Direction.BACKWARD, this.facingDirection.oppositeDirection());

        return directions;
    }

    /**
     * Method to reset all transient variables after winning
     * Allows you to start a new game with fresh settings
     */
    public void reset() {
        this.isStopped = false;
        this.batteryLevel = 2500;
        this.facingDirection = CardinalDirection.East;
    }

}
