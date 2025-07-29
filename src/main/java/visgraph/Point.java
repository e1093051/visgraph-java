package visgraph;

import java.util.Objects;

public class Point implements Comparable<Point> {
    public static int globalNodeId = 0;
    public final double x;
    public final double y;
    public final int nodeId;
    private int polygonId;

    public Point(double x, double y) {
        this(x, y, -1);
    }

    public Point(double x, double y, int polygonId) {
        this.x = x;
        this.y = y;
        this.polygonId = polygonId;
        this.nodeId = globalNodeId++;
        // System.out.println("Point " + this.nodeId + " x: " + this.x + " y: " + this.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPolygonId(int polygonId) {
        this.polygonId = polygonId;
    }

    public int getPolygonId() {
        return polygonId;
    }

    public int getNodeId() {
        return nodeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point(%.2f, %.2f)", x, y);
    }

    @Override
    public int compareTo(Point other) {
        // Used for priority queues (e.g. heap)
        return Integer.compare(this.hashCode(), other.hashCode());
    }

    public String toRepr() {
        return String.format("Point(%.2f, %.2f)", x, y);
    }
}
