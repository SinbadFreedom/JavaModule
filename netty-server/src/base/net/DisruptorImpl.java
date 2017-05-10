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
import java.util.concurrent.atomic.AtomicInteger;

public class DisruptorImpl {
    private static int sessionBufferSize = 4096;
    private Disruptor<MessageEvent> sessionDisruptor;
    private Disruptor<MessageEvent> sessionDisruptor1;
    private Disruptor<MessageEvent> sessionDisruptor2;
    private Disruptor<MessageEvent> sessionDisruptor3;
    private AtomicInteger count = new AtomicInteger();

    private DisruptorImpl() {
        sessionDisruptor = new Disruptor(new MessageEventFactory(), sessionBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
        sessionDisruptor.handleEventsWith(new MessageEventHandler());
        sessionDisruptor.start();

        sessionDisruptor1 = new Disruptor(new MessageEventFactory(), sessionBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
        sessionDisruptor1.handleEventsWith(new MessageEventHandler());
        sessionDisruptor1.start();

        sessionDisruptor2 = new Disruptor(new MessageEventFactory(), sessionBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
        sessionDisruptor2.handleEventsWith(new MessageEventHandler());
        sessionDisruptor2.start();

        sessionDisruptor3 = new Disruptor(new MessageEventFactory(), sessionBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
        sessionDisruptor3.handleEventsWith(new MessageEventHandler());
        sessionDisruptor3.start();
    }

    public static DisruptorImpl getInstance() {
        return SingletonHolder.instance;
    }

    public Disruptor<MessageEvent> getSessionDisruptor() {
        int nowCount = count.addAndGet(1);
        int select = nowCount % 4;
        if (select == 0) {
            return sessionDisruptor;
        } else if (select == 1) {
            return sessionDisruptor1;
        } else if (select == 2) {
            return sessionDisruptor2;
        } else {
            return sessionDisruptor3;
        }
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
