package base.message;

import com.lmax.disruptor.EventHandler;
import protocol.InLogin;
import util.LogUtil;

import java.nio.ByteBuffer;

public class MessageEventHandler implements EventHandler<MessageEvent> {

    public void onEvent(MessageEvent message, long sequence, boolean endOfBatch) {
        ByteBuffer byteBuffer = message.getByteBuffer();
        LogUtil.LOGGER.info("message " + message.getMessageId());
        switch (message.getMessageId()) {
            case 0x0101:
                InLogin inLogin = InLogin.getRootAsInLogin(byteBuffer);
                int accountId = inLogin.account();
                LogUtil.LOGGER.info("[onEvent]: accountId " + accountId);
                break;
        }
    }
}