package ca.pluszero.emotive.models;

public class YelpData {
    private final String businessName;
    private final int distanceInKm;
    private final String mobileUrl;
    private final String ratingImageUrl;
    private final int reviewCount;
    private final String thumbnailImageUrl;
    private final String phoneNumber;
    private final String displayPhoneNumber;

    public YelpData(String businessName, int distanceInKm, String mobileUrl, String ratingImageUrl, int reviewCount, String thumbnailImageUrl, String phoneNumber, String displayPhoneNumber) {
        this.businessName = businessName;
        this.distanceInKm = distanceInKm;
        this.mobileUrl = mobileUrl;
        this.ratingImageUrl = ratingImageUrl;
        this.reviewCount = reviewCount;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.phoneNumber = phoneNumber;
        this.displayPhoneNumber = displayPhoneNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public String getRatingImageUrl() {
        return ratingImageUrl;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDisplayPhoneNumber() {
        return displayPhoneNumber;
    }
}
