package cn.maodun.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前日期
     *
     * @return 当前日期字符串
     */
    public static String getCurrentDate() {
        return formatDate(new Date(), DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间字符串
     */
    public static String getCurrentTime() {
        return formatDate(new Date(), DEFAULT_TIME_FORMAT);
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间字符串
     */
    public static String getCurrentDateTime() {
        return formatDate(new Date(), DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 格式化日期
     *
     * @param date   日期对象
     * @param format 日期格式
     * @return 格式化后的日期字符串
     */
    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 解析日期字符串
     *
     * @param dateString 日期字符串
     * @param format     日期格式
     * @return 解析后的日期对象
     * @throws ParseException 如果解析失败则抛出 ParseException 异常
     */
    public static Date parseDate(String dateString, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(dateString);
    }

    /**
     * 增加指定天数
     *
     * @param date      原始日期
     * @param numDays   增加的天数
     * @return 增加天数后的日期对象
     */
    public static Date addDays(Date date, int numDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, numDays);
        return calendar.getTime();
    }

    /**
     * 获取两个日期之间的天数差
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 日期差（以天为单位）
     */
    public static int getDaysDifference(Date startDate, Date endDate) {
        long startTime = truncateDate(startDate).getTime();
        long endTime = truncateDate(endDate).getTime();
        long diffTime = endTime - startTime;
        return (int) (diffTime / (24 * 60 * 60 * 1000));
    }

    /**
     * 将日期的时分秒部分清零
     *
     * @param date 原始日期
     * @return 清零后的日期对象
     */
    public static Date truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 判断两个日期是否为同一天
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果是同一天返回true，否则返回false
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时区
     *
     * @return 当前时区的字符串表示
     */
    public static String getCurrentTimeZone() {
        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        return timeZone.getID();
    }

    /**
     * 设置默认时区
     *
     * @param timeZone 时区的字符串表示
     */
    public static void setDefaultTimeZone(String timeZone) {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }
}