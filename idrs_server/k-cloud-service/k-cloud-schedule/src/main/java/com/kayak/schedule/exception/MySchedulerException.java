package com.kayak.schedule.exception;


/**
 * 自定义定时任务异常
 * Title:  ast_web
 * Description:  描述
 * Company:	kayak
 * Makedate: 2015-12-21 上午10:23:00
 *
 * @author lixiao
 */
public class MySchedulerException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MySchedulerException() {
        super();
    }

    public MySchedulerException(String message) {
        super(message);
    }

}
