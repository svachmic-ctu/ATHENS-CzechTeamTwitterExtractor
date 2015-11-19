import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Set;
import java.util.TreeSet;

public class Tweet {

    protected Set<String> hashtags;

    protected String user;

    public Tweet(JSONObject json) {
        hashtags = new TreeSet<String>();

        JSONObject entities = (JSONObject) json.get("entities");

        for (Object hashtag : (JSONArray) entities.get("hashtags")) {
            String hashtagStr = ((String) ((JSONObject) hashtag).get("text")).toLowerCase();
            hashtags.add(hashtagStr);
        }

        JSONObject user = (JSONObject) json.get("user");
        this.user = (Long) user.get("id") + "";
    }


    public Set<String> getHashtags() {
        return hashtags;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return hashtags.toString();
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
