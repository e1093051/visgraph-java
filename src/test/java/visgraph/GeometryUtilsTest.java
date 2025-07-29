package visgraph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.*;

public class GeometryUtilsTest {

    @Test
    public void testAngleFunction() {
        Point center = new Point(1.0, 1.0);
        Point pointA = new Point(3.0, 1.0); // angle = 0
        Point pointB = new Point(1.0, 0.0); // angle = 270 deg
        Point pointC = new Point(0.0, 2.0); // angle = 135 deg
        Point pointD = new Point(2.0, 2.0); // angle = 45 deg
        Point pointE = new Point(2.0, 0.0); // angle = 315 deg
        Point pointF = new Point(0.0, 0.0); // angle = 225 deg

        // Check raw radians where expected
        assertEquals(0.0, GeometryUtils.angle(center, pointA), 1e-9);
        assertEquals(3 * PI / 2, GeometryUtils.angle(center, pointB), 1e-9);

        // Check degrees for readability
        assertEquals(135, toDegrees(GeometryUtils.angle(center, pointC)), 1e-6);
        assertEquals(45, toDegrees(GeometryUtils.angle(center, pointD)), 1e-6);
        assertEquals(315, toDegrees(GeometryUtils.angle(center, pointE)), 1e-6);
        assertEquals(225, toDegrees(GeometryUtils.angle(center, pointF)), 1e-6);
    }

    @Test
    public void testEdgeIntersectFunction() {
        Point a = new Point(3.0, 5.0);
        Point b = new Point(5.0, 3.0);
        Edge edge = new Edge(a, b);

        Point c = new Point(4.0, 2.0);
        Point d = new Point(4.0, 5.0);
        Point e = new Point(5.0, 4.0);
        Point f = new Point(3.0, 4.0);
        Point g = new Point(4.0, 1.0);
        Point h = new Point(6.0, 4.0);
        Point i = new Point(4.0, 4.0);

        assertTrue(GeometryUtils.edgeIntersect(c, d, edge));
        assertTrue(GeometryUtils.edgeIntersect(c, e, edge));
        assertTrue(GeometryUtils.edgeIntersect(f, e, edge));
        assertTrue(GeometryUtils.edgeIntersect(g, b, edge));
        assertTrue(GeometryUtils.edgeIntersect(c, h, edge));
        assertTrue(GeometryUtils.edgeIntersect(h, i, edge));
    }

    @Test
    public void testPointEdgeDistanceFunction() {
        Point a = new Point(3.0, 1.0);
        Point b = new Point(3.0, 5.0);
        Point c = new Point(2.0, 2.0);
        Point d = new Point(4.0, 4.0);
        Point e = new Point(1.0, 1.0);
        Point f = new Point(1.0, 2.0);
        Point g = new Point(3.0, 4.0);
        Point h = new Point(2.0, 5.0);

        Edge edge1 = new Edge(a, b);
        Edge edge2 = new Edge(c, d);
        Edge edge3 = new Edge(e, b);

        assertEquals(1.4142135623730951, GeometryUtils.pointEdgeDistance(c, d, edge1), 1e-9);
        assertEquals(2.0, GeometryUtils.pointEdgeDistance(a, b, edge2), 1e-9);
        assertEquals(1.4142135623730951, GeometryUtils.pointEdgeDistance(f, g, edge3), 1e-9);
        assertEquals(0.9428090415820635, GeometryUtils.pointEdgeDistance(h, g, edge3), 1e-9);
    }

}
