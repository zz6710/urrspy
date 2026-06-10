package com.kayak.pms.opFlow.engine.constant;

/**
 * 工作流常量-流程实例状态
 * @author  xiamh
 * @date    20200122
 */
public class ProcessInstanceStatus {
    public static final String PREPARATION = "0";           //预备
    /**
     * 开始
     */
    public static final String RUNNING = "1";
    /**
     * 终止
     */
    public static final String STOP = "2";
    /**
     * 回退
     */
    public static final String BACK = "3";
    /**
     * 拒绝
     */
    public static final String REFUSE = "4";
    public static final String PASS = "5";                  //通过
    public static final String RE_APPLY = "6";              //重新提交申请
    public static final String APPROVALING = "7";           //审批中
    public static final String REVOKE = "8";                //撤销
    public static final String FINISH = "9";                //完成
}
