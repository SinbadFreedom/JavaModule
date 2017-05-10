public class NettyClient {
    public static void main(String[] ar) {
        for (int i = 0; i < 100; i++) {
            ClientSocket clientSocket = new ClientSocket();
            clientSocket.connect();
        }
    }
}
