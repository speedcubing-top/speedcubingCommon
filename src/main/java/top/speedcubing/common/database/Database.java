package top.speedcubing.common.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class Database {

    public static ConcurrentHashMap<String, HikariDataSource> dataSourceMap = new ConcurrentHashMap<>();

    public static SQLConnection getCubing() {
        return get("speedcubing");
    }

    public static SQLConnection getSystem() {
        return get("speedcubingsystem");
    }

    public static SQLConnection getConfig() {
        return get("sc_config");
    }

    public static String user, password, url;
    public static SQLConnection get(String database) {
        HikariDataSource dataSource = dataSourceMap.get(database);
        if (dataSource == null) {
            throw new IllegalArgumentException("No such database");
        }

        try {
            return new SQLConnection(DriverManager.getConnection(url.replace("%db%", database),user,password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void connect(String url, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            return;
        }

        String[] databases = {"sc_config", "speedcubing", "speedcubingsystem"};
        Database.url = url;
        Database.user = user;
        Database.password = password;
        for (String db : databases) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url.replace("%db%", db));
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setIdleTimeout(Long.MAX_VALUE);
            dataSourceMap.put(db, new HikariDataSource(config));
        }
    }

    public static void closeAllConnections() {
        for (Map.Entry<String, HikariDataSource> entry : dataSourceMap.entrySet()) {
            String dbName = entry.getKey();
            HikariDataSource dataSource = entry.getValue();
            try {
                if (dataSource != null && !dataSource.isClosed()) {
                    dataSource.close();
                    System.out.println("Closed connection pool for database: " + dbName);
                }
            } catch (Exception e) {
                System.err.println("Failed to close connection pool for database: " + dbName + " - " + e.getMessage());
            }
        }
        dataSourceMap.clear();
    }
}
