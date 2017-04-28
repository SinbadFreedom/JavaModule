import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Simple singleton redis controller.
 *
 * @author Sinbad
 * @email 151229152@qq.com
 */
public class RedisController {

    public static int MAX_IDLE = 8;
    public static int MAX_TOTAL = 8;
    public static int MAX_WAIT_MILLIS = 3000;
    public static int TIMEOUT = 1000;
    public static String PASSWORD = null;
    public static String REDIS_IP = "127.0.0.1";
    public static int REDIS_PORT = 6379;
    public static JedisPool pool = null;

    private RedisController() {
    }

    public static RedisController getInstance() {
        return SingletonHolder.instance;
    }

    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(MAX_IDLE);
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxWaitMillis(MAX_WAIT_MILLIS);
        config.setTestOnReturn(true);
        pool = new JedisPool(config, REDIS_IP, REDIS_PORT, TIMEOUT, PASSWORD);
    }

    public void setContent(String key, String field, String value) {
        Jedis jedis = pool.getResource();
        jedis.hset(key, field, value);
        jedis.close();
    }

    public boolean checkContent(String key, String field) {
        Jedis jedis = pool.getResource();
        boolean hasName = jedis.hexists(key, field);
        jedis.close();
        return hasName;
    }

    public String getContent(String key, String field) {
        Jedis jedis = pool.getResource();
        String content = jedis.hget(key, field);
        jedis.close();
        return content;
    }

    private static class SingletonHolder {
        public static RedisController instance = new RedisController();
    }
}
