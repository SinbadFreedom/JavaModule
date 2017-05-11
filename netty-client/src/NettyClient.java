public class NettyClient {
    public static void main(String[] ar) {
        for (int i = 0; i < 100; i++) {
            ClientSocket clientSocket = new ClientSocket();
            clientSocket.connect();
        }

        new Thread(new PrintClass()).start();
    }

    static class PrintClass implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(ClientSocket.IN_COUNT.get());
                    ClientSocket.IN_COUNT.set(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
