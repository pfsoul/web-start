package top.soulblack.quick.common.enity;

import top.soulblack.quick.common.enity.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author soulblack
 * @since 2021-10-31
 */
@ApiModel(description= "")
@Data
@Accessors(chain = true)
@TableName("quick_message")
public class Message extends BaseModel {


    private String msg;


}