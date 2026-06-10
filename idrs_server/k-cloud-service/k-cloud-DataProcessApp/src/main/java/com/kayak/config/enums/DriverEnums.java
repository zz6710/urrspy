package com.kayak.config.enums;

/**
 * mengp 分红方案自动结转类型
 * 2020-09-16
 */
public enum DriverEnums {

	ORACLE("oracle"),
	DB2("db2"),
	MYSQL("mysql");
	private final String type;
	DriverEnums(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
