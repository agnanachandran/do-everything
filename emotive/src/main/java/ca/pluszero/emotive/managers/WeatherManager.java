package ca.pluszero.emotive.managers;

import android.support.v4.app.Fragment;

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

    private static AsyncHttpClient client = new AsyncHttpClient();
    private final OnFinishedListener listener;
    private final Fragment fragment;

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

                List<Forecast.FutureWeather> hourlyWeatherList = new ArrayList<Forecast.FutureWeather>();
                List<Forecast.FutureWeather> dailyWeatherList = new ArrayList<Forecast.FutureWeather>();

                JSONArray hourlyData = response.getJSONObject("hourly").getJSONArray("data");
                // TODO: check for exceptions; stop when no more data left. Or rather, safeguard against it (also; just retrieve a few, i.e., make i go up to 10 or so).
                for (int i = 0; i < 15; i++) {
                    JSONObject hourData = hourlyData.getJSONObject(i);
                    Forecast.Temperature temperature = new Forecast.Temperature((int) Math.round(hourData.getDouble("temperature")));
                    String hourlyIconName = hourData.getString("icon");
                    WeatherIcon hourlyWeatherIcon = WeatherIcon.getEnumForString(hourlyIconName);
                    long timestamp = 1000L * hourData.getLong("time"); // time in seconds needs to be converted to ms
                    hourlyWeatherList.add(new Forecast.FutureWeather(temperature, hourlyWeatherIcon, timestamp));
                }

                JSONArray dailyData = response.getJSONObject("daily").getJSONArray("data");
                // TODO: check for exceptions; stop when no more data left. Or rather, safeguard against it (also; just retrieve a few, i.e., make i go up to 10 or so).
                for (int i = 0; i < 6; i++) {
                    JSONObject dayData = dailyData.getJSONObject(i);
                    Forecast.Temperature temperature = new Forecast.Temperature((int) Math.round(dayData.getDouble("temperatureMax")));
                    String dailyIconName = dayData.getString("icon");
                    WeatherIcon dailyWeatherIcon = WeatherIcon.getEnumForString(dailyIconName);
                    long timestamp = 1000L * dayData.getLong("time"); // time in seconds needs to be converted to ms
                    dailyWeatherList.add(new Forecast.FutureWeather(temperature, dailyWeatherIcon, timestamp));
                }

                if (fragment.isAdded()) {
                    listener.onWeatherQueryFinished(new Forecast(summary, temperatureInFahrenheit, apparentTemperatureInFahrenheit, humidity, precipitation, hourlyWeatherList, dailyWeatherList, weatherIcon));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public WeatherManager(Fragment fragment, OnFinishedListener listener) {
        this.fragment = fragment;
        this.listener = listener;
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {

        client.get(url, params, responseHandler);
    }

    public void getWeatherQuery(PlaceDetails placeDetails) {
        get(BASE_URL + placeDetails.getLatitude() + "," + placeDetails.getLongitude(), null, responseHandler);
    }

    public interface OnFinishedListener {
        public void onWeatherQueryFinished(Forecast weatherData);
    }
}
