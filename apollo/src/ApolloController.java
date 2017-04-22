import org.fusesource.stomp.jms.StompJmsConnectionFactory;

import javax.jms.*;

/**
 * Simple singleton Apollo controller.
 *
 * @author Sinbad
 * @email 151229152@qq.com
 */
public class ApolloController {

    public static Session APOLLO_SESSION = null;
    public static String USER = "admin";
    public static String PASSWORD = "password";
    public static String APOLLO_IP = "127.0.0.1:61613";
    public static String TOPIC = "topic_";
    public static String CLIENT_ID = "1000";

    public static MessageConsumer consumer;
    public static MessageProducer producer;
    public static Topic topic;

    private ApolloController() {
    }

    public static ApolloController getInstance() {
        return SingletonHolder.instance;
    }

    public void init() {
        try {
            StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
            factory.setBrokerURI("tcp://" + APOLLO_IP);
            Connection connection = factory.createConnection(USER, PASSWORD);
            /** 绑定服务器id */
            connection.setClientID(CLIENT_ID);
            connection.start();
            APOLLO_SESSION = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            topic = APOLLO_SESSION.createTopic(TOPIC + CLIENT_ID);
            /** announcement */
            consumer = APOLLO_SESSION.createConsumer(topic);
            consumer.setMessageListener(new Listener());
            producer = APOLLO_SESSION.createProducer(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishMessage() {
        try {
            TextMessage mailMsg = APOLLO_SESSION.createTextMessage("test content.");
            producer.send(mailMsg);
            /** commit之后，消息才会进入队列*/
            APOLLO_SESSION.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static class SingletonHolder {
        public static ApolloController instance = new ApolloController();
    }
}
