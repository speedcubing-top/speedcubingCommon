package top.speedcubing.common.namedb;

import top.speedcubing.common.database.Database;
import top.speedcubing.lib.bukkit.events.packet.ProfileRespondEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLResult;
import top.speedcubing.lib.utils.UUIDUtils;

public class NameDb {

    @CubingEventHandler
    public void ProfileRespondEvent(ProfileRespondEvent e) {
        pushData(e.getProfile().getUUID(), e.getProfile().getName(), e.getProfile().getTimeMillis());
    }

    public static void pushData(String uuid, String name, long millis) {
        long t = millis / 1000L;
        uuid = UUIDUtils.undash(uuid);
        try (SQLConnection connection = Database.getSystem()) {
            SQLResult result = connection.select("name,first").from("namedb").where("uuid=X'" + uuid + "'").orderBy("last DESC").executeResult();
            if (!result.isEmpty() && result.get(0).getString("name").equals(name))
                connection.update("namedb", "last=" + t, "uuid=X'" + uuid + "' AND first=" + result.get(0).getInt("first"));
            else
                connection.insert("namedb", "uuid,name,first,last", "X'" + uuid + "','" + name + "'," + t + "," + t);
        }
    }
}