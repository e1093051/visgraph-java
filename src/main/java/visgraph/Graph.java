package visgraph;

import java.util.*;

public class Graph {
    private final Map<Point, Set<Edge>> graph;
    public final Set<Edge> edges;
    private final Map<Integer, Set<Edge>> polygons;

    public Graph(List<List<Point>> polygonsInput) {
        this.graph = new HashMap<>();
        this.edges = new HashSet<>();
        this.polygons = new HashMap<>();

        int polygonId = 0;
        for (List<Point> polygon : polygonsInput) {
            if (polygon.size() > 1 && polygon.get(0).equals(polygon.get(polygon.size() - 1))) {
                polygon.remove(polygon.size() - 1);
            }

            for (int i = 0; i < polygon.size(); i++) {
                Point point = polygon.get(i);
                Point siblingPoint = polygon.get((i + 1) % polygon.size());
                Edge edge = new Edge(point, siblingPoint);

                if (polygon.size() > 2) {
                    point.setPolygonId(polygonId);
                    siblingPoint.setPolygonId(polygonId);
                    this.polygons.computeIfAbsent(polygonId, k -> new HashSet<>()).add(edge);
                }

                this.addEdge(edge);
            }
            polygonId += 1;
        }
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
        Point p1 = edge.getP1();
        Point p2 = edge.getP2();
        
        this.graph.computeIfAbsent(p1, k -> new HashSet<>()).add(edge);
        this.graph.computeIfAbsent(p2, k -> new HashSet<>()).add(edge);
    }


    public boolean contains(Point point) {
        return this.graph.containsKey(point);
    }

    public boolean contains(Edge edge) {
        return this.edges.contains(edge);
    }

    public Set<Edge> getEdgesForPoint(Point point) {
        return this.graph.getOrDefault(point, Collections.emptySet());
    }

    public List<Point> getAdjacentPoints(Point point) {
        Set<Edge> pointEdges = this.getEdgesForPoint(point);
        List<Point> adjacentPoints = new ArrayList<>();
        for (Edge edge : pointEdges) {
            adjacentPoints.add(edge.getAdjacent(point));
        }
        return adjacentPoints;
    }

    public List<Point> getPoints() {
        return new ArrayList<>(this.graph.keySet());
    }

    public Set<Edge> getEdges() {
        return this.edges;
    }

    public Map<Integer, Set<Edge>> getPolygons() {
        return this.polygons;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Point, Set<Edge>> entry : this.graph.entrySet()) {
            sb.append("\n").append(entry.getKey()).append(": ");
            for (Edge edge : entry.getValue()) {
                sb.append(edge.toString()).append(" ");
            }
        }
        return sb.toString();
    }

    public String toRepr() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{\n");
        for (Map.Entry<Point, Set<Edge>> entry : this.graph.entrySet()) {
            sb.append("  ").append(entry.getKey().toRepr()).append(": ");
            for (Edge edge : entry.getValue()) {
                sb.append(edge.toRepr()).append(" ");
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public List<Edge> getEdgesFrom(Point point) {
        List<Edge> result = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getP1().equals(point) || edge.getP2().equals(point)) {
                result.add(edge);
            }
        }
        return result;
    }
    public boolean areAdjacent(Point p1, Point p2) {
        for (Edge edge : edges) {
            if ((edge.getP1().equals(p1) && edge.getP2().equals(p2)) ||
                (edge.getP1().equals(p2) && edge.getP2().equals(p1))) {
                return true;
            }
        }
        return false;
    }
    public List<Edge> getPolygon(int polygonId) {
        Set<Edge> polygonEdges = polygons.get(polygonId);
        if (polygonEdges == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(polygonEdges);
    }
}
