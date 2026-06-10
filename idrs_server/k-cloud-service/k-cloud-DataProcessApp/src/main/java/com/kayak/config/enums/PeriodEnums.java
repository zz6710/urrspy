package com.kayak.config.enums;

/**
 * mengp 分红方案自动结转类型
 * 2020-09-16
 */
public enum PeriodEnums {
	/**
	 * 按周
	 */
	DAY("3"),

	/**
	 * 按周
	 */
	WEEK("2"),

	/**
	 * 按月
	 */
	MONTH("1");

	/**
	 * 状态值
	 */
	private final String type;

	PeriodEnums(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
