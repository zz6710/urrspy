package com.kayak.common.constant;

/**
 * 工作流flow_busi_info表的业务执行状态
 * @author yuanjinqiao
 */
public class BusinessStatus {

    /** 就绪 */
    public static final String READY = "0";

    /** 处理中 */
    public static final String PROCESSING = "1";

    /** 完成 */
    public static final String FINISH = "2";

    /** 处理异常 */
    public static final String ERROR = "3";

    /** 处理异常已确认 */
    public static final String ERROR_CONFIRMED = "4";


}
