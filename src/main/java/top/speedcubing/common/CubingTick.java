package top.speedcubing.common;

import com.google.common.collect.Sets;
import java.util.Timer;
import java.util.TimerTask;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.database.DatabaseData;
import top.speedcubing.common.events.CubingTickEvent;

public class CubingTick {
    public static Timer calcTimer;
    private static final CubingTickEvent event = new CubingTickEvent();

    public static void init() {
        calcTimer = new Timer("Cubing-Tick-Thread");
        calcTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    DatabaseData.champs = Sets.newHashSet(Database.connection.select("id").from("champ").getIntArray());
                    DatabaseData.onlineCount = Database.systemConnection.select("SUM(onlinecount)").from("proxies").getInt();
                    event.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }
}
