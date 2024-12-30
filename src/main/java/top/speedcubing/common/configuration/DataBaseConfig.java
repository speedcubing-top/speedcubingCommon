package top.speedcubing.common.configuration;

import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class DataBaseConfig {
    public static String get(String key) {
        try (SQLConnection connection = Database.getConfig()) {
            return connection.prepare("SELECT value FROM settings WHERE name=?").setString(1, key).executeResult().get(0).get(0).getString();
        }
    }
}
