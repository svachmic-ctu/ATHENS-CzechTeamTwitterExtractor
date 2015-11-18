import java.io.IOException;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        Set<Tweet> tweets = TweetFileReader.readFile(filename);
        HashtagGraph graph = new HashtagGraph(tweets);
    }
}
