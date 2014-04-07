package ca.pluszero.emotive.managers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.R;
import ca.pluszero.emotive.models.YouTubeVideo;
import ca.pluszero.emotive.utils.DateTimeUtils;

public class YouTubeManager {

    public static final int[] COLORS = {R.color.red, R.color.win8_pink};
    // private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static final String API_KEY = ApiKeys.GOOGLE_KEY;
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String currentQuery;
    private OnFinishedListener listener;
    private String nextPageToken = "";
    private JsonHttpResponseHandler firstResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                final JSONArray searchJsonItems = response.getJSONArray("items");
                final String currentNextPageToken = nextPageToken;
                nextPageToken = response.getString("nextPageToken");
                final List<YouTubeVideo> moreVideos = new ArrayList<YouTubeVideo>();
                for (int i = 0; i < searchJsonItems.length(); i++) {
                    final JSONObject videoObject = searchJsonItems.getJSONObject(i);
                    final String videoId = videoObject.getJSONObject("id").getString(
                            "videoId");
                    // Get statistics; specifically # of views
                    YouTubeManager.getYouTubeVideo(videoId, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public synchronized void onSuccess(JSONObject response) {
                            try {
                                addYoutubeVideo(response, videoObject, moreVideos, videoId);
                                if (moreVideos.size() == searchJsonItems.length()) {
                                    // If next token is empty string, there are no more videos to load
                                    if (currentNextPageToken.isEmpty()) {
                                        listener.onInitialYoutubeQueryFinished(moreVideos);
                                    } else {
                                        listener.onMoreVideosReceived(moreVideos);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public YouTubeManager(OnFinishedListener listener) {
        this.listener = listener;
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler, Type type) {
        // Put API_KEY in params as well

        params.put("key", API_KEY);
        if (type == Type.VIDEO) {
            client.get(getAbsoluteVideoUrl(url), params, responseHandler);
        } else {
            client.get(getAbsoluteSearchUrl(url), params, responseHandler);
        }
    }

    // GET request with API endpoint signified by url, and params
    // other than the API key specified as a RequestParams

    public static void getYouTubeVideo(String videoId, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("part", "snippet,statistics,contentDetails");
        params.put("id", videoId); // Specify query param
        get("", params, responseHandler, Type.VIDEO);

    }

    private static String getAbsoluteVideoUrl(String relativeUrl) {
        return BASE_URL + "/videos/" + relativeUrl;
    }

    private static String getAbsoluteSearchUrl(String relativeUrl) {
        return BASE_URL + "/search/" + relativeUrl;
    }

    private void addYoutubeVideo(JSONObject response, JSONObject videoObject, List<YouTubeVideo> moreVideos, String videoId) throws JSONException {
        JSONArray jsonItems = response.getJSONArray("items");
        JSONObject item = jsonItems.getJSONObject(0);
        String duration = formatDuration(item);
        int viewCount = Integer.parseInt(item.getJSONObject(
                "statistics").getString("viewCount"));
        JSONObject snippet = videoObject.getJSONObject("snippet");
        String videoName = snippet.getString("title");
        String channelName = snippet.getString("channelTitle");
        // get # of views
        String publishedISODate = snippet.getString("publishedAt");
        try {
            Date publishedDate = ISO_DATE_FORMAT.parse(publishedISODate);
            publishedISODate = DateFormat.getDateInstance().format(publishedDate);
        } catch (ParseException e) {
            // TODO: Failed to parse.
        }
        String thumbnailUrl = snippet.getJSONObject("thumbnails")
                .getJSONObject("medium").getString("url");
        moreVideos.add(new YouTubeVideo(videoId, videoName,
                thumbnailUrl, viewCount, channelName, duration, publishedISODate));
    }

    private String formatDuration(JSONObject item) throws JSONException {
        return DateTimeUtils.parseDuration(item.getJSONObject("contentDetails").getString("duration"));
    }

    public void getYouTubeSearch(String query) {
        currentQuery = query;
        RequestParams params = new RequestParams();
        params.put("part", "snippet");
        params.put("q", currentQuery); // Specify query param
        params.put("type", "video");
        if (!nextPageToken.isEmpty()) {
            params.put("pageToken", nextPageToken);
        }
        get("", params, firstResponseHandler, Type.SEARCH);
        // TODO: filter using fields to get only the fields required
        // https://developers.google_icon.com/youtube/v3/getting-started#part
    }

    public void loadMoreDataFromApi() {
        getYouTubeSearch(currentQuery);
    }

    public void clearNextPageToken() {
        nextPageToken = "";
    }

    private static enum Type {
        SEARCH, VIDEO;
    }

    public interface OnFinishedListener {
        public void onInitialYoutubeQueryFinished(List<YouTubeVideo> videos);

        public void onMoreVideosReceived(List<YouTubeVideo> videos);
    }
}