package ca.pluszero.emotive.models;

public class DailyWeather {
    private final String summary;

    public DailyWeather(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }
}
