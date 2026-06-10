package com.kayak.xsql;

import com.alibaba.druid.filter.config.ConfigTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 辅助类
 * 
 * @author zuojie
 * 
 */
public class XsqlUtils {
	private static final Logger log = LoggerFactory.getLogger(XsqlUtils.class);
	public static void close(AutoCloseable o) {
		if (o != null)
			try {
				o.close();
				XsqlImpl.dbKeepMap.remove(o);
			} catch (Exception e) {
				log.error("关闭连接错误");
			}
	}

	/** SQL风格的字段名(下划线分割)转换成Java风格的字段名(驼峰形式) */
	public static String sqlToJava(String field) {
		String[] a = field.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer(a[0]);
		for (int i = 1; i < a.length; i++) {
			String b = a[i];
			if (b.length() == 0)
				continue;
			sb.append(Character.toUpperCase(b.charAt(0)));
			sb.append(b.substring(1));
		}

		return sb.toString();
	}

	public static String toString(Object[] params) {
		if (params == null)
			return "[]";
		StringBuffer sb = new StringBuffer();
		String d = "";
		sb.append("[");
		for (Object o : params) {
			if (o == null) {
				sb.append(d).append("null");
			} else {
				sb.append(d).append(o.toString());
			}
			d = ", ";
		}
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Druid密码解密
	 * @param publicKey
	 * @param password
	 * @return
	 */
	public static String parsePwd(String publicKey, String password){

		try {
			return ConfigTools.decrypt(publicKey, password);
		} catch (Exception e) {
			log.error("Druid数据库密码解析错误");
		}
		return null;
	}
}
