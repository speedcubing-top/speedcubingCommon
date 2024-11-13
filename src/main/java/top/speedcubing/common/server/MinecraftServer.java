package top.speedcubing.common.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.internet.HostAndPort;
import top.speedcubing.lib.utils.sockets.TCPClient;

public class MinecraftServer {
    private static final Map<String, MinecraftServer> servers = new HashMap<>();

    public static MinecraftServer getServer(String name) {
        return servers.get(name);
    }

    public static Collection<MinecraftServer> getServers() {
        return servers.values();
    }

    public static void loadServers() {
        try {
            servers.clear();
            ResultSet r = Database.getConfig().select("name,host,port").from("mc_servers").executeQuery();
            while (r.next()) {
                String name = r.getString("name");
                String host = r.getString("host");
                int port = r.getInt("port");
                servers.put(name, new MinecraftServer(name, new HostAndPort(host, port)));
            }
            r.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private final HostAndPort address;
    private final HostAndPort listenerAddress;
    private final String name;

    public MinecraftServer(String name, HostAndPort address) {
        this.name = name;
        this.address = address;
        this.listenerAddress = new HostAndPort(address.getHost(), address.getPort() + 1000);
        servers.put(name, this);
    }

    public int getPlayerCount() {
        return Database.getSystem().select("SUM(onlinecount)").from("stat_onlinecount").where("server='" + name + "'").getInt();
    }

    public void write(byte[] data) {
        TCPClient.write(listenerAddress, data);
    }

    public HostAndPort getAddress() {
        return address;
    }

    public HostAndPort getListenerAddress() {
        return listenerAddress;
    }

    public String getName() {
        return name;
    }

    public String getWebhook() {
        return Database.getConfig().select("discord_webhook").from("mc_servers").where("name='" + name + "'").getString();
    }
}
