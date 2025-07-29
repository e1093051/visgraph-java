package visgraph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {

    private Point pointA, pointB, pointC, pointD;
    private Edge edgeA, edgeB, edgeC, edgeD;

    @BeforeEach
    public void setup() {
        pointA = new Point(0.0, 1.0);
        pointB = new Point(1.0, 2.0);
        pointC = new Point(0.0, 1.0);  // same as A
        pointD = new Point(1.0, 2.0);  // same as B

        edgeA = new Edge(pointA, pointB);
        edgeB = new Edge(pointB, pointA); // reversed
        edgeC = new Edge(pointC, pointD); // same as A
        edgeD = new Edge(pointD, pointC); // reversed of C
    }

    @Test
    public void testPointEquality() {
        assertEquals(new Point(0.0, 1.0), pointA);
        assertNotEquals(new Point(1.0, 0.0), pointA);
    }

    @Test
    public void testEdgeEquality() {
        assertEquals(edgeA, edgeB);  // reversed
        assertEquals(edgeA, edgeA);  // identical
        assertEquals(edgeA, edgeC);  // equal values
        assertEquals(edgeA, edgeD);  // reversed
    }
}
