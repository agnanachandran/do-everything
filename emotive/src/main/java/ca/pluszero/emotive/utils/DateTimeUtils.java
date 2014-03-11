package ca.pluszero.emotive.utils;

/**
 * Created by chronos on 2014-03-10.
 */
public abstract class DateTimeUtils {
    public static String parseDuration(String duration) {
        duration = duration.replace("PT", "");
        duration = duration.replace('H', ':');
        duration = duration.replace('M', ':');
        duration = duration.replace("S", "");
        String[] splits = duration.split(":");
        duration = "";
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].length() == 1 && i != 0) {
                splits[i] = "0" + splits[i];
            }
            duration += splits[i];
            if (i != splits.length - 1) {
                duration += ":";
            }
        }
        if (duration.length() == 1) {
            duration = "0" + duration;
        }
        if (duration.length() == 2) {
            duration = "0:" + duration;
        }
        return duration;
    }
}
