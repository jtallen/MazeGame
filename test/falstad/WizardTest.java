package falstad;

import static org.junit.Assert.*;

import generation.Order.*;
import org.junit.Test;

/**
 * Tests all methods of Wizard class using various tests
 * No explicit test for Drive2Exit, but it is tested incidentally in each test
 */
public class WizardTest {

    private TestController controller;

    private Wizard testDriver;
    private BasicRobot testRobot;

    private boolean reachedExit;

    private void setUp() {
        reachedExit = false;

        this.controller = new TestController();

        this.controller.setDriver("Wizard");
        this.controller.setSkillLevel(3);
        this.controller.setBuilder(Builder.DFS);

        this.controller.generate();

        this.testDriver = (Wizard) this.controller.getDriver();
        this.testRobot = (BasicRobot) this.testDriver.getRobot();
    }

    /**
     * Completes the maze with the current driver, returning whether or not it finished correctly
     */
    private boolean completeMaze() {
        try {
            return this.testDriver.drive2Exit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tests Wizard.getPathLength() for before and after
     * Also tests setDistance
     */
    @Test
    public final void testPathLength() {
        setUp();

        int [] startingPosition = this.controller.getMazeConfiguration().getStartingPosition();
        int distanceToExit = this.controller.getMazeConfiguration().getDistanceToExit(startingPosition[0], startingPosition[1]);

        // check that distance object was set properly
        assertEquals(this.testRobot.getMaze().getMazeConfiguration().getMazedists(), this.testDriver.getDistance());

        // this tests the setDistance method by setting it equal to what is standard in the class
        // if the code does not run properly, there will be an issue here
        this.testDriver.setDistance(this.testRobot.getMaze().getMazeConfiguration().getMazedists());

        // check that starting path length is zero
        assertEquals(this.testDriver.getPathLength(), 0); // check if starting path length is zero

        reachedExit = completeMaze();

        // check counter has reached exit
        assertTrue(reachedExit);

        // check that path length is equal to initial distance to exit
        assertEquals(this.testDriver.getPathLength(), distanceToExit);

    }

    /**
     * Tests Battery Consumption behavior of robot
     */
    @Test
    public final void testEnergyConsumption() {
        setUp();

        float initialBattery = this.testRobot.getBatteryLevel();
        float initialConsumption = this.testDriver.getEnergyConsumption();

        // check that starting consumption is 0
        assertEquals(0, initialConsumption, 0);
        // check that robot's starting battery is 2500
        assertEquals(2500, initialBattery, 0);

        reachedExit = completeMaze();

        float finalBattery = this.testRobot.getBatteryLevel();
        float finalConsumption = this.testDriver.getEnergyConsumption();

        // test that final consumption is the difference of the two battery states
        assertEquals(finalConsumption, initialBattery - finalBattery, 0);
    }

    /**
     * Tests setRobot() and getRobot() methods of driver
     */
    @Test
    public final void testRobot() {
        controller = new TestController();
        testRobot = new BasicRobot();

        controller.setDriver("Wizard");
        controller.setSkillLevel(1);
        controller.setBuilder(Builder.DFS);

        // use modified generate() method to pass in custom robot
        controller.generate(testRobot);

        testDriver = (Wizard) this.controller.getDriver();

        // check that getRobot() method works properly
        assertEquals(testRobot, testDriver.getRobot());

        // check that setRobot() method works properly
        testDriver.setRobot(testRobot);

        assertEquals(testRobot, testDriver.getRobot());
    }

    @Test
    public final void testDimensions() {

    }



}
