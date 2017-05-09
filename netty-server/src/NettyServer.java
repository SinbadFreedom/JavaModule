import base.net.SocketServer;

public class NettyServer {

    public static void main(String[] args) {
        SocketServer.getInstance().startServer();

        new Thread(new PrintClass()).start();
    }

    static class PrintClass implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(SocketServer.IN_COUNT.get());
                    SocketServer.IN_COUNT.set(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
