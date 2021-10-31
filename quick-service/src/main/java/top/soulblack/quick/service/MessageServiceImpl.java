package top.soulblack.quick.service;

import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.quick.cache.MessageCacheHelper;
import top.soulblack.quick.common.cmd.CreateMsgCmd;
import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.manager.MessageManager;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soulblack
 * @since 2021-10-31
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private MessageCacheHelper messageCacheHelper;

    @Override
    public Message create(CreateMsgCmd msgCmd) {
        // 验证参数
        if (msgCmd == null || StringUtil.isBlank(msgCmd.getMessage())) {
            return null;
        }
        // 创建并返回
        return messageManager.create(new Message().setMsg(msgCmd.getMessage()));
    }

    @Override
    public Message fetchById(Long id) {
        // 缓存获取
        return messageCacheHelper.fetchById(id);
    }
}
