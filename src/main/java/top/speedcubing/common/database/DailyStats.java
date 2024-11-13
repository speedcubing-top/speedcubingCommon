package top.speedcubing.common.database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DailyStats {
    public static void validate() {
        String today = getToday();
        Database.getSystem().executeUpdate("INSERT IGNORE INTO daily (date) VALUES ("+today+")");
    }

    public static void update(String s) {
        validate();
        Database.getSystem().executeUpdate("UPDATE daily SET " + s + " WHERE date='" + getToday() + "'");
    }

    public static String getToday() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
    }
}
