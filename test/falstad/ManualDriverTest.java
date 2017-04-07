package falstad;

import org.junit.Test;

import static org.junit.Assert.*;

public class ManualDriverTest {
    MazePanel panel;

    @Test
    public final void testConstructor() {
        ManualDriver driver = new ManualDriver();

        System.out.println(driver.getPathLength());
    }
}
