import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RawDataProcessor {

    public static Tweet process(String tweet) {
        Object obj = JSONValue.parse(tweet);

        return new Tweet((JSONObject) obj);
    }
}
