import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class Tweet {

    protected String text;

    protected Set<String> hashtags;

    public Tweet(JSONObject json) {
        text = (String) json.get("text");
        hashtags = new HashSet<String>();

        JSONObject entities = (JSONObject) json.get("entities");

        for (Object hashtag : (JSONArray) entities.get("hashtags")) {
            String hashtagStr = (String) ((JSONObject) hashtag).get("text");
            hashtags.add(hashtagStr);
        }
    }

    public String getText() {
        return text;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }
}
