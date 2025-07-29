package visgraph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollinearTest {

    private Point pointA, pointB, pointC, pointD, pointE, pointF;

    @BeforeEach
    public void setUp() {
        pointA = new Point(0.0, 1.0);
        pointB = new Point(1.0, 0.0);
        pointC = new Point(2.0, 3.0);
        pointD = new Point(3.0, 2.0);
        pointE = new Point(3.5, 0.5);
        pointF = new Point(4.5, 3.5);
    }

    @Test
    public void testCollinear1() {
        Graph graph = new Graph(List.of(
            List.of(pointA, pointB, pointC),
            List.of(pointD, pointE, pointF)
        ));

        List<Point> visible = VisibilityHelper.visibleVertices(new Point(1, 4), graph, false);


        List<Point> expected = List.of(pointA, pointC, pointD, pointF);
        assertTrue(containsSamePoints(expected, visible), "Visible points mismatch");
    }


    @Test
    public void testCollin2() {
        Point pointG = new Point(2.0, 5.0);
        Point pointH = new Point(3.0, 5.0);

        Graph graph = new Graph(List.of(
            List.of(pointG, pointH, pointC),
            List.of(pointD, pointE, pointF)
        ));

        List<Point> visible = VisibilityHelper.visibleVertices(new Point(1, 4), graph, false);
        List<Point> expected = List.of(pointG, pointE, pointC, pointD);
        assertTrue(containsSamePoints(expected, visible), "Visible points mismatch");
    }

    @Test
    public void testCollin3() {
        Point pointG = new Point(2.0, 2.0);
        Point pointH = new Point(3.5, 5.0);
        Point pointI = new Point(2.5, 2.0);

        Graph graph = new Graph(List.of(
            List.of(pointA, pointB, pointC),
            List.of(pointG, pointH, pointI),
            List.of(pointD, pointE, pointF)
        ));

        List<Point> visible = VisibilityHelper.visibleVertices(new Point(1, 4), graph, false);
        List<Point> expected = List.of(pointH, pointA, pointC);
        assertTrue(containsSamePoints(expected, visible), "Visible points mismatch");
    }


    @Test
    public void testCollin4() {
        Point.globalNodeId = 0;
        Graph graph = new Graph(List.of(
            List.of(new Point(1, 1), new Point(2, 3), new Point(3, 1), new Point(2, 2)),
            List.of(new Point(2, 4))
        ));

        List<Point> visible = VisibilityHelper.visibleVertices(new Point(2, 1), graph, false);
        List<Point> expected = List.of(
            new Point(3, 1),
            new Point(2, 2),
            new Point(1, 1)
        );
        assertTrue(containsSamePoints(expected, visible), "Visible points mismatch");
    }


    @Test
    public void testCollin6() {
        Graph graph = new Graph(List.of(
            List.of(new Point(0, 0), new Point(2, 1), new Point(0, 2))
        ));

        int pip = GeometryUtils.pointInPolygon(new Point(1, 1), graph);

        assertTrue(pip > -1, "Point (1,1) should be inside the polygon");
    }

    @Test
    public void testCollin7() {
        Graph graph = new Graph(List.of(
            List.of(
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 0),
                new Point(2, 2),
                new Point(0, 2)
            )
        ));

        int pip = GeometryUtils.pointInPolygon(new Point(0.5, 1), graph);

        assertTrue(pip > -1, "Point (0.5, 1) should be inside the polygon");
    }

    @Test
    public void testCollin8() {
        Graph graph = new Graph(List.of(
            List.of(
                new Point(0, 0),
                new Point(2, 0),
                new Point(2, 2),
                new Point(1, 1),
                new Point(0, 2)
            )
        ));

        int pip = GeometryUtils.pointInPolygon(new Point(0.5, 1), graph);

        assertTrue(pip > -1, "Point (0.5, 1) should be inside the polygon");
    }

    @Test
    public void testCollin9() {
        Graph graph = new Graph(List.of(
            List.of(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1),
                new Point(2, 1),
                new Point(2, 2),
                new Point(0, 2)
            )
        ));

        int pip = GeometryUtils.pointInPolygon(new Point(0.5, 1), graph);

        assertTrue(pip > -1, "Point (0.5, 1) should be inside the polygon");
    }

    @Test
    public void testCollin10() {
        Graph graph = new Graph(List.of(
            List.of(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1),
                new Point(2, 1),
                new Point(2, 0),
                new Point(3, 0),
                new Point(3, 2),
                new Point(0, 2)
            )
        ));

        int pip = GeometryUtils.pointInPolygon(new Point(0.5, 1), graph);

        assertTrue(pip > -1, "Point (0.5, 1) should be inside the polygon");
    }

    @Test
    public void testCollin11() {
        Graph graph = new Graph(List.of(
            List.of(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 2),
                new Point(2, 3),
                new Point(2, 1),
                new Point(1, 1),
                new Point(1, 2),
                new Point(0, 2)
            )
        ));

        int pip = GeometryUtils.pointInPolygon(new Point(0.5, 1), graph);

        assertTrue(pip > -1, "Point (0.5, 1) should be inside the polygon");
    }


    private boolean containsSamePoints(List<Point> expected, List<Point> actual) {
        Set<String> expectedSet = new HashSet<>();
        Set<String> actualSet = new HashSet<>();
        for (Point p : expected) expectedSet.add(p.toRepr());
        System.out.println(expectedSet);
        for (Point p : actual) actualSet.add(p.toRepr());
        System.out.println(actualSet);
        return expectedSet.equals(actualSet);
    }
}
