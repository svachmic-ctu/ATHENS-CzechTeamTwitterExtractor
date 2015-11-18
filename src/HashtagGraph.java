import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HashtagGraph {
    SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    public HashtagGraph(Set<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            for (String hashtag : tweet.getHashtags()) {
                graph.addVertex(hashtag);
            }

            List<String[]> pairs = generatePairs(tweet.getHashtags());
            for (String[] pair : pairs) {
                DefaultWeightedEdge e = graph.getEdge(pair[0], pair[1]);
                double weight = e == null ? 0 : graph.getEdgeWeight(e);
                graph.addEdge(pair[0], pair[1]);

                e = graph.getEdge(pair[0], pair[1]);
                graph.setEdgeWeight(e, weight + 1);
            }

        }
    }

    public List<String[]> generatePairs(Set<String> hastags) {
        List<String> list = new ArrayList<String>(hastags);
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
}
