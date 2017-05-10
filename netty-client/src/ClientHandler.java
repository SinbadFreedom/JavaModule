import com.google.flatbuffers.FlatBufferBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.InLogin;

import java.nio.ByteBuffer;

public class ClientHandler extends SimpleChannelInboundHandler<byte[]> {

    Channel channel;

    public ClientHandler() {
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        this.sendLogin(100);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        int msgId = buffer.getInt();
//        /** resend*/
        this.sendLogin(100);
    }

    public void sendLogin(int accountId) throws Exception {
        FlatBufferBuilder builder = new FlatBufferBuilder(1);
        InLogin.startInLogin(builder);
        InLogin.addAccount(builder, accountId);
        int endIndex = InLogin.endInLogin(builder);
        builder.finish(endIndex);
        this.send(0x0101, builder);
    }

    private void send(int mesgId, FlatBufferBuilder builder) {
        try {
            byte[] bytes = builder.sizedByteArray();
            ByteBuf byteBuf = Unpooled.directBuffer();
            byteBuf.writeInt(mesgId);
            byteBuf.writeBytes(bytes);
            ChannelFuture future = channel.writeAndFlush(byteBuf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
