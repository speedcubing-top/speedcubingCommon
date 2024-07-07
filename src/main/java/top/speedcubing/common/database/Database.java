package top.speedcubing.common.database;

import top.speedcubing.lib.utils.SQL.SQLConnection;

public class Database {
    public static SQLConnection connection;
    public static SQLConnection systemConnection;
    public static SQLConnection configConnection;

    public static void connect(String url, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = new SQLConnection(url.replace("%db%", "speedcubing"), user, password);
        systemConnection = new SQLConnection(url.replace("%db%", "speedcubingsystem"), user, password);
        configConnection = new SQLConnection(url.replace("%db%", "sc_config"), user, password);
    }
}
