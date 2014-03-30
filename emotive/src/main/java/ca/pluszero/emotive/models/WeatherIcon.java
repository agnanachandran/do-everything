package ca.pluszero.emotive.models;

import ca.pluszero.emotive.R;

public enum WeatherIcon {
    CLEAR_DAY("clear-day", R.drawable.weather_clear_day),
    CLEAR_NIGHT("clear-night", R.drawable.weather_clear_night),
    RAIN("rain", R.drawable.weather_rain),
    SNOW("snow", R.drawable.weather_snow),
    SLEET("sleet", R.drawable.weather_sleet),
    WIND("wind", R.drawable.weather_wind),
    FOG("fog", R.drawable.weather_fog),
    CLOUDY("cloudy",R.drawable.weather_cloudy),
    PARTLY_CLOUDY_DAY("partly-cloudy-day", R.drawable.weather_partly_cloudy_day),
    PARTLY_CLOUDY_NIGHT("partly-cloudy-night", R.drawable.weather_partly_cloudy_night);
    private final String name;
    private final int drawableId;

    WeatherIcon(String name, int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }

    public static WeatherIcon getEnumForString(String iconName) {
        for (WeatherIcon weatherIcon : WeatherIcon.values()) {
            if (weatherIcon.getName().equals(iconName)) {
                return weatherIcon;
            }
        }
        return CLEAR_DAY; // Should never occur
    }

    public String getName() {
        return name;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
