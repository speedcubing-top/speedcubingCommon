package top.speedcubing.common.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.common.rank.RankLoader;
import top.speedcubing.common.server.MinecraftProxy;
import top.speedcubing.common.server.MinecraftServer;
import top.speedcubing.lib.eventbus.CubingEventHandler;

public class ServerConfig {
    public static JsonObject config;
    private static String configPath;

    public static void reload(String path, boolean init) {
        configPath = path;
        reload(init);
    }

    public static void reload(boolean init) {
        try {
            config = JsonParser.parseReader(new FileReader(configPath)).getAsJsonObject();

            if (init) {
                Database.connect();
            } else
                Database.reloadDataSourceConfig();

            RankLoader.loadRanks();
            MinecraftServer.loadServers();
            MinecraftProxy.loadProxies();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @CubingEventHandler(priority = 10)
    public void configReloadEvent(ConfigReloadEvent e) {
        reload(false);
    }
}
