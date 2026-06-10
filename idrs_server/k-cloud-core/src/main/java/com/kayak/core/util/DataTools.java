package com.kayak.core.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataTools {

	/**
	 * yyyyMMdd HHmmss
	 */
	public static final String DATETIMEFORMAT = "yyyyMMdd HHmmss";

	public static final  String DATETIMEFORMAT3339 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULTDATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATATIMEFORMAT = "MM-dd HH:mm:ss";

	/**
	 * Java将Unix时间戳转换成指定格式日期字符串
	 *
	 * @param timestampString
	 * @param formats
	 * @return
	 */
	public static String timeStamp2Date(String timestampString, String formats) {
		Long timestamp = new BigDecimal(timestampString).multiply(new BigDecimal(1000)).longValue();
		String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
		return date;
	}

	/**
	 * Java将Unix时间戳转换成指定格式日期字符串
	 *
	 * @param timestampString
	 * @return
	 */
	public static String timeStamp2Date(String timestampString) {
		return DataTools.timeStamp2Date(timestampString, DataTools.DEFAULTDATETIMEFORMAT);
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String date2TimeStamp(String datewfromat, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(datewfromat).getTime() / 1000);
		} catch (Exception e) {
			return "";
		}
	}

	public static String date2Formate(String datewfromat, String format) {
		try {
			SimpleDateFormat waitformat = new SimpleDateFormat(datewfromat);
			SimpleDateFormat formatnow = new SimpleDateFormat(format);
			return formatnow.format(waitformat.parse(datewfromat));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getNowTimeStamp(long time) {
		String nowTimeStamp = String.valueOf(time / 1000);
		return nowTimeStamp;
	}

	public static String getNowTimeStamp() {
		long time = System.currentTimeMillis();
		return DataTools.getNowTimeStamp(time);
	}

	public static String getMovTimeStampByMINUTE(long time,int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.add(Calendar.MINUTE, minutes);
		return DataTools.getNowTimeStamp(calendar.getTimeInMillis());
	}

	/**
	 * 按秒移动
	 * @param time
	 * @param minutes
	 * @return
	 */
	public static String getMovTimeStampBySecond(long time,int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.add(Calendar.SECOND, minutes);
		return DataTools.getNowTimeStamp(calendar.getTimeInMillis());
	}


	public static String date2TimeStamp(String datewfromat) {
		return DataTools.date2TimeStamp(datewfromat, DataTools.DEFAULTDATETIMEFORMAT);
	}

	public static String getDateRfc3339() {
		return DataTools.getDateRfc3339(new Date());
	}

	public static String getDateRfc3339(Date date) {
		return new SimpleDateFormat(DataTools.DATETIMEFORMAT3339).format(date);
	}

	public static String getAddMinutesDateRfc3339(int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, minutes);
		return DataTools.getDateRfc3339(calendar.getTime());
	}

	public static String getAddMinutesDateRfc3339(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return DataTools.getDateRfc3339(calendar.getTime());
	}

}
