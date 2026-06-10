package com.kayak.core.util;

import com.kayak.core.sql.SqlRow;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	private static final Logger log = LoggerFactory.getLogger(Tools.class);

	static {
		log.info(String.format("\njava.version:%s\njava.home:%s\njava.class.path:%s\njava.library.path:%s",
				System.getProperty("java.version"), System.getProperty("java.home"),
				System.getProperty("java.class.path"), System.getProperty("java.library.path")));
	}

	public static final String[] emptyArrayString = new String[] {};

	public static final Integer[] emptyArrayInteger = new Integer[] {};

	public static final BigDecimal ZERO_E_BIGDECIMAL = new BigDecimal("0E-10");
	/**
	 * 值为0的Short实例
	 */
	public static final Short zeroShort = 0;

	public static final Date emptyDate = new Date(-28800000);

	/**
	 * 取得new Date(0l)对象 在系统中,将把该对象作为空白日期对象处理
	 */
	public static Date getEmptyDate() {
		return emptyDate;
	}

	/**
	 * 将秒值转换成具体时间描述
	 */
	public static String second2mGb(long seconds) {
		Integer[] padding = new Integer[] { 60 * 60 * 24, 60 * 60, 60, 1 };
		String[] text = new String[] { "天", "小时", "分钟", "秒" };
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < padding.length; i++) {
			long n = seconds / padding[i];
			if (n > 0) {
				seconds = seconds % padding[i];
				sb.append(n).append(text[i]);
			}
		}
		return sb.toString();
	}

	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();
		//参数length，表示生成几位随机数

		for(int i = 0; i < length; i++) {


			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";

			//输出字母还是数字

			if( "char".equalsIgnoreCase(charOrNum) ) {

				//输出是大写字母还是小写字母

				int temp = random.nextInt(2)%2 == 0 ? 65 : 97;

				val += (char)(random.nextInt(26) + temp);

			} else if( "num".equalsIgnoreCase(charOrNum) ) {

				val += String.valueOf(random.nextInt(10));

			}

		}

		return val;

	}



	/**
	 * 取得今天日期对象（时分秒值为0）
	 */
	public static Date today() {
		return str2Dt(dt2Date1(new Date()));
	}

	/**
	 * 把yyyyMMdd格式的日期字符串转成yyyy-MM-dd格式字符串返回
	 */
	public static String dateFormat(String date) {
		if (date == null)
			return "";
		String str = date.trim();
		if (str.length() == 8) {
			return Tools.substr(str, 0, 4) + '-' + Tools.substr(str, 4, 2) + '-' + Tools.substr(str, 6, 2);
		}
		return str;
	}

	/**
	 * 把hhmmss格式的日期字符串转成hh:mm:ss格式字符串返回<br />
	 * 或hhmm => hh:mm
	 */
	public static String timeFormat(String time) {
		if (time == null)
			return "";
		String str = time.trim();
		if (str.length() == 6) {
			return Tools.substr(str, 0, 2) + ':' + Tools.substr(str, 2, 2) + ':' + Tools.substr(str, 4, 2);
		} else if (str.length() == 4) {
			return Tools.substr(str, 0, 2) + ':' + Tools.substr(str, 2, 2);
		}
		return str;
	}

	/**
	 * 把yyyyMMdd hhmmss格式的日期字符串转成yyyy-MM-dd hh:mm:ss格式字符串返回
	 */
	public static String datetimeFormat(String datetime) {
		if (datetime == null)
			return "";
		String str = datetime.trim();
		String[] strs = str.split("[ ]");
		if (strs.length == 2) {
			return Tools.dateFormat(strs[0]) + ' ' + Tools.timeFormat(strs[1]);
		}
		return str;
	}

	/**
	 * 把日期字符串转换成Date类型<br />
	 *
	 * @param date
	 * @return
	 */
	public static Date str2Dt(String date) {
		if (date.indexOf('-') == -1)
			return str2Dt1(date);
		else
			return str2Dt2(date);
	}

	/**
	 * "yyyyMMdd"格式的字段串转换成Date类型
	 */
	private static Date str2Dt1(String date) {
		Date dt;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		try {
			dt = fmt.parse(date);
		} catch (Exception e) {
			dt = getEmptyDate();
		}
		return dt;
	}

	/**
	 * 把日期时间字符串（精确到时分秒）转换成Date类型
	 *
	 * @param datetime
	 * @return
	 */
	public static Date strDt2Dt(String datetime) {
		String format;
		if (datetime.indexOf('-') > -1)
			format = "yyyy-MM-dd";
		else
			format = "yyyyMMdd";

		if (datetime.indexOf(':') > -1)
			format += " HH:mm:ss";
		else
			format += " HHmmss";

		Date dt;
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		try {
			dt = fmt.parse(datetime);
		} catch (Exception e) {
			dt = getEmptyDate();
		}
		return dt;

	}

	/**
	 * 时间字符串转换成Date类型
	 */
	public static Date strTime2Dt(String time) {
		if (time.indexOf(':') == -1)
			return strTime2Dt1(time);
		else
			return strTime2Dt2(time);
	}

	/**
	 * "HHmm"格式的字段串转换成Date类型
	 */
	private static Date strTime2Dt1(String time) {
		Date dt;
		SimpleDateFormat fmt = new SimpleDateFormat("HHmm");
		try {
			dt = fmt.parse(time);
		} catch (Exception e) {
			dt = getEmptyDate();
		}
		return dt;
	}

	/**
	 * "HH:mm"格式的字段串转换成Date类型
	 */
	private static Date strTime2Dt2(String time) {
		Date dt;
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
		try {
			dt = fmt.parse(time);
		} catch (Exception e) {
			dt = getEmptyDate();
		}
		return dt;
	}

	/**
	 * "yyyy-MM-dd"格式的字段串转换成Date类型
	 */
	private static Date str2Dt2(String date) {
		Date dt;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dt = fmt.parse(date);
		} catch (Exception e) {
			dt = getEmptyDate();
		}
		return dt;
	}

	/**
	 * Date类型转换成yyyyMMdd格式的字符串
	 */
	public static String dt2Date1(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成yyyy-MM-dd格式的字符串
	 */
	public static String dt2Date2(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成yyyy-MM-dd HH:mm:ss格式的字符串
	 */
	public static String dt2Datetime(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成yyyyMMdd HHmmss格式的字符串
	 */
	public static String dt2Datetime2(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		return formatter.format(date);
	}
	/**
	 * Date类型转换成yyyyMMddHHmmss格式的字符串
	 */
	public static String dt2Datetime3(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成HHmmss格式的字符串
	 */
	public static String dt2Time1(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成HH:mm:ss格式的字符串
	 */
	public static String dt2Time2(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成[HH时mm分]格式的字符串
	 */
	public static String dt2TimeBg(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("HH时mm分");
		return formatter.format(date);
	}

	/**
	 * Date类型转换成 yyyy年MM月dd日 星期几 格式的字符串
	 */
	public static String dt2DateBg(Date date) {
		return dt2DateBg(date, true);
	}

	/**
	 * Date类型转换成 yyyy年MM月dd日 星期几 格式的字符串
	 *
	 * @param date     要转换的Date对象
	 * @param showWeek 是否输出星期几
	 */
	@SuppressWarnings("deprecation")
	public static String dt2DateBg(Date date, Boolean showWeek) {
		if (date == null)
			return "";

		String[] weeks = new String[] { "日", "一", "二", "三", "四", "五", "六" };
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String str = formatter.format(date);
		if (showWeek) {
			str += " 星期" + weeks[date.getDay()];
		}
		return str;
	}

	/**
	 * Date类型转换成 yyyy年MM月dd日 星期几 HH时mm分 格式的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String dt2DatetimeBg(Date date) {
		if (date == null)
			return "";

		String[] weeks = new String[] { "日", "一", "二", "三", "四", "五", "六" };
		SimpleDateFormat formatter = new SimpleDateFormat(
				String.format("yyyy年MM月dd日 星期%s HH时mm分", weeks[date.getDay()]));
		return formatter.format(date);
	}

	/**
	 * 返回一个新的Date对象，其值是在date日期加上addMonths个月的日期<br />
	 * （此方法不改变date对象的值）
	 */
	public static Date dtAddMonths(Date date, int addMonths) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, addMonths);
		return calendar.getTime();
	}

	/**
	 * 返回中文格式的当前日期时间
	 */
	public static String nowDateBg() {
		return dt2DateBg(new Date());
	}

	/**
	 * 返回 yyyy年MM月dd日 星期几 HH时mm分 格式的当前日期时间
	 */
	public static String nowDatetimeBg() {
		return dt2DatetimeBg(new Date());
	}

	/**
	 * 返回Date对象是否空日期<br />
	 * 空日期的定义是：是否为null
	 */
	public static Boolean dtIsEmpty(Date date) {
		return date == null;
	}

	/**
	 * 返回当前年份的第一天的日期对象
	 */
	public static Date firstDayOfCurrYear() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, 0);// 一月
		c.set(Calendar.DATE, 1); // 一日
		return c.getTime();
	}

	/**
	 * 返回date里当月的第一天的日期对象
	 */
	public static Date firstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1); // 一日
		return c.getTime();
	}

	/**
	 * 返回date里当月的最后一天的日期对象
	 */
	public static Date lastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1); // 把日期设置为当月第一天
		c.add(Calendar.DATE, -1); // 日期回滚一天，也就是最后一天
		return c.getTime();
	}

	/**
	 * 获以昨天的日期对象
	 *
	 * @return
	 */
	public static Date yeatoday() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());// 今天的日期
		c.add(Calendar.DATE, -1);// 日期减一天
		return c.getTime();
	}

	/**
	 * 返回日期对象的年份
	 */
	@SuppressWarnings("deprecation")
	public static Integer getYear(Date date) {
		return date.getYear() + 1900;
	}

	/**
	 * 返回日期对象的月份
	 */
	@SuppressWarnings("deprecation")
	public static Integer getMonth(Date date) {
		return date.getMonth() + 1;
	}

	/**
	 * 返回日期对象的第几日
	 *
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Integer getDay(Date date) {
		return date.getDate();
	}

	/**
	 * 内存分页
	 *
	 * @param fromIndex
	 * @param pageSize
	 * @param collection
	 * @return
	 */
	public static <T> List<T> getSubList(int fromIndex, int pageSize, List<T> collection) {
		if (fromIndex >= collection.size()) {
			return Collections.emptyList();
		}
		int toIndex = fromIndex + pageSize;
		if (toIndex >= collection.size()) {
			toIndex = collection.size();
		}
		return collection.subList(fromIndex, toIndex);
	}

	/**
	 * 检查一个包含多个ID的字符串,格式是否ID1,ID2,ID3,...IDn,<br />
	 * 检查内容包括:<br />
	 * 1. 以逗号分格;<br />
	 * 2. 逗号分格的字符都是正确的长整型数
	 */
	public static boolean checkIds(String ids) {
		if (ids != null) {
			String[] arr = ids.split(",");
			if (arr.length > 0) {
				for (String str : arr) {
					try {
						Long.parseLong(str);
					} catch (NumberFormatException e) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查str字符串里的字符是否全都是整数字符
	 */
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 检查str字符串里的字符是否有效的数字字符
	 */
	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 将字符串转换为int型,如果转换失败,则返回0
	 */
	public static Integer str2Int(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 字符串转换成Long类型
	 */
	public static Long str2Long(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return 0l;
		}
	}

	/**
	 * 字符串转换成Double类型
	 */
	public static Double str2Double(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	/**
	 * 字符串转换成BigDecimal类型
	 */
	public static BigDecimal str2BigDecimal(String str) {
		try {
			return new BigDecimal(str);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 字符串转换成Short类型
	 */
	public static Short str2Short(String str) {
		try {
			return Short.parseShort(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 判断一个字符是AscII字符还是其它字符（如汉，日，韩文字符）
	 *
	 * @param c, 需要判断的字符
	 * @return boolean, 返回true,AscII字符
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 返回字符串str的长度（中文字符以两位计算）
	 */
	public static int strLen(String str) {
		if (str == null)
			return 0;
		int len = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < str.length(); i++) {
			/* 获取一个字符 */
			String temp = str.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				len += 2;
			} else {
				/* 其他字符长度为1 */
				len += 1;
			}
		}
		return len;
	}

	/**
	 * 在str的左边不够len长度的位数补chr字符<br />
	 * (chr将str填够len长度，如果str比len长,则直接返回str)
	 */
	public static String padLeft(String str, int len, char chr) {
		if (str == null)
			return makeChars(len, chr);

		return makeChars(len - strLen(str), chr) + str;
	}

	/**
	 * 在str的右边不够len长度的位数补chr字符<br />
	 * (chr将str填够len长度，如果str比len长,则直接返回str)
	 */
	public static String padRight(String str, int len, char chr) {
		if (str == null)
			return makeChars(len, chr);

		return str + makeChars(len - strLen(str), chr);
	}

	/**
	 * 产生num个以chr组成的字符串返回
	 */
	public static String makeChars(int num, char chr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(chr);
		}
		return sb.toString();
	}

	/**
	 * 给出一个类(多为model类)的.class,取得其类名(去掉前缀的包名)
	 */
	public static String getClassLastName(@SuppressWarnings("rawtypes") Class mClass) {
		String str = mClass.getName();

		return str.substring(str.lastIndexOf(".") + 1);
	}

	/**
	 * html编码<br />
	 * 空格不会被编码，如果要连空格一起编码，请使用这个方法的另一个重载htmlEncode(String str, Boolean
	 * encodeBlank)<br />
	 * 单引号没有被编码，因为很多情况下单引号的编码都不被解析为单引号<br />
	 * 所以要求所有的html编码输出都用双引号括起来
	 */
	public static String htmlEncode(String str) {
		return htmlEncode(str, false);
	}

	/**
	 * html编码<br />
	 * 单引号没有被编码，因为很多情况下单引号的编码都不被解析为单引号<br />
	 * 所以要求所有的html编码输出都用双引号括起来
	 *
	 * @param encodeBlank 是否对空格进行编码默认false
	 */
	public static String htmlEncode(String str, Boolean encodeBlank) {
		if (str == null)
			return "";

		str = str.replace("&", "&amp;");
		// str = str.replace("'", "&apos;");
		str = str.replace("\"", "&quot;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		if (encodeBlank)
			str = str.replace(" ", "&nbsp;");
		return str;
	}

	/**
	 * html解码
	 */
	public static String htmlDecode(String str, Boolean decodeBlank) {
		if (str == null)
			return "";

		str = str.replace("&amp;", "&");
		// str = str.replace("&apos;", "'");
		str = str.replace("&quot;", "\"");
		str = str.replace("&lt;", "<");
		str = str.replace("&gt;", ">");
		str = str.replace("&nbsp;", " ");
		return str;
	}

	/**
	 * 去掉字符串中的html标签
	 *
	 * @param input 要处理的字符串
	 */
	public static String htmlErase(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
	}

	/**
	 * url编码
	 */
	@SuppressWarnings("deprecation")
	public static String urlEncode(String url) {
		try {
			return java.net.URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return java.net.URLEncoder.encode(url);
		}
	}

	/**
	 * url解码
	 */
	@SuppressWarnings("deprecation")
	public static String urlDecode(String url) {
		try {
			return java.net.URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return java.net.URLDecoder.decode(url);
		}
	}

	/**
	 * 将回车换行符转换成HTML的换行标签<br />
	 */
	public static String nl2br(String str) {
		return str.replaceAll("\r\n", "<br />").replaceAll("\n", "<br />");
	}

	/**
	 * 将HTML的换行标签<br />
	 * 转换成换行符
	 */
	public static String br2nl(String str) {
		return str.replaceAll("<br />", "\n").replaceAll("<br>", "\n");
	}

	/**
	 * 如果str为空(null), 或者将str去掉首尾空格后,等于空字符串(""), 则返回真
	 */
	public static boolean strIsEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str))
			return true;
		else
			return false;
	}

	/**
	 * 如果list为空(null), 或者list长度不大于0, 则返回真
	 */
	public static boolean listIsEmpty(List<?> list) {
		if (list == null || list.size() <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 如果list不为空(null), 并且list长度大于0, 则返回真
	 */
	public static boolean listIsNotEmpty(List<?> list) {
		return !listIsEmpty(list);
	}

	/**
	 * 把字符串去掉前后空格后转换成Long
	 */
	public static Long str2Long2(String str) {
		if (str == null)
			return 0l;

		return str2Long(str.trim());
	}

	/**
	 * 把int型数字转换成字符串类型String
	 */
	public static String int2Str(Integer num) {
		if (num == null)
			return "";

		return num.toString();
	}

	/**
	 * 把long型数字转换成字符串类型String
	 */
	public static String long2Str(Long num) {
		if (num == null)
			return "";

		return num.toString();
	}

	/**
	 * 把Object转回成字符串
	 *
	 * @param obj
	 * @return
	 */
	public static String obj2Str(Object obj) {
		if (obj == null)
			return null;

		if ("null".equals(obj)) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 把Object转回成整型
	 *
	 * @param obj
	 * @return
	 */
	public static int obj2Int(Object obj) {
		return str2Int(obj2Str(obj));
	}

	/**
	 * 把int型转换成Long
	 */
	public static Long int2Long(Integer num) {
		if (num == null)
			return 0l;

		return Long.parseLong(num.toString());
	}

	public static Long BigInteger2Long(BigInteger num) {
		if (num == null)
			return 0l;

		return num.longValue();
	}

	public static Long BigDecimal2Long(BigDecimal num) {
		if (num == null)
			return 0l;

		return num.longValue();
	}

	public static Double BigDecimal2Double(BigDecimal num) {
		if (num == null)
			return 0d;

		return Double.parseDouble(num.toString());
	}

	/**
	 * 把int型转换成Short
	 */
	public static Short int2Short(Integer num) {
		if (num == null)
			return 0;

		return num.shortValue();
	}

	/**
	 * 将Object[]里的元素以sparator为间隔串连接起来成String返回
	 */
	public static String arrayJoin(Object[] arr, String sparator) {
		if (arr == null || arr.length == 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Object str : arr) {
			if (str == null)
				continue;

			if (sb.length() > 0) {
				sb.append(sparator);
			}
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 将list里的元素以","号为间隔串连接起来成String返回
	 */
	public static String listJoin(@SuppressWarnings("rawtypes") List list) {
		return listJoin(list, ",");
	}

	/**
	 * 将list里的元素以sparator为间隔串连接起来成String返回
	 */
	public static String listJoin(@SuppressWarnings("rawtypes") List list, String sparator) {
		if (list == null || list.size() == 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Object obj : list) {
			if (obj == null)
				continue;

			if (sb.length() > 0) {
				sb.append(sparator);
			}
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * 将set里的元素以sparator为间隔串连接起来成String返回
	 */
	public static String setJoin(@SuppressWarnings("rawtypes") Set set, String sparator) {
		if (set == null || set.size() == 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Object obj : set) {
			if (obj == null)
				continue;

			if (sb.length() > 0) {
				sb.append(sparator);
			}
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * 将obj的值转成type指定类型的对象返回<br />
	 *
	 * @param type 转换的类型为：<br />
	 *
	 *             <pre>
	 * int        - Integer
	 * double     - Double
	 * short      - Short
	 * date       - yyyyMMdd格式的日期值转成Date对象
	 * date2      - yyyy-MM-dd格式的日期值转成Date对象
	 * long       - Long
	 * bigdecimal - BigDecimal
	 * money	  - BigDecimal
	 * time	      - HH:mm格式的时间值转成Date对象
	 * datetime   - yyyy-MM-dd HH:mm格式的日期值转成Date对象
	 *             </pre>
	 */
	public static Object formatToType(String obj, String type) {
		if ("int".equalsIgnoreCase(type)) {
			return str2Int(obj);
		} else if ("double".equalsIgnoreCase(type)) {
			return str2Double(obj);
		} else if ("short".equalsIgnoreCase(type)) {
			return str2Short(obj);
		} else if ("date".equalsIgnoreCase(type)) {
			return str2Dt(obj);
		} else if ("time".equalsIgnoreCase(type)) {
			return strTime2Dt(obj);
		} else if ("datetime".equalsIgnoreCase(type)) {
			return strDt2Dt(obj);
		} else if ("long".equalsIgnoreCase(type)) {
			return str2Long(obj);
		} else if ("bigdecimal".equalsIgnoreCase(type) || "money".equalsIgnoreCase(type)) {
			return str2BigDecimal(obj);
		} else {
			return obj;
		}
	}

	/**
	 * 在date日期上加上(或减去)amount天后返回一个新的Date对象
	 *
	 * @param amount 正数则加，负数则减
	 */
	public static Date dateAdd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);
		return cal.getTime();
	}

	/**
	 * 在date日期上加上(或减去)amount个月后返回一个新的Date对象
	 *
	 * @param amount 正数则加，负数则减
	 */
	public static Date dateAddMonth(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);
		return cal.getTime();
	}

	/**
	 * 传入周几的数字（0 - 6）返回中文周几（日 - 一）
	 */
	public static String getWeekDay(Short week) {
		String[] weeks = new String[] { "日", "一", "二", "三", "四", "五", "六" };
		if (week >= 0 && week <= 6) {
			return weeks[week];
		}
		return "";
	}

	/**
	 * 用urlDecode解码list里的所有元素
	 */
	public static void urlDecodeList(List<String> list) {
		if (list == null)
			return;

		for (int i = 0; i < list.size(); i++) {
			list.set(i, urlDecode(list.get(i)).replace("\r\n", ""));
		}
	}

	/**
	 * 截取字符串
	 *
	 * @param str        被截取的字符串
	 * @param startIndex 开始位置
	 * @param length     截取长度
	 */
	public static String substr(String str, int startIndex, int length) {
		return str.substring(startIndex, startIndex + length);
	}

	/**
	 * 截取字符串
	 *
	 * @param str        被截取的字符串
	 * @param startIndex 开始位置
	 * @param length     截取长度
	 */
	public static String substr(String str, Long startIndex, int length) {
		return substr(str, startIndex.intValue(), length);
	}

	/**
	 * 截取带中文（一个中文算两个字符）的字符串
	 *
	 * @param str        被截取的字符串
	 * @param startIndex 截取开始位置
	 * @param len        截取长度
	 * @return 截取后的字符串
	 */
	public static String substrGb(String str, int startIndex, int len) {
		byte[] bStr = str.getBytes();
		if (startIndex > bStr.length)
			return "";

		if (startIndex + len > bStr.length) {
			len = bStr.length - startIndex;
		}

		String cStr = new String(bStr, startIndex, len);
		if (!str.contains(cStr)) {
			return new String(bStr, startIndex, len - 1);
		} else {
			return cStr;
		}
	}

	/**
	 * 调用owner对象的get方法取得owner里的某属性值
	 *
	 * @param owner        源对象
	 * @param propertyName 要取值的成员变量名称
	 * @return 返回取得的值
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeGetMethod(Object owner, String propertyName) throws Exception {
		@SuppressWarnings("rawtypes")
		Class ownerClass = owner.getClass();
		propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + propertyName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return null;
		}

		Object obj = method.invoke(owner);

		ownerClass = null;
		method = null;

		return obj;
	}

	/**
	 * 给系统java.library.path参数添加路径
	 *
	 * @param s
	 * @throws IOException
	 */
	public static void addDir(String s) {
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (s.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = s;
			field.set(null, tmp);
		} catch (IllegalAccessException e) {
			System.out.println("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			System.out.println("Failed to get field handle to set library path");
		}
	}

	/**
	 * 将字符串str截取到len长度返回，<br />
	 * 返回的符串长度一定是len，如果str不够len长，则补空格<br />
	 */
	public static String formatString2Length(String str, int len) {
		if (str == null)
			return Tools.makeChars(len, ' ');
		else if (len < 1)
			return "";

		byte[] bStr = str.replaceAll("—", "-").getBytes();
		if (len >= bStr.length)
			return Tools.padRight(str, len, ' ');

		String cStr = new String(bStr, 0, len);
		if (!str.contains(cStr)) {
			return new String(bStr, 0, len - 1) + ' ';
		} else {
			return cStr;
		}
	}

	/**
	 * 当str等于"true"(不区分大小写)返回true，否则返回false
	 */
	public static boolean str2Boolean(String str) {
		return "true".equalsIgnoreCase(str);
	}

	/**
	 * Object转成boolean
	 */
	public static boolean toBoolean(Object bool) {
		if (bool == null)
			return false;
		return "true".equalsIgnoreCase(bool.toString());
	}

	/**
	 * 当字符串要用来拼接成JSON格式的字符时，需要用这个函数来过滤一遍<br />
	 * 处理如下：<br />
	 * 1. 双引号(")转换成：\"<br />
	 * 2. 去掉换行符(\r)<br />
	 * 3. 回车符(\n)转换成：\\n
	 */
	public static String str2Json(String str) {
		return str.replaceAll("\"", "\\\"").replaceAll("\r", "").replaceAll("\n", "\\n");
	}

	/**
	 * 把obj对象实例里的所有属性构造成一个JSON对象返回
	 *
	 * @param obj 要构造的对象
	 * @return 返回构造完成的JSON对象
	 */
	public static JSONObject getObjectFieldsMap(Object obj) {
		return getObjectFieldsMap(obj, new String[] {}, null, null);
	}

	/**
	 * 把obj对象实例里的所有属性构造成一个JSON对象返回
	 *
	 * @param obj   要构造的对象
	 * @param level 如果有传入level则默认在最后以"No"结尾的属性值里添加level个空白（用以得到缩进的效果）
	 * @return 返回构造完成的JSON对象
	 */
	public static JSONObject getObjectFieldsMap(Object obj, Integer level, String levelField) {
		return getObjectFieldsMap(obj, new String[] {}, level, levelField);
	}

	/**
	 * 把obj对象实例里的所有属性构造成一个JSON对象返回
	 *
	 * @param obj     要构造的对象
	 * @param excepts 要排除不加入构造的属性名的字符串数组
	 * @return 返回构造完成的JSON对象
	 */
	public static JSONObject getObjectFieldsMap(Object obj, String[] excepts) {
		return getObjectFieldsMap(obj, excepts, null, null);
	}

	/**
	 * 把obj对象实例里的所有属性构造成一个JSON对象返回
	 *
	 * @param obj     要构造的对象
	 * @param excepts 要排除不加入构造的属性名的字符串数组
	 * @param level   如果有传入level则默认在最后以"No"结尾的属性值里添加level个空白（用以得到缩进的效果）
	 * @return 返回构造完成的JSON对象字符串
	 */
	public static JSONObject getObjectFieldsMap(Object obj, String[] excepts, Integer level, String levelField) {
		List<String> exs = Arrays.asList(excepts);
		Field[] fields = obj.getClass().getDeclaredFields();
		JSONObject json = new JSONObject();
		for (Field field : fields) {
			if (exs.contains(field.getName()))
				continue;

			Object value = "";
			try {
				value = Tools.invokeGetMethod(obj, field.getName());
			} catch (Exception e) {
				continue;
			}
			if (field.getType().equals(String.class)) {
				// 编码缩进
				if (level != null && level > 0 && field.getName().equals(levelField)) {// 添加缩进空白
					value = Tools.makeChars(level * 4, ' ') + value;
				} else {
					value = (String) value;
				}
			} else if (field.getType().equals(Date.class))
				value = Tools.dt2Datetime((Date) value);

			try {
				json.append(field.getName(), value);
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
		return json;
	}

	/**
	 * 将List里的对象构造成供页面grid使用的json字符串
	 */
	public static JSONObject makeListJson(@SuppressWarnings("rawtypes") List list) {
		return makeListJson(list, null, null);
	}

	/**
	 * 将List里的对象构造成供页面grid使用的json对象
	 *
	 * @param list  要构造的List
	 * @param count 列表的总数，如果是null则取list的size()
	 */
	public static JSONObject makeListJson(@SuppressWarnings("rawtypes") List list, Long count) {
		return makeListJson(list, count, null);
	}

	/**
	 * 将List里的对象构造成供页面grid使用的json对象
	 *
	 * @param list          要构造的List
	 * @param count         列表的总数，如果是null则取list的size()
	 * @param addAttributes 添加到json里的附加属性
	 */
	public static JSONObject makeListJson(@SuppressWarnings("rawtypes") List list, Long count,
			Map<String, Object> addAttributes) {
		JSONObject json = new JSONObject();
		if (list == null)
			return json;

		Long ct = count;
		if (count == null)
			ct = Tools.int2Long(list.size());

		try {
			json.append("results", ct);
			if (addAttributes != null && addAttributes.size() > 0) {// 附加的属性
				for (Iterator<String> it = addAttributes.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					json.append(key, addAttributes.get(key));
				}
			}
			JSONArray rows = new JSONArray();
			json.append("rows", rows);
			for (Object m : list) {
				rows.put(getObjectFieldsMap(m));
			}
		} catch (JSONException e) {
			log.error(e.getMessage(), e);
		}

		return json;
	}

	/**
	 * 把sourceJson的属性添加到targetJson对象中
	 *
	 * @param targetJson 目标对象
	 * @param sourceJson 来源对象
	 */
	public static void jsonAddProperties(JSONObject targetJson, JSONObject sourceJson) {
		for (@SuppressWarnings("rawtypes")
		Iterator it = sourceJson.keys(); it.hasNext();) {
			String key = (String) it.next();
			try {
				targetJson.put(key, sourceJson.get(key));
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 用jsonString字符串创建一个JSONObject对象，<br />
	 * 如果jsonString为空，则返回一个空的JSONObject对象
	 *
	 * @throws JSONException
	 */
	public static JSONObject JSONObject(String jsonString) throws JSONException {
		if (Tools.strIsEmpty(jsonString))
			return new JSONObject();
		else
			return new JSONObject(jsonString);
	}

	public static String gbk2utf8(String chenese) throws UnsupportedEncodingException {
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);
			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			// 补零
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");

			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);

			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];

		}
		return new String(fullByte, "UTF-8");
	}

	/**
	 * 查找str在字符串数组strs中最后出现的索引位置
	 *
	 * @param start 从start索引位置开始往前查找
	 */
	public static int findLastIndex(String[] strs, String str, int start) {
		for (int i = strs.length - 1; i >= start; i--) {
			if (str.equals(strs[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 查找str在字符串数组strs中的索引位置
	 *
	 * @param start 从start索引位置开始查找
	 */
	public static int findIndex(String[] strs, String str, int start) {
		for (int i = start; i < strs.length; i++) {
			if (str.equals(strs[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 如果string!=null则返回trim后的string值，如果string==null则返回""<br />
	 * 此方法不返回null
	 */
	public static String trimString(String string) {
		if (string == null)
			return "";
		else
			return string.trim();
	}

	/**
	 * 如果string!=null则返回trim后的string值，如果string==null则返回""<br />
	 * 此方法不返回null
	 */
	public static String trimString(Object obj) {
		if (obj == null)
			return "";
		else
			return obj.toString().trim();
	}

	/**
	 * 数字格式化实例
	 **/
	private static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

	private static NumberFormat DECIMAL_FORMAT;
	static {
		DECIMAL_FORMAT = DecimalFormat.getInstance();
		DECIMAL_FORMAT.setGroupingUsed(false);
	}

	/**
	 * 金额数字添加千分号输出
	 *
	 * @param money
	 * @return
	 */
	public static String moneyStr(String money) {
		return NUMBER_FORMAT.format(str2BigDecimal(money));
	}

	/**
	 * 金额数字添加千分号输出
	 *
	 * @param money
	 * @return
	 */
	public static String moneyStr(Double money) {
		return NUMBER_FORMAT.format(money);
	}

	/**
	 * 金额数字添加千分号输出
	 *
	 * @param money
	 * @return
	 */
	public static String moneyStr(BigDecimal money) {
		return NUMBER_FORMAT.format(money);
	}

	/**
	 * 金额数字转成中文大写输出
	 *
	 * @param money
	 * @return
	 */
	public static String moneyGb(Double money) {
		return moneyGb(DECIMAL_FORMAT.format(money));
	}

	/**
	 * 把数字字符转成中文数字返回
	 *
	 * @param num
	 * @return
	 */
	private static String gbDigit(char num) {
		return digit[num - 48];
	}

	private static final String[] digit = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static final String[] fraction = { "角", "分" };
	private static final String[] digit_unit1 = { "", "拾", "佰", "仟" };
	private static final String[] digit_unit2 = { "元", "万", "亿" };

	/**
	 * 金额数字转成中文大写输出
	 *
	 * @param money
	 * @return
	 */
	public static String moneyGb(String money) {
		StringBuffer sb_f = new StringBuffer();// 整数部分
		StringBuffer sb_b = new StringBuffer();// 小数部分

		char[] mchr = money.toCharArray();

		int idx = money.indexOf('.');
		if (idx >= 0 && idx < mchr.length - 1) {// 处理小数部分
			for (int i = 0; i < 2; i++) {
				if (i + idx + 1 >= mchr.length)
					break;
				char c = mchr[i + idx + 1];
				if (c == '0')
					continue;
				sb_b.append(gbDigit(c)).append(fraction[i]);
			}
		}

		if (idx == -1)// 没有小数
			idx = mchr.length;
		for (int i = 0, un1 = 0, un2 = 0; i < idx; i++) {
			char c = mchr[i];

			un1 = (idx - i + 3) % digit_unit1.length;
			un2 = (int) Math.floor((idx - i) / digit_unit1.length);

			if (c == '0') {
				if (sb_f.length() > 0 && sb_f.lastIndexOf("零") != sb_f.length() - 1)
					sb_f.append(gbDigit(c));
			} else
				sb_f.append(gbDigit(c) + digit_unit1[un1]);

			if (sb_f.length() > 0 && un1 == 0) {
				if (sb_f.lastIndexOf("零") == sb_f.length() - 1)
					sb_f.replace(sb_f.length() - 1, sb_f.length(), digit_unit2[un2]);
				else
					sb_f.append(digit_unit2[un2]);
			}
		}

		if (sb_f.length() == 0)
			sb_f.append("零元");

		if (sb_b.length() == 0)
			sb_b.append("整");

		return sb_f.append(sb_b).toString().replace("亿万", "亿");
	}

	/**
	 * 将18位身份证号降位转成15位返回<br />
	 * 此函数是直接将多余的位数删除后返回，如果输入的id_code不是18位，则直接返回id_code本身
	 */
	public static String id18To15(String id_code) {
		if (Tools.strIsEmpty(id_code))// 输入字符为空则返回空字符
			return "";

		if (id_code.length() != 18)// 输入字符不是18位则返回原字符串
			return id_code;

		StringBuffer sb = new StringBuffer(id_code);
		sb.replace(17, 18, "");// 删除校验位
		sb.replace(6, 8, "");// 删除年份的前两位
		return sb.toString();
	}

	public static String sqlOrgnoToID(String colname) {
		// String dbtype =
		// SysSql.getDBType(CustomerContextHolder.getCustomerType());

		/**
		 * select $orgid{a.orgno} from sys_user a select (select orgid from sys_org
		 * where orgno=a.orgno) from sys_user a select * from sys_user where orgid like
		 */
		String sql = "(select orgid from sys_org where orgno=" + colname + ")";
		return sql;

	}

	/**
	 * 返回obj是否存在于arr数组中
	 *
	 * @param arr
	 * @param obj
	 * @return
	 */
	public static boolean inArray(Object[] arr, Object obj) {
		for (Object o : arr) {
			if (o.equals(obj))
				return true;
		}
		return false;
	}

	/**
	 * 将JSONArray对象转为List
	 *
	 * @param arr
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List json2list(JSONArray arr) {
		if (arr == null)
			return null;

		List list = new ArrayList();
		for (int i = 0; i < arr.length(); i++) {
			Object obj;
			try {
				obj = arr.get(i);
				if (obj instanceof JSONObject) {
					list.add(json2map((JSONObject) obj));
				} else if (obj instanceof JSONArray) {
					list.add(json2list((JSONArray) obj));
				} else {
					list.add(obj);
				}
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
		return list;
	}


	/**
	 * null
	 * 将JSONArray对象转为Map
	 * @param arr
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map array2map(JSONArray arr) {
		if (arr == null)
			return null;

		Map arrMap = new HashMap();
		for (int i = 0; i < arr.length(); i++) {
			Object obj;
			try {
				obj = arr.get(i);
				arrMap.put(((JSONObject) obj).get("label"), ((JSONObject) obj).get("value"));
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
		return arrMap;
	}

	/**
	 * 将JSONObject对象转为Map
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, Object> json2map(JSONObject json) {
		if (json == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		for (@SuppressWarnings("rawtypes")
		Iterator it = json.keys(); it.hasNext();) {
			String key = (String) it.next();
			Object obj;
			try {
				obj = json.get(key);
				if (obj instanceof JSONObject) {
					map.put(key, json2map((JSONObject) obj));
				} else if (obj instanceof JSONArray) {
					map.put(key, json2list((JSONArray) obj));
				} else {
					map.put(key, obj);
				}
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
		return map;
	}

	/**
	 * 将JSONObject对象转为Map
	 *
	 * @param json
	 * @return
	 */
	public static SqlRow json2sqlrow(JSONObject json) {
		if (json == null)
			return null;

		SqlRow map = new SqlRow();
		for (@SuppressWarnings("rawtypes")
		Iterator it = json.keys(); it.hasNext();) {
			String key = (String) it.next();
			Object obj;
			try {
				obj = json.get(key);
				if (obj instanceof JSONObject) {
					map.put(key, json2map((JSONObject) obj));
				} else if (obj instanceof JSONArray) {
					map.put(key, json2list((JSONArray) obj));
				} else {
					map.put(key, obj);
				}
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
		return map;
	}

	public static Map<String, String> str2map(String jsonStr) {
		if (strIsEmpty(jsonStr))
			return null;
		JSONObject json = new JSONObject(jsonStr);
		Map<String, String> map = new HashMap<String, String>();
		for (@SuppressWarnings("rawtypes")
		Iterator it = json.keys(); it.hasNext();) {
			String key = (String) it.next();
			try {
				map.put(key, obj2Str(json.get(key)));
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}
		return map;
	}

	public static <T> List<T> array2list(T[] arr) {
		if (arr == null) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}

	public static List<String> split(String str, String sp) {
		Pattern ptn = Pattern.compile(sp);
		Matcher matcher = ptn.matcher(str);
		List<String> list = new ArrayList<String>();
		int i = 0;
		while (matcher.find()) {
			String match = str.substring(i, matcher.start());
			list.add(match);
			i = matcher.end();
		}
		String match = str.substring(i);
		list.add(match);
		return list;
	}

	/**
	 * 根据当前日期获取未来日期
	 *
	 * @param startDate
	 * @param year
	 * @param months
	 * @param days
	 * @return
	 */
	public static Date getEndTime(Date startDate, int year, int months, int days) {
		if (days > 30) {
			months = months + days / 30;
			days = days / 30;
		}
		if (months > 12) {
			year = year + months / 12;
			months = months / 12;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startDate.getTime());

		calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year);
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * 比较startDate与endDate，相等则返回0，大于返回1，小于返回-1
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int dateCompare(Date startDate, Date endDate) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(startDate);
		c2.setTime(endDate);

		int result = c1.compareTo(c2);

		if (result == 0)
			return 0;
		else if (result < 0)
			return -1;
		else
			return 1;
	}

	/**
	 * 获取那个时间的小时差
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getHourInterval(Date startDate, Date endDate) {
		return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60));
	}

	// 日期转化为大小写
	public static String dataToUpper(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		return numToUpper(year) + "年" + monthToUppder(month) + "月" + dayToUppder(day) + "日";
	}

	// 将数字转化为大写
	public static String numToUpper(int num) {
		// String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
		String u[] = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		char[] str = String.valueOf(num).toCharArray();
		String rstr = "";
		for (int i = 0; i < str.length; i++) {
			rstr = rstr + u[Integer.parseInt(str[i] + "")];
		}
		return rstr;
	}

	// 月转化为大写
	public static String monthToUppder(int month) {
		if (month < 10) {
			return numToUpper(month);
		} else if (month == 10) {
			return "十";
		} else {
			return "十" + numToUpper(month - 10);
		}
	}

	// 日转化为大写
	public static String dayToUppder(int day) {
		if (day < 20) {
			return monthToUppder(day);
		} else {
			char[] str = String.valueOf(day).toCharArray();
			if (str[1] == '0') {
				return numToUpper(Integer.parseInt(str[0] + "")) + "十";
			} else {
				return numToUpper(Integer.parseInt(str[0] + "")) + "十" + numToUpper(Integer.parseInt(str[1] + ""));
			}
		}
	}

	/**
	 * 根据字符串返回对应日期
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date getDateFromString(String dateStr) {
		return getDateFromString("yyyy-MM-dd", dateStr);
	}

	/**
	 * 根据字符串返回对应日期
	 *
	 * @param pattern
	 * @param dateStr
	 * @return
	 */
	public static Date getDateFromString(String pattern, String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("字符串转换失败");
			return null;
		}
	}

	/**
	 * 根据日期生成对应的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String getStringFromDate(Date date) {
		return getStringFromDate("yyyy-MM-dd", date);
	}

	/**
	 * 根据日期生成对应的字符串
	 *
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String getStringFromDate(String pattern, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将客户端参数值中的String值转换成系统编码字符串返回，<br />
	 * 如果val不是String类型则返回val本身<br />
	 * 通常用get方式传递的参数的中文都需要用这个方法转换才能正常显示
	 */
	public static Object string2SysCharset(Object val) {
		if (val == null)
			return null;
		if (val.getClass().isArray()) {
			Object[] vals = (Object[]) val;
			for (int i = 0; i < vals.length; i++) {
				vals[i] = string2SysCharset(vals[i]);// 转换String值编码
			}
		}
		if (val.getClass().equals(String.class)) {
			try {
				return new String(((String) val).getBytes("ISO8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}
		return val;
	}

	public static String string2SysCharset(String val) {
		if (val == null)
			return null;
		try {
			return new String((val).getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return val;
	}

	public static boolean checkStr(String str) {
		if (str != null && !"".equals(str)) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean isEmptyObjOrString(Object obj) {
		if (obj == null) {
			return true;
		}
		String str = (String) obj;
		return isEmpty(str);
	}

	/**
	 * 判断字符串是否为非空
	 *
	 * @param str 字符串
	 * @return 是否为非空标识
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static ParamsBuilder getParams() {
		return new ParamsBuilder();
	}

	public static String getLastWeekDate() {
		return DateTime.now().minusDays(7).toString("yyyyMMdd");
	}

	public static String getCurrentDate() {
		Date date = new Date();
		return Tools.dt2Date1(date);
	}

	public static String getCurrentDateAndTime() {
		Date date = new Date();
		return Tools.dt2Datetime3(date);
	}

	public static ParamsBuilder makeParams() {
		return new ParamsBuilder();
	}

	public static class ParamsBuilder {
		Map<String, Object> params = new HashMap<String, Object>();

		public ParamsBuilder put(String key, Object value) {
			params.put(key, value);
			return this;
		}

		public Map<String, Object> build() {
			return params;
		}
	}

	// 获取两个日期之间的日期集合
	public static List<Date> getBetweenDates(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);
		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		while (tempStart.before(tempEnd)) {
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		if (start.equals(end)) {
			result.add(start);
		} else {
			result.add(start);
			result.add(end);
		}
		return result;
	}

	/**
	 * 计算天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long getBetweenDays(Date date1, Date date2) {
		Long days = (Long) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	/**
	 * 获取两个日期相差的月数
	 *
	 * @param d1 较大的日期
	 * @param d2 较小的日期
	 * @return 如果d1>d2返回 月数差 否则返回0
	 */
	public static int getMonthDiff(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		if (c1.getTimeInMillis() < c2.getTimeInMillis())
			return -1;
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);
		int day1 = c1.get(Calendar.DAY_OF_MONTH);
		int day2 = c2.get(Calendar.DAY_OF_MONTH);
		// 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
		int yearInterval = year1 - year2;
		// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
		if (month1 < month2 || month1 == month2 && day1 < day2)
			yearInterval--;
		// 获取月数差值
		int monthInterval = (month1 + 12) - month2;
		if (day1 < day2)
			monthInterval--;
		monthInterval %= 12;
		return yearInterval * 12 + monthInterval;
	}

	/**
	 * 获取周id
	 *
	 * @param work_date
	 * @return
	 */
	public static String getWeekID(String work_date) {
		String sys_week_str = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		try {
			if (Tools.strIsEmpty(work_date)) { // 如果为空；取当前日期
				Date now_date = new Date();
				work_date = sdf.format(now_date);
			}
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(sdf.parse(work_date));
			int sys_week = calendar.get(Calendar.WEEK_OF_YEAR);
			int sys_year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			if (month == 12 && sys_week == 1) { // 跨年当周返回周数为1；
				sys_year = sys_year + 1;
			}
			if (sys_week < 10) {
				sys_week_str = sys_year + "0" + sys_week;
			} else {
				sys_week_str = sys_year + "" + sys_week;
			}
		} catch (ParseException e) {
			log.error("获取周id失败");
		}
		return sys_week_str;
	}

	/**
	 * 根据具体年份周数获取日期范围
	 *
	 * @param year
	 * @param week
	 * @param targetNum
	 * @return
	 */
	public static List<Date> getWeekDays(int year, int week, int targetNum) {
		List<Date> result = new ArrayList<>();
		// 计算目标周数
		if (week + targetNum > 52) {
			year++;
			week += targetNum - 52;
		} else if (week + targetNum <= 0) {
			year--;
			week += targetNum + 52;
		} else {
			week += targetNum;
		}
		Calendar calendar = Calendar.getInstance();
		// 设置每周的开始日期
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		for (int i = 1; i < 8; i++) {
			result.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	public static List<Date> getWeekDaysMondySundy(int year, int week, int targetNum) {
		List<Date> result = new ArrayList<>();
		// 计算目标周数
		if (week + targetNum > 52) {
			year++;
			week += targetNum - 52;
		} else if (week + targetNum <= 0) {
			year--;
			week += targetNum + 52;
		} else {
			week += targetNum;
		}
		Calendar calendar = Calendar.getInstance();
		// 设置每周的开始日期
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		for (int i = 0; i < 7; i++) {
			result.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public static int getMonths(String startDate, String endDate) {
		if (Tools.isBlank(startDate) || Tools.isBlank(endDate)) {
			return 0;
		}
		int year = Integer.valueOf(endDate.substring(0, 4)) - Integer.valueOf(startDate.substring(0, 4));
		int month = Integer.valueOf(endDate.substring(4, 6)) - Integer.valueOf(startDate.substring(4, 6));
		return year * 12 + month + 1;
	}

	public static String numSubtract(String a, String b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).toPlainString();
	}

	public static String numAdd(String a, String b) {
		return new BigDecimal(a).add(new BigDecimal(b)).toPlainString();
	}

	public static int checkCharacterType(char c) {
		if (c >= 48 && c <= 57) {
			return 1;// 数字
		}
		if (c >= 65 && c <= 90) {
			return 2;// 大写字母
		}
		if (c >= 97 && c <= 122) {
			return 3;// 小写字母
		}
		return 4;
	}

	public static int countLetter(String passwd, int type) {
		int count = 0;
		if (null != passwd && passwd.length() > 0) {
			for (char c : passwd.toCharArray()) {
				if (checkCharacterType(c) == type) {
					count++;
				}
			}
		}
		return count;
	}

	private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	/**
	 * 判断SQL参数是否存在注入问题
	 *
	 * @param str
	 * @return
	 */
	public static boolean isSqlInjection(String str) {
//		if (sqlPattern.matcher(str).find()) {
//			return true;
//		}
		return false;
	}

	/**
	 * 判断字符串是否是json
	 *
	 * @param content
	 * @return
	 */
	public static boolean isJson(String content) {
		try {
			new JSONObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String toString(Object obj) {
		return obj == null ? null : obj.toString();
	}

	public static String toString(Object obj, String nullStr) {
		return obj == null ? nullStr : obj.toString();
	}

	/**
	 * 支持查询父类属性
	 *
	 * @param fieldDatas
	 * @param modelClass
	 */
	public static void getFields(List<Field> fieldDatas, Class modelClass) {
		Field[] fields = modelClass.getDeclaredFields();
		if (fields.length > 0) {
			fieldDatas.addAll(Arrays.asList(fields));
		}

		Class superclass = modelClass.getSuperclass();
		if (superclass != null) {
			getFields(fieldDatas, superclass);
		}
	}

	/**
	 * 格式化数值，解决科学计数法
	 * @param obj
	 * @param dit 小数位数
	 * @return
	 */
	public static Object formatObjVal(Object obj, int dit) {
		try{
			String pattern = "#.#";
			if(dit == -1) dit = 8;//为-1时取默认值，默认8位
			for(int i = 1; i < dit; i++){
				pattern += "#";
			}
			if(obj instanceof Double){
				BigDecimal b = new BigDecimal(String.valueOf(obj));
				DecimalFormat df = new DecimalFormat(pattern);
				return df.format(b);
			}
			if("null".equals(obj.toString())){
				return "";
			}
			return obj;
		} catch (Exception e){
			return obj;
		}
	}
}
