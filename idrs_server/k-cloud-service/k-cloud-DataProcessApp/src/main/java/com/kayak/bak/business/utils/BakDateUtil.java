package com.kayak.bak.business.utils;

import com.kayak.bak.enums.DateFormatEnum;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BakDateUtil {

    private BakDateUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatDate(Date date, DateFormatEnum formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType.getValue());
        //设置为东八区
        //format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return format.format(date);
    }
    public static final SimpleDateFormat SDF_DATE_8 = new SimpleDateFormat("yyyyMMdd");

    private static final ThreadLocal<Map<String, DateFormat>> LOCAL_FORMAT = new ThreadLocal<Map<String, DateFormat>>() {
        @Override
        protected Map<String, DateFormat> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * 获取一个DateFormat对象
     *
     * @param pattern
     *            时间格式
     * @return 返回java.text.DateFormat对象
     */
    public static DateFormat getFormat(String pattern) {
        Map<String, DateFormat> local = LOCAL_FORMAT.get();
        DateFormat format = local.get(pattern);
        if (format == null) {
            format = new SimpleDateFormat(pattern);
            local.put(pattern, format);
        }
        return format;
    }

    /**
     * 主动删除DateFormat对象，一般情况下不会需要
     */
    public static void delFormat() {
        LOCAL_FORMAT.remove();
    }

    /**
     * 获取yyyyMMddHHmmss格式的当前时间的时间戳字符串
     *
     * @return 14位当前时间时间戳
     */
    public static String getTimestamp14() {
        return getFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 获取yyyyMMddHHmmssSSS格式的当前时间的时间戳字符串
     *
     * @return 17位当前时间时间戳
     */
    public static String getTimestamp17() {
        return getFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 获取yyyy-MM-dd HH:mm:ss格式的当前时间的时间戳字符串
     *
     * @return 19位当前时间时间戳
     */
    public static String getTimestamp19() {
        return getFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取yyyyMMdd格式的当前时间的日期字符串
     *
     * @return 当前日期
     */
    public static String getNowDate() {
        return getFormat("yyyyMMdd").format(new Date());
    }

    /**
     * 获取yyyyMMdd格式的当前时间的日期字符串
     *
     * @return 返回前一天日期
     */
    public static String getBeforeDate(){
        SimpleDateFormat predf = new SimpleDateFormat("yyyyMMdd");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
    }

    /**
     * 获取yyyy-MM-dd格式的当前时间的日期字符串
     *
     * @return 返回前一天日期
     */
    public static String getBeforeDate1(){
        SimpleDateFormat predf = new SimpleDateFormat("yyyy-MM-dd");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
    }

    /**
     * 获取yyyyMMdd格式的当前时间的日期字符串
     *
     * @return 返回前一天日期
     */
    public static String getBeforeDate3(){
        SimpleDateFormat predf = new SimpleDateFormat("yyyyMMdd");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000*3));
    }

    /**
     *
     * @return 返回当前日期前一天三年前日期   yyyy-MM-dd格式
     */
    public static String getThreeYearsAgoDate(){
        Date d=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
        cal.add(cal.YEAR,-3);
        return format(cal.getTime(),"yyyy-MM-dd");
    }

    /**
     * 返回前一天按年
     * @return
     */
    public static String getBeforeDateyyyy(){
        SimpleDateFormat predf = new SimpleDateFormat("yyyy");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
    }

    /**
     * 返回前一天按月
     * @return
     */
    public static String getBeforeDateMM(){
        SimpleDateFormat predf = new SimpleDateFormat("MM");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
    }

    /**
     * 返回前一天按日
     * @return
     */
    public static String getBeforeDatedd(){
        SimpleDateFormat predf = new SimpleDateFormat("dd");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)24 * 60 * 60 * 1000));
    }

    /**
     * 获取yyyyMMdd格式的当前时间的日期字符串
     *
     * @return 返回前两天日期
     */
    public static String getBeforeYesterdayDate(){
        SimpleDateFormat predf = new SimpleDateFormat("yyyyMMdd");
        Date d=new Date();
        return predf.format(new Date(d.getTime() - (long)48 * 60 * 60 * 1000));
    }

    /**
     * 获取HHmmss格式的当前时间的时间字符串
     *
     * @return 当前时间
     */
    public static String getNowTime() {
        return getFormat("HHmmss").format(new Date());
    }

    /**
     * 获取指定格式的日期/时间字符串
     *
     * @param date
     *            时间对象
     * @param pattern
     *            格式
     * @return 返回格式化后的日期字符串
     */
    public static String format(Date date, String pattern) {
        return getFormat(pattern).format(date);
    }

    /**
     * 获取指定格式的Date对象
     *
     * @param date
     *            时间对象
     * @param pattern
     *            格式
     * @return 返回java.util.Date对象
     * @throws ParseException
     */
    public static Date parse(String date, String pattern) throws ParseException {
        return getFormat(pattern).parse(date);
    }

    /**
     * 检查日期合法性
     *
     * @param date
     *            被检查日期字符串
     * @param pattern
     *            日期字符串格式
     * @param lenient
     *            是否宽松模式
     * @return 检查合法返回true，否则false
     */
    public static boolean formatChecking(String date, String pattern, boolean lenient) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        try {
            DateFormat format = getFormat(pattern);
            format.setLenient(lenient);
            format.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 转换日期字符串为指定的其他格式
     *
     * @param date
     *            日期字符串
     * @param pre
     *            原格式
     * @param pro
     *            新格式
     * @return String 返回转换后的日期
     * @throws ParseException
     */
    public static String formatConvert(String date, String pre, String pro) throws ParseException {
        if (date == null || date.isEmpty()) {
            return null;
        }
        Date _date = parse(date, pre);
        return format(_date, pro);
    }

    /**
     * 字符串日期转为Calendar
     *
     * @param date
     *            时间对象
     * @param pattern
     *            时间格式
     * @return 返回java.util.Calendar对象
     * @throws ParseException
     */
    public static Calendar getCalendar(String date, String pattern) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, pattern));
        return c;
    }

    /**
     * 日期计算（加减）
     *
     * @param date
     *            基本日期
     * @param amount
     *            增减天数
     * @param pattern
     *            日期格式
     * @return 返回计算之后的日期字符串
     * @throws ParseException
     */
    public static String add(String date, String pattern, int amount) throws ParseException {
        return calculation(date, pattern, amount, Calendar.DATE);
    }

    /**
     * java.util.Date类时间计算方法
     *
     * @param time
     *            基日期/时间
     * @param pattern
     *            time的格式
     * @param amount
     *            增减量
     * @param calendar
     *            计算类型（例：Calendar.DATE）
     * @return 返回计算之后的日期字符串
     * @throws ParseException
     */
    public static String calculation(String time, String pattern, int amount, int calendar) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(parse(time, pattern));
        c.add(calendar, amount);
        return format(c.getTime(), pattern);
    }

    public static String getWholeTime(Date date) {
        DateFormat df = new SimpleDateFormat("HHmmssSSS");
        return df.format(date);
    }

    /**
     * 计算两个日期相差的秒数
     * @param str1		日期1
     * @param str2		日期2
     * @param pattern	日期格式
     * @return
     */
    public static String getDistanceTime(String str1, String str2,String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long diffMin = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            diffMin = (day * 24 * 60) +  hour * 60 + min;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(diffMin);
    }

    /**
     * 相差后日期
     * @param diffModth 相差月数
     * @param pattern 格式化类型
     * @return
     * @throws ParseException
     */
    public static String getDiffDate(int diffModth,String datetime, String pattern,String type) throws ParseException {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(datetime);
        c.setTime(date);
        if (type.equals("sub")) {
            c.add(Calendar.MONTH, - diffModth);
        }else if (type.equals("add")) {
            c.add(Calendar.MONTH, + diffModth);
        }
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    /**
     * 判断日期相差月份
     * @param startDate 开始日期
     * @param endDate	结束日期
     * @return
     * @throws ParseException
     */
    public static int getMonth(String startDate, String endDate,String pattern) throws ParseException {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date start = df.parse(startDate);
        Date end = df.parse(endDate);
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 校验日期
     * @param startDate
     * @param endDate
     * @param pattern
     * @param month
     * @return
     * @throws ParseException
     */
    public static boolean checkDate(String startDate,String endDate,String pattern,int month) throws ParseException {
        boolean result = false;
        Calendar c = Calendar.getInstance();
        //获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        //初始日期;
        Date date = sdf.parse(startDate);
        //设置日历时间
        c.setTime(date);
        //在日历的月份上增加6个月
        c.add(Calendar.MONTH,month);
        //得到N个月后的日期
        String date2 = sdf.format(c.getTime());
        //判断结束日期是否大于n个月后的日期
        if (Integer.parseInt(endDate) > Integer.parseInt(date2)) {
            return result;
        }
        return true;
    }

    /**
     * 校验日期：检验 起始日期与终止日期不能超过N天
     * @param startDate
     * @param endDate
     * @param pattern
     * @param day
     * @return
     * @throws ParseException
     */
    public static boolean checkDateDays(String startDate,String endDate,String pattern,int day) throws ParseException {
        boolean result = false;
        Calendar c = Calendar.getInstance();
        //获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        //初始日期;
        Date date = sdf.parse(startDate);
        //设置日历时间
        c.setTime(date);
        //在日历的月份上增加N天
        c.add(Calendar.DATE,day);
        //得到N天后的日期
        String date2 = sdf.format(c.getTime());
        //判断结束日期是否大于N天后的日期
        if (Integer.parseInt(endDate) > Integer.parseInt(date2)) {
            return result;
        }
        return true;
    }

    /** 字符串转Date */
    public static Date getDate(String date, SimpleDateFormat df) throws ParseException {
        return getFormat(df).parse(date);
    }

    private static DateFormat getFormat(SimpleDateFormat format) {
        String pattern = format.toPattern();
        return getFormat(pattern);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s,String pattern){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    //比较两个日期大小
    public static int compare_date(String DATE1, String DATE2,String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据送过来的时间获取前一天日期
     *
     * @return 返回前一天日期
     * @throws ParseException
     */
    public static String getupdateoneDate(String nowdate){

        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=Calendar.getInstance();
        Date d;
        try {
            d = df.parse(nowdate);
            cal.setTime(d);
            cal.add(Calendar.DATE, -1);  //减1天

        } catch (ParseException e) {
        }
        return df.format(cal.getTime());
    }

    /**
     * 判断一个时间（与日期无关）是否存在于一个时间范围中。此范围为{@code origin ± second}秒所表示的范围。
     * <p>
     * 该方法对日期不敏感，如果上送的时间中包含日期信息将会被忽略
     *
     * @param time
     *            需要判断的时间
     * @param origin
     *            原点时间
     * @param second
     *            区间大小，单位：秒
     * @param pattern
     *            时间格式
     * @return 如果存在于范围中则返回true，否则返回false。若time值等于边界值时认为存在于范围中
     * @throws ParseException
     */
    public static boolean intInterval(String time, String origin, int second, String pattern) throws ParseException {
        second = Math.abs(second);
        String s = calculation(origin, pattern, -second, Calendar.SECOND);
        String e = calculation(origin, pattern, second, Calendar.SECOND);
        return intInterval(time, s, e, pattern);
    }

    /**
     * 判断一个时间（与日期无关）是否存在于一个时间范围中。此范围开始于参数{@code start}结束于{@code end}
     * <p>
     * 该方法对日期不敏感，如果上送的时间中包含日期信息将会被忽略
     *
     * @param time
     *            需要判断的时间
     * @param start
     *            范围开始时间节点
     * @param end
     *            范围结束时间节点
     * @param pattern
     *            时间格式
     * @return 如果存在于范围中则返回true，否则返回false。若time值等于边界值时认为存在于范围中
     * @throws ParseException
     */
    public static boolean intInterval(String time, String start, String end, String pattern) throws ParseException {
        time = toZero(time, pattern, "HHmmss");
        start = toZero(start, pattern, "HHmmss");
        end = toZero(end, pattern, "HHmmss");

        int i = start.compareTo(end);
        if (i > 0) { // start > stop
            if (time.compareTo(end) > 0 && (time.compareTo(start) < 0))
                return false;
            return true;
        } else if (i < 0) { // start < stop
            if (time.compareTo(start) >= 0 && (time.compareTo(end) <= 0))
                return true;
            return false;
        }
        return time.compareTo(start) == 0;
    }

    static String toZero(String time, String pattern) throws ParseException {
        return toZero(time, pattern, pattern);
    }

    static String toZero(String time, String pre, String pro) throws ParseException {
        Date date = parse(time, pre);
        return format(date, pro);
    }

    /**
     * 获取当月的天数
     *
     * @return 天数
     * @throws ParseException
     */
    public static int getCurrentMonthDay(){
        Calendar time = Calendar.getInstance();
        time.set(Calendar.DATE, 1);
        time.roll(Calendar.DATE, -1);
        int maxDate = time.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取指定月份的天数
     *
     * @return 天数
     * @throws ParseException
     */
    public static int getDaysByYearMonth(int year , int month){
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month -1);
        time.set(Calendar.DATE, 1);
        time.roll(Calendar.DATE, -1);
        int maxDate = time.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取指定去年年份
     * @return 年份
     */
    public static String getDayByYear(){
        SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy");
        Calendar time = Calendar.getInstance();
        time.setTime(new Date());
        time.add(Calendar.YEAR, -1);
        Date y = time.getTime();
        String year = SDF_DATE.format(y);
        return  year;
    }


    /**
     * dateFirst比dateSeconed多的天数
     * @param dateFirst
     * @param dateSeconed
     * @return
     */
    public static int getdifferentDays(String dateFirst,String dateSeconed) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = SDF_DATE_8.parse(dateFirst);
            date2 = SDF_DATE_8.parse(dateSeconed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2){   //同一年
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++) {
                //判断是否为闰年
                if(i%4==0 && i%100!=0 || i%400==0){//闰年
                    timeDistance += 366;
                }else{//不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2-day1) ;
        }else {//不同年
            return day2-day1;
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String diffTime(String startTime) throws ParseException{
        // 定义相差秒数
        long res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date startDate = simpleDateFormat.parse(startTime);
        long ts = startDate.getTime();
        long nowTime = System.currentTimeMillis();
        res = (nowTime - ts) / 1000;
        return String.valueOf(res);
    }

    /*
     * 	获取当前日期一年后的日期
     */
    public static String getNowDateAddYear() throws ParseException {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);// 把日期往后增加一年.整数往后推,负数往前移动
        return getFormat("yyyyMMdd").format(calendar.getTime());
    }

    /**
     * 获取上个月第一天
     */
    public static String getLastMonthStartDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取上个月最后一天
     * @return
     */
    public static String getLastMonthEndDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取上个月月份，yyyyMM格式
     * @return
     */
    public static String getLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取上个月月份，MM格式
     * @return
     */
    public static String getLastMMMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return sdf.format(calendar.getTime());
    }

    // 获取本周的开始时间
    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        //当前日期前一日为统计的T日
        Date date = new Date(new Date().getTime() - (long)24 * 60 * 60 * 1000);
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    // 获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获取上周的开始时间
    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date(new Date().getTime() - (long)24 * 60 * 60 * 1000);
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    // 获取上周的结束时间
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获取系统时间前一天本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    // 获取系统时间前一天本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    // 获取系统时间前一天上月的开始时间
    public static Date getBeginDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayStartTime(calendar.getTime());
    }

    // 获取系统时间前一天上月的结束时间
    public static Date getEndDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar.getTime());
    }

    // 获取系统时间前一天本年的开始时间
    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    // 获取系统时间前一天本年的结束时间
    public static Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    // 获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取系统前一天的今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date(new Date().getTime() - (long)24 * 60 * 60 * 1000);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    // 获取系统前一天的本月是哪一月
    public static int getNowMonth() {
        Date date = new Date(new Date().getTime() - (long)24 * 60 * 60 * 1000);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    public static String getNextMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        return sdf.format(calendar.getTime()).substring(4, 6);
    }

    /**
     * 功能：获取指定日期所在月份的第一天
     * 作者：rennannan
     * 日期：20210525
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getFirstDayDateOfMonth(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        cal.setTime(BakDateUtil.getDate(date, sd));
        int first = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, first);
        return sd.format(cal.getTime());
    }

    /**
     * 功能：获取指定日期所在月份的最后一天
     * 作者：rennannan
     * 日期：20210525
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getLastDayOfMonth(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        cal.setTime(BakDateUtil.getDate(date, sd));
        int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return sd.format(cal.getTime());
    }

    /**
     * 功能：获取某段时间内的周一（二等等）的日期
     * 作者：rennannan
     * 日期：20210525
     *
     * @param dataBegin 开始日期
     * @param dataEnd   结束日期
     * @param weekDays  获取周几，1－6代表周一到周六。0代表周日
     * @return 返回日期List
     */
    public static List<String> getDayOfWeekWithinDateInterval(String dataBegin, String dataEnd, int weekDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> dateResult = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String[] dateInterval = {dataBegin, dataEnd};
        Date[] dates = new Date[dateInterval.length];
        for (int i = 0; i < dateInterval.length; i++) {
            cal.set(Integer.parseInt(dateInterval[i].substring(0, 4)), Integer.parseInt(dateInterval[i].substring(4, 6)) - 1, Integer.parseInt(dateInterval[i].substring(6, 8)));
            dates[i] = cal.getTime();
        }
        for (Date date = dates[0]; date.compareTo(dates[1]) <= 0; ) {
            cal.setTime(date);
            if (cal.get(Calendar.DAY_OF_WEEK) - 1 == weekDays) {
                String format = sdf.format(date);
                dateResult.add(format);
            }
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
        return dateResult;
    }

    /**
     * 功能：获取当前日期所在季度的最后一天
     * 作者：rennannan
     * 日期：20210525
     *
     * @param date
     * @return
     */
    public static String getMaxOrMinDateOfQuarter(String date, String type) {
        String year = date.substring(0, 4);
        String strMonth = date.substring(4, 6);
        int month = Integer.parseInt(strMonth.startsWith("0") ? strMonth.substring(1) : strMonth.substring(0));
        String monthDay = "";
        if (month <= 3) {
            if (type.equals("max")) {
                monthDay = "0331";
            } else {
                monthDay = "0101";
            }

        }
        if (month >= 4 && month <= 6) {
            if (type.equals("max")) {
                monthDay = "0630";
            } else {
                monthDay = "0401";
            }

        }
        if (month >= 7 && month <= 9) {
            if (type.equals("max")) {
                monthDay = "0930";
            } else {
                monthDay = "0701";
            }

        }
        if (month >= 10) {
            if (type.equals("max")) {
                monthDay = "1231";
            } else {
                monthDay = "1001";
            }

        }
        return year + monthDay;
    }

    /**
     * 功能：获取日期属于第几季度
     * 作者：rennannan
     * 日期：20210617
     *
     * @param date yyyymmdd  pattern取大写还是小写  0小写 1代表大写
     * @return
     */
    public static String getQuarterNumber(String date, int pattern) {
        String strMonth = date.substring(4, 6);
        int month = Integer.parseInt(strMonth.startsWith("0") ? strMonth.substring(1) : strMonth.substring(0));
        String number = "";
        String chineseNumber = "";
        if (month <= 3) {
            number = "1";
            chineseNumber = "一";
        }
        if (month >= 4 && month <= 6) {
            number = "2";
            chineseNumber = "二";
        }
        if (month >= 7 && month <= 9) {
            number = "3";
            chineseNumber = "三";
        }
        if (month >= 10) {
            number = "4";
            chineseNumber = "四";
        }
        if (pattern == 1) {
            number = chineseNumber;
        }
        return number;
    }

    /**
     * 功能：获取当前周的周一的日期
     * 作者：rennannan
     * 日期：20210526
     *
     * @param date 传入当前日期
     * @return
     */
    public static String getThisWeekMonday(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return sdf.format(cal.getTime());
    }

    /**
     * 功能：获取下周一的日期
     * @param date 传入当前日期
     * @return
     */
    public static String getNextWeekMonday(String date, Integer frequency) throws ParseException {
        return add(getThisWeekMonday(date), "yyyyMMdd", 7 * frequency);
    }

    /**
     * 功能：查询格式为yyyyMM的两个日期之间的所有月份
     * 作者：rennannan
     * 日期：20210526
     *
     * @param minDate
     * @param maxDate
     * @return 返回格式为yyyyMM的日期集合
     * @throws ParseException
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        List<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");// 格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * 功能：将字符串日期格式化为传入的格式
     * 作者：rennannan
     * 日期：20210608
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(dateStr);
        return date;
    }

    /**
     * 功能：将指定的日期格式化成指定的日期字符串
     * 作者：rennannan
     * 日期：20210608
     *
     * @param date    日期对象
     * @param pattern 格式
     * @return 格式化后的日期字符串
     */
    public static String dateFormate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr;
        if (date == null) {
            return "";
        }
        dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 功能：计算两个指定格式的字符串类型的日期相差的天数
     * 作者：rennannan
     * 日期：20210608
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static int computeTwoDateDays(String date1, String date2, String pattern) throws ParseException {
        Date startDate = parseDate(date1, pattern);
        Date endDate = parseDate(date2, pattern);
        long betweenDate = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
        return Integer.parseInt(String.valueOf(betweenDate));
    }

    /**
     * 功能：获取指定日期下月月份的第一天
     * 作者：rennannan
     * 日期：20210624
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getNextMonthFirstDay(String date, Integer frequency) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        cal.setTime(BakDateUtil.getDate(date, sd));
        int first = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, first);
        cal.add(Calendar.MONTH, frequency);
        return sd.format(cal.getTime());
    }

    /**
     * 	获取当前日期2个月前的时间
     */
    public static String getTwoMonthBefore(String nowTime)throws ParseException{

        // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=dateFormat.parse(nowTime);

        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -2); //设置为前2月，可根据需求进行修改
        date = calendar.getTime();//获取2个月前的时间

        return dateFormat.format(date);
    }

    /**
     * 计算两个任意大小和精度的数的乘积
     * @param first 第一个参数
     * @param second 第二个参数
     * @return 两个数的乘积
     */
    public static String bigNumberMultiply(String first, String second) {
        // 正负号判断标志
        boolean flag = false;
        if (first.charAt(0) == '-') {
            flag = !flag;
            first = first.substring(1);
        }
        if (second.charAt(0) == '-') {
            flag = !flag;
            second = second.substring(1);
        }
        // 小数点的位置
        int aPoints = first.length() - first.indexOf('.') - 1;
        int bPoints = second.length() - second.indexOf('.') - 1;
        int pointPos = aPoints + bPoints; // 结果的小数点位置
        // 删除小数点
        StringBuffer aBuffer = new StringBuffer(first.replaceAll("\\.", ""));
        StringBuffer bBuffer = new StringBuffer(second.replaceAll("\\.", ""));
        int[] a = string2IntArray(aBuffer.toString());
        int[] b = string2IntArray(bBuffer.toString());
        int[] result = new int[a.length + b.length - 1]; // 保存结果的数组
        // 计算
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                result[i + j] += a[i] * b[j];
            }
        }
        // result中的某一位大于9的话需要进位
        for (int i = result.length - 1; i >= 0; --i) {
            if (result[i] > 9) {
                result[i - 1] += result[i] / 10;
                result[i] = result[i] % 10;
            }
        }
        StringBuffer buffer = new StringBuffer(); // 将result数组转换为字符串
        for (int i = 0; i < result.length; ++i) {
            // 添加小数点
            if(result.length - i == pointPos) {
                buffer.append(".");
            }
            buffer.append(String.valueOf(result[i]));
        }
        if (buffer.indexOf(".") != -1)
        {
            // 删除最开始的0
            int i = 0;
            while (i < buffer.length()) {
                if (buffer.length() > 2 && buffer.charAt(i+1) == '.') { // 小数点前只有一个数 0.
                    break;
                } else if (buffer.charAt(i) == '0') { // 删除最前边的0
                    buffer.deleteCharAt(i);
                    i = 0;
                    continue;
                } else { // 当第一位不是0的时候
                    break;
                }
            }
            // 删除末尾的0
            i = buffer.length() - 1;
            while (i >= 0) {
                // 小数点后直接是数字
                if (buffer.length() > 2 && buffer.charAt(i-1) == '.') {
                    break;
                } else if (buffer.charAt(i) == '0') { // 删除末尾的0
                    buffer.deleteCharAt(i);
                    i = buffer.length() - 1;
                    continue;
                } else { // 当最后一位不是0的时候
                    break;
                }
            }
        }
        // 根据符号位, 返回值的正负标志
        if (flag) {
            return "-" + buffer.toString();
        } else {
            return buffer.toString();
        }
    }

    /**
     * 将字符串装换为数组
     * @param number
     * @return
     */
    public static int[] string2IntArray(String number) {
        // 判断输入是否符合浮点数的要求
        Pattern pattern = Pattern.compile("^(-?\\d+|\\d*)\\.?\\d*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.find()) {
            throw new IllegalArgumentException("输入的数不正确!");
        }
        int[] result = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            result[i] = (int) (number.charAt(i) - '0');
        }
        return result;
    }

    /**
     * 获取当前时间，格式为xx年xx月xx日
     * @return
     */
    public static String getNowDate1(){
        Calendar cal = Calendar.getInstance();
        String day =String.valueOf(cal.get(Calendar.DATE));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1) ;
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String date = year + "年" + month + "月" + day +"日";

        return date;
    }

    /**
     * 获取上个月时间，格式为xx年xx月
     * @return
     */
    public static String getLastDate(){
        Calendar cal = Calendar.getInstance();
        String month = String.valueOf(cal.get(Calendar.MONTH)) ;
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String date = year + "年" + month + "月" ;

        return date;
    }

    /**
     * 获取上个月月份，xx月
     * @return
     */
    public static String getNowMonth1() {
        Calendar cal = Calendar.getInstance();
        String month = String.valueOf(cal.get(Calendar.MONTH));
        String date = month + "月";

        return date;
    }

    public static String  getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date)+"年";
    }

    public static boolean isDate(String date) {
        /**
         * 判断日期格式和范围
         */
        String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(date);
        boolean dateType = mat.matches();
        return dateType;
    }

    /**
     * 获取下个月第一天
     */
    public static String getNextMonthStartDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取下个月最后一天
     * @return
     */
    public static String getNextMonthEndDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return sdf.format(calendar.getTime());
    }
    /*
     * 获取当前日期所属第几季度
     * */
    public static String getCurrentQuarterNumbers(){
        String date = BakDateUtil.format(new Date(), "yyyyMMdd");
        String quarterNumber = BakDateUtil.getQuarterNumber(date,0);
        String baseDate = "";
        String year = BakDateUtil.getNowDate1().substring(0,4);
        //当前日期 执行的是 上个季度公告
        if ("1".equals(quarterNumber)) {
            year = BakDateUtil.getDayByYear();
            baseDate= year +"1231";
        } else if ("2".equals(quarterNumber)) {
            baseDate = year + "0331";
        } else if ("3".equals(quarterNumber)) {
            baseDate =year + "0630";
        } else if ("4".equals(quarterNumber)){
            baseDate = year +"0930";
        }
        return baseDate;
    }


    public static String getMonthBefore(String nowTime,int month)throws ParseException{

        // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date=dateFormat.parse(nowTime);

        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -month); //设置为前2月，可根据需求进行修改
        date = calendar.getTime();//获取2个月前的时间

        return dateFormat.format(date);
    }


    /**
     * @Description 获取指定日期后一天的数据
     * @Param []
     * @Return java.lang.String`
     */
    public static String getAfterDay(String day) throws ParseException{
        SimpleDateFormat predf = new SimpleDateFormat("yyyyMMdd");
        Date d = predf.parse(day);
        return predf.format(new Date(d.getTime() + (long)24 * 60 * 60 * 1000));
    }

    /**
     * @Description 获取指定日期后n月的日期
     * @Param []
     * @Return java.lang.String
     */
    public static String getAfterMonath(String date,Integer n) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date d = dateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH,n);
        return dateFormat.format(cal.getTime());
    }
    /**
     * 获取当月第一天
     */
    public static String getMonStartDt() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return dateFormat.format(cal.getTime());
    }

    /**
     * 获取当月最后一天
     */
    public static String getMonEndtDt() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(cal.getTime());
    }

    /**
     * 判断时间格式 格式必须为“YYYY-MM-dd”
     * 2004-2-30 是无效的
     * 2003-2-29 是无效的
     * @param sDate
     * @return
     */
    public static boolean isLegalDate(String sDate) {
        int legalLen = 10;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
}
