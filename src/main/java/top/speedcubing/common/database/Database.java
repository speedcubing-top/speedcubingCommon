package top.speedcubing.common.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class Database {

    public static ConcurrentHashMap<String, HikariDataSource> dataSourceMap;

    public static SQLConnection getCubing() {
        return get("speedcubing");
    }

    public static SQLConnection getSystem() {
        return get("speedcubingsystem");
    }

    public static SQLConnection getConfig() {
        return get("sc_config");
    }

    public static SQLConnection get(String database) {
        HikariDataSource dataSource = dataSourceMap.get(database);
        if (dataSource == null) {
            throw new IllegalArgumentException("No such database");
        }

        try {
            return new SQLConnection(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void connect(String url, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String[] databases = {"sc_config", "speedcubing", "speedcubingsystem"};
            for (String db : databases) {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url.replace("%db%", db));
                config.setUsername(user);
                config.setPassword(password);
                config.setMaximumPoolSize(10);
                config.setIdleTimeout(Long.MAX_VALUE);
                dataSourceMap.put(db, new HikariDataSource(config));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
