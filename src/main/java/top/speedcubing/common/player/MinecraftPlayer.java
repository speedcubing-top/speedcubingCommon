package top.speedcubing.common.player;

import java.util.UUID;

public class MinecraftPlayer {
    public final UUID uuid;
    public final String realName;

    public MinecraftPlayer(UUID uuid, String realName) {
        this.uuid = uuid;
        this.realName = realName;
    }
}
