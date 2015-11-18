import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TweetFileReader {

    public static Set<Tweet> readFile(String filename) throws IOException {
        Set<Tweet> tweets = new HashSet<Tweet>();
        FileInputStream fstream = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;
        while ((strLine = br.readLine()) != null) {
            Tweet tw = RawDataProcessor.process(strLine);
            tweets.add(tw);
        }

        br.close();

        return tweets;
    }
}
