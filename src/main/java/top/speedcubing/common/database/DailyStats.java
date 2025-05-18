package top.speedcubing.common.database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class DailyStats {
    public static void update(String s) {
        try (SQLConnection connection = Database.getSystem()) {
            connection.executeUpdate("INSERT INTO daily (date, playtime) VALUES (" + getToday() + ", 0) " +
                    "ON DUPLICATE KEY UPDATE " + s);
        }
    }

    private static String getToday() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
    }
}
