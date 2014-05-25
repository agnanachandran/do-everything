package ca.pluszero.emotive.managers;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.models.YelpData;
import ca.pluszero.emotive.yelp.Yelp;

public class YelpManager {

    private final OnYelpFinishedListener listener;

    public interface OnYelpFinishedListener {
        public void onYelpDataRetrieved(List<YelpData> datas);
    }

    private class YelpSearch {
        private final String query;
        private final Location location;

        YelpSearch(String query, Location location) {
            this.query = query;
            this.location = location;
        }

    }

    private class DownloadFilesTask extends AsyncTask<YelpSearch, Void, List<YelpData>> {
        protected List<YelpData> doInBackground(YelpSearch... yelpSearches) {
            YelpSearch yelpSearch = yelpSearches[0];
            Yelp yelp = new Yelp(ApiKeys.YELP_CONSUMER_KEY, ApiKeys.YELP_CONSUMER_SECRET, ApiKeys.YELP_TOKEN, ApiKeys.YELP_TOKEN_SECRET);
            String body = yelp.search(yelpSearch.query, yelpSearch.location.getLatitude(), yelpSearch.location.getLongitude());
            List<YelpData> yelpDatas = new ArrayList<YelpData>();
            try {
                JSONObject yelpObject = new JSONObject(body);
                JSONArray businesses = yelpObject.getJSONArray("businesses");
                for (int i = 0; i < businesses.length(); i++) {
                    JSONObject business = businesses.getJSONObject(i);
                    String businessName  = business.getString("name");
                    double distanceInMetres = business.getDouble("distance");
                    int distanceInKm = (int) Math.round(distanceInMetres/1000);
                    String mobileUrl = business.getString("mobile_url");
                    String ratingImageUrl = business.getString("rating_img_url");
                    int reviewCount = business.getInt("review_count");
                    String thumbnailImageUrl  = business.getString("image_url");
                    String phoneNumber = business.getString("display_phone");
                    String displayPhoneNumber = business.getString("phone");
                    boolean isClosed = business.getBoolean("is_closed");
                    if (!isClosed) {
                        YelpData yelpData = new YelpData(businessName, distanceInKm, mobileUrl, ratingImageUrl, reviewCount, thumbnailImageUrl, phoneNumber, displayPhoneNumber);
                        yelpDatas.add(yelpData);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return yelpDatas;
        }

        @Override
        protected void onPostExecute(List<YelpData> yelpDatas) {
            listener.onYelpDataRetrieved(yelpDatas);
        }
    }

    public YelpManager(OnYelpFinishedListener listener) {
        this.listener = listener;
    }

    public void query(String query, Location location) {
        new DownloadFilesTask().execute(new YelpSearch(query, location));
    }

}
