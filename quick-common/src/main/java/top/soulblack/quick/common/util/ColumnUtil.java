package top.soulblack.quick.common.util;


import cn.hutool.core.util.ReflectUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public class ColumnUtil {
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    private static final Pattern INSTANTIATED_METHOD_TYPE = Pattern.compile("\\(L(?<instantiatedMethodType>[\\S&&[^;)]]+);\\)L[\\S]+;");

    private static final Pattern INSTANTIATED_METHOD_RETURN_TYPE = Pattern.compile("\\(L[\\S]+;\\)L(?<instantiatedMethodReturnType>[\\S&&[^;)]]+);");

    private static final char UNDERLINE = '_';

    private static final String NAME_METHOD_WRITE_REPLACE = "writeReplace";

    /**
     * 获取方法对应的变量名
     * @param fun
     * @param <T> 方法所属类名
     * @param <R> 方法返回值类型
     * @return
     */
    public static <T, R> String resolveField(Fun<T, R> fun) {
        SerializedLambda serializedLambda = getSerializedLambda(fun);
        return methodToField(serializedLambda.getImplMethodName());
    }

    /**
     * 获取方法对应的表字段名
     * @param fun
     * @param <T> 方法所属类名
     * @param <R> 方法返回值类型
     * @return
     */
    public static <T, R> String resolveColumn(Fun<T, R> fun) {
        return fieldToColumn(resolveField(fun));
    }

    /**
     * 将sql聚合查询的结果，转化为方法的范型返回值类型
     * @param fun
     * @param sqlResult
     * @param <T> 法所属类名
     * @param <R> 方法返回值类型
     * @return
     */
    public static <T, R> R convertSqlResultToPatternReturnType(Fun<T, R> fun, Object sqlResult) {
        // 空校验
        if (sqlResult == null) {
            return null;
        }

        // 获取应该返回的类型
        String returnTypeStr = getInstantiatedMethodReturnType(fun);
        if (returnTypeStr == null) {
            return null;
        }

        // 常见返回类型转换
        if (sqlResult instanceof Timestamp) {
            if (LocalDateTime.class.getName().equals(returnTypeStr)) {
                return (R) ((Timestamp) sqlResult).toLocalDateTime();
            } else if (Date.class.getName().equals(returnTypeStr)) {
                return (R) new Date(((Timestamp) sqlResult).getTime());
            } else if (LocalDate.class.getName().equals(returnTypeStr)) {
                return (R) ((Timestamp) sqlResult).toLocalDateTime().toLocalDate();
            } else if (LocalTime.class.getName().equals(returnTypeStr)) {
                return (R) ((Timestamp) sqlResult).toLocalDateTime().toLocalTime();
            }
        }

        if (sqlResult instanceof Long) {
            if (LocalDateTime.class.getName().equals(returnTypeStr)) {
                return (R) DateUtil.longToLocalDateTime((Long) sqlResult);
            } else if (Date.class.getName().equals(returnTypeStr)) {
                return (R) DateUtil.longToDate((Long) sqlResult);
            } else if (LocalDate.class.getName().equals(returnTypeStr)) {
                return (R) DateUtil.longToLocalDate((Long) sqlResult);
            } else if (LocalTime.class.getName().equals(returnTypeStr)) {
                return (R) DateUtil.longToLocalTime((Long) sqlResult);
            }
        }

        return (R) sqlResult;
    }

    /**
     * 获取方法所属类名
     * @param fun
     * @param <T> 方法所属类名
     * @param <R> 方法返回值类型
     * @return
     */
    public static <T, R> String getInstantiatedMethodType(Fun<T, R> fun) {
        SerializedLambda serializedLambda = getSerializedLambda(fun);
        Matcher matcher = INSTANTIATED_METHOD_TYPE.matcher(serializedLambda.getInstantiatedMethodType());
        if (matcher.find()) {
            return matcher.group("instantiatedMethodType").replace('/', '.');
        }
        return null;
    }

    /**
     * 获取方法返回值类名
     * @param fun
     * @param <T> 方法所属类名
     * @param <R> 方法返回值类型
     * @return
     */
    public static <T, R> String getInstantiatedMethodReturnType(Fun<T, R> fun) {
        SerializedLambda serializedLambda = getSerializedLambda(fun);
        Matcher matcher = INSTANTIATED_METHOD_RETURN_TYPE.matcher(serializedLambda.getInstantiatedMethodType());
        if (matcher.find()) {
            return matcher.group("instantiatedMethodReturnType").replace('/', '.');
        }
        return null;
    }

    /**
     * 用Serializable的writeReplace方法获取lambda序列化信息
     * @param fun
     * @param <T> 方法所属类名
     * @param <R> 方法返回值类型
     * @return
     */
    private static <T, R> SerializedLambda getSerializedLambda(Fun<T, R> fun) {
        Class<? extends Fun> clazz = fun.getClass();
        WeakReference<SerializedLambda> weakReference = FUNC_CACHE.get(clazz);
        if (weakReference == null || weakReference.get() == null) {
            SerializedLambda serializedLambda = ReflectUtil.invoke(fun, NAME_METHOD_WRITE_REPLACE);
            weakReference = new WeakReference<>(serializedLambda);
            FUNC_CACHE.put(clazz, weakReference);
        }
        return weakReference.get();
    }

    /**
     * 方法名转java字段
     * @param methodName
     * @return
     */
    private static String methodToField(String methodName) {
        String fieldName;
        if (methodName.startsWith("is")) {
            fieldName = methodName.substring(2);
        } else if (methodName.startsWith("get") || methodName.startsWith("set")) {
            fieldName = methodName.substring(3);
        } else {
            fieldName = methodName;
        }

        if (fieldName.length() == 1 || (fieldName.length() > 1 && !Character.isUpperCase(fieldName.charAt(1)))) {
            fieldName = fieldName.substring(0, 1).toLowerCase(Locale.ENGLISH) + fieldName.substring(1);
        }

        return fieldName;
    }

    /**
     * java字段转表字段
     * @param fieldName
     * @return
     */
    public static String fieldToColumn(String fieldName) {
        int len = fieldName.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    @FunctionalInterface
    public interface Fun<T, R> extends Function<T, R>, Serializable {
    }
}