package top.soulblack.quick.common.util;

import java.time.*;
import java.util.Date;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public class DateUtil {

    // 上海时间
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    public static LocalDateTime longToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZONE_ID);
    }

    public static LocalDate longToLocalDate(Long timestamp) {
        return DateUtil.longToLocalDateTime(timestamp).toLocalDate();
    }

    public static LocalTime longToLocalTime(Long timestamp) {
        return DateUtil.longToLocalDateTime(timestamp).toLocalTime();
    }

    public static Date longToDate(Long timestamp) {
        return new Date(timestamp);
    }

    public static Long localDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE_ID).toInstant().toEpochMilli();
    }

    public static Long DateToLong(Date date) {
        return date.getTime();
    }

}
