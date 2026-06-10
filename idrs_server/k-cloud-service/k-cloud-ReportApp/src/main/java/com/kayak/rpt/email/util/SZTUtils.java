package com.kayak.rpt.email.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class SZTUtils {

    private static final String[] DATE_LINK_SIGN = {" ","/","-","年","月","日"};

    private static final String[] FIRST_SESSION_MONTH = {"01","02","03"};
    private static final String[] SECOND_SESSION_MONTH = {"04","05","06"};
    private static final String[] THIRD_SESSION_MONTH = {"07","08","09"};
    private static final String[] FOURTH_SESSION_MONTH = {"10","11","12"};

    private static final String DATE_FORMAT_8 = "yyyyMMdd";
    private static final String DATE_FORMAT_10 = "yyyy-MM-dd";
    private static final String TIME_FORMAT_6 = "HHmmss";
    private static final String TIME_FORMAT_8 = "HH:mm:ss";
    private static final String TIME_FORMAT_17 = "yyyyMMddHHmmssSSS";

    private static final String FIRST_SESSION_START_DAY = "0101";
    private static final String FIRST_SESSION_END_DAY = "0331";
    private static final String SECOND_SESSION_START_DAY = "0401";
    private static final String SECOND_SESSION_END_DAY = "0630";
    private static final String THIRD_SESSION_START_DAY = "0701";
    private static final String THIRD_SESSION_END_DAY = "0930";
    private static final String FOURTH_SESSION_START_DAY = "1001";
    private static final String FOURTH_SESSION_END_DAY = "1231";
    /**
     * 获取上季度的第一天，返回格式为yyyyMMdd
     * @param dateStr 当前日期
     * @return 当前日期上季度第一天
     */
    public static String getLastSessionFirstDateStr(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        String formatDate = getFormat8Date(dateStr);
        if(formatDate.length() != DATE_FORMAT_8.length()){
            return formatDate;
        }

        String monthStr = getCurrentMonth(formatDate);
        if(Arrays.binarySearch(FIRST_SESSION_MONTH,monthStr) >= 0){
            return getLastYear(formatDate) + FOURTH_SESSION_START_DAY;
        }
        if(Arrays.binarySearch(SECOND_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + FIRST_SESSION_START_DAY;
        }
        if(Arrays.binarySearch(THIRD_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + SECOND_SESSION_START_DAY;
        }
        if(Arrays.binarySearch(FOURTH_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + THIRD_SESSION_START_DAY;
        }
        return formatDate;
    }

    /**
     * 获取上季度末的最后一天，返回格式为yyyyMMdd
     * @param dateStr 当前日期
     * @return 当前日期上季度末最后一天
     */
    public static String getLastSessionEndDateStr(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        String formatDate = getFormat8Date(dateStr);
        if(formatDate.length() != DATE_FORMAT_8.length()){
            return formatDate;
        }
        String monthStr = getCurrentMonth(formatDate);
        if(Arrays.binarySearch(FIRST_SESSION_MONTH,monthStr) >= 0){
            return getLastYear(formatDate) + FOURTH_SESSION_END_DAY;
        }
        if(Arrays.binarySearch(SECOND_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + FIRST_SESSION_END_DAY;
        }
        if(Arrays.binarySearch(THIRD_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + SECOND_SESSION_END_DAY;
        }
        if(Arrays.binarySearch(FOURTH_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + THIRD_SESSION_END_DAY;
        }
        return formatDate;
    }
    /**
     * 获取当前季度的第一天，返回格式为yyyyMMdd
     * @param dateStr 当前日期
     * @return 当前季度第一天
     */
    public static String getCurrentSessionFirstDateStr(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        String formatDate = getFormat8Date(dateStr);
        if(formatDate.length() != DATE_FORMAT_8.length()){
            return formatDate;
        }

        String monthStr = getCurrentMonth(formatDate);
        if(Arrays.binarySearch(FIRST_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + FIRST_SESSION_START_DAY;
        }
        if(Arrays.binarySearch(SECOND_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + SECOND_SESSION_START_DAY;
        }
        if(Arrays.binarySearch(THIRD_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + THIRD_SESSION_START_DAY;
        }
        if(Arrays.binarySearch(FOURTH_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + FOURTH_SESSION_START_DAY;
        }
        return formatDate;
    }

    /**
     * 获取当前季度末的最后一天，返回格式为yyyyMMdd
     * @param dateStr 当前日期
     * @return 当前季度末最后一天
     */
    public static String getCurrentSessionEndDateStr(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        String formatDate = getFormat8Date(dateStr);
        if(formatDate.length() != DATE_FORMAT_8.length()){
            return formatDate;
        }
        String monthStr = getCurrentMonth(formatDate);
        if(Arrays.binarySearch(FIRST_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + FIRST_SESSION_END_DAY;
        }
        if(Arrays.binarySearch(SECOND_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + SECOND_SESSION_END_DAY;
        }
        if(Arrays.binarySearch(THIRD_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + THIRD_SESSION_END_DAY;
        }
        if(Arrays.binarySearch(FOURTH_SESSION_MONTH,monthStr) >= 0){
            return getCurrentYear(formatDate) + FOURTH_SESSION_END_DAY;
        }
        return formatDate;
    }
    /**
     * 将日期格式化为YYYYMMDD格式
     * @param dateStr 日期字符串
     * @return 格式化为yyyyMMdd的字符串
     */
    public static String getFormat8Date(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        for (String sign : DATE_LINK_SIGN) {
            if(dateStr.length() == DATE_FORMAT_8.length()){
                return dateStr;
            }
            dateStr = dateStr.replaceAll(sign, EmailDict.SymbolType.T_INIT_STR);
        }
        return dateStr;
    }

    /**
     * 获取当前日期年份数据，格式为yyyy
     * @param dateStr 当前日期
     * @return 当前日期年份
     */
    public static String getCurrentYear(String dateStr){
        if(StringUtils.isEmpty(dateStr) || dateStr.length() < 4){
            return "";
        }
        return dateStr.substring(0,4);
    }
    /**
     * 获取当前日期上一年份数据，格式为yyyy
     * @param dateStr 当前日期
     * @return 当前日期上一年份
     */
    public static String getLastYear(String dateStr){
        String yearStr = getCurrentYear(dateStr);
        if(StringUtils.isEmpty(yearStr)){
            return "";
        }
        return String.valueOf(Integer.parseInt(yearStr) - 1);
    }
    /**
     * 获取当前日期月份数据，格式为MM
     * @param dateStr 当前日期
     * @return 当前日期月份
     */
    public static String getCurrentMonth(String dateStr){
        dateStr = getFormat8Date(dateStr);
        if(StringUtils.isEmpty(dateStr) || dateStr.length() < 6){
            return "";
        }
        return dateStr.substring(4,6);
    }

    public static String getCurrentDateStr8(){
        return dateFormat(new Date(),DATE_FORMAT_8);
    }
    public static String getCurrentDateStr10(){
        return dateFormat(new Date(),DATE_FORMAT_10);
    }
    public static String getCurrentTimeStr8(){
        return dateFormat(new Date(),TIME_FORMAT_8);
    }
    public static String getCurrentTimeStr6(){
        return dateFormat(new Date(),TIME_FORMAT_6);
    }
    public static String getCurrentTimeStr17(){
        return dateFormat(new Date(),TIME_FORMAT_17);
    }

    public static String getFormatDateStr10(String str){
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT_8).parse(getFormat8Date(str));
            return dateFormat(date,DATE_FORMAT_10);
        } catch (ParseException e) {
            log.error(e.getMessage());
            log.error("日期格式转化出错，日期字符串为【{}】！",str);
        }
        return "";
    }
    private static String dateFormat(Date date,String dateFormat){
        if(StringUtils.isEmpty(dateFormat)){
            dateFormat = DATE_FORMAT_8;
        }
        if(date == null){
            date = new Date();
        }
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 根据信披公告子类型及基准日期获取报告起始日期
     * @param disclosureSonType 信披公告子类型，仅支持定期公告
     * @param prodBaseDate 报告基准日期
     * @return 报告起始日期，月初、季度初，半年初，年初
     */
    public static String getReportStartDateStr(String disclosureSonType,String prodBaseDate){
        if(StringUtils.isEmpty(disclosureSonType) || StringUtils.isEmpty(prodBaseDate)){
            return "";
        }
        prodBaseDate = getFormat8Date(prodBaseDate);
        if(EmailDict.RegularReportType.MONTH_REPORT.equals(disclosureSonType)){
            return getCurrentYear(prodBaseDate) + getCurrentMonth(prodBaseDate) + "01";
        }else if(EmailDict.RegularReportType.QUARTER_REPORT.equals(disclosureSonType)){
            return getCurrentSessionFirstDateStr(prodBaseDate);
        }else if(EmailDict.RegularReportType.HALF_YEAR_REPORT.equals(disclosureSonType)){
            String month = getCurrentMonth(prodBaseDate);
            if(Arrays.binarySearch(FIRST_SESSION_MONTH,month) >= 0 || Arrays.binarySearch(SECOND_SESSION_MONTH,month) >= 0){
                return getCurrentYear(prodBaseDate) + FIRST_SESSION_START_DAY;
            }else{
                return getCurrentYear(prodBaseDate) + THIRD_SESSION_START_DAY;
            }
        }else if(EmailDict.RegularReportType.YEAR_REPORT.equals(disclosureSonType)){
            return getCurrentYear(prodBaseDate) + FIRST_SESSION_START_DAY;
        }
        return "";
    }
    /**
     * 根据信披公告子类型及基准日期获取报告截止日期
     * @param disclosureSonType 信披公告子类型，仅支持定期公告
     * @param prodBaseDate 报告基准日期
     * @return 报告截止日期，月末、季度末，半年末，年末
     */
    public static String getReportEndDateStr(String disclosureSonType,String prodBaseDate){
        if(StringUtils.isEmpty(disclosureSonType) || StringUtils.isEmpty(prodBaseDate)){
            return "";
        }
        prodBaseDate = getFormat8Date(prodBaseDate);
        if(EmailDict.RegularReportType.MONTH_REPORT.equals(disclosureSonType)){
            return getCurrentMonthLastDateStr(prodBaseDate);
        }else if(EmailDict.RegularReportType.QUARTER_REPORT.equals(disclosureSonType)){
            return getCurrentSessionEndDateStr(prodBaseDate);
        }else if(EmailDict.RegularReportType.HALF_YEAR_REPORT.equals(disclosureSonType)){
            String month = getCurrentMonth(prodBaseDate);
            if(Arrays.binarySearch(FIRST_SESSION_MONTH,month) >= 0 || Arrays.binarySearch(SECOND_SESSION_MONTH,month) >= 0){
                return getCurrentYear(prodBaseDate) + SECOND_SESSION_END_DAY;
            }else{
                return getCurrentYear(prodBaseDate) + FOURTH_SESSION_END_DAY;
            }
        }else if(EmailDict.RegularReportType.YEAR_REPORT.equals(disclosureSonType)){
            return getCurrentYear(prodBaseDate) + FOURTH_SESSION_END_DAY;
        }
        return "";
    }

    private static String getCurrentMonthLastDateStr(String currentDate){
        if(StringUtils.isEmpty(currentDate)){
            return "";
        }
        String formatDate = getFormat8Date(currentDate);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat(DATE_FORMAT_8).parse(formatDate));
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return dateFormat(calendar.getTime(),DATE_FORMAT_8);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * 将数字格式中的千分符去掉
     * @param moneyStr 数字格式数据
     * @return 去掉千分符后的数据
     */
    public static String getFormatStrByMoney(String moneyStr){
        if(StringUtils.isEmpty(moneyStr)){
            return "";
        }
        if(EmailDict.SymbolType.T_MIDDLE_LINE.equals(moneyStr)){
            return "";
        }
        return moneyStr.replaceAll(EmailDict.SymbolType.T_COMMA,EmailDict.SymbolType.T_INIT_STR);
    }
}
