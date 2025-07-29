package visgraph;

import java.util.*;
import static java.lang.Math.*;

public class VisibilityHelper {
    private static final double PI = Math.PI;
    private static final double INF = 10000;
    private static final int CCW = 1;
    private static final int CW = -1;
    private static final int COLLINEAR = 0;

    public static List<Edge> computeVisibleEdges(Graph graph, List<Point> points) {
        List<Edge> visibleEdges = new ArrayList<>();
        for (Point p1 : points) {
            // System.out.println("processing Point " + p1.nodeId);
            List<Point> visibles = visibleVertices(p1, graph, true);
            for (Point p2 : visibles) {
                visibleEdges.add(new Edge(p1, p2));
            }
        }
        return visibleEdges;
    }

    public static List<Point> visibleVertices(Point point, Graph graph, boolean halfScan) {
        List<Edge> edges = new ArrayList<>(graph.getEdges());
        List<Point> points = new ArrayList<>(graph.getPoints());

        points.sort(Comparator
            .comparing((Point p) -> GeometryUtils.angle(point, p))
            .thenComparing(p -> GeometryUtils.edgeDistance(point, p)));

        OpenEdges openEdges = new OpenEdges();
        Point pointInf = new Point(GeometryUtils.INF, point.getY());

        for (Edge edge : edges) {
            if (edge.contains(point)) continue;
            if (GeometryUtils.edgeIntersect(point, pointInf, edge)) {
                if (GeometryUtils.onSegment(point, edge.getP1(), pointInf)) continue;
                if (GeometryUtils.onSegment(point, edge.getP2(), pointInf)) continue;
                openEdges.insert(point, pointInf, edge);
            }
        }

        List<Point> visible = new ArrayList<>();
        Point prev = null;
        boolean prevVisible = false;

        for (Point p : points) {
            System.out.println("point currently processing: " + p);
            if (p.equals(point)) continue;
            if (halfScan && GeometryUtils.angle(point, p) > Math.PI) break;
            // Remove CW edges
            System.out.println("openEdges: " + openEdges.size());
            if (!openEdges.isEmpty()) {
                for (Edge e : graph.getEdgesFrom(p)) {
                    if (GeometryUtils.ccw(point, p, e.getAdjacent(p)) == GeometryUtils.CW) {
                        System.out.println("delete...");
                        openEdges.delete(point, p, e);
                    }
                }
            }
            System.out.println("openEdges: " + openEdges.size());


            boolean isVisible = false;
            if (prev == null || GeometryUtils.ccw(point, prev, p) != GeometryUtils.COLLINEAR ||
                !GeometryUtils.onSegment(point, prev, p)) {
                if (openEdges.isEmpty()) isVisible = true;
                else if (!GeometryUtils.edgeIntersect(point, p, openEdges.smallest()))
                    isVisible = true;
            } else if (!prevVisible) {
                isVisible = false;
            } else {
                isVisible = true;
                for (Edge e : openEdges) {
                    if (!e.contains(prev) && GeometryUtils.edgeIntersect(prev, p, e)) {
                        isVisible = false;
                        break;
                    }
                }
                if (isVisible && GeometryUtils.edgeInPolygon(prev, p, graph))
                    isVisible = false;
            }

            if (isVisible && !graph.areAdjacent(point, p)) {
                isVisible = !GeometryUtils.edgeInPolygon(point, p, graph);
            }


            if (isVisible) visible.add(p);

            // Add CCW edges
            for (Edge e : graph.getEdgesFrom(p)) {
                if (!e.contains(point) && GeometryUtils.ccw(point, p, e.getAdjacent(p)) == GeometryUtils.CCW)
                    openEdges.insert(point, p, e);
            }

            prev = p;
            prevVisible = isVisible;
        }
        return visible;
    }


    public static Point closestPoint(Point p, int polygonId, Graph graph, double length) {
        List<Edge> polygonEdges = graph.getPolygon(polygonId);
        Point closePoint = null;
        Edge closeEdge = null;
        double closeDist = Double.MAX_VALUE;

        for (Edge e : polygonEdges) {
            double num = ((p.getX() - e.getP1().getX()) * (e.getP2().getX() - e.getP1().getX()) +
                        (p.getY() - e.getP1().getY()) * (e.getP2().getY() - e.getP1().getY()));
            double denom = Math.pow(e.getP2().getX() - e.getP1().getX(), 2) +
                        Math.pow(e.getP2().getY() - e.getP1().getY(), 2);
            double u = num / denom;

            Point pu;
            if (u < 0) {
                pu = e.getP1();
            } else if (u > 1) {
                pu = e.getP2();
            } else {
                pu = new Point(
                    e.getP1().getX() + u * (e.getP2().getX() - e.getP1().getX()),
                    e.getP1().getY() + u * (e.getP2().getY() - e.getP1().getY())
                );
            }

            double d = GeometryUtils.edgeDistance(p, pu);
            if (d < closeDist) {
                closeDist = d;
                closePoint = pu;
                closeEdge = e;
            }
        }

        if (closePoint.equals(closeEdge.getP1()) || closePoint.equals(closeEdge.getP2())) {
            Point c = closePoint;
            List<Edge> edges = graph.getEdgesFrom(c);
            if (edges.size() < 2) return c;

            Point a1 = edges.get(0).getAdjacent(c);
            Point a2 = edges.get(1).getAdjacent(c);
            Point v1 = GeometryUtils.unitVector(c, a1);
            Point v2 = GeometryUtils.unitVector(c, a2);
            Point vsum = GeometryUtils.unitVector(new Point(0, 0), new Point(v1.getX() + v2.getX(), v1.getY() + v2.getY()));
            Point close1 = new Point(c.getX() + vsum.getX() * length, c.getY() + vsum.getY() * length);
            Point close2 = new Point(c.getX() - vsum.getX() * length, c.getY() - vsum.getY() * length);

            if (GeometryUtils.pointInPolygon(close1, graph) == -1) return close1;
            return close2;
        } else {
            Point v = GeometryUtils.unitVector(p, closePoint);
            return new Point(closePoint.getX() + v.getX() * length, closePoint.getY() + v.getY() * length);
        }
    }


}
