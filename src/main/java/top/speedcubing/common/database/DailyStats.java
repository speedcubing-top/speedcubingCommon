package top.speedcubing.common.database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class DailyStats {
    public static void update(String s) {
        try (SQLConnection connection = Database.getSystem()) {
            connection.doTransaction(() -> {
                connection.executeUpdate("INSERT IGNORE INTO daily (date) VALUES (" + getToday() + ")");
                connection.executeUpdate("UPDATE daily SET " + s + " WHERE date='" + getToday() + "'");
            });
        }
    }

    public static String getToday() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
    }
}
