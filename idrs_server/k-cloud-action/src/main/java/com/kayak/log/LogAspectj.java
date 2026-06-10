//package com.kayak.log;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import com.kayak.aspect.annotations.API;
//
//@Aspect
//@Order(5)
//@Component
//public class LogAspectj {
//
//	private Logger logger = LoggerFactory.getLogger(LogAspectj.class);
//
//	@Pointcut("@annotation(com.kayak.aspect.annotations.API)")
//	public void pointcut() {
//
//	}
//	
//	@Pointcut("@annotation(com.kayak.aspect.annotations.API)")
//	public void pointLog() {
//
//	}
//
//	@Around(value = "pointcut() && @annotation(api)")
//	public Object doBefore(ProceedingJoinPoint point, API api) {
//		try {
//
//			String desc = api.desc();
//			Object []args = point.getArgs();
//			Class clazz = point.getTarget().getClass();
//			Signature sig = point.getSignature();
//			MethodSignature msg = (MethodSignature) sig;
//
//			Object target = point.getTarget();
//			Method currentMethod;
//
//			currentMethod = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
//
//			//logger.info(" 类：【{}】,方法：【{}】,描述：【{}】,参数【{}】", clazz.getSimpleName(), currentMethod.getName(), desc,JSON.toJSONString(args));
//
//			try {
//				return point.proceed();
//			} catch (Throwable throwable) {
//				throwable.printStackTrace();
//				logger.error("日志aspectJ异常【{}】", throwable.getMessage());
//				return throwable.getMessage();
//
//			}
//		} catch (Exception e) {
//			logger.error("日志aspectJ异常【{}】", e);
//		}
//		return null;
//	}
//
//	@AfterReturning(returning = "result", pointcut = "pointcut()")
//	public void doAfter(Object result) {
//
//		//logger.info("返回结果【{}】", JSON.toJSON(result));
//
//	}
//
//}
