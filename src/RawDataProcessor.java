import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RawDataProcessor {

    public static Tweet process(String tweet) {
        Object obj = JSONValue.parse(tweet);

        if (obj == null) {
            return null;
        }

        return new Tweet((JSONObject) obj);
    }
}
