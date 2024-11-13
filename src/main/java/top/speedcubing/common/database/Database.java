package top.speedcubing.common.database;

import top.speedcubing.lib.utils.SQL.SQLConnection;

public class Database {
    private static ThreadLocal<SQLConnection> cubingConnectionHolder;
    private static ThreadLocal<SQLConnection> systemConnectionHolder;
    private static ThreadLocal<SQLConnection> configConnectionHolder;

    public static SQLConnection getCubing() {
        return cubingConnectionHolder.get();
    }

    public static SQLConnection getSystem() {
        return systemConnectionHolder.get();
    }

    public static SQLConnection getConfig() {
        return configConnectionHolder.get();
    }

    public static void connect(String url, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cubingConnectionHolder = ThreadLocal.withInitial(() -> new SQLConnection(url.replace("%db%", "speedcubing"), user, password));
        systemConnectionHolder = ThreadLocal.withInitial(() -> new SQLConnection(url.replace("%db%", "speedcubingsystem"), user, password));
        configConnectionHolder = ThreadLocal.withInitial(() -> new SQLConnection(url.replace("%db%", "sc_config"), user, password));
    }
}
