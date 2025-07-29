package visgraph;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisGraphTest {

    private VisGraph g;
    private Point pointA, pointB, pointC, pointD;

    @BeforeEach
    public void setUp() {
        g = new VisGraph();
        pointA = new Point(0, 0);
        pointB = new Point(4, 0);
        pointC = new Point(2, 4);
        pointD = new Point(1, 0.5);
        g.build(List.of(List.of(pointA, pointB, pointC)), 1, true);
    }

    @Test
    public void testClosestPoint() {
        int pid = g.pointInPolygon(pointD);
        Point cp = g.closestPoint(pointD, pid);
        assertEquals(-1, g.pointInPolygon(cp));
    }

    @Test
    public void testClosestPointLength() {
        int pid = g.pointInPolygon(pointD);
        Point cp = g.closestPoint(pointD, pid, 0.5);
        Point ip = GeometryUtils.intersectPoint(pointD, cp, new Edge(pointA, pointB));
        double dist = GeometryUtils.edgeDistance(ip, cp);

        assertEquals(0.5, dist, 1e-9);
    }

    @Test
    public void testClosestPointEdgePoint() {
        // This polygon has a vertex that will be the closest point
        VisGraph g2 = new VisGraph();
        List<Point> polygon = List.of(
            new Point(0, 1),
            new Point(2, 0),
            new Point(1, 1),
            new Point(2, 2)
        );
        g2.build(List.of(polygon), 1, true);

        Point p = new Point(1, 0.9);
        int pid = g2.pointInPolygon(p);
        Point cp = g2.closestPoint(p, pid, 0.001);

        assertEquals(-1, g2.pointInPolygon(cp));
    }



    @Test
    public void testBuildWithHighPrecisionPolygon() {
        List<Point> polygon = List.of(
            new Point(353.6790486272709, 400.99387840984855),
            new Point(351.1303807396073, 398.8696192603927),
            new Point(349.5795890382704, 397.8537806679034),
            new Point(957.1067811865476, -207.10678118654744),
            new Point(-457.10678118654766, -207.10678118654744),
            new Point(-457.10678118654744, 1207.1067811865476),
            new Point(957.1067811865476, 1207.1067811865473),
            new Point(353.52994294901674, 606.0798841165788),
            new Point(354.0988628008279, 604.098862800828),
            new Point(354.52550331744527, 601.3462324760635),
            new Point(352.6969055662087, 602.6943209889012),
            new Point(351.22198101804634, 603.781672670995),
            new Point(247.0, 500.0),
            new Point(341.8964635104416, 405.50444716676054),
            new Point(349.24224903733045, 410.671256247085),
            new Point(350.84395848060774, 407.17766877398697)
        );

        VisGraph vg = new VisGraph();

        assertDoesNotThrow(() -> vg.build(List.of(polygon), 1, true));
    }

    @Test
    public void testPointInPolygon() {
        VisGraph g = new VisGraph();

        Point a = new Point(0, 0);
        Point b = new Point(4, 0);
        Point c = new Point(2, 4);
        Point d = new Point(1, 0.5);

        g.build(List.of(List.of(a, b, c)), 1, true);

        int pid = g.pointInPolygon(d);
        assertNotEquals(-1, pid); // point is inside the triangle
    }
}
