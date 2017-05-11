import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import util.LogUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientSocket {
    public static final int CONNECT_LOAD_BALANCE_TIME_OUT = 60 * 1000;

    public static String SERVER_IP = "127.0.0.1";
    public static int SERVER_PORT = 23456;

    public static AtomicInteger IN_COUNT = new AtomicInteger();

    private ClientHandler clientHandler;

    public ClientSocket() {
        clientHandler = new ClientHandler();
    }

    public void connect() {
        Bootstrap b = new Bootstrap();
        try {
            b.group(new NioEventLoopGroup());
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_LOAD_BALANCE_TIME_OUT);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("lengthDecoder", new LengthFieldBasedFrameDecoder(30720, 0, 4));
                    socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4, false));
                    socketChannel.pipeline().addLast("byteDecoder", new ByteArrayDecoder());
                    socketChannel.pipeline().addLast("handler", clientHandler);
                }
            });
            ChannelFuture f = b.connect(SERVER_IP, SERVER_PORT).sync();
            f.awaitUninterruptibly();

            if (f.isCancelled()) {
            } else if (!f.isSuccess()) {
                LogUtil.LOGGER.error("ERROR: Connect " + f.cause().toString());
            } else {
                LogUtil.LOGGER.warn("Connect success.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
