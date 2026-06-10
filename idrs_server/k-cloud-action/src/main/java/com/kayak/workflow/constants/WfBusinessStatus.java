package com.kayak.workflow.constants;

/**
 * 工作流wf_busi_extend表的业务状态
 * @author libo
 */
public class WfBusinessStatus {

    /**
     * 处理异常已确认
     */
    public static final String ERROR_CONFIRMED = "4";

    /**
     * 处理异常
     */
    public static final String ERROR = "3";

    /**
     * 完成
     */
    public static final String FINISH = "2";

    /**
     * 处理中
     */
    public static final String PROCESSING = "1";

    /**
     * 就绪
     */
    public static final String READY = "0";

}
