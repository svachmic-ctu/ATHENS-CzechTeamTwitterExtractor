import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Set;
import java.util.TreeSet;

public class Tweet {

    protected String text;

    protected Set<String> hashtags;

    public Tweet(JSONObject json) {
        text = (String) json.get("text");
        hashtags = new TreeSet<String>();

        JSONObject entities = (JSONObject) json.get("entities");

        for (Object hashtag : (JSONArray) entities.get("hashtags")) {
            String hashtagStr = ((String) ((JSONObject) hashtag).get("text")).toLowerCase();
            hashtags.add(hashtagStr);
        }
    }

    public String getText() {
        return text;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    @Override
    public String toString() {
        return text + " " + "|" + hashtags;
    }

    @Override
    public int hashCode() {
        int hashcode = 0;

        for (String hashtag : hashtags) {
            hashcode += hashtag.hashCode();
        }

        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Tweet other = (Tweet) obj;

        return hashtags.equals(other.hashtags);
    }
}
