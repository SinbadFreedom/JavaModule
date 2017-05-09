package base.message;

import io.netty.channel.Channel;

import java.nio.ByteBuffer;

public class MessageEvent {
    private int messageId;
    private ByteBuffer byteBuffer;
    private Channel channel;

    public void init(int messageId, ByteBuffer byteBuffer, Channel channel) {
        this.messageId = messageId;
        this.byteBuffer = byteBuffer;
        this.channel = channel;
    }

    public int getMessageId() {
        return messageId;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public Channel getChannel() {
        return channel;
    }
}
