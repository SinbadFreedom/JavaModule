package base.message;

import com.google.flatbuffers.FlatBufferBuilder;
import com.lmax.disruptor.EventHandler;
import protocol.InLogin;
import protocol.OutLogin;
import util.ArrayUtil;

import java.nio.ByteBuffer;

public class MessageEventHandler implements EventHandler<MessageEvent> {

    public void onEvent(MessageEvent message, long sequence, boolean endOfBatch) {
        ByteBuffer byteBuffer = message.getByteBuffer();
        switch (message.getMessageId()) {
            case 0x0101:
                InLogin inLogin = InLogin.getRootAsInLogin(byteBuffer);
                int accountId = inLogin.account();
                try {
                    byte[] mesgId = ArrayUtil.int2Byte(0x0102);
                    byte[] dataBytes = this.outLogin();
                    byte[] finalBytes = new byte[dataBytes.length + 4];
                    System.arraycopy(mesgId, 0, finalBytes, 0, 4);
                    System.arraycopy(dataBytes, 0, finalBytes, 4, dataBytes.length);

                    message.getChannel().writeAndFlush(finalBytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public byte[] outLogin() throws Exception {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        OutLogin.startOutLogin(builder);
        OutLogin.addHasRole(builder, true);
        int endIndex = OutLogin.endOutLogin(builder);
        builder.finish(endIndex);
        return builder.sizedByteArray();
    }
}