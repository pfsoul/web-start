package top.soulblack.quick.common.cmd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author : soulblack
 * @since : 2021/10/31
 */
@Data
@ApiModel(value = "创建消息请求体")
public class CreateMsgCmd {

    @NotBlank(message = "消息体不可为空")
    @ApiModelProperty(value = "消息体")
    private String message;
}
