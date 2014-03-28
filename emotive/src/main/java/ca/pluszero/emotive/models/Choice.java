package ca.pluszero.emotive.models;

public enum Choice {
    FOOD("Eat", "What do you want to eat?"),
    LISTEN("Listen", "Search for a song, artist, or album"),
    GOOGLE("Learn", "Google search"),
    FIND("Find", "Find a place"),
    YOUTUBE("Watch", "Search YouTube"),
    WEATHER("Weather", "Search for a city");

    private final String mainInfo;
    private final String title;
    private String id;
    private int timesTapped;

    Choice(String title, String mainInfo) {
        this.title = title;
        this.mainInfo = mainInfo;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimesTapped() {
        return timesTapped;
    }

    public void setTimesTapped(int timesTapped) {
        this.timesTapped = timesTapped;
    }

    public static Choice getEnumForTitle(String title) {
        for (Choice value : Choice.values()) {
            if (title.equals(title)) {
                return value;
            }
        }
        return FOOD;
    }
}
