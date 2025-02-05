package top.speedcubing.common;

import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.eventbus.CubingEventManager;

public class CommonLib {
    public static void init() {
        init("/storage/server.json");
    }

    public static void init(String configPath) {
        ServerConfig config = new ServerConfig();
        config.reload(configPath, true);
        CubingEventManager.registerListeners(config);
        CubingTick.init();
    }

    public static void shutdown() {
        CubingTick.tickTimer.cancel();
        Database.closeAllConnections();
    }
}
