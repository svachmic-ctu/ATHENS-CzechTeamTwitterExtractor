import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Main {

    private final static String FILE = "file";
    private final static String DIR = "dir";
    private final static String DIRS = "dirs";

    private final static int SUBGRAPH_COUNT = 20;

    private final static String OUTPUT_FILE_EXTENSION = ".subgr";

    public static void main(String[] args) throws IOException {
        String mode = args[0];
        TweetSet tweets;

        if (mode.equals(Main.FILE)) {
            String filename = args[1];

            System.out.println("Reading file " + filename);
            System.out.println("-----------------");

            tweets = TweetFileReader.readFile(filename);
            Main.getSubgraphsAndOutputThemToFiles(tweets, filename);
        } else if (mode.equals(Main.DIR)) {
            String dirname = args[1];
            readDirectory(dirname);
        } else if (mode.equals(Main.DIRS)) {
            for (int i = 1; i < args.length; i++) {
                String dirname = args[1];
                readDirectory(dirname);
            }
        } else {
            throw new RuntimeException("Invalid first argument. Use '" + Main.FILE + "', '" + Main.DIR + "' or '" + Main.DIRS + "'");
        }
    }

    private static void readDirectory(String directory) throws IOException {
        System.out.println("Reading directory " + directory);
        System.out.println("-----------------");

        TweetSet tweets = TweetFileReader.readDirectory(directory);
        Main.getSubgraphsAndOutputThemToFiles(tweets, directory + "/out");
    }

    private static void getSubgraphsAndOutputThemToFiles(TweetSet tweets, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        HashtagGraph graph = new HashtagGraph(tweets);
        List<HashtagGraph> subgraphs = graph.getSubgraphsWithHighestDensities(Main.SUBGRAPH_COUNT);

        for (int i = 0; i < subgraphs.size(); i++) {
            Main.outputSubgraphToFile(subgraphs.get(i), filename + "_" + i);
        }
    }

    private static void outputSubgraphToFile(HashtagGraph subgraph, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename + Main.OUTPUT_FILE_EXTENSION, "UTF-8");

        writer.println(subgraph);
        writer.println("Density: " + subgraph.getGraphDensity());
        writer.println("Users: " + subgraph.getUsersCount());
        writer.println("Tweets: " + subgraph.getTweetsCount());

        writer.close();
    }
}
