package ca.pluszero.emotive.models;

public class YouTubeVideo extends BaseModel {

    private final String id;
    private final String name;
    private final String thumbnailUrl;
    private final int viewCount;
    private final String channelName;
    private final String duration;
    private final String publishedDate;

    public YouTubeVideo(String id, String name, String thumbnailUrl, int viewCount, String channelName, String duration, String publishedDate) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.viewCount = viewCount;
        this.channelName = channelName;
        this.duration = duration;
        this.publishedDate = publishedDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDuration() {
        return duration;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}
