package top.soulblack.quick.common.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import top.soulblack.quick.client.so.base.BaseSo;
import top.soulblack.quick.common.annotation.QueryCondition;
import top.soulblack.quick.common.enity.base.BaseModel;
import top.soulblack.quick.common.enums.ConditionTypeEnum;
import top.soulblack.quick.common.so.sort.SortSo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Slf4j
public class WrapperUtil {

    public static final String CHARACTER_AND_NUMBER_PATTERN = "[a-zA-Z0-9_]+";

    /**
     * 根据字段注解对生成的wrapper进行补充
     * {@link QueryCondition}
     * @param queryWrapper wrapper
     * @param so BaseSo 请求体
     */
    public static <T> void convertByQueryCondition(QueryWrapper<T> queryWrapper, BaseSo so) {
        Assert.notNull(so, "So为空");
        injectConditionByField(queryWrapper, so);
        injectConditionWithOrderBy(queryWrapper, so);
    }

    /**
     * 读取每个属性的注解，并进行分批处理
     * @param queryWrapper wrapper
     * @param so 参数实体类
     */
    private static void injectConditionByField(QueryWrapper<?> queryWrapper, BaseSo so) {
        Field[] fields = ReflectUtil.getFields(so.getClass());
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof QueryCondition) {
                    QueryCondition query = (QueryCondition) annotation;
                    Object targetVal = ReflectUtil.getFieldValue(so, field.getName());
                    if (targetVal == null) {
                        log.debug("ConditionWrapperInterceptor#injectConditionByField targetVal is null, query type = {}, column name = {}", query.type(), query.columnName());
                        continue;
                    }
                    String columnName = ColumnUtil.fieldToColumn(query.columnName());
                    query.type().setup(columnName, targetVal, queryWrapper);
                    break;
                }
            }
        }
    }

    /**
     * 根据LinkedList进行排序
     * @param queryWrapper 查询wrapper
     * @param so searchObject
     */
    private static void injectConditionWithOrderBy(QueryWrapper<?> queryWrapper, BaseSo so) {
        if (so instanceof SortSo) {
            ((SortSo) so).getSorts().forEach(sort -> {
                if (StringUtils.isBlank(sort.getKey()) || sort.getType() == null) {
                    throw new RuntimeException("当前排序字段或类型不能为空");
                }
                if (!checkColumnName(sort.getKey())) {
                    throw new RuntimeException("验证异常");
                }
                String columnName = ColumnUtil.fieldToColumn(sort.getKey());
                switch (sort.getType()) {
                    case ASC:
                        queryWrapper.orderByAsc(columnName);
                        break;
                    case DESC:
                        queryWrapper.orderByDesc(columnName);
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            });
        }
    }

    /**
     * 判断传入的columnName是否是只包含数字，字母
     * @param columnName 字段名
     * @return 满足返回true，包含其他符号返回false
     */
    private static boolean checkColumnName(String columnName) {
        return Pattern.matches(CHARACTER_AND_NUMBER_PATTERN, columnName);
    }

    /**
     * 根据传过来的字段进行eq赋值
     * @param queryModel 请求model
     * @param <T> 泛型
     * @return 返回QueryWrapper
     */
    public static <T extends BaseModel> QueryWrapper<T> injectEqConditionByField(BaseModel queryModel) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Field[] fields = ReflectUtil.getFields(queryModel.getClass());
        for (Field field : fields) {
            Object targetVal = ReflectUtil.getFieldValue(queryModel, field.getName());
            if (targetVal == null || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String columnName = ColumnUtil.fieldToColumn(field.getName());
            ConditionTypeEnum.EQ.setup(columnName, targetVal, queryWrapper);
        }
        return queryWrapper;
    }
}
