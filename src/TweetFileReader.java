import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TweetFileReader {

    public static List<Tweet> readFile(String filename) throws IOException {
        List<Tweet> tweets = new ArrayList<Tweet>();
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
