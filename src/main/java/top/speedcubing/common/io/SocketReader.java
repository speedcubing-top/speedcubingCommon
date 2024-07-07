package top.speedcubing.common.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import top.speedcubing.common.events.SocketInputEvent;
import top.speedcubing.common.events.SocketReadEvent;
import top.speedcubing.lib.utils.Threads;
import top.speedcubing.lib.utils.bytes.ByteArrayBuffer;
import top.speedcubing.lib.utils.bytes.IOUtils;
import top.speedcubing.lib.utils.internet.HostAndPort;

public class SocketReader {
    private static SocketReader instance;

    public static void init(HostAndPort hostPort) {
        instance = new SocketReader(hostPort);
    }

    public static HostAndPort getHostAndPort() {
        return instance.hostPort;
    }

    public static void shutdown() {
        instance.running = false;
    }

    private ServerSocket tcpServer;
    private boolean running = true;
    private HostAndPort hostPort;

    private SocketReader(HostAndPort hostPort) {
        try {
            this.hostPort = hostPort;
            this.tcpServer = new ServerSocket(hostPort.getPort());
            this.tcpServer.setSoTimeout(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        Thread thread = Threads.newThread("Cubing-Socket-Thread", () -> {
            while (running) {
                try {
                    Socket s = tcpServer.accept();
                    OutputStream out = s.getOutputStream();
                    InputStream in = s.getInputStream();
                    DataInputStream data = new DataInputStream(in);
                    try {
                        String packetID = data.readUTF();
                        if (packetID.equals("in")) {
                            try {
                                byte[] resend = new ByteArrayBuffer().write(((SocketInputEvent) new SocketInputEvent(data.readUTF(), data).call()).respond.toByteArray()).toByteArray();
                                out.write(resend);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            SocketReadEvent event = new SocketReadEvent(packetID, data, out);
                            event.call();
                        }
                    } catch (Exception ex) {
                        continue;
                    }
                    IOUtils.closeQuietly(in, out, data, s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
