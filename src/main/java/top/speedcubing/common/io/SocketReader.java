package top.speedcubing.common.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import top.speedcubing.common.events.SocketInputEvent;
import top.speedcubing.common.events.SocketReadEvent;
import top.speedcubing.lib.utils.bytes.ByteArrayBuffer;
import top.speedcubing.lib.utils.bytes.IOUtils;
import top.speedcubing.lib.utils.internet.HostAndPort;

public class SocketReader {
    private final HostAndPort hostPort;

    public HostAndPort getHostAndPort() {
        return this.hostPort;
    }

    public SocketReader(HostAndPort hostPort) {
        this.hostPort = hostPort;

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            b.bind(hostPort.getHost(), hostPort.getPort()).sync();
        } catch (InterruptedException e) {

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
            byte[] bytes = new byte[msg.readableBytes()];
            msg.readBytes(bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            DataInputStream data = new DataInputStream(in);
            try {
                String packetID = data.readUTF();
                if (packetID.equals("in")) {
                    try {
                        byte[] resend = new ByteArrayBuffer().write(((SocketInputEvent) new SocketInputEvent(data.readUTF(), data).call()).respond.toByteArray()).toByteArray();
                        ctx.writeAndFlush(Unpooled.wrappedBuffer(resend));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    SocketReadEvent event = new SocketReadEvent(packetID, data, ctx);
                    event.call();
                    if (!event.isWritten()) {
                        ctx.writeAndFlush(Unpooled.wrappedBuffer(new ByteArrayBuffer().writeUTF("OK").toByteArray()));
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            IOUtils.closeQuietly(in, data);
        }
    }
}
