import base.net.SocketServer;

public class NettyServer {
    public static void main(String[] args) {
        SocketServer.getInstance().startServer();
    }
}
