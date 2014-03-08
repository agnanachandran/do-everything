package ca.pluszero.emotive;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ca.pluszero.emotive.models.YouTubeVideo;

public class YouTubeClient {

    private static enum Type {
        SEARCH, VIDEO;
    }

    // private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static final String API_KEY = ApiKeys.GOOGLE_KEY;
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3";

    private static AsyncHttpClient client = new AsyncHttpClient();

    // GET request with API endpoint signified by url, and params
    // other than the API key specified as a RequestParams
    public static void get(String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler, Type type) {
        // Put API_KEY in params as well

        params.put("key", API_KEY);
        if (type == Type.VIDEO) {
           client.get(getAbsoluteVideoUrl(url), params, responseHandler); 
        } else {
            client.get(getAbsoluteSearchUrl(url), params, responseHandler);
        }
    }

    public static void getYouTubeSearch(String query, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("part", "snippet");
        params.put("q", query); // Specify query param
        params.put("type", "video");
        get("", params, responseHandler, Type.SEARCH);
        // TODO: filter using fields to get only the fields required
        // https://developers.google.com/youtube/v3/getting-started#part
    }

    public static void getYouTubeVideo(String videoId, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("part", "snippet,statistics");
        params.put("id", videoId); // Specify query param
        get("", params, responseHandler, Type.VIDEO);

    }

    private static String getAbsoluteVideoUrl(String relativeUrl) {
        return BASE_URL + "/videos/" + relativeUrl;
    }

    private static String getAbsoluteSearchUrl(String relativeUrl) {
        return BASE_URL + "/search/" + relativeUrl;
    }

    public YouTubeClient() {
    }

    public static void parseVideos(final List<YouTubeVideo> videos, JSONObject response)
            throws JSONException {

    }
}