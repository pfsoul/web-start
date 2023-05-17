package top.soulblack.quick.disruptor.multi;

import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : soulblack
 * @since : 2022/9/11
 */
@Slf4j
public class SimpleEventHandler implements WorkHandler<Event<Integer>> {

    private final String consumerId;

    public SimpleEventHandler(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Event<Integer> integerEvent) {
        log.info("consumerId : {} ,listen : {}", this.consumerId, integerEvent.getData());
    }
}
