package top.soulblack.quick.disruptor.multi;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : soulblack
 * @since : 2022/9/11
 */
@Slf4j
public class SimpleEventProduce {

    /**
     * 构建多生产者、初始化每个元素，设定bufferSize以及默认 的消费者等待策略
     */
    public static final RingBuffer<Event<Integer>> SIMPLE_RING_BUFFER = RingBuffer.create(
            ProducerType.MULTI,
            Event::new,
            1024,
            new BlockingWaitStrategy()
    );

    public void onData(Integer value) {
        Long sequence = null;
        try {
            sequence = SIMPLE_RING_BUFFER.tryNext();
        } catch (InsufficientCapacityException e) {
            log.error("队列已满 TODO STH", e);
        }
        if (sequence == null) {
            return;
        }
        try {
            Event<Integer> integerEvent = SIMPLE_RING_BUFFER.get(sequence);
            integerEvent.setData(value);
        }finally {
            SIMPLE_RING_BUFFER.publish(sequence);
        }
    }
}
