package ca.pluszero.emotive.utils;

public final class ConversionUtils {

    public static int fahrenheitToCelsius(int temperatureInFahrenheit) {
        return (int) Math.round((temperatureInFahrenheit - 32) * 5 / 9.0);
    }
}
