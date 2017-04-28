public class ShutDownHandler {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Handler());
    }

    public static class Handler extends Thread {
        @Override
        public void run() {
            //TODO save data when shutdown
        }
    }
}
