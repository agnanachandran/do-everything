package ca.pluszero.emotive.utils;

import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateTimeUtils {
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

    public static String formatMillisToHourOfDay(long timeInMs) {
        Calendar calendar = getCalendarFromTimeInMs(timeInMs);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // 24 hrs
        int hour = calendar.get(Calendar.HOUR); // 12 hrs
        String ampm = " am";
        if (hourOfDay >= 12) {
            ampm = " pm";
        }
        if (hourOfDay == 0 || hourOfDay == 12) {
            hour = 12;
        }
        return hour + ampm;
    }

    public static String formatMillisToDayOfWeek(long timeInMs) {
        Calendar calendar = getCalendarFromTimeInMs(timeInMs);
        int day = calendar.get(Calendar.DAY_OF_WEEK); // 24 hrs
        String dayOfWeek;
        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeek = "SUN";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "MON";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "TUE";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "WED";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "THU";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "FRI";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "SAT";
                break;
            default:
                dayOfWeek = "";
        }
        return dayOfWeek;
    }

    private static Calendar getCalendarFromTimeInMs(long timeInMs) {
        Date date = new Date(timeInMs);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
