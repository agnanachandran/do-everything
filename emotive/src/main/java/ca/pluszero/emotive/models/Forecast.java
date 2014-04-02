package ca.pluszero.emotive.models;

import java.util.List;

import ca.pluszero.emotive.utils.ConversionUtils;
import ca.pluszero.emotive.utils.DateTimeUtils;

public class Forecast {
    private final String summary;
    private final Temperature temperature;
    private final Temperature apparentTemperature;
    private final int humidity;
    private final int precipitationPercentage;
    private final List<HourlyWeather> hourlyWeatherList;
    private WeatherIcon icon;

    public Forecast(String summary, int temperatureInFahrenheit, int apparentTemperatureInFahrenheit, int humidity, int precip, List<HourlyWeather> hourlyWeatherList, WeatherIcon icon) {
        this.summary = summary;
        this.temperature = new Temperature(temperatureInFahrenheit);
        this.apparentTemperature = new Temperature(apparentTemperatureInFahrenheit);
        this.humidity = humidity;
        this.precipitationPercentage = precip;
        this.hourlyWeatherList = hourlyWeatherList;
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public int getTemperatureInCelsius() {
        return temperature.toCelsius();
    }

    public int getTemperatureInFahrenheit() {
        return temperature.getValue();
    }

    public WeatherIcon getIcon() {
        return icon;
    }

    public int getApparentTemperatureInFahrenheit() {
        return apparentTemperature.getValue();
    }

    public int getApparentTemperatureInCelsius() {
        return apparentTemperature.toCelsius();
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPrecipitationPercentage() {
        return precipitationPercentage;
    }

    public List<HourlyWeather> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public static class HourlyWeather {

        private final Temperature temp;
        private final WeatherIcon icon;
        private final String hourAsString;

        public HourlyWeather(Temperature temp, WeatherIcon icon, int timeInMs) {
            this.temp = temp;
            this.icon = icon;
            this.hourAsString = DateTimeUtils.formatMillisToHourOfDay(timeInMs);
        }

        public Temperature getTemp() {
            return temp;
        }

        public WeatherIcon getIcon() {
            return icon;
        }

        public String getHourAsString() {
            return hourAsString;
        }
    }

    public static class Temperature {

        private final int temperatureInFahrenheit;

        public Temperature(int temperatureInFahrenheit) {
            this.temperatureInFahrenheit = temperatureInFahrenheit;
        }

        public int toCelsius() {
            return ConversionUtils.fahrenheitToCelsius(temperatureInFahrenheit);
        }

        public int getValue() {
            return temperatureInFahrenheit;
        }
    }
}
