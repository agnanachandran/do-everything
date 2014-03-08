package ca.pluszero.emotive;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PlacesClient {
    
//    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static final String API_KEY = ApiKeys.GOOGLE_KEY;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

    private static AsyncHttpClient client = new AsyncHttpClient();

    // GET request with API endpoint signified by url, and params
    // other than the API key specified as a RequestParams
    public static void get(String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {

        // Put API_KEY in params as well
        params.put("key", API_KEY);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    
    public static void getPlacesSearch(String query, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("input", query);
        params.put("sensor", "true");
        get("", params, responseHandler);
    }
    
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + "/"+ relativeUrl;
    }
    
    public PlacesClient() { }

}
