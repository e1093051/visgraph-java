package visgraph;

import java.util.*;

public class VisGraph {
    private Graph graph;
    private Graph visgraph;

    public VisGraph() {
        this.graph = null;
        this.visgraph = null;
    }

    public void build(List<List<Point>> input, int workersIgnored, boolean status) {
        this.graph = new Graph(input);
        this.visgraph = new Graph(new ArrayList<>());

        List<Point> points = this.graph.getPoints();
        int batchSize = 10;

        for (int i = 0; i < points.size(); i += batchSize) {
            int end = Math.min(i + batchSize, points.size());
            List<Point> batch = points.subList(i, end);

            List<Edge> visibleEdges = new ArrayList<>();
            for (Point p1 : batch) {
                List<Point> visibles = VisibilityHelper.visibleVertices(p1, this.graph, true); // true = half-scan
                for (Point p2 : visibles) {
                    visibleEdges.add(new Edge(p1, p2));
                }
            }

            for (Edge edge : visibleEdges) {
                this.visgraph.addEdge(edge);
            }
        }
    }


    public List<Point> findVisible(Point point) {
        return VisibilityHelper.visibleVertices(point, this.graph, true);
    }

    public void update(List<Point> points, Point origin, Point destination) {
        for (Point p : points) {
            List<Point> visibles = VisibilityHelper.visibleVertices(p, this.graph, true);
            for (Point v : visibles) {
                this.visgraph.addEdge(new Edge(p, v));
            }
        }
    }

    public Graph getGraph() {
        return this.graph;
    }

    public Graph getVisgraph() {
        return this.visgraph;
    }

    public int pointInPolygon(Point point) {
        return GeometryUtils.pointInPolygon(point, this.graph);
    }

    public Point closestPoint(Point p, int polygonId) {
        return VisibilityHelper.closestPoint(p, polygonId, this.graph, 0.001);
    }

    public Point closestPoint(Point p, int polygonId, double length) {
        return VisibilityHelper.closestPoint(p, polygonId, this.graph, length);
    }
}
