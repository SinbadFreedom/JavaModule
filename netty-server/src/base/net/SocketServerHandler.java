package base.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.LogUtil;

import java.nio.ByteBuffer;

public class SocketServerHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        int length = byteBuffer.getInt();
        int messageId = byteBuffer.getInt();

        LogUtil.LOGGER.info("[messageReceived ] length " + length + " messageId " + messageId);
        DisruptorImpl.getInstance().publish(messageId, byteBuffer);
    }
}
