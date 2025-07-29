package visgraph;

import java.util.*;

public class OpenEdges implements Iterable<Edge> {
    private final List<Edge> openEdges = new ArrayList<>();

    public void insert(Point p1, Point p2, Edge edge) {
        int index = index(p1, p2, edge);
        System.out.println("insert into openEdges: p1" + p1 + " p2" + p2 + " edge" + edge + " at " + index);
        openEdges.add(index, edge);
    }


    public void delete(Point p1, Point p2, Edge edge) {
        int index = index(p1, p2, edge) - 1;
        if (openEdges.get(index).equals(edge)) {
            openEdges.remove(index);
            return;
        }
        for (int i = 0; i < openEdges.size(); i++) {
            if (openEdges.get(i).equals(edge)) {
                openEdges.remove(i);
                return;
            }
        }
    }
    

    public Edge smallest() {
        return openEdges.get(0);
    }

    private int index(Point p1, Point p2, Edge edge) {
        int lo = 0, hi = openEdges.size();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (lessThan(p1, p2, edge, openEdges.get(mid))) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    private boolean lessThan(Point p1, Point p2, Edge e1, Edge e2) {
        if (e1.equals(e2)) return false;
        if (!GeometryUtils.edgeIntersect(p1, p2, e2)) return true;

        double d1 = GeometryUtils.pointEdgeDistance(p1, p2, e1);
        double d2 = GeometryUtils.pointEdgeDistance(p1, p2, e2);
        if (d1 < d2) return true;
        if (d1 > d2) return false;


        Point samePoint;
        if (e1.p1.equals(e2.p1) || e1.p1.equals(e2.p2)) {
            samePoint = e1.p1;
        }
        else {
            samePoint = e1.p2;
        }
        double angleEdge1 = GeometryUtils.angle2(p1, p2, e1.getAdjacent(samePoint));
        double angleEdge2 = GeometryUtils.angle2(p1, p2, e2.getAdjacent(samePoint));
        return angleEdge1 < angleEdge2;
    }
    

    public boolean isEmpty() {
        return openEdges.isEmpty();
    }

    @Override
    public Iterator<Edge> iterator() {
        return openEdges.iterator();
    }

    public int size() {
        return openEdges.size();
    }
}
