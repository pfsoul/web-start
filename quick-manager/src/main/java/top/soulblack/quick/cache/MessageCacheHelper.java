package top.soulblack.quick.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.soulblack.quick.cache.base.DefaultCacheAdapter;
import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.manager.MessageManager;

/**
 * @author : soulblack
 * @since : 2021/10/31
 */
@Component
public class MessageCacheHelper {

    @Autowired
    private MessageManager messageManager;

    private final DefaultCacheAdapter<Long, Message> ID_AND_MESSAGE_CACHE = new DefaultCacheAdapter.Builder<Long, Message>()
            .setL1TTLSeconds(300L)
            .setL2TTLSeconds(500)
            .setL1MaxSize(500)
            .setCacheNull(true)
            .setEnableL2(true)
            .setL2KeyPrefix("ID_AND_MESSAGE_CACHE")
            .setFinalGet(k -> messageManager.fetchById(k))
            .build();

    public Message fetchById(Long id) {
        if (id == null || id < 1) {
            return null;
        }
        return ID_AND_MESSAGE_CACHE.get(id);
    }
}
