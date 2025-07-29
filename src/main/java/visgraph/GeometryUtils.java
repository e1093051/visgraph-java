package visgraph;

import java.util.*;

public class GeometryUtils {
    public static final double PI = Math.PI;
    public static final double INF = 10000;
    public static final int CCW = 1;
    public static final int CW = -1;
    public static final int COLLINEAR = 0;
    public static final int COLIN_TOLERANCE = 10;
    public static final double T = Math.pow(10, COLIN_TOLERANCE);
    public static final double T2 = Math.pow(10.0, COLIN_TOLERANCE);

    public static double edgeDistance(Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static Point intersectPoint(Point p1, Point p2, Edge edge) {
        if (edge.contains(p1)) return p1;
        if (edge.contains(p2)) return p2;

        Point e1 = edge.getP1();
        Point e2 = edge.getP2();

        if (e1.getX() == e2.getX()) {
            if (p1.getX() == p2.getX()) return null;
            double pslope = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
            double intersectX = e1.getX();
            double intersectY = pslope * (intersectX - p1.getX()) + p1.getY();
            return new Point(intersectX, intersectY);
        }

        if (p1.getX() == p2.getX()) {
            double eslope = (e1.getY() - e2.getY()) / (e1.getX() - e2.getX());
            double intersectX = p1.getX();
            double intersectY = eslope * (intersectX - e1.getX()) + e1.getY();
            return new Point(intersectX, intersectY);
        }

        double pslope = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
        double eslope = (e1.getY() - e2.getY()) / (e1.getX() - e2.getX());
        if (Double.compare(eslope, pslope) == 0) return null;

        double intersectX = (eslope * e1.getX() - pslope * p1.getX() + p1.getY() - e1.getY()) / (eslope - pslope);
        double intersectY = eslope * (intersectX - e1.getX()) + e1.getY();
        return new Point(intersectX, intersectY);
    }

    
    public static double pointEdgeDistance(Point p1, Point p2, Edge e) {
        Point ip = intersectPoint(p1, p2, e);
        if (ip == null) return 0;
        return edgeDistance(p1, ip);
    }

    public static double angle(Point center, Point p) {
        double dx = p.getX() - center.getX();
        double dy = p.getY() - center.getY();

        if (dx == 0) return (dy < 0) ? 1.5 * PI : 0.5 * PI;
        if (dy == 0) return (dx < 0) ? PI : 0;

        if (dx < 0) return PI + Math.atan(dy / dx);
        if (dy < 0) return 2 * PI + Math.atan(dy / dx);
        return Math.atan(dy / dx);
    }

    public static double angle2(Point a, Point b, Point c) {
        double ab2 = squareDistance(c, b);
        double ac2 = squareDistance(c, a);
        double bc2 = squareDistance(b, a);
        double cosValue = (ab2 + bc2 - ac2) / (2 * Math.sqrt(ab2) * Math.sqrt(bc2));
        return Math.acos((int) (cosValue * T) / T2);
    }

    public static int ccw(Point A, Point B, Point C) {
        double area = ((B.getX() - A.getX()) * (C.getY() - A.getY()) - (B.getY() - A.getY()) * (C.getX() - A.getX()));
        int rounded = (int) (area * T);
        double result = rounded / T2;

        if (result > 0) return CCW;
        if (result < 0) return CW;
        return COLLINEAR;
    }

    public static boolean onSegment(Point p, Point q, Point r) {
        return (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
                && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()));
    }

    public static boolean edgeIntersect(Point p1, Point q1, Edge edge) {
        Point p2 = edge.getP1();
        Point q2 = edge.getP2();

        int o1 = ccw(p1, q1, p2);
        int o2 = ccw(p1, q1, q2);
        int o3 = ccw(p2, q2, p1);
        int o4 = ccw(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4) return true;

        // Special cases (collinear + overlap)
        if (o1 == COLLINEAR && onSegment(p1, p2, q1)) return true;
        if (o2 == COLLINEAR && onSegment(p1, q2, q1)) return true;
        if (o3 == COLLINEAR && onSegment(p2, p1, q2)) return true;
        if (o4 == COLLINEAR && onSegment(p2, q1, q2)) return true;

        return false;
    }


    public static Point unitVector(Point from, Point to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        return new Point(dx / magnitude, dy / magnitude);
    }

    private static double squareDistance(Point a, Point b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return dx * dx + dy * dy;
    }

    public static boolean edgeInPolygon(Point p1, Point p2, Graph graph) {
        if (p1.getPolygonId() != p2.getPolygonId()) return false;
        if (p1.getPolygonId() == -1 || p2.getPolygonId() == -1) return false;

        Point mid = new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
        List<Edge> polygonEdges = graph.getPolygon(p1.getPolygonId());
        return polygonCrossing(mid, polygonEdges);
    }



    public static boolean polygonCrossing(Point p, List<Edge> edges) {
        Point far = new Point(INF, p.getY());
        int count = 0;
        for (Edge e : edges) {
            if (p.getY() < e.p1.getY() && p.getY() < e.p2.getY()) continue;
            if (p.getY() > e.p1.getY() && p.getY() > e.p2.getY()) continue;
            if (p.getX() > e.p1.getX() && p.getX() > e.p2.getX()) continue;

            boolean col1 = ccw(p, e.p1, far) == 0;
            boolean col2 = ccw(p, e.p2, far) == 0;
            if (col1 && col2) continue;
            if (col1 || col2) {
                Point colPt = col1 ? e.p1 : e.p2;
                if (e.getAdjacent(colPt).getY() > p.getY()) count++;
            } else if (edgeIntersect(p, far, e)) {
                count++;
            }
        }
        return count % 2 == 1;
    }


    public static int pointInPolygon(Point p, Graph graph) {
        for (Map.Entry<Integer, Set<Edge>> entry : graph.getPolygons().entrySet()) {
            int polygonId = entry.getKey();
            List<Edge> polygonEdges = new ArrayList<>(entry.getValue());
            if (polygonCrossing(p, polygonEdges)) {
                return polygonId;
            }
        }
        return -1;
    }

}

