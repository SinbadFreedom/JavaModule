public class Apollo {

    public static void main(String[] args) {
        ApolloController.getInstance().init();
        long t1 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            ApolloController.getInstance().publishMessage();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("time cost : " + (t2 - t1));
    }
}
