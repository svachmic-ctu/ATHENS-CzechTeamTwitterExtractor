import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class HashtagGraph {

    private Set<String> ommitedVertices = new HashSet<String>();

    SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    private final Comparator<Vertex> byDegree = new Comparator<Vertex>() {
        public int compare(Vertex o1, Vertex o2) {
            if (o1.degree == o2.degree) {
                return 0;
            }

            if (o1.degree > o2.degree) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    public HashtagGraph(Set<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            for (String hashtag : tweet.getHashtags()) {
                graph.addVertex(hashtag);
            }

            List<String[]> pairs = generatePairs(tweet.getHashtags());
            for (String[] pair : pairs) {
                DefaultWeightedEdge e = graph.getEdge(pair[0], pair[1]);
                double weight = e == null ? 0.0D : graph.getEdgeWeight(e);
                graph.addEdge(pair[0], pair[1]);

                e = graph.getEdge(pair[0], pair[1]);
                graph.setEdgeWeight(e, weight + 1);
            }
        }
    }

    private List<String[]> generatePairs(Set<String> hashtags) {
        List<String> list = new ArrayList<String>(hashtags);
        List<String[]> pairs = new ArrayList<String[]>();

        for (int i = 0; i < list.size(); i++) {
            String first = list.get(i);

            for (int j = i + 1; j < list.size(); j++) {
                String second = list.get(j);

                String[] pair = {first, second};
                pairs.add(pair);
            }
        }

        return pairs;
    }

    // helper methods

    // reference: https://www.quora.com/How-do-you-compute-the-density-of-a-weighted-graph
    private double getGraphDensity() {
        double density = 0.0D;

        double doubleWeightSum = 2 * this.getEdgeSetSum();
        int vertexCount = this.graph.vertexSet().size();
        int possibleVertexCount = vertexCount * (vertexCount - 1);

        if (possibleVertexCount != 0) {
            density = doubleWeightSum / possibleVertexCount;
        }

        return density;
    }

    private double getVertexDegree(String vertex) {
        double degree = 0;
        Set<DefaultWeightedEdge> edges = this.graph.edgesOf(vertex);

        for (DefaultWeightedEdge e : edges) {
            degree += this.graph.getEdgeWeight(e);
        }

        return degree;
    }

    private double getEdgeSetSum() {
        Set<DefaultWeightedEdge> edges = this.graph.edgeSet();
        double sum = 1.0D;

        for (DefaultWeightedEdge e : edges) {
            sum += this.graph.getEdgeWeight(e);
        }

        return sum;
    }

    private List<Vertex> getSortedVertices() {
        List<Vertex> vertices = new ArrayList<Vertex>();
        Set<String> vertexSet = this.graph.vertexSet();

        for (String vertex : vertexSet) {
            double degree = this.getVertexDegree(vertex);
            Vertex v = new Vertex(degree, vertex);
            vertices.add(v);
        }

        vertices.sort(byDegree);
        return vertices;
    }

    private class Vertex {
        String name;
        double degree;

        public Vertex(double degree, String name) {
            this.degree = degree;
            this.name = name;
        }
    }
}