package top.speedcubing.common.events;

import java.io.DataInputStream;
import java.io.OutputStream;
import top.speedcubing.lib.eventbus.CubingEvent;

public class SocketReadEvent extends CubingEvent {
    private final String packetID;
    private final DataInputStream data;
    private final OutputStream outputStream;

    public SocketReadEvent(String packetID, DataInputStream data,OutputStream outputStream) {
        this.packetID = packetID;
        this.data = data;
        this.outputStream = outputStream;
    }

    public String getPacketID() {
        return packetID;
    }

    public DataInputStream getData() {
        return data;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
