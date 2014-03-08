package ca.pluszero.emotive.models;

public class YouTubeVideo extends BaseModel {
    
    private String id;
    private String name;
    private String thumbnailUrl;
    private int viewCount;
    private String channelName;

    public YouTubeVideo(String id, String name, String thumbnailUrl, int viewCount, String channelName) {
        this.setId(id);
        this.setName(name);
        this.setThumbnailUrl(thumbnailUrl);
        this.setViewCount(viewCount);
        this.setChannelName(channelName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String videoName) {
        this.name = videoName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

}
