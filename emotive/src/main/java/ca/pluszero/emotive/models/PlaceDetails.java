package ca.pluszero.emotive.models;

public class PlaceDetails {
    private final String latitude;
    private final String longitude;

    public PlaceDetails(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
