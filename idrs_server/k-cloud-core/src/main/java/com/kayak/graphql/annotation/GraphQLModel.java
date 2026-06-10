package com.kayak.graphql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GraphQLModel {

	/** 数据库表名 **/
	String table() default "";

	/** 操作对象名，spring注册的名称 **/
	String fetcher();

	/** 操作对象名，spring注册的名称 **/
	String label() default "";

	/** 响应数据首条不脱敏 **/
	boolean firstLineNotDesensitized() default false;
}
