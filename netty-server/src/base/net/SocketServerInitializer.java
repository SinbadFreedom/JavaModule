package base.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // Decoders
        pipeline.addLast("lengthDecoder", new LengthFieldBasedFrameDecoder(256, 0, 4));
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4, false));
        pipeline.addLast("byteDecoder", new ByteArrayDecoder());
        pipeline.addLast("handler", new SocketServerHandler());
        pipeline.addLast("byteEncoder", new ByteArrayEncoder());
    }
}
