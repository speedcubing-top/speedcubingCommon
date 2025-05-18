package top.speedcubing.common.database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLRuntimeException;

public class DailyStats {
    public static void update(String s) {
        try (SQLConnection connection = Database.getSystem()) {
            connection.doTransaction(() -> {
                try {
                    connection.executeUpdate("INSERT INTO daily (date, playtime) VALUES (" + getToday() + ", 0) ");
                } catch (SQLRuntimeException ignored) {
                }
                connection.executeUpdate("UPDATE daily SET " + s + " WHERE date='" + getToday() + "'");
            });
        }
    }

    private static String getToday() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
    }
}
