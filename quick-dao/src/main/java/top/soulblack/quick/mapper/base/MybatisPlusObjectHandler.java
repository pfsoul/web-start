package top.soulblack.quick.mapper.base;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.soulblack.quick.common.enity.base.BaseModel;

import java.time.LocalDateTime;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Component
public class MybatisPlusObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间和更新时间赋默认值
        this.setFieldValByName(BaseModel.CREATE_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(BaseModel.UPDATE_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(BaseModel.VERSION, 1, metaObject);
        // 若creatorId为null，则填充默认值
        Long creatorId = (Long) getFieldValByName(BaseModel.CREATOR_ID, metaObject);
        if (creatorId == null) {
            this.setFieldValByName(BaseModel.CREATOR_ID, BaseModel.DEFAULT_OPERATOR_ID, metaObject);
            this.setFieldValByName(BaseModel.UPDATER_ID, BaseModel.DEFAULT_OPERATOR_ID, metaObject);
            this.setFieldValByName(BaseModel.CREATOR, BaseModel.DEFAULT_OPERATOR, metaObject);
            this.setFieldValByName(BaseModel.UPDATER, BaseModel.DEFAULT_OPERATOR, metaObject);
        } else {
            String creator = (String) getFieldValByName(BaseModel.CREATOR, metaObject);
            this.setFieldValByName(BaseModel.UPDATER_ID, creatorId, metaObject);
            this.setFieldValByName(BaseModel.UPDATER, creator, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间赋默认值
        this.setFieldValByName(BaseModel.UPDATE_TIME, LocalDateTime.now(), metaObject);

        // 若updatorId为null，则填充默认值
        if (getFieldValByName(BaseModel.UPDATER_ID, metaObject) == null) {
            this.setFieldValByName(BaseModel.UPDATER_ID, BaseModel.DEFAULT_OPERATOR_ID, metaObject);
            this.setFieldValByName(BaseModel.UPDATER, BaseModel.DEFAULT_OPERATOR, metaObject);
        }
    }
}
