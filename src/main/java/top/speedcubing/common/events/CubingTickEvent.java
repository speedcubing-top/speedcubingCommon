package top.speedcubing.common.events;

import top.speedcubing.lib.eventbus.CubingEvent;

public class CubingTickEvent extends CubingEvent {
    private final int tick;

    public CubingTickEvent(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }
}
