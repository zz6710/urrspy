package com.kayak.xsql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 数据库名称，可用于分辨当前数据库类型
 * <p>
 * 现支持Oracle和MySQL
 * 
 * @author vinson
 *
 */
public abstract class DatabaseName {

	/** JDBC获取到的Oracle数据库产品的名称 */
	public static final String Oracle = "Oracle";
	/** JDBC获取到的MySQL数据库产品的名称 */
	public static final String MySQL = "MySQL";
	/** JDBC获取到的Microsoft SQL Server数据库产品的名称 */
	public static final String Microsoft_SQL_Server = "Microsoft SQL Server";

	private static final List<String> NAMES = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(Oracle);
			add(MySQL);
			add(Microsoft_SQL_Server);
		}
	};

	/**
	 * 名称指定的数据库产品是否为MySQL数据库
	 * 
	 * @param name 数据库产品名称
	 * @return 入参名称是MySQL时返回{@code true}，否则返回{@code false}
	 */
	public static boolean isMySQL(String name) {
		return MySQL.equals(name);
	}

	/**
	 * 名称指定的数据库产品是否为Oracle数据库
	 * 
	 * @param name 数据库产品名称
	 * @return 入参名称是Oracle时返回{@code true}，否则返回{@code false}
	 */
	public static boolean isOracle(String name) {
		return Oracle.equals(name);
	}

	/**
	 * 名称指定的数据库产品是否为SQL Server数据库
	 * 
	 * @param name 数据库产品名称
	 * @return 入参名称是SQL Server时返回{@code true}，否则返回{@code false}
	 */
	public static boolean isSqlServer(String name) {
		return Microsoft_SQL_Server.equals(name);
	}

	/**
	 * 判断当前数据库产品是否被XSQL所支持
	 * 
	 * @param name 数据库产品名称
	 * @return 被XSQL支持时返回{@code true}，否则返回{@code false}
	 * 
	 */
	public static boolean isSupport(String name) {
		return NAMES.contains(name);
	}

	/**
	 * 当数据库产品不被XSQL所支持时，可以调用该方法抛出一个具有固定错误描述的异常
	 * 
	 * @param name 不被支持的数据库产品的名称
	 * @return 抛出带有固定错误描述的{@link Exception}对象
	 */
	public static Exception unsupported(String name) {
		String _name = Optional.ofNullable(name).orElse("null");
		return new Exception("Types of databases that are not currently supported [" + _name + "]");
	}

}
