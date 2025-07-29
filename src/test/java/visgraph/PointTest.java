package visgraph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    public void testPointEquality() {
        Point a = new Point(0.0, 1.0);
        assertEquals(new Point(0.0, 1.0), a);
        assertNotEquals(new Point(1.0, 0.0), a);
    }
}
