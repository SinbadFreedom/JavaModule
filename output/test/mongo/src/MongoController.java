import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Simple singleton Mongodb controller.
 *
 * @author Sinbad
 * @email 151229152@qq.com
 */
public class MongoController {

    public static final String DB_GLOBAL = "db_global";
    public static final String COL_INCREASE = "col_increase";
    public static String MONGO_DB_IP = "127.0.0.1";
    public static int MONGO_DB_PORT = 27017;
    private static Mongo mongo = null;

    private MongoController() {
    }

    public static MongoController getInstance() {
        return SingletonHolder.instance;
    }

    public void init() {
        try {
            mongo = new Mongo(MONGO_DB_IP, MONGO_DB_PORT);
        } catch (UnknownHostException e) {
        }
    }

    public DBCollection getDbCollection(String dbName, String collection) {
        DB db = mongo.getDB(dbName);
        if (null == db) {
            return null;
        }
        return db.getCollection(collection);
    }

    public int getIncreaseId() {
        BasicDBObject where = new BasicDBObject();
        where.put("table", "globalId");
        BasicDBObject value = new BasicDBObject();
        value.put("$inc", new BasicDBObject("globalId", 1));
        BasicDBObject obj = findAndModifyBridge(DB_GLOBAL, COL_INCREASE, where, value);
        if (null == obj) {
            return -1;
        } else {
            return obj.getInt("globalId");
        }
    }

    public BasicDBObject findOneBridge(String dbName, String collectionName, BasicDBObject where) {
        return findOneWithFieldsBridge(dbName, collectionName, where, null);
    }

    public DBCursor findWithLimitBridge(String dbName, String collectionName, BasicDBObject where, int limit) {
        DBCollection collection = getDbCollection(dbName, collectionName);
        if (null == collection) {
            return null;
        } else {
            return collection.find(where).limit(limit);
        }
    }

    public BasicDBObject findOneWithFieldsBridge(String dbName, String collectionName, BasicDBObject where, BasicDBObject fields) {
        DBCollection collection = getDbCollection(dbName, collectionName);
        if (null == collection) {
            return null;
        } else {
            if (null == fields) {
                return (BasicDBObject) collection.findOne(where);
            } else {
                return (BasicDBObject) collection.findOne(where, fields);
            }
        }
    }

    public void insertBridge(String dbName, String collectionName, DBObject value) {
        DBCollection collection = getDbCollection(dbName, collectionName);
        if (null != collection) {
            collection.insert(value);
        }
    }

    public void updateBridge(String dbName, String collectionName, BasicDBObject where, DBObject value, boolean upsert) {
        DBCollection collection = getDbCollection(dbName, collectionName);
        if (null != collection) {
            collection.update(where, value, upsert, false);
        }
    }

    public BasicDBObject findAndModifyBridge(String dbName, String collectionName, BasicDBObject where, DBObject value) {
        DBCollection collection = getDbCollection(dbName, collectionName);
        if (null == collection) {
            return null;
        } else {
            return (BasicDBObject) collection.findAndModify(where, null, null, false, value, true, true);
        }
    }

    private static class SingletonHolder {
        public static MongoController instance = new MongoController();
    }
}
