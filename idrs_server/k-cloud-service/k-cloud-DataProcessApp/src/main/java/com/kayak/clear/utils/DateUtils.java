package com.kayak.clear.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 *
 * @author kkws-admin
 */
public class DateUtils {

    private final static Logger log = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 默认日期格式
     */
    public static final String DATE_DEFAULT_FORMAT = "yyyyMMdd";
    /**
     * 默认时间格式
     */
    public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";
    /**
     * 默认日期时间格式
     */
    public static final String DATETIME_DEFAULT_FORMAT = "yyyyMMdd HH:mm:ss";

    private static Calendar gregorianCalendar = null;

    static {
        gregorianCalendar = new GregorianCalendar();
    }

    /**
     * 取年龄
     *
     * @param
     * @return
     */
    public static int getAge(String birthday, String now_date) throws Exception {
        log.debug("获取客户年龄，客户生日为：{}, 当前日期为{}", birthday, now_date);

        Calendar cal = Calendar.getInstance();

        int yearNow       = cal.get(Calendar.YEAR);
        int monthNow      = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf           = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             birthday_date = sdf.parse(birthday);
        cal.setTime(birthday_date);

        int yearBirth       = cal.get(Calendar.YEAR);
        int monthBirth      = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        log.debug("计算客户年龄为{}岁", age);
        return age;

    }

