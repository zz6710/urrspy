package com.kayak.utils;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.system.SysUtil;
import com.kayak.utils.DateHelper;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class DateUtils  extends ComnDao {

    public static String close = "1";
    public static String daily = "2";
    public static String fixed = "3";
    public static String unit = "4";
    public static String custom = "5";
    public static String formatDate = "yyyyMMdd";
    public static SimpleDateFormat sf = new SimpleDateFormat(formatDate);

    /**
     * axin
     * 非工作日时计算规则
     *
     * @param date1,起始日期
     * @param date2 结束日期
     * @param workday 系统工作日
     * @param openDays 产品开放日
     * @param postponeRule 规则，顺延-提前-取消
     * @return
     * @throws ParseException
     */
    public static List<String> getDateListWorkdays(String date1, String date2,List<String> openDays,List<String> workday,String postponeRule )
            throws ParseException {
        List<String> dates = new ArrayList<String>();

        for (String openDay:openDays) {
            if (!workday.contains(openDay)){
                if(Integer.parseInt(date2) <Integer.parseInt(openDay) ||
                        Integer.parseInt(Collections.max(workday)) < Integer.parseInt(openDay)){
                    continue;
                }
                int i = -1;
                LocalDate lastDayOfThisMonth = LocalDate.parse(openDay, DateTimeFormatter.ofPattern(formatDate));
                if(postponeRule.equals("1")){//延顺
                    while ( i == -1 ){
                        lastDayOfThisMonth = lastDayOfThisMonth.plusDays(1);//下一天
                        i = workday.indexOf(lastDayOfThisMonth.format(DateTimeFormatter.ofPattern(formatDate)));
                    }
                }else if(postponeRule.equals("2")){//提前
                    while ( i == -1 ){
                        lastDayOfThisMonth = lastDayOfThisMonth.plusDays(-1);//上一天
                        i = workday.indexOf(lastDayOfThisMonth.format(DateTimeFormatter.ofPattern(formatDate)));
                    }
                }else if(postponeRule.equals("3")){//取消
                    continue;
                }
                String s = workday.get(i);
                if (Integer.parseInt(s) < Integer.parseInt(date2)) dates.add(s);
            }else{
                dates.add(openDay);
            }

        }
        List<String> myList = dates.stream().distinct().collect(Collectors.toList());//去重
        return myList;
    }




    /**
     * axin
     * 连续开放
     *
     * @param openDays 每次起始开放日
     * @param openPeriodDays 规则，开放天数
     * @return
     * @throws ParseException
     */
    public static List<String> getDateListForNumble(List<String> openDays,String openPeriodDays )
            throws ParseException {
        List<String> dates = new ArrayList<String>();
        for (String openDay:openDays) {
            LocalDate yyyyMMdd = LocalDate.parse(openDay, DateTimeFormatter.ofPattern(formatDate));
            LocalDate firstDayOfNext = yyyyMMdd.plusDays(Integer.parseInt(openPeriodDays)-1);
            dates.addAll(getDates(openDay,firstDayOfNext.format(DateTimeFormatter.ofPattern(formatDate)),formatDate));
        }

        return dates;
    }

    /**
     * axin
     * 根据时间间隔，找出范围时间的有效时间
     *
     * @param date1,起始日期
     * @param date2 结束日期
     * @param cycleOpenTerm 规则1，每 cycleOpenTerm 天
     * @return
     * @throws ParseException
     */
    public static List<String> getDateListDay(String date1, String date2,String cycleOpenTerm) throws ParseException {
        List<String> dates = new ArrayList<String>();
        Date d1 = sf.parse(date1);
        Date d2 = sf.parse(date2);
        Calendar cr = Calendar.getInstance();
        while ((d1.getTime() - d2.getTime()) < 0) {
            cr.clear();
            cr.setTime(d1);
            dates.add(sf.format(d1));
            cr.add(Calendar.DAY_OF_YEAR, Integer.parseInt(cycleOpenTerm));
            d1 = cr.getTime();
        }
//        if(!dates.contains(date2)){
//            dates.add(date2);
//        }

        return dates;
    }

    /**
     * axin
     * 根据时间间隔，找出范围时间的有效时间(按周)
     *
     * @param date1,起始日期
     * @param date2 结束日期
     * @param cycleOpenTerm 规则1，每 cycleOpenTerm 周
     * @param orderOpenDays 规则2，周几开放
     * @return
     * @throws ParseException
     */
    public static List<String> getDateListForWeek(String date1, String date2,String cycleOpenTerm,
                                           String orderOpenDays)
            throws ParseException {

        List<String> dates = new ArrayList<String>();

        LocalDate yyyyMMdd = LocalDate.parse(date1, DateTimeFormatter.ofPattern(formatDate));
        DayOfWeek dayOfWeek = yyyyMMdd.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.of(Integer.parseInt(orderOpenDays)))){
            date1 = yyyyMMdd.format(DateTimeFormatter.ofPattern(formatDate));
        }else {
            LocalDate localDate = yyyyMMdd.plusWeeks(1);
            LocalDate localDate1 = localDate.with(TemporalAdjusters.next(DayOfWeek.of(Integer.parseInt(orderOpenDays))));
            date1 = localDate1.format(DateTimeFormatter.ofPattern(formatDate));
        }


        Date d1 = sf.parse(date1);
        Date d2 = sf.parse(date2);

        Calendar cr = Calendar.getInstance();
        while ((d1.getTime() - d2.getTime()) < 0) {
            cr.clear();
            cr.setTime(d1);
            dates.add(sf.format(d1));
            cr.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(cycleOpenTerm));
            d1 = cr.getTime();
        }

