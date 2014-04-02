package ca.pluszero.emotive.managers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.models.Forecast;
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
                int humidity = (int) Math.round(100 * currently.getDouble("humidity"));
                int precipitation = (int) Math.round(100 * currently.getDouble("precipProbability"));
                String iconName = currently.getString("icon");
                WeatherIcon weatherIcon = WeatherIcon.getEnumForString(iconName);


                List<Forecast.HourlyWeather> hourlyWeatherList = new ArrayList<Forecast.HourlyWeather>();

                // Deal with hourly forecast
                JSONArray hourlyData = response.getJSONObject("hourly").getJSONArray("data");
                // TODO: check for exceptions; stop when no more data left. Or rather, safeguard against it (also; just retrieve a few, i.e., make i go up to 10 or so).
                for (int i = 0; i < 15; i++) {
                    JSONObject hourData = hourlyData.getJSONObject(i);
                    Forecast.Temperature temperature = new Forecast.Temperature((int) Math.round(hourData.getDouble("temperature")));
                    String hourlyIconName = hourData.getString("icon");
                    WeatherIcon hourlyWeatherIcon = WeatherIcon.getEnumForString(hourlyIconName);
                    int timestamp = 1000 * hourData.getInt("time"); // time in seconds needs to be converted to ms
                    hourlyWeatherList.add(new Forecast.HourlyWeather(temperature, hourlyWeatherIcon, timestamp));
                }

                listener.onWeatherQueryFinished(new Forecast(summary, temperatureInFahrenheit, apparentTemperatureInFahrenheit, humidity, precipitation, hourlyWeatherList, weatherIcon));
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
        get(BASE_URL + placeDetails.getLatitude() + "," + placeDetails.getLongitude(), null, responseHandler);
    }

    public interface OnFinishedListener {
        public void onWeatherQueryFinished(Forecast weatherData);
    }
}
