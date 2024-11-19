package top.speedcubing.common.database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DailyStats {
    public static void update(String s) {
        Database.getSystem().doTransaction(() -> {
            Database.getSystem().executeUpdate("INSERT IGNORE INTO daily (date) VALUES (" + getToday() + ")");
            Database.getSystem().executeUpdate("UPDATE daily SET " + s + " WHERE date='" + getToday() + "'");
        });
    }

    public static String getToday() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
    }
}
