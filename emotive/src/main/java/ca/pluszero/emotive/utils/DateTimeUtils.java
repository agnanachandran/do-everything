package ca.pluszero.emotive.utils;

import android.text.format.Time;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static int getCurrentTimeInHours() {
        Time now = new Time();
        now.setToNow();
        return now.hour;
    }

    public static String getGreetingBasedOnTimeOfDay() {
        int hours = getCurrentTimeInHours();
        Log.d("TAG", hours + "");
        if (hours <= 3 || hours >= 18) {
            return "Good evening";
        } else if (hours >= 4 && hours <= 11) {
            return "Good morning";
        } else {
            return "Good afternoon";
        }
    }

    public static String formatMillis(String millis) {
        int total = Integer.parseInt(millis);
        Date date = new Date(total);
        DateFormat formatter = new SimpleDateFormat("m:ss");
        return formatter.format(date);
    }
}
