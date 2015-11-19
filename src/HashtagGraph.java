import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class HashtagGraph {

    private final int MIN_VERTICES_IN_SUBGRAPH = 2;
    private final int MAX_VERTICES_IN_SUBGRAPH = 10;

    private TweetSet tweets;

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

    public HashtagGraph(TweetSet tweets) {
        this.tweets = tweets;

        for (Tweet tweet : tweets.getTweets()) {
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

    public HashtagGraph(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, TweetSet tweets) {
        this.graph = graph;
        this.tweets = tweets;
    }

    public List<HashtagGraph> getSubgraphsWithHighestDensities(int howMany) {
        List<HashtagGraph> subgraphs = new ArrayList<HashtagGraph>();
        List<String> verticesToRemove = new ArrayList<String>();

        for (int k = 0; k < howMany; k++) {
            HashtagGraph mGraph = this.getDeepCopy();
            mGraph.graph.removeAllVertices(verticesToRemove);

            int graphSize = mGraph.graph.vertexSet().size();

            // density in each step of removing a vertex with lowest degree
            Map<Integer, Double> steps = new HashMap<Integer, Double>();
            List<String> removedVertices = new LinkedList<String>();

            int step = 0;
            double density;
            String removedVertexName;

            while (removedVertices.size() < graphSize) {
                int subgraphSize = mGraph.graph.vertexSet().size();

                if (subgraphSize >= this.MIN_VERTICES_IN_SUBGRAPH && subgraphSize <= this.MAX_VERTICES_IN_SUBGRAPH) {
                    density = mGraph.getGraphDensity();
                    steps.put(step, density);
                }

                removedVertexName = mGraph.removeVertexWithLowestDegree();
                removedVertices.add(removedVertexName);

                step++;
            }

            // find the step with the highest density
            Map<Integer, Double> sortedSteps = Utils.sortByValue(steps);
            List<Integer> sortedStepsList = new ArrayList<Integer>(sortedSteps.keySet());

            int step2 = sortedStepsList.get(0);
            Set<String> vertexSet = new HashSet<String>();

            ListIterator<String> it = removedVertices.listIterator(removedVertices.size());
            Map<String, Double> preVerticesToRemove = new HashMap<String, Double>();

            for (int j = step2; j < removedVertices.size(); j++) {
                String vertex = it.previous();
                vertexSet.add(vertex);

                double weightRatio = this.getVertexDegree(vertex) / this.graph.edgesOf(vertex).size();
                preVerticesToRemove.put(vertex, weightRatio);
            }

            Map<String, Double> sortedPreVerticesToRemove = Utils.sortByValue(preVerticesToRemove);
            List<String> sortedPreVerticesToRemoveList = new ArrayList<String>(sortedPreVerticesToRemove.keySet());
            ListIterator<String> preIt = sortedPreVerticesToRemoveList.listIterator(sortedPreVerticesToRemoveList.size());

            for (int i = 0; i < sortedPreVerticesToRemoveList.size() / 2; i++) {
                String vertex = preIt.previous();
                verticesToRemove.add(vertex);
            }

            HashtagGraph subgraph = this.buildSubgraph(vertexSet);
            subgraphs.add(subgraph);

            System.out.println(subgraph);
        }

        return subgraphs;
    }

    public int getUsersCount() {
        Set<String> usersUnion = new HashSet<String>();

        for (String hashtag : graph.vertexSet()) {
            Set<String> users = tweets.getUsersForHashtag(hashtag);
            usersUnion.addAll(users);
        }

        return usersUnion.size();
    }

    public int getTweetsCount() {
        Set<Tweet> tweetsUnion = new HashSet<Tweet>();

        for (String hashtag : graph.vertexSet()) {
            Set<Tweet> tweets = this.tweets.getTweetsForHashtag(hashtag);
            tweetsUnion.addAll(tweets);
        }

        int total = 0;

        for (Tweet tweet : tweetsUnion) {
            total += tweets.getDuplicatesCountForTweet(tweet);
        }

        return total;
    }

    // reference: https://www.quora.com/How-do-you-compute-the-density-of-a-weighted-graph
    public double getGraphDensity() {
        double density = 0.0D;

        double doubleWeightSum = 2 * this.getEdgeSetSum();
        int vertexCount = this.graph.vertexSet().size();
        int possibleVertexCount = vertexCount * (vertexCount - 1);

        if (possibleVertexCount != 0) {
            density = doubleWeightSum / possibleVertexCount;
        }

        return density;
    }

    @Override
    public String toString() {
        return graph.vertexSet().toString();
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
        double sum = 0.0D;

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

    private String removeVertexWithLowestDegree() {
        List<Vertex> vertices = this.getSortedVertices();
        Vertex vertex = vertices.get(0);

        graph.removeVertex(vertex.name);

        return vertex.name;
    }

    private HashtagGraph getDeepCopy() {
        return new HashtagGraph(tweets);
    }

    private HashtagGraph buildSubgraph(Set<String> vertices) {
        // build subgraph
        SimpleWeightedGraph<String, DefaultWeightedEdge> subgraph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        for (String vertex : vertices) {
            subgraph.addVertex(vertex);
        }

        for (String vertex : vertices) {
            Set<DefaultWeightedEdge> edges = this.graph.edgesOf(vertex);

            for (DefaultWeightedEdge edge : edges) {
                String source = this.graph.getEdgeSource(edge);
                String target = this.graph.getEdgeTarget(edge);

                if (subgraph.containsVertex(source) && subgraph.containsVertex(target)) {
                    double weight = this.graph.getEdgeWeight(edge);
                    DefaultWeightedEdge newEdge = subgraph.addEdge(source, target);

                    if (newEdge == null) {
                        newEdge = subgraph.getEdge(source, target);
                    }

                    subgraph.setEdgeWeight(newEdge, weight);
                }
            }
        }

        return new HashtagGraph(subgraph, this.tweets);
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