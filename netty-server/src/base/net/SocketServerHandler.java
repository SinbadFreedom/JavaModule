package base.net;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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

        SocketServer.IN_COUNT.addAndGet(1);
        /** use disruptor*/
        DisruptorImpl.getInstance().publish(messageId, byteBuffer, ctx.channel());
        /** direct handle*/
//        byte[] mesgId = ArrayUtil.int2Byte(0x0102);
//        byte[] dataBytes = ArrayUtil.outLogin();
//        byte[] finalBytes = new byte[dataBytes.length + 4];
//        System.arraycopy(mesgId, 0, finalBytes, 0, 4);
//        System.arraycopy(dataBytes, 0, finalBytes, 4, dataBytes.length);
//        ctx.channel().writeAndFlush(finalBytes);
    }
}
