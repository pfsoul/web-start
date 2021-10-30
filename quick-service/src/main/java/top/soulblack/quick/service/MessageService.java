package top.soulblack.quick.service;

import top.soulblack.quick.common.cmd.CreateMsgCmd;
import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.common.vo.base.Result;

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
}
