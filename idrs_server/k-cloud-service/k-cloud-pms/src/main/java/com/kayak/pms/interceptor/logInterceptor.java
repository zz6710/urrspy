package com.kayak.pms.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.model.FetcherData;
import com.kayak.log.service.LogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class logInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LogService logService = SysBeans.getBean("logService");
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			MethodAnnotation annotation = handlerMethod.getMethod().getAnnotation(MethodAnnotation.class);
			if (annotation != null) {
				String desc = annotation.desc();
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("method_desc", desc);
				param.put("operation_date", DateUtil.getNowDate());
				param.put("operation_time", DateUtil.getNowTime());
				param.put("userid", SysUtil.getLoginUserid());
				param.put("result", "成功");
				logService.addLog(param);
			}

		}
		HandlerInterceptor.super.preHandle(request, response, handler);
		return true;
	}
}
