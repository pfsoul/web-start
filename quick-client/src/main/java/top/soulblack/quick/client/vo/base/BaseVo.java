package top.soulblack.quick.client.vo.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Data
public class BaseVo implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人id
     */
    private Long creatorId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人id
     */
    private Long updaterId;
    /**
     * 修改人
     */
    private String updater;
    /**
     * 备注
     */
    private String reMark;
}
