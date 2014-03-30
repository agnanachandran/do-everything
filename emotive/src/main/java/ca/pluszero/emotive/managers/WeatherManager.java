package ca.pluszero.emotive.managers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.models.DailyWeather;
import ca.pluszero.emotive.models.PlaceDetails;
import ca.pluszero.emotive.models.WeatherIcon;

public class WeatherManager {
    private static final String API_KEY = ApiKeys.FORECAST_KEY;
    private static final String BASE_URL = "https://api.forecast.io/forecast/" + API_KEY + "/";
    private static WeatherManager instance;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private final OnFinishedListener listener;

    private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                JSONObject currently = response.getJSONObject("currently");
                String summary = currently.getString("summary");
                int temperatureInFahrenheit = (int) Math.round(currently.getDouble("temperature"));
                int apparentTemperatureInFahrenheit = (int) Math.round(currently.getDouble("apparentTemperature"));
                String iconName = currently.getString("icon");
                WeatherIcon weatherIcon = WeatherIcon.getEnumForString(iconName);

                listener.onWeatherQueryFinished(new DailyWeather(summary, temperatureInFahrenheit, apparentTemperatureInFahrenheit, weatherIcon));
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

        client.get(url, params, responseHandler);
    }

    // GET request with API endpoint signified by url, and params
    // other than the API key specified as a RequestParams

    public static WeatherManager getInstance(OnFinishedListener listener) {
        if (instance == null) {
            instance = new WeatherManager(listener);
        }
        return instance;
    }

    public void getWeatherQuery(PlaceDetails placeDetails) {
        get(BASE_URL + placeDetails.getLatitude()+","+placeDetails.getLongitude(), null, responseHandler);
    }

    public interface OnFinishedListener {
        public void onWeatherQueryFinished(DailyWeather weatherData);
    }
}
