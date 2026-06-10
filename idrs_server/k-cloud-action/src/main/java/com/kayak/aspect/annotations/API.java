package com.kayak.aspect.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface API {

	// API作用
	String desc() default "";

	// API参数
	String params() default "";

	// 操作类型
	APIOperation operation() default APIOperation.SELECT;

	// 是否需要授权
	APIAuth auth() default APIAuth.YES;

	String page() default "";

	/**
	 * 可用于导出表格
	 * @return
	 */
	boolean excel() default false;

	/**
	 * 表格导出服务名，用于权限配置展示
	 * @return
	 */
	String excelServerName() default "导出";
	String StatusChangeFlow() default "";

}
