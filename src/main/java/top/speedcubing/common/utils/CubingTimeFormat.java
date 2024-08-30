package top.speedcubing.common.utils;

import java.util.concurrent.TimeUnit;
import top.speedcubing.lib.utils.TimeFormatter;

public class CubingTimeFormat {

    public static String toYMDHMS(String timeString) {
        return toYMDHMS(Long.parseLong(timeString));
    }

    public static String toYMDHMS(long time) {
        return TimeFormatter.unixToRealTime(time, "yyyy/MM/dd HH:mm:ss", TimeUnit.SECONDS);
    }


    public static String period(String timeString) {
        return period(Long.parseLong(timeString));
    }
    public static String period(long time) {
        return new TimeFormatter(time, TimeUnit.SECONDS).format("%D%d ", true).format("%h%h %m%m %s%s", false).toString();
    }
}
