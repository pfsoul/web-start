package top.soulblack.quick.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author : soulblack
 * @since : 2022/9/11
 */
public class DisruptorTest {
    
    public static class Element{
        int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "disruptorThread");
            }
        };

        // RingBuffer生产工厂，初始化RingBuffer时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 处理Event的handler
        EventHandler<Element> handler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element o, long l, boolean b) throws Exception {
                System.out.println("Element : " + o.getValue() + Thread.currentThread());
                TimeUnit.SECONDS.sleep(1);
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 缓存大小
        int bufferSize = 4;

        // 创建disruptor 单生产者模式
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

        // 设置eventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        for (int i = 0; i < 10; i++) {
            Long sequence = null;
            try {
                sequence = ringBuffer.tryNext();
            } catch (InsufficientCapacityException e) {
                e.printStackTrace();
            }
            if (sequence == null) {
                continue;
            }
            System.out.println("publish " + sequence);
            try {
                Element element = ringBuffer.get(sequence);
                element.setValue(1);
            }finally {
                ringBuffer.publish(sequence);
            }
        }
    }
}
