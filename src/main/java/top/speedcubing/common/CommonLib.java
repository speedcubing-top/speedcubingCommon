package top.speedcubing.common;

import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.database.Database;

public class CommonLib {
    public static void init() {
        init("/storage/server.json");
    }

    public static void init(String configPath) {
        ServerConfig.reload(configPath, true);
        CubingTick.init();
    }

    public static void shutdown() {
        CubingTick.tickTimer.cancel();
        Database.closeAllConnections();
    }
}
