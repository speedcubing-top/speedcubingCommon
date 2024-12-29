package top.speedcubing.common;

import java.util.Timer;
import java.util.TimerTask;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.database.DatabaseData;
import top.speedcubing.common.events.CubingTickEvent;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public class CubingTick {
    public static Timer tickTimer;
    private static final CubingTickEvent event = new CubingTickEvent();

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
                    event.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }
}
