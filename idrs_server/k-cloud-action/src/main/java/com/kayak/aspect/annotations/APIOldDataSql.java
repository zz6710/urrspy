package com.kayak.aspect.annotations;

/**
 * 配置日志记录的时候，获取旧数据的SQL查询语句
 * 
 * @author liuyg
 *
 */
public @interface APIOldDataSql {
	String value();
}
