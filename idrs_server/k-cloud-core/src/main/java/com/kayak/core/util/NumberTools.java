package com.kayak.core.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberTools {

	/**
	 * 不要数字是科学计数法
	 * 
	 * @param value
	 * @return
	 */
	public static String formatDouble(Object value) {
		if(value == null || "".equals(value)) {
			return "0";
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		return nf.format(value);
	}

	/**
	 * 不要数字是科学计数法
	 * 
	 * @param value
	 * @return
	 */
	public static String formatDouble(double value) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		return nf.format(value);
	}

	/**
	 * 相加取平均值
	 * 
	 * @param scale
	 * @param roundingMode
	 * @param str
	 * @return
	 */
	public static String getAverageValForStr(int scale, int roundingMode, String... str) {
		if (str.length < 1) {
			return "0";
		}
		BigDecimal bigdecimal = new BigDecimal("0");
		for (int i = 0; i < str.length; i++) {
			bigdecimal = bigdecimal.add(new BigDecimal(str[i]));
		}
		return bigdecimal.divide(new BigDecimal(str.length), scale, roundingMode).toPlainString();
	}
	
	
	
	/***
	 * 求和
	 * @param scale
	 * @param roundingMode
	 * @param str
	 * @return
	 */
	public static String getAddValForStr(int scale, int roundingMode, String... str) {
		if (str.length < 1) {
			return "0";
		}
		BigDecimal bigdecimal = new BigDecimal("0");
		for (int i = 0; i < str.length; i++) {
			bigdecimal = bigdecimal.add(new BigDecimal(str[i]));
		}
		return bigdecimal.setScale(2, roundingMode).toPlainString();
	}

	/**
	 * 相加取平均值
	 * 
	 * @param scale
	 * @param roundingMode
	 * @param str
	 * @return
	 */
	public static double getAverageValForDouble(int scale, int roundingMode, double... str) {
		if (str.length < 1) {
			return 0d;
		}
		BigDecimal bigdecimal = new BigDecimal(0);
		for (int i = 0; i < str.length; i++) {
			bigdecimal = bigdecimal.add(new BigDecimal(str[i]));
		}
		return bigdecimal.divide(new BigDecimal(str.length), scale, roundingMode).doubleValue();
	}

	/**
	 * 除法
	 * @param divisor
	 * @param dividend
	 * @param scale
	 * @param roundingMode
	 * @return
	 */
	public static String getDivideVal(String divisor, String dividend, int scale, int roundingMode) {
		if(divisor == null || "".equals(divisor) || dividend == null || "".equals(dividend)) {
			return "0";
		}
		BigDecimal divisorbg = new BigDecimal(divisor);
		if (divisorbg.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		}

		BigDecimal dividendbg = new BigDecimal(dividend);
		if (dividendbg.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		}
		return divisorbg.divide(dividendbg, scale, roundingMode).toPlainString();
	}
	
	public static String getBigDecimalToStr(String value,int scale,int roundingMode) {
		if(value == null || "".equals(value)) {
			return "0";
		}
		BigDecimal valuebg = new BigDecimal(value);
		if (valuebg.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		}
		return valuebg.setScale(scale, roundingMode).toPlainString();
	}

}
