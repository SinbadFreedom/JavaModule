public class Main {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutDownHandler());
    }

    public static class ShutDownHandler extends Thread {
        @Override
        public void run() {
            //TODO save data when shutdown
        }
    }
}
