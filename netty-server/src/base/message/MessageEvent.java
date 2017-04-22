package base.message;

import java.nio.ByteBuffer;

public class MessageEvent {
    private int messageId;
    private ByteBuffer byteBuffer;

    public void init(int messageId, ByteBuffer byteBuffer) {
        this.messageId = messageId;
        this.byteBuffer = byteBuffer;
    }

    public int getMessageId() {
        return messageId;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }
}
