package ca.pluszero.emotive.managers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.models.DailyWeather;

public class WeatherManager {
    private static final String API_KEY = ApiKeys.FORECAST_KEY;
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3";
    private static WeatherManager instance;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private final OnFinishedListener listener;
    String currentQuery;

    private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                final JSONArray searchJsonItems = response.getJSONArray("items");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private WeatherManager(OnFinishedListener listener) {
        this.listener = listener;
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        // Put API_KEY in params as well

        params.put("key", API_KEY);
        client.get(getAbsoluteVideoUrl(url), params, responseHandler);
    }

    // GET request with API endpoint signified by url, and params
    // other than the API key specified as a RequestParams

    public static void getYouTubeVideo(String videoId, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("part", "snippet,statistics,contentDetails");
        params.put("id", videoId); // Specify query param
        get("", params, responseHandler);

    }

    private static String getAbsoluteVideoUrl(String relativeUrl) {
        return BASE_URL + "/videos/" + relativeUrl;
    }

    public static WeatherManager getInstance(OnFinishedListener listener) {
        if (instance == null) {
            instance = new WeatherManager(listener);
        }
        return instance;
    }

    public void getWeatherQuery(String query) {
        currentQuery = query;
        RequestParams params = new RequestParams();
        params.put("q", currentQuery); // Specify query param
        get("", params, responseHandler);
    }

    public interface OnFinishedListener {
        public void onWeatherQueryFinished(DailyWeather weatherData);
    }
}
