package top.soulblack.quick.common.enums;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public enum ConditionTypeEnum {

    IN{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            if (targetVal instanceof Collection) {
                queryWrapper.in((columnName), (Collection<?>) targetVal);
            } else if (targetVal.getClass().isArray()) {
                queryWrapper.in((columnName), Arrays.asList((Object[]) targetVal));
            } else {
                throw new RuntimeException("系统错误");
            }
        }
    },

    /* 比较类型枚举 */
    GREATER_THAN{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.gt(columnName, targetVal);
        }
    },

    GREATER_EQUAL{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.ge(columnName, targetVal);
        }
    },

    LESS_THAN{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.lt(columnName, targetVal);
        }
    },

    LESS_EQUAL{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.le(columnName, targetVal);
        }
    },

    /* 模糊匹配类型枚举 */
    LIKE_BOTH_WAY{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.like(columnName, targetVal);
        }
    },

    LIKE_LEFT_WAY{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.likeLeft(columnName, targetVal);
        }
    },

    LIKE_RIGHT_WAY{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.likeRight(columnName, targetVal);
        }
    },

    EQ{
        @Override
        public void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper) {
            queryWrapper.eq(columnName, targetVal);
        }
    };

    public abstract void setup(String columnName, Object targetVal, QueryWrapper<?> queryWrapper);

}
