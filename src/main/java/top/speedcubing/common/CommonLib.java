package top.speedcubing.common;

import top.speedcubing.common.configuration.ServerConfig;

public class CommonLib {
    public static void init() {
        ServerConfig.reload(true);
        CubingTick.init();
    }

    public static void init(String configPath) {
        ServerConfig.reload(configPath, true);
        CubingTick.init();
    }

    public static void shutdown() {
        CubingTick.calcTimer.cancel();
    }
}
