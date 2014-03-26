package ca.pluszero.emotive.models;

public enum PrimaryOption {
    FOOD("Eat", "What do you want to eat?", 1),
    LISTEN("Listen", "Search for a song, artist, or album", 2),
    GOOGLE("Learn", "Google search", 3),
    FIND("Find", "Find a place", 4),
    YOUTUBE("Watch", "Search YouTube", 5),
    WEATHER("Note", "Add a note to Google Keep", 6);
    private final String mainInfo;
    private final String title;
    private final int optionNumber;

    PrimaryOption(String title, String mainInfo, int optionNumber) {
        this.title = title;
        this.mainInfo = mainInfo;
        this.optionNumber = optionNumber;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public String getTitle() {
        return title;
    }

    public int getOptionNumber() {
        return optionNumber;
    }
}
