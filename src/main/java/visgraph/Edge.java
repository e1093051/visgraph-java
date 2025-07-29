package visgraph;

import java.util.*;

public class Edge {
    public final Point p1;
    public final Point p2;

    public Edge(Point point1, Point point2) {
        this.p1 = point1;
        this.p2 = point2;
    }

    public Point getP1() {
        return this.p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public Point getAdjacent(Point point) {
        if (point.equals(this.p1)) {
            return this.p2;
        }
        return this.p1;
    }

    public boolean contains(Point point) {
        return this.p1.equals(point) || this.p2.equals(point);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge)) return false;
        Edge edge = (Edge) obj;
        // Undirected edge comparison
        return (this.p1.equals(edge.p1) && this.p2.equals(edge.p2)) ||
               (this.p1.equals(edge.p2) && this.p2.equals(edge.p1));
    }

    @Override
    public int hashCode() {
        // Order-independent hash for undirected edge
        return this.p1.hashCode() ^ this.p2.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Edge(Point(%s, %s), Point(%s, %s))", this.p1.x, this.p1.y, this.p2.x, this.p2.y);
    }

    public String toRepr() {
        return String.format("Edge(%s, %s)", this.p1.nodeId, this.p2.nodeId);
    }

}
