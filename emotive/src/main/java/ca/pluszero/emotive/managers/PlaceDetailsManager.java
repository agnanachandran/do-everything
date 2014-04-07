package ca.pluszero.emotive.managers;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.models.PlaceDetails;

public class PlaceDetailsManager {
    private static final String API_KEY = ApiKeys.GOOGLE_KEY;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private final OnFinishedListener listener;

    private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                // TODO: check status with response.getJSONObject("status") (it needs to be "OK").
                JSONObject geometry = response.getJSONObject("result").getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String latitude = location.getString("lat");
                String longitude = location.getString("lng");
                Log.d("TAG", "LAT: " + latitude + " LONG: " + longitude);
                listener.onPlaceDetailsQueryFinished(new PlaceDetails(latitude, longitude));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public PlaceDetailsManager(OnFinishedListener listener) {
        this.listener = listener;
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {

        client.get(url, params, responseHandler);
    }

    public void getPlaceDetailsQuery(String reference) {
        RequestParams params = new RequestParams();
        params.put("reference", reference);
        params.put("sensor", "true");
        params.put("key", API_KEY);
        get(BASE_URL, params, responseHandler);
    }

    public interface OnFinishedListener {
        public void onPlaceDetailsQueryFinished(PlaceDetails placeDetails);
    }
}
