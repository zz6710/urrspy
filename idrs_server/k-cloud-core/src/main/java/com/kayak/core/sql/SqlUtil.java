package com.kayak.core.sql;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import com.kayak.core.util.Tools;

/**
 * 封装一些数据库操作时使用的方法
 */
public class SqlUtil {

	/**
	 * 返回可直接放在SQL语句中使用的字符串<br />
	 * 对str做了以下处理：<br />
	 * 1. 单引号过滤<br />
	 * 2. 前后用单引号括起来
	 */
	public static String sqlString(String str) {
		return "'" + str2query(str) + "'";
	}

	/**
	 * 字符串直接用在sql(或hql)语句上时,必须经过此函数过滤<br />
	 * (过滤是将str里的单引号(')转换成两个单引号(''))
	 */
	public static String str2query(String str) {
		if (str == null) {
			return "";
		}
		return str.replace("'", "''");// 处理所有单引号
		// .replace("{''}", "'");//处理转意单引号
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Long对象返回，如果obj为null，则返回0
	 */
	public static Long obj2Long(Object obj) {
		if (obj == null)
			return 0l;

		try {
			String str = obj.toString();

			if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
				DecimalFormat df = new DecimalFormat("#.#");
				str = df.format(obj);
			}

			int idx = str.indexOf(".");
			if (idx > -1)
				str = str.substring(0, str.indexOf("."));
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return 0l;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Integer对象返回，如果obj为null，则返回0
	 */
	public static Integer obj2Integer(Object obj) {
		if (obj == null)
			return 0;

		try {
			String str = obj.toString();

			if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
				DecimalFormat df = new DecimalFormat("#.#");
				str = df.format(obj);
			}

			int idx = str.indexOf(".");
			if (idx > -1)
				str = str.substring(0, str.indexOf("."));
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Short对象返回，如果obj为null，则返回0
	 */
	public static Short obj2Short(Object obj) {
		if (obj == null)
			return 0;

		try {
			String str = obj.toString();

			if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
				DecimalFormat df = new DecimalFormat("#.#");
				str = df.format(obj);
			}

			int idx = str.indexOf(".");
			if (idx > -1)
				str = str.substring(0, str.indexOf("."));
			return Short.parseShort(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Double对象返回，如果obj为null，则返回0
	 */
	public static Double obj2Double(Object obj) {
		if (obj == null)
			return 0d;

		try {
			return Double.parseDouble(obj.toString());
		} catch (NumberFormatException e) {
			return 0d;
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为BigDecimal对象返回，如果obj为null，则返回0
	 */
	public static BigDecimal obj2BigDecimal(Object obj) {
		if (obj == null || obj.equals(Tools.ZERO_E_BIGDECIMAL))
			return new BigDecimal(0);

		try {
			return (BigDecimal) obj;
		} catch (ClassCastException e) {
			return BigDecimal.valueOf(Tools.str2Double(obj.toString()));
		}
	}

	/**
	 * 把从用sql语句里取得的对象，转换为String对象返回，如果obj为null，则返回""
	 */
	public static String obj2String(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}

	/**
	 * 把从用sql语句里取得的对象，转换为Date对象返回，如果obj为null，则返回EmptyDate
	 */
	public static Date obj2Date(Object obj) {
		if (obj == null)
			return Tools.getEmptyDate();

		try {
			return (Date) obj;
		} catch (ClassCastException e) {
			return Tools.getEmptyDate();
		}
	}

	/**
	 * 获取byte[]类型的值，如果类型转换出错或者obj==null则返回一个空的byte[]对象
	 */
	public static byte[] obj2ByteArray(Object obj) {

		if (obj == null)
			return new byte[] {};
		try {
			return (byte[]) obj;
		} catch (ClassCastException e) {
			return new byte[] {};
		}
	}

}
