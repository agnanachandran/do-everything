package ca.pluszero.emotive.models;

import ca.pluszero.emotive.R;

public enum Choice {
    FOOD("Eat", "What do you want to eat?", R.drawable.primary_food_selector, true),
    LISTEN("Listen", "Search for a song, artist, or album", R.drawable.primary_music_selector, true),
    GOOGLE("Learn", "Google search", R.drawable.primary_google_selector, true),
    FIND("Find", "Find a place", R.drawable.primary_find_selector, true),
    YOUTUBE("Watch", "Search YouTube", R.drawable.primary_watch_selector, true),
    WEATHER("Weather", "Search for a city", R.drawable.primary_weather_selector, false);

    private final String mainInfo;
    private final String title;
    private final int drawableId;
    private final boolean shouldImmediatelyShowKeyboard;
    private int timesTapped;

    Choice(String title, String mainInfo, int drawableId, boolean shouldImmediatelyShowKeyboard) {
        this.title = title;
        this.mainInfo = mainInfo;
        this.drawableId = drawableId;
        this.shouldImmediatelyShowKeyboard = shouldImmediatelyShowKeyboard;
    }

    public static Choice getEnumForTitle(String title) {
        for (Choice value : Choice.values()) {
            if (value.getTitle().equals(title)) {
                return value;
            }
        }
        return FOOD; // Default; should never ever happen.
    }

    public static Choice getEnumForString(String enumValueString) {
        for (Choice value : Choice.values()) {
            if (value.toString().equals(enumValueString)) {
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

    public int getTimesTapped() {
        return timesTapped;
    }

    public void setTimesTapped(int timesTapped) {
        this.timesTapped = timesTapped;
    }

    public int getSelector() {
        return this.drawableId;
    }

    public boolean shouldImmediatelyShowKeyboard() {
        return shouldImmediatelyShowKeyboard;
    }
}
