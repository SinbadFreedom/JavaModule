import com.mongodb.BasicDBObject;

public class Mongo {

    public static void main(String[] args) {
        MongoController.getInstance().init();
        long t1 = System.currentTimeMillis();

        String dbName = "db_test";
        String colName = "col_test";
        for (int i = 0; i < 10000; i++) {
//            MongoController.getInstance().insertBridge(dbName, colName, new BasicDBObject("testId", i));
//            MongoController.getInstance().findOneBridge(dbName, colName, new BasicDBObject("testId", i));
            MongoController.getInstance().updateBridge(dbName, colName, new BasicDBObject("testId", i), new BasicDBObject("testId", i), true);
        }

        long t2 = System.currentTimeMillis();

        System.out.println("time cost : " + (t2 - t1));
    }
}
