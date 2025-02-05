package top.speedcubing.common;

import java.util.Timer;
import java.util.TimerTask;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.database.DatabaseData;
import top.speedcubing.common.events.CubingTickEvent;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class CubingTick {
    public static Timer tickTimer;
    public static int tick = 0;

    public static void init() {
        tickTimer = new Timer("Cubing-Tick-Thread");
        tickTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                try (SQLConnection cubing = Database.getCubing();
                     SQLConnection system = Database.getSystem()) {
                    DatabaseData.champs.clear();
                    cubing.select("id").from("champ").executeResult().forEach(r -> r.forEach(f -> DatabaseData.champs.add(f.getInt())));
                    DatabaseData.onlineCount = system.select("SUM(onlinecount)").from("proxies").executeResult().getInt();
                    CubingTickEvent event = new CubingTickEvent(tick);
                    event.call();
                    tick++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }
}
