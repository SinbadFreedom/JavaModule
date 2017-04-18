import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * Simple singleton Apollo controller.
 *
 * @author Sinbad
 * @email 151229152@qq.com
 */
public class ApolloController {

    public static Session APOLLO_SESSION = null;
    public static String user = "admin";
    public static String password = "password";
    public static String APOLLO_IP;
    public static String ANNOUNCEMENT = "announcement_";
    public static String CLOSE_SERVER = "closeServer_";
    public static String SEND_MAIL = "sendMail_";
    private MessageConsumer consumerAnnouncement = null;
    private MessageConsumer consumerCloseServer = null;
    private MessageConsumer consumerSendMail = null;


    private ApolloController() {
    }

    public static ApolloController getInstance() {
        return SingletonHolder.instance;
    }

    public void init() {
    }

    private static class SingletonHolder {
        public static ApolloController instance = new ApolloController();
    }
}
