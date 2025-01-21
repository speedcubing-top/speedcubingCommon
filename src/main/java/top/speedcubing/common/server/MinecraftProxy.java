package top.speedcubing.common.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLResult;
import top.speedcubing.lib.utils.SQL.SQLRow;
import top.speedcubing.lib.utils.bytes.ByteArrayBuffer;
import top.speedcubing.lib.utils.bytes.IOUtils;
import top.speedcubing.lib.utils.internet.HostAndPort;
import top.speedcubing.lib.utils.sockets.TCPClient;

public class MinecraftProxy {
    private static final Map<String, MinecraftProxy> proxies = new HashMap<>();

    public static MinecraftProxy getProxy(String name) {
        return proxies.get(name);
    }

    public static MinecraftProxy getProxy(HostAndPort listenerAddress) {
        for (MinecraftProxy s : proxies.values()) {
            if (s.getListenerAddress().equals(listenerAddress)) {
                return s;
            }
        }
        return null;
    }

    public static Collection<MinecraftProxy> getProxies() {
        return proxies.values();
    }

    public static void loadProxies() {
        proxies.clear();
        try (SQLConnection connection = Database.getConfig()) {
            SQLResult result = connection.select("name,host,port").from("mc_proxies").executeResult();
            for (SQLRow r : result) {
                String name = r.getString("name");
                String host = r.getString("host");
                int port = r.getInt("port");
                proxies.put(name, new MinecraftProxy(name, new HostAndPort(host, port)));
            }
        }
    }
    private final HostAndPort listenerAddress;
    private final String name;

    public MinecraftProxy(String name, HostAndPort address) {
        this.name = name;
        this.listenerAddress = new HostAndPort(address.getHost(), address.getPort() + 1000);
        proxies.put(name, this);
    }

    public int getPlayerCount() {
        try (SQLConnection connection = Database.getSystem()) {
            return connection.select("SUM(onlinecount)").from("stat_onlinecount").where("server='" + name + "'").executeResult().getInt();
        }
    }

    public void write(byte[] data) {
        TCPClient.write(listenerAddress, data);
    }

    public DataInputStream writeResponse(byte[] data) {
        byte[] packet = (new ByteArrayBuffer()).writeUTF("in").write(data).toByteArray();
        byte[] response = TCPClient.writeAndReadAll(listenerAddress, packet);
        return response == null ? null : IOUtils.toDataInputStream(response);
    }

    public HostAndPort getListenerAddress() {
        return listenerAddress;
    }

    public String getName() {
        return name;
    }
}
