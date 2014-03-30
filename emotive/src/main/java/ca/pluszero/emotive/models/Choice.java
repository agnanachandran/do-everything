package ca.pluszero.emotive.models;

import ca.pluszero.emotive.R;

public enum Choice {
    FOOD("Eat", "What do you want to eat?", R.drawable.primary_find_selector),
    LISTEN("Listen", "Search for a song, artist, or album", R.drawable.primary_music_selector),
    GOOGLE("Learn", "Google search", R.drawable.primary_google_selector),
    FIND("Find", "Find a place", R.drawable.primary_find_selector),
    YOUTUBE("Watch", "Search YouTube", R.drawable.primary_watch_selector),
    WEATHER("Weather", "Search for a city", R.drawable.primary_weather_selector);

    private final String mainInfo;
    private final String title;
    private final int drawableId;
//    private String id;
    private int timesTapped;

    Choice(String title, String mainInfo, int drawableId) {
        this.title = title;
        this.mainInfo = mainInfo;
        this.drawableId = drawableId;
    }

    public static Choice getEnumForTitle(String title) {
        for (Choice value : Choice.values()) {
            if (value.getTitle().equals(title)) {
                return value;
            }
        }
        return FOOD; // Default; should never ever happen.
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public String getTitle() {
        return title;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public int getTimesTapped() {
        return timesTapped;
    }

    public int getSelector() {
        return this.drawableId;
    }

    public void setTimesTapped(int timesTapped) {
        this.timesTapped = timesTapped;
    }
}
