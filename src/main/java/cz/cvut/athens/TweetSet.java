package cz.cvut.athens;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TweetSet {

    Map<Tweet, Integer> tweets = new HashMap<Tweet, Integer>();

    Map<String, Set<Tweet>> tweetsByHashtag = new HashMap<String, Set<Tweet>>();

    Map<String, Set<String>> usersByHashtag = new HashMap<String, Set<String>>();

    public Set<Tweet> getTweets() {
        return tweets.keySet();
    }

    public Set<Tweet> getTweetsForHashtag(String hashtag) {
        return tweetsByHashtag.get(hashtag);
    }

    public Set<String> getUsersForHashtag(String hashtag) {
        return usersByHashtag.get(hashtag);
    }

    public int getDuplicatesCountForTweet(Tweet tweet) {
        return tweets.get(tweet);
    }

    public void add(Tweet tweet) {
        if (tweets.containsKey(tweet)) {
            int duplicates = tweets.get(tweet);
            tweets.put(tweet, duplicates + 1);
        } else {
            tweets.put(tweet, 1);
        }

        this.addTweetToTweetsByHashtag(tweet);
        this.addUserToUsersByHashtag(tweet.getUser(), tweet.getHashtags());
    }

    private void addTweetToTweetsByHashtag(Tweet tweet) {
        for (String hashtag : tweet.getHashtags()) {
            Set<Tweet> tweets;

            if (tweetsByHashtag.containsKey(hashtag)) {
                tweets = tweetsByHashtag.get(hashtag);
            } else {
                tweets = new HashSet<Tweet>();
                tweetsByHashtag.put(hashtag, tweets);
            }

            tweets.add(tweet);
        }
    }

    private void addUserToUsersByHashtag(String user, Set<String> hashtags) {
        for (String hashtag : hashtags) {
            Set<String> users;

            if (usersByHashtag.containsKey(hashtag)) {
                users = usersByHashtag.get(hashtag);
            } else {
                users = new HashSet<String>();
                usersByHashtag.put(hashtag, users);
            }

            users.add(user);
        }
    }
}
