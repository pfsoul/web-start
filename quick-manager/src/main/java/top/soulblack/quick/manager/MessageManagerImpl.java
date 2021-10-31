package top.soulblack.quick.manager;

import org.springframework.stereotype.Service;
import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.manager.base.BaseManagerImpl;
import top.soulblack.quick.mapper.MessageMapper;

/**
 * @author soulblack
 * @since 2021-10-31
 */
@Service
public class MessageManagerImpl extends BaseManagerImpl<MessageMapper, Message> implements MessageManager {

    @Override
    public Message create(Message message) {
        return this.saveAndReturn(message);
    }

    @Override
    public Message fetchById(Long id) {
        return this.getById(id);
    }
}