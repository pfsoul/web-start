package top.soulblack.quick.disruptor.multi;

import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ExceptionHandlerWrapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : soulblack
 * @since : 2022/9/11
 */
public class DisruptorMultiTest {

    public static void main(String[] args) {
        SequenceBarrier sequenceBarrier = SimpleEventProduce.SIMPLE_RING_BUFFER.newBarrier();

        SimpleEventHandler[] simpleEventHandlers = new SimpleEventHandler[]{
                new SimpleEventHandler("1"),
                new SimpleEventHandler("2"),
                new SimpleEventHandler("3")
        };


        WorkerPool<Event<Integer>> workerPool = new WorkerPool<>(
                SimpleEventProduce.SIMPLE_RING_BUFFER,
                sequenceBarrier,
                new ExceptionHandlerWrapper<>(),
                simpleEventHandlers
        );

        SimpleEventProduce.SIMPLE_RING_BUFFER.addGatingSequences(workerPool.getWorkerSequences());

        workerPool.start(new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100)));

        for (int i = 0; i < 5; i++) {
            SimpleEventProduce simpleEventProduce = new SimpleEventProduce();
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    simpleEventProduce.onData(j);
                }
            }).start();
        }
    }
}