    /**
     * 日期格式化yyyyMMdd
     *
     * @param date
     * @return
     */
    public static Date formatDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            log.error("Msg:[{}]", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 日期格式化yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String getDateFormat(Date date) {
        return new SimpleDateFormat(DATE_DEFAULT_FORMAT).format(date);
    }

    /**
     * 日期格式化yyyyMMdd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getDateTimeFormat(Date date) {
        return new SimpleDateFormat(DATETIME_DEFAULT_FORMAT).format(date);
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return HH:mm:ss
     */
    public static String getTimeFormat(Date date) {
        return new SimpleDateFormat(TIME_DEFAULT_FORMAT).format(date);
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param 格式类型
     * @return
     */
    public static String getDateFormat(Date date, String formatStr) {
        if (formatStr != null && !"".equals(formatStr)) {
            return new SimpleDateFormat(formatStr).format(date);
        }
        return null;
    }

    /**
     * 日期格式化
     *
     * @param date
     * @return
     */
    public static Date getDateFormat(String date) {
        try {
            return new SimpleDateFormat(DATE_DEFAULT_FORMAT).parse(date);
        } catch (ParseException e) {
            log.error("Msg:[{}]", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    public static Date getDateTimeFormat(String date) {
        try {
            return new SimpleDateFormat(DATETIME_DEFAULT_FORMAT).parse(date);
        } catch (ParseException e) {
            log.error("Msg:[{}]", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取当前日期(yyyyMMdd)
     *
     * @param date
     * @return
     */
    public static Date getNowDate() {
        return getDateFormat(new SimpleDateFormat(DATE_DEFAULT_FORMAT).format(new Date()));
    }

    /**
     * 获取当前日期星期一日期
     *
     * @return date
     */
    public static Date getFirstDayOfWeek() {
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取当前日期星期日日期
     *
     * @return date
     */
    public static Date getLastDayOfWeek() {
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取日期星期一日期
     *
     * @param 指定日期
     * @return date
     */
    public static Date getFirstDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取日期星期一日期
     *
     * @param 指定日期
     * @return date
     */
    public static Date getLastDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取当前月的第一天
     *
     * @return date
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(new Date());
    }

    /**
     * 获取日期的第一点
     *
     * @param workdate
     * @return
     * @throws Exception
     */
    public static String getFirstDayOfMonth(String workdate) throws Exception {
        SimpleDateFormat sdf             = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             date            = sdf.parse(workdate);
        Date             firstDayOfMonth = getFirstDayOfMonth(date);
        return sdf.format(firstDayOfMonth.getTime());
    }

    /**
     * 获取当前月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth() {
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 1);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 0);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的最后一天
     *
     * @param workdate
     * @return
     * @throws Exception
     */
    public static String getLastDayOfMonth(String workdate) throws Exception {
        SimpleDateFormat sdf            = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             date           = sdf.parse(workdate);
        Date             lastDayOfMonth = getLastDayOfMonth(date);
        return sdf.format(lastDayOfMonth.getTime());
    }

    /**
     * 获取上个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 0);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的最后一天
     *
     * @param workdate
     * @return
     * @throws Exception
     */
    public static String getLastDayOfLastMonth(String workdate) throws Exception {
        SimpleDateFormat sdf            = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             date           = sdf.parse(workdate);
        Date             lastDayOfMonth = getLastDayOfLastMonth(date);
        return sdf.format(lastDayOfMonth.getTime());
    }

    /**
     * 获取日期前一天
     *
     * @param date
     * @return
     */
    public static Date getDayBefore(Date date) {
        gregorianCalendar.setTime(date);
        int day = gregorianCalendar.get(Calendar.DATE);
        gregorianCalendar.set(Calendar.DATE, day - 1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取日期后一天
     *
     * @param date
     * @return
     */
    public static Date getDayAfter(Date date) {
        gregorianCalendar.setTime(date);
        int day = gregorianCalendar.get(Calendar.DATE);
        gregorianCalendar.set(Calendar.DATE, day + 1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getNowYear() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getNowMonth() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当月天数
     *
     * @return
     */
    public static int getNowMonthDay() {
        Calendar d = Calendar.getInstance();
        return d.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取时间段的每一天
     *
     * @param 开始日期
     * @param 结算日期
     * @return 日期列表
     */
    public static List<Date> getEveryDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        // 格式化日期(yy-MM-dd)
        startDate = getDateFormat(getDateFormat(startDate));
        endDate = getDateFormat(getDateFormat(endDate));
        List<Date> dates = new ArrayList<Date>();
        gregorianCalendar.setTime(startDate);
        dates.add(gregorianCalendar.getTime());
        while (gregorianCalendar.getTime().compareTo(endDate) < 0) {
            // 加1天
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(gregorianCalendar.getTime());
        }
        return dates;
    }

    /**
     * 获取提前多少个月
     *
     * @param monty
     * @return
     */
    public static Date getFirstMonth(int monty) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -monty);
        return c.getTime();
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static int getBetweenDays(String startDate, String endDate) throws Exception {

        SimpleDateFormat sdf    = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             smdate = sdf.parse(startDate);
        Date             bdate  = sdf.parse(endDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2        = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 日期srcDate 加上 天数days 后的日期
     *
     * @param srcDate
     * @param days
     * @return
     * @throws Exception
     */
    public static String getDateAddDays(String srcDate, int days) throws Exception {
        SimpleDateFormat sdf   = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             sDate = sdf.parse(srcDate);
        Calendar         cal   = Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.HOUR, days * 24);
        Date targetDate = cal.getTime();
        return sdf.format(targetDate);
    }

    /**
     * 日期srcDate 加上 月数months 后的日期
     *
     * @param srcDate
     * @param months
     * @return
     * @throws Exception
     */
    public static String getDateAddMonths(String srcDate, int months) throws Exception {
        SimpleDateFormat sdf   = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             sDate = sdf.parse(srcDate);
        Calendar         cal   = Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.MONTH, months);
        Date targetDate = cal.getTime();
        return sdf.format(targetDate);
    }

    /**
     * 日期srcDate 加上 年数years 后的日期
     *
     * @param srcDate
     * @param years
     * @return
     * @throws Exception
     */
    public static String getDateAddYears(String srcDate, int years) throws Exception {
        SimpleDateFormat sdf   = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             sDate = sdf.parse(srcDate);
        Calendar         cal   = Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.YEAR, years);
        Date targetDate = cal.getTime();
        return sdf.format(targetDate);
    }

    public static String getBrithdayByIdCode(String id_code) {
        if (Tools.isBlank(id_code) || (id_code.length() != 15 && id_code.length() != 18)) {
            log.error("身份证号码错误， 证件号码为{}", id_code);
            return "";
        }

        return id_code.substring(6, 14);
    }

    public static Boolean isLastDayOfMonth(String date) throws Exception {
        SimpleDateFormat sdf            = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        Date             date1           = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }
}