//        if(!dates.contains(date2)){
//            dates.add(date2);
//        }
        return dates;
    }







    /**
     * axin
     * 根据时间间隔，找出范围时间的有效时间(按月)
     *
     * @param date1,起始日期
     * @param date2 结束日期
     * @param cycleOpenTerm 规则1，每 cycleOpenTerm 周
     * @param orderOpenDays 规则2，周几开放
     * @return
     * @throws ParseException
     */
    public static List<String> getDateListForMonth(String date1, String date2,String cycleOpenTerm,
                                                   String orderOpenDays) throws ParseException {

        LocalDate yyyyMMdd = LocalDate.parse(date1, DateTimeFormatter.ofPattern(formatDate));
        //date1月份的orderOpenDays号对应的日期
        LocalDate localDate  = yyyyMMdd.with(MonthDay.of(yyyyMMdd.minusMonths(0).getMonth(),Integer.parseInt(orderOpenDays)));
        if (localDate.getDayOfMonth() >= yyyyMMdd.getDayOfMonth()){
            date1 = localDate.format(DateTimeFormatter.ofPattern(formatDate));
        }else{
            LocalDate localDate2 = yyyyMMdd.with(MonthDay.of(yyyyMMdd.minusMonths(-1).getMonth(),Integer.parseInt(orderOpenDays)));
            date1 = localDate2.format(DateTimeFormatter.ofPattern(formatDate));
        }

        List<String> dates = new ArrayList<String>();
        Date d1 = sf.parse(date1);
        Date d2 = sf.parse(date2);
        Calendar cr = Calendar.getInstance();
        while ((d1.getTime() - d2.getTime()) < 0) {
            cr.clear();
            cr.setTime(d1);
            dates.add(sf.format(d1));
            cr.add(Calendar.MONTH, Integer.parseInt(cycleOpenTerm));
            d1 = cr.getTime();
        }
//        if(!dates.contains(date2)){
//            dates.add(date2);
//        }
        return dates;
    }






    /**
     * axin
     * 获取两日期之间的日期数据集（包含起始日期，结束日期 ）
     * @param
     * @return
     */
    public static List<String>  getDates(String beginDate , String endDate , String dateType) throws ParseException {
        List<String> dateList = new ArrayList<String >();
        SimpleDateFormat sdf = new SimpleDateFormat(dateType);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(beginDate));
            for (long d = cal.getTimeInMillis(); d < sdf.parse(endDate).getTime(); d = cal.getTimeInMillis()) {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
                dateList.add(sdf.format(d));
            }
            dateList.add(endDate);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dateList;
    }



    /**
     * axin
     * 日期排序
     *
     * @param str 日期数组
     * @return
     * @throws SQLException
     */
    public static String[] sortListDesc(String[] str) throws ParseException {
        List<String> retStr=new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map<Long,String> map = new TreeMap<Long,String>();
        for(int i=0;i<str.length;i++){
            String dateStr = str[i];
            map.put(sdf.parse(dateStr).getTime(), dateStr);
        }
        Collection<String> coll=map.values();
        retStr.addAll(coll);
        return (String[]) retStr.toArray(str);
    }

    /**
     * axin
     * 得到星期几
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getweekday(String date) throws ParseException {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        Date bdate = format1.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(bdate);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "星期一";
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            return "星期二";
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            return "星期三";
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return "星期四";
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            return "星期五";
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return "星期六";
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return "星期日";
        }
        return "";
    }


    /**
     * 获取两个日期中的最大日期
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static String getMaxDate(String beginDateStr, String endDateStr) throws Exception {
        Date beginDate = toDate(beginDateStr,formatDate);
        Date endDate = toDate(endDateStr,formatDate);
        if(beginDate==null) {
            return toDate(endDate,formatDate);
        }
        if(endDate==null) {
            return toDate(beginDate,formatDate);
        }
        if(beginDate.after(endDate)) {//beginDate日期大于endDate
            return toDate(beginDate,formatDate);
        }
        return toDate(endDate,formatDate);
    }


    /**
     * 获取两个日期中的最小日期
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static String getMinDate(String beginDateStr, String endDateStr) throws Exception {
        Date beginDate = toDate(beginDateStr,formatDate);
        Date endDate = toDate(endDateStr,formatDate);
        if(beginDate==null) {
            return  toDate(endDate,formatDate);
        }
        if(endDate==null) {
            return  toDate(beginDate,formatDate);
        }
        if(beginDate.after(endDate)) {
            return  toDate(endDate,formatDate);
        }
        return toDate(beginDate,formatDate);
    }

    /**
     * 将时间时间转化成format格式
     * @return String result
     * @throws Exception
     */
    public static String toDate(Date date ,String format) throws Exception{
        String result = null;
        if(date != null) {//如果日期不为空就返回
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        }
        return result;
    }
    /**
     * 字符串to日期时间
     * 注意要指定日期格式，不像vb可以自动识别
     * 	想要自动补0，用 "yyyy-MM-dd"
     不想自动补0，用"yyyy-M-d"
     * @param sDate 传入时间字符串
     * @param format 传入时间字符串的转换格式有用户按定义
     * @return
     */
    public static Date toDate(String sDate, String format) throws Exception {
        Calendar cl = new GregorianCalendar();
        //开始转换时间
        cl.setTime( (new SimpleDateFormat(format)).parse(sDate));
        return cl.getTime();
    }



}
