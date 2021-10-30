package top.soulblack.quick.service;

import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.quick.common.cmd.CreateMsgCmd;
import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.common.vo.base.Result;
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

    @Override
    public Message create(CreateMsgCmd msgCmd) {
        // 验证参数
        if (msgCmd == null || StringUtil.isBlank(msgCmd.getMessage())) {
            return null;
        }
        return messageManager.create(new Message().setMsg(msgCmd.getMessage()));
    }
}
