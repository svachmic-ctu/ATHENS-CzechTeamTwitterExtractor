package cz.cvut.athens;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TweetFileReader {

    public static TweetSet readFile(String filename) throws IOException {
        TweetSet tweets = new TweetSet();

        return TweetFileReader.readFile(filename, tweets);
    }

    public static TweetSet readFile(String filename, TweetSet tweets) throws IOException {
        FileInputStream fstream = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        while ((strLine = br.readLine()) != null) {
            Tweet tw = RawDataProcessor.process(strLine);

            if (tw != null) {
                tweets.add(tw);
            }
        }

        br.close();

        return tweets;
    }

    public static TweetSet readDirectory(String directory) throws IOException {
        List<String> filenames = TweetFileReader.getFilenamesFromDirectory(directory);
        TweetSet tweets = new TweetSet();

        for (String filename : filenames) {
            TweetFileReader.readFile(directory + "/" + filename, tweets);
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
