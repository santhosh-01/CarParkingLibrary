package core;

import java.time.LocalTime;

// Utility Class
public class TimeFormat {

    private TimeFormat() {
    }

    public static String getTime(LocalTime time) {
        return String.format("%02d:%02d:%02d",time.getHour(),time.getMinute(),time.getSecond());
    }

}
