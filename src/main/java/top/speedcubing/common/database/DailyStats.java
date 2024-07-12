package top.speedcubing.common.database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DailyStats {
    public static void validate() {
        String today = getToday();
        if (!Database.systemConnection.exist("daily", "date='" + today + "'"))
            Database.systemConnection.insert("daily", "date", "'" + today + "'");
    }

    public static void update(String s) {
        validate();
        Database.systemConnection.update("daily", s, "date='" + getToday() + "'");
    }

    public static String getToday() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
    }
}
