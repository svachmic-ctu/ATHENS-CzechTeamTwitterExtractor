import java.io.IOException;
import java.util.List;

public class Main {

    private final static String FILE = "file";
    private final static String DIR = "dir";

    public static void main(String[] args) throws IOException {
        String mode = args[0];
        TweetSet tweets;

        if (mode.equals(Main.FILE)) {
            String filename = args[1];

            System.out.println("Reading file " + filename);

            tweets = TweetFileReader.readFile(filename);
        } else if (mode.equals(Main.DIR)) {
            String dirname = args[1];

            System.out.println("Reading directory " + dirname);

            tweets = TweetFileReader.readDirectory(dirname);
        } else {
            throw new RuntimeException("Invalid first argument. Use '" + Main.FILE + "' or '" + Main.DIR + "'");
        }

        int subgraphCount = Integer.parseInt(args[2]);

        System.out.println("Files processed");
        System.out.println(tweets.getTweets().size() + " unique tweets");
        System.out.println("-----------------");

        HashtagGraph graph = new HashtagGraph(tweets);

        System.out.println("Graph constructed");
        System.out.println("-----------------");

        List<HashtagGraph> subgraphs = graph.getSubgraphsWithHighestDensities(subgraphCount);

        System.out.println("-----------------");

        for (HashtagGraph subgraph : subgraphs) {
            System.out.println("Subgraph " + subgraph);
            System.out.println("Density: " + subgraph.getGraphDensity());
            System.out.println("Users: " + subgraph.getUsersCount());
            System.out.println("Tweets: " + subgraph.getTweetsCount());
        }
    }
}
