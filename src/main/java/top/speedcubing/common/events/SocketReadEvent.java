package top.speedcubing.common.events;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import java.io.DataInputStream;
import top.speedcubing.lib.eventbus.CubingEvent;

public class SocketReadEvent extends CubingEvent {
    private final String packetID;
    private final DataInputStream data;
    private final ChannelHandlerContext ctx;
    private boolean written = false;

    public SocketReadEvent(String packetID, DataInputStream data, ChannelHandlerContext ctx) {
        this.packetID = packetID;
        this.data = data;
        this.ctx = ctx;
    }

    public String getPacketID() {
        return this.packetID;
    }

    public DataInputStream getData() {
        return this.data;
    }

    public void write(byte[] b) {
        this.ctx.writeAndFlush(Unpooled.wrappedBuffer(b));
        this.written = true;
    }

    public boolean isWritten() {
        return this.written;
    }
}
