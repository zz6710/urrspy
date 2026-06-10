package com.kayak.core.system;

import com.kayak.core.spring.SpringContextHolder;

/**
 * 用于通过Spring框架静态取得定义好的bean对象实例
 */
public class SysBeans {
	/**
	 * 静态获取bean实例
	 * 
	 * @param beanId
	 * @return
	 */
	public static <T> T getBean(String beanId) {
		return SpringContextHolder.getBean(beanId);
	}

	public static <T> T getBean(Class<T> clazz) {
		return SpringContextHolder.getBean(clazz);
	}

}
