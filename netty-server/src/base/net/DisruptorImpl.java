package base.net;

import base.message.MessageEvent;
import base.message.MessageEventFactory;
import base.message.MessageEventHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import io.netty.channel.Channel;

import java.nio.ByteBuffer;

public class DisruptorImpl {
    private static int sessionBufferSize = 4096;
    private Disruptor<MessageEvent> sessionDisruptor;

    private DisruptorImpl() {
        sessionDisruptor = new Disruptor(new MessageEventFactory(), sessionBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
        sessionDisruptor.handleEventsWith(new MessageEventHandler());
        sessionDisruptor.start();
    }

    public static DisruptorImpl getInstance() {
        return SingletonHolder.instance;
    }

    public Disruptor<MessageEvent> getSessionDisruptor() {
        return sessionDisruptor;
    }

    public void publish(int messageId, ByteBuffer buffer, Channel channel) {
        RingBuffer<MessageEvent> ringBuffer = DisruptorImpl.getInstance().getSessionDisruptor().getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            MessageEvent event = ringBuffer.get(sequence);
            event.init(messageId, buffer, channel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    private static class SingletonHolder {
        public final static DisruptorImpl instance = new DisruptorImpl();
    }
}
