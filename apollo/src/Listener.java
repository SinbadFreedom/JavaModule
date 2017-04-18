import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener implements MessageListener {
    public void onMessage(Message message) {
        try {
            TextMessage byteMesg = (TextMessage) message;
            String content = byteMesg.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}