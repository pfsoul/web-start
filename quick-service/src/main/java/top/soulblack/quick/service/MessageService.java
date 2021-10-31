package top.soulblack.quick.service;

import top.soulblack.quick.common.cmd.CreateMsgCmd;
import top.soulblack.quick.common.enity.Message;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soulblack
 * @since 2021-10-31
 */
public interface MessageService {

    /**
     * 创建一条消息
     * @param msgCmd 消息体
     * @return 创建后返回实体
     */
    Message create(CreateMsgCmd msgCmd);

    /**
     * 根据ID获取对应消息
     * @param id 主键id
     * @return 返回对应实体
     */
    Message fetchById(Long id);
}
