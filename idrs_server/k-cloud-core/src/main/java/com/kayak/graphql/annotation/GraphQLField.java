package com.kayak.graphql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kayak.core.desensitized.DefaultDesensitized;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GraphQLField {

	boolean argument() default true;

	/** 表主键 **/
	boolean key() default false;

	/* 数据库对应的字段 ***/
	String field() default "";

	/* 使用html的控件名 **/
	String kkhtml() default "";

	/* 控件的扩展属性 ***/
	String kkhtmlExt() default "";

	/* 默认显示的控件 **/
	boolean kkhtmlDefault() default false;

	/* 页面label **/
	String label() default "";

	/* sql中的连接符，and or **/
	String operate() default "AND";

	/* 拼接到sql中的语句 t1.f1=$S(f1) **/
	String sql() default "";

	/* 自增ID参数 **/
	String autoid() default "";

	/* 自增ID生成类 **/
	String autoidClass() default "";

	/* 是否纳入更新参数 **/
	boolean update() default true;

	/* 是否需要加密 ***/
	int encryptType() default 0;

	/* 页面label **/
	String encryptNoField() default "";

	/* 是否需要脱敏 ***/
	Class<?> desensitized() default DefaultDesensitized.class;

	/* 是否默认显示表格列 **/
	boolean gridDefault() default true;

	/* 是否作为表格列的设置项 **/
	boolean gridShow() default true;

	/* 表格列扩展属性 ***/
	String gridExt() default "";

	/**
	 * 默认值，不加密
	 */
	public static int ENCRYPT_NONE = 0;

	/**
	 * 全部人加密，只对有权限的人解密
	 */
	public static int ENCRYPT_ALL = 1;

	/**
	 * 只对自己的数据解密，需要同时配置encryptNoField员工编号映射字段，以及查询数据库必须返回改字段
	 */
	public static int ENCRYPT_SELF = 2;

}
