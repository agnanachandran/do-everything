package ca.pluszero.emotive.models;

import ca.pluszero.emotive.utils.ConversionUtils;

public class DailyWeather {
    private final String summary;
    private final int temperatureInFahrenheit;
    private final int apparentTemperatureInFahrenheit;
    private WeatherIcon icon;

    public DailyWeather(String summary, int temperatureInFahrenheit, int apparentTemperatureInFahrenheit, WeatherIcon icon) {
        this.summary = summary;
        this.temperatureInFahrenheit = temperatureInFahrenheit;
        this.apparentTemperatureInFahrenheit = apparentTemperatureInFahrenheit;
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public int getTemperatureInCelsius() {
        return ConversionUtils.fahrenheitToCelsius(temperatureInFahrenheit);
    }

    public int getTemperatureInFahrenheit() {
        return temperatureInFahrenheit;
    }

    public WeatherIcon getIcon() {
        return icon;
    }

    public int getApparentTemperatureInFahrenheit() {
        return apparentTemperatureInFahrenheit;
    }
}
