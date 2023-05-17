package top.soulblack.quick.common.enity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import top.soulblack.quick.common.enity.base.BaseModel;

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