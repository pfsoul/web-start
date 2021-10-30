package top.soulblack.quick.controller;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.soulblack.quick.common.cmd.CreateMsgCmd;
import top.soulblack.quick.common.enity.Message;
import top.soulblack.quick.common.vo.base.Result;
import top.soulblack.quick.service.MessageService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author soulblack
 * @since 2021-10-31
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "创建消息", notes = "创建消息")
    @PostMapping("/create")
    public Result<Message> create(@RequestBody CreateMsgCmd msgCmd) {
        return new Result<>(messageService.create(msgCmd));
    }
}
