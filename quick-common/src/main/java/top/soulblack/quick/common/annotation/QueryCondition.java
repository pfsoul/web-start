package top.soulblack.quick.common.annotation;

import top.soulblack.quick.common.enums.ConditionTypeEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface QueryCondition {

    /*
     * 命名规范，columnName值必须与数据库字段一一对应，例如需要对数据库中user_id操作，则columnName()为userId
     * 属性值在当前so为null时，忽略条件,不会报错
     */

    /**
     * 数据库中字段
     * 例如：我们需要查找一个大于当前时间的范围 create_time columnName为 createTime
     *      当前注解字段为createTimeFrom  所有的大小比较均在数据库字段columnName视角下
     *      最终注解为：
     *      \@QueryCondition(type = ConditionTypeEnum.GREATER_EQUAL,
     *         columnName = "createTime")
     *       private LocalDateTime createTimeFrom;
     *       即代表createTimeFrom所赋值时间 <= 数据库中createTime字段
     */
    String columnName();

    /**
     * 查询条件类型
     * {@link ConditionTypeEnum}
     */
    ConditionTypeEnum type();

}
