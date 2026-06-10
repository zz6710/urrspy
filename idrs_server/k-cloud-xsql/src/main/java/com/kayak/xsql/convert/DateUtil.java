package com.kayak.xsql.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期操作工具类
 * 
 * @author Lyee
 *
 */
class DateUtil {

	public static final SimpleDateFormat SDF_DATETIME_19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final ThreadLocal<Map<String, DateFormat>> LOCAL_FORMAT = new ThreadLocal<Map<String, DateFormat>>() {
		@Override
		protected Map<String, DateFormat> initialValue() {
			return new HashMap<>();
		}
	};

	public static DateFormat getFormat(String pattern) {
		Map<String, DateFormat> local = LOCAL_FORMAT.get();
		DateFormat format = local.get(pattern);
		if (format == null) {
			format = new SimpleDateFormat(pattern);
			local.put(pattern, format);
		}
		return format;
	}

	private static DateFormat getFormat(SimpleDateFormat format) {
		String pattern = format.toPattern();
		return getFormat(pattern);
	}

	public static void delFormat() {
		LOCAL_FORMAT.remove();
	}

	/** 字符串转Date */
	public static Date getDate(String date, SimpleDateFormat df) throws ParseException {
		return getFormat(df).parse(date);
	}
}
