package ca.pluszero.emotive.managers;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.pluszero.emotive.ApiKeys;
import ca.pluszero.emotive.models.YouTubeVideo;

public class YouTubeManager {

    // private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static final String API_KEY = ApiKeys.GOOGLE_KEY;
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3";
    private static YouTubeManager instance;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private OnFinishedListener listener;
    private JsonHttpResponseHandler firstResponse = new JsonHttpResponseHandler() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(JSONObject response) {
            final List<YouTubeVideo> videos = new ArrayList<YouTubeVideo>();
            try {
                final JSONArray searchJsonItems = response.getJSONArray("items");
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
                                addYoutubeVideo(response, videoObject, videos, videoId);
                                if (videos.size() == searchJsonItems.length()) {
                                    listener.onYoutubeQueryFinished(videos);
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

    private YouTubeManager(OnFinishedListener listener) {
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

    public static YouTubeManager getInstance(OnFinishedListener listener) {
        if (instance == null) {
            instance = new YouTubeManager(listener);
        }
        return instance;
    }

    private void addYoutubeVideo(JSONObject response, JSONObject videoObject, List<YouTubeVideo> videos, String videoId) throws JSONException {
        JSONArray jsonItems = response.getJSONArray("items");
        JSONObject item = jsonItems.getJSONObject(0);
        String duration = formatDuration(item);
        int viewCount = Integer.parseInt(item.getJSONObject(
                "statistics").getString("viewCount"));
        JSONObject snippet = videoObject.getJSONObject("snippet");
        String videoName = snippet.getString("title");
        String channelName = snippet.getString("channelTitle");
        // get # of views
        String thumbnailUrl = snippet.getJSONObject("thumbnails")
                .getJSONObject("medium").getString("url");
        videos.add(new YouTubeVideo(videoId, videoName,
                thumbnailUrl, viewCount, channelName, duration));
    }

    private String formatDuration(JSONObject item) throws JSONException {
        String duration = item.getJSONObject("contentDetails").getString("duration");
        duration = duration.replace("PT", "");
        duration = duration.replace('H', ':');
        duration = duration.replace('M', ':');
        duration = duration.replace("S", "");
        if (duration.length() == 2) {
            duration = "0:" + duration;
        }
        return duration;
    }

    public void getYouTubeSearch(String query) {
        RequestParams params = new RequestParams();
        params.put("part", "snippet");
        params.put("q", query); // Specify query param
        params.put("type", "video");
        get("", params, firstResponse, Type.SEARCH);
        // TODO: filter using fields to get only the fields required
        // https://developers.google_icon.com/youtube/v3/getting-started#part
    }

    private static enum Type {
        SEARCH, VIDEO;
    }

    public interface OnFinishedListener {
        public void onYoutubeQueryFinished(List<YouTubeVideo> videos);
    }
}