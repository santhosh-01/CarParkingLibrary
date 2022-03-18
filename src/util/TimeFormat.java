package util;

import java.time.LocalTime;

public class TimeFormat {

    public static String getTime(LocalTime time) {
        return String.format("%02d:%02d:%02d",time.getHour(),time.getMinute(),time.getSecond());
    }

}
