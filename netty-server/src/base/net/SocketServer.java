package base.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import util.LogUtil;

public final class SocketServer implements Runnable {

    public static int SERVER_PORT = 23456;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private SocketServer() {
    }

    public static SocketServer getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new SocketServerInitializer())
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(SERVER_PORT).sync();
            // Wait until the server socket is closed.
            LogUtil.LOGGER.info("bind : " + SERVER_PORT);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void startServer() {
        new Thread(SocketServer.getInstance(), "SocketServer-thread").start();
    }

    public void closeServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private static class SingletonHolder {
        public final static SocketServer instance = new SocketServer();
    }
}
