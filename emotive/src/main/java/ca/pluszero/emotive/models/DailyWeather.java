package ca.pluszero.emotive.models;

import ca.pluszero.emotive.utils.ConversionUtils;

public class DailyWeather {
    private final String summary;
    private final int temperatureInFahrenheit;
    private final int apparentTemperatureInFahrenheit;
    private final int humidity;
    private final int precipitationPercentage;
    private WeatherIcon icon;

    public DailyWeather(String summary, int temperatureInFahrenheit, int apparentTemperatureInFahrenheit, int humidity, int precip, WeatherIcon icon) {
        this.summary = summary;
        this.temperatureInFahrenheit = temperatureInFahrenheit;
        this.apparentTemperatureInFahrenheit = apparentTemperatureInFahrenheit;
        this.humidity = humidity;
        this.precipitationPercentage = precip;
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

    public int getApparentTemperatureInCelsius() {
        return ConversionUtils.fahrenheitToCelsius(apparentTemperatureInFahrenheit);
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPrecipitationPercentage() {
        return precipitationPercentage;
    }
}
