package ca.pluszero.emotive.models;

import android.telephony.PhoneNumberUtils;

import java.text.DecimalFormat;

public class YelpData extends BaseModel {
    private final String businessName;
    private final float distanceInKm;
    private final String mobileUrl;
    private final String ratingImageUrl;
    private final int reviewCount;
    private final String thumbnailImageUrl;
    private final String phoneNumber;
    private final String displayPhoneNumber;
    private final String address;
    private DecimalFormat twoDecimalPlaces = new DecimalFormat("#.##");


    public YelpData(String businessName, float distanceInKm, String mobileUrl, String ratingImageUrl, int reviewCount, String thumbnailImageUrl, String phoneNumber, String displayPhoneNumber, String address) {
        this.businessName = businessName;
        this.distanceInKm = distanceInKm;
        this.mobileUrl = mobileUrl;
        this.ratingImageUrl = ratingImageUrl;
        this.reviewCount = reviewCount;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.phoneNumber = phoneNumber;
        this.displayPhoneNumber = displayPhoneNumber;
        this.address = address;
    }

    public String getBusinessName() {
        return businessName;
    }

    public float getDistanceInKm() {
        return distanceInKm;
    }

    public float getDistanceInMiles() {
        return getDistanceInKm() / 1.609f;
    }

    public String getFormattedDistanceInKm() {
        return twoDecimalPlaces.format(getDistanceInKm());
    }

    public String getFormattedDistanceInMiles() {
        return twoDecimalPlaces.format(getDistanceInMiles());
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
        return PhoneNumberUtils.formatNumber(displayPhoneNumber);
    }

    public String getAddress() {
        return address;
    }

}
