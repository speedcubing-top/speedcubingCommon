package top.speedcubing.common.configuration;

import top.speedcubing.common.database.Database;

public class DataBaseConfig {
    public static String get(String key) {
        return Database.configConnection.prepare("SELECT value FROM settings WHERE name=?").setString(1, key).getString();
    }
}
