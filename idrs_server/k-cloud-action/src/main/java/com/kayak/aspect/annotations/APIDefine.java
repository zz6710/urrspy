package com.kayak.aspect.annotations;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @author: beacon
 * @Date: 2019-03-21 11:46
 * @Description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface APIDefine {

	String desc() default "";

	boolean log() default true;

	// 传输的对象内容
	Class<?> model();

}
