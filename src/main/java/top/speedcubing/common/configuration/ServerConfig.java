package top.speedcubing.common.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.common.rank.RankLoader;
import top.speedcubing.common.server.MinecraftServer;

public class ServerConfig {
    public static JsonObject config;
    private static final ConfigReloadEvent event = new ConfigReloadEvent();

    public static void reload(boolean init) {
        reload("/storage/server.json", init);
    }

    public static void reload(String path, boolean init) {
        try {
            config = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
            String DatabaseURL = config.getAsJsonObject("database").get("url").getAsString();
            String DatabaseUser = config.getAsJsonObject("database").get("user").getAsString();
            String DatabasePassword = config.getAsJsonObject("database").get("password").getAsString();

            if (init) {
                Database.connect(DatabaseURL, DatabaseUser, DatabasePassword);
            }
            RankLoader.loadRanks();
            MinecraftServer.loadServers();
            event.call();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
