public class Main {

    public static void main(String[] args) {
        RedisController.getInstance().init();

        String key = "test:folder";
        String field = "id";
        String value = "testId";

        long t1 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            RedisController.getInstance().setContent(key, field, value);
            String content = RedisController.getInstance().getContent(key, field);
        }

        long t2 = System.currentTimeMillis();

        System.out.println("time cost : " + (t2 - t1));
    }
}
