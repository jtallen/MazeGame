package generation;

import static org.junit.Assert.*;

import falstad.Constants;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;

public class MazeFactoryTest {

    private int skillLevel;

    private StubOrder stubOrder;

    private MazeConfiguration mazeContainer;

    private int height;
    private int width;

    // test builder

    // test distance - distance thru maze make sense?

    // test cell - get to exit from any cell?

    // test single exit - only one exit



    @Before
    public void setUp() {
        this.skillLevel = 4; // set skill level here
        this.stubOrder = new StubOrder();

        this.stubOrder.setSkillLevel(this.skillLevel);
        this.stubOrder.start(Order.Builder.Eller);

        this.mazeContainer = this.stubOrder.getMazeConfig();

        this.height = this.mazeContainer.getHeight();
        this.width = this.mazeContainer.getWidth();

        Object test = new Object();

        test.equals(this.skillLevel);

        System.out.println(test.equals(this.skillLevel));
    }


    // test that height and width are set properly based on skill level
    @Test
    public final void testDimensions () {
        assertTrue(this.mazeContainer.getHeight() == Constants.SKILL_Y[this.skillLevel]);
        assertTrue(this.mazeContainer.getWidth() == Constants.SKILL_X[this.skillLevel]);
    }

    // test that the edges of the maze are correct
    @Test
    public final void testEdges () {
        Cells cells = this.mazeContainer.getMazecells();

        // test left edge
        for (int i = 0; i < this.height; i++) {
            if (!cells.isExitPosition(0, i)) {
                assertTrue(cells.hasWall(0, i, CardinalDirection.West));
            }
        }

        // test right edge
        for (int i = 0; i < this.height; i++) {
            if (!cells.isExitPosition(this.width - 1, i)) {
                assertTrue(cells.hasWall(this.width - 1, i, CardinalDirection.East));
            }
        }

        // test top edge
        for (int i = 0; i < this.width; i++) {
            if (!cells.isExitPosition(i, 0)) {
                assertTrue(cells.hasWall(i, 0, CardinalDirection.North));
            }
        }

        // test bottom edge
        for (int i = 0; i < this.width; i++) {
            if (!cells.isExitPosition(i, this.height - 1)) {
                assertTrue(cells.hasWall(i, this.height - 1, CardinalDirection.South));
            }
        }


    }

    // test that there is one and only one exit
    @Test
    public final void testExit () {
        Cells cells = this.mazeContainer.getMazecells();

        int [] exitTile = null;
        int exitAmount = 0;

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (cells.isExitPosition(x, y)) {
                    exitTile = new int[] {x,y};
                    exitAmount++;
                }
            }
        }

        //System.out.println("Exit located at: (" + hasExit[0] + ", " + hasExit[1] + ")");
        //System.out.println("Distance from exit at exit: " + this.mazeContainer.getDistanceToExit(exitTile[0], exitTile[1])); // 1

        // check that there is one and only one exit tile
        assertNotNull(exitTile);
        assertTrue(exitAmount == 1);
    }

    // test that it is possible to exit the maze from every tile
    @Test
    public final void testDistance() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                assertNotNull(this.mazeContainer.getDistanceToExit(x, y));
            }
        }
    }

    // test proper number of rooms
    @Test
    public final void testRooms() {
        int countedRooms = 0;

        Cells cells = this.mazeContainer.getMazecells();

        // checks each cell, increments countedRooms if cell is the northwest corner of a room
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (cells.isInRoom(x, y) && !cells.isInRoom(x - 1, y) && !cells.isInRoom(x, y - 1)) countedRooms++;
            }
        }

        //System.out.println("Total Counted Rooms: " + countedRooms);

        assertEquals(countedRooms, Constants.SKILL_ROOMS[this.skillLevel] + 1);
    }
}
