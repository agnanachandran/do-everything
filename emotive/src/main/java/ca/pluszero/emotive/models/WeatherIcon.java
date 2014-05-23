package ca.pluszero.emotive.models;

import java.util.HashSet;
import java.util.Set;

import ca.pluszero.emotive.R;

public enum WeatherIcon {
    CLEAR_DAY( R.drawable.weather_clear_day,"01d"),
    CLEAR_NIGHT(R.drawable.weather_clear_night, "01n"),
    RAIN(R.drawable.weather_rain, "10d", "10n", "09d", "09n"),
    SNOW( R.drawable.weather_snow, "13d","13n"),
    SLEET( R.drawable.weather_sleet,"09d"),
//    WIND("wind", R.drawable.weather_wind),
    FOG( R.drawable.weather_fog,"50d", "50n"),
    CLOUDY(R.drawable.weather_cloudy,"03d", "03n"),
    PARTLY_CLOUDY_DAY( R.drawable.weather_partly_cloudy_day,"04d"),
    PARTLY_CLOUDY_NIGHT( R.drawable.weather_partly_cloudy_night,"04n");
    private final Set<String> names;
    private final int drawableId;

    WeatherIcon(int drawableId, String... iconNames) {
        names = new HashSet<String>();
        for (String name : iconNames) {
            names.add(name);
        }
        this.drawableId = drawableId;
    }

    public static WeatherIcon getEnumForString(String iconName) {
        for (WeatherIcon weatherIcon : WeatherIcon.values()) {
            if (weatherIcon.getNames().contains(iconName)) {
                return weatherIcon;
            }
        }
        return CLEAR_DAY; // Should never occur
    }

    public Set<String> getNames() {
        return names;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
