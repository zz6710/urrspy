package com.kayak.common.exception;

import com.kayak.common.entity.result.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 其他异常报错
	 *
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public Object allExceptionHandler(HttpServletRequest request, Exception e) {
		logger.error(e.getMessage(), e);
		return R.fail(e.getMessage());
	}

	/**
	 * 工作流异常报错
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = WorkflowException.class)
	public Object businessExceptionHandler(HttpServletRequest request, WorkflowException e) {
		logger.error(e.getMessage(), e);
		return R.fail(e.getMessage());
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(BindException.class)
	public R<Void> handleBindException(BindException e) {
		logger.error(e.getMessage(), e);
		String message = e.getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(", "));
		return R.fail(message);
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public R<Void> constraintViolationException(ConstraintViolationException e) {
		logger.error(e.getMessage(), e);
		String message = e.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(", "));
		return R.fail(message);
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		logger.error(e.getMessage(), e);
		String message = e.getBindingResult().getFieldError().getDefaultMessage();
		return R.fail(message);
	}
}