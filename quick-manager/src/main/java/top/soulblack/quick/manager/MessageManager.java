package top.soulblack.quick.manager;

import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.manager.base.BaseManager;

/**
 * <p>
 *  manager类
 * </p>
 *
 * @author soulblack
 * @since 2021-10-31
 */
public interface MessageManager extends BaseManager<Message> {

    /**
     * 创建消息
     * @param message 消息体
     * @return 创建并返回
     */
    Message create(Message message);

    /**
     * 根据ID获取消息
     * @param id 主键id
     * @return 返回消息
     */
    Message fetchById(Long id);
}