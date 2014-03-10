package ca.pluszero.emotive.models;

public class YouTubeVideo extends BaseModel {

    private String id;
    private String name;
    private String thumbnailUrl;
    private int viewCount;
    private String channelName;
    private String duration;

    public YouTubeVideo(String id, String name, String thumbnailUrl, int viewCount, String channelName, String duration) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.viewCount = viewCount;
        this.channelName = channelName;
        this.duration = duration;
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
}
