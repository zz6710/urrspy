package com.kayak.core.exception;

/**
 * 系统级程序产生错误时抛出
 */
public class SystemException extends Exception
{
	private static final long serialVersionUID = -5567022272108120638L;

	public SystemException(String message)
	{
		super(message);
	}
}
