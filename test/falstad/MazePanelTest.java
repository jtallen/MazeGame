package falstad;

import generation.Order.Builder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests various methods of MazePanel to make sure they work correctly
 */
public class MazePanelTest {
    MazePanel panel;

    @Test
    public final void testGraphics2D() {
        panel = new MazePanel();

        System.out.println("Type of Graphics: " + panel.getGraphics2D().getClass());
    }
}
