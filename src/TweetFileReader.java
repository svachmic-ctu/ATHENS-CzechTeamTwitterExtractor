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
        int count = 0;

        while ((strLine = br.readLine()) != null) {
            Tweet tw = RawDataProcessor.process(strLine);
            tweets.add(tw);

            count++;
        }

        br.close();

        System.out.println("File " + filename + " processed: " + count + " tweets.");

        return tweets;
    }

    public static Set<Tweet> readDirectory(String directory) throws IOException {
        List<String> filenames = TweetFileReader.getFilenamesFromDirectory(directory);
        Set<Tweet> tweets = new HashSet<Tweet>();

        for (String filename : filenames) {
            Set<Tweet> tweetsInFile = TweetFileReader.readFile(directory + "/" + filename);
            tweets.addAll(tweetsInFile);
        }

        return tweets;
    }

    private static List<String> getFilenamesFromDirectory(String directory) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        List<String> filenames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                filenames.add(listOfFiles[i].getName());
            }
        }

        return filenames;
    }
}
