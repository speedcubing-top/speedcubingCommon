package top.speedcubing.common.events;

import java.io.DataInputStream;
import top.speedcubing.lib.eventbus.CubingEvent;
import top.speedcubing.lib.utils.bytes.ByteArrayBuffer;

public class SocketReadEvent extends CubingEvent {
    private final String packetID;
    private final DataInputStream data;
    private final ByteArrayBuffer buffer = new ByteArrayBuffer();

    public SocketReadEvent(String packetID, DataInputStream data) {
        this.packetID = packetID;
        this.data = data;
    }

    public String getPacketID() {
        return this.packetID;
    }

    public DataInputStream getData() {
        return this.data;
    }

    public ByteArrayBuffer getBuffer() {
        return this.buffer;
    }
}
