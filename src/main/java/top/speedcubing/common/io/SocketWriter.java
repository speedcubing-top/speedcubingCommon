package top.speedcubing.common.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.concurrent.CompletableFuture;
import top.speedcubing.lib.utils.internet.HostAndPort;

public class SocketWriter {

    public static CompletableFuture<DataInputStream> writeResponse(HostAndPort hostPort, byte[] data) {
        EventLoopGroup group = new NioEventLoopGroup();
        CompletableFuture<DataInputStream> futureResponse = new CompletableFuture<>();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
                                    byte[] receivedBytes = new byte[buf.readableBytes()];
                                    buf.readBytes(receivedBytes);
                                    futureResponse.complete(new DataInputStream(new ByteArrayInputStream(receivedBytes)));
                                    ctx.close();
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    futureResponse.completeExceptionally(cause);
                                    ctx.close();
                                }
                            });
                        }
                    });

            ChannelFuture f = b.connect(hostPort.getHost(), hostPort.getPort()).sync();
            f.channel().writeAndFlush(Unpooled.wrappedBuffer(data));
            futureResponse.whenComplete((result, error) -> group.shutdownGracefully());
        } catch (Exception e) {
            futureResponse.completeExceptionally(e);
            group.shutdownGracefully();
        }

        return futureResponse;
    }
}
