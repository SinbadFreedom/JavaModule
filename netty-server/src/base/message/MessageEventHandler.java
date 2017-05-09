package base.message;

import com.google.flatbuffers.FlatBufferBuilder;
import com.lmax.disruptor.EventHandler;
import protocol.InLogin;
import protocol.OutLogin;
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

                try {
                    byte[] mesgId = this.int2Byte(0x0102);
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

    public byte[] int2Byte(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value & 0xFF000000) >> 24);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
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