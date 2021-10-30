package top.soulblack.quick.common.util;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Optional;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public class MybatisPlusUtil {
    /**
     * 获取数据库字段名
     * @param column 解析方法
     * @return 返回数据库字段名
     * @throws MybatisPlusException 特殊异常
     */
    public static <T> String resolve(SFunction<T, ?> column) throws MybatisPlusException {
        return getColumn(LambdaUtils.resolve(column));
    }

    /**
     * 根据方法名解析数据库字段名
     * @param lambda
     */
    private static String getColumn(SerializedLambda lambda) throws MybatisPlusException {
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());

        return Optional.ofNullable(LambdaUtils.getColumnMap(lambda.getInstantiatedType()).getOrDefault(fieldName, null))
                .map(ColumnCache::getColumn)
                .orElseThrow(() ->
                        ExceptionUtils.mpe("Your property named \"%s\" cannot find the corresponding database column name!", fieldName)
                );
    }
}
