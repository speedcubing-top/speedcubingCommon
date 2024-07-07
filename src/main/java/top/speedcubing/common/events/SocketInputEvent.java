package top.speedcubing.common.events;


import java.io.DataInputStream;
import top.speedcubing.lib.eventbus.CubingEvent;
import top.speedcubing.lib.utils.bytes.ByteArrayBuffer;

public class SocketInputEvent extends CubingEvent {
    public final String subHeader;
    private final DataInputStream data;
    public final ByteArrayBuffer respond = new ByteArrayBuffer().writeUTF("out");

    public SocketInputEvent(String subHeader, DataInputStream data) {
        this.subHeader = subHeader;
        this.data = data;
    }

    public DataInputStream getData() {
        return data;
    }
}
