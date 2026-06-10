package com.kayak.workflow.constants;

/**
 * @author libo
 */
public class WfProcessInstanceStatusConstant {
    public static final String PREPARATION = "0";//预备
    public static final String RUNNING = "1";//开始
    public static final String REJECT = "2";//驳回
    public static final String REFUSE = "3";//拒绝
    public static final String REJECT_TO_APPLY = "4"; //驳回到开始申请节点
    public static final String PASS = "5"; //通过
    public static final String RE_APPLY = "6"; //重新提交申请
    public static final String APPROVALING = "7"; // 审批中
    public static final String REVOKE = "8"; // 撤销
    public static final String FINISH = "9"; //完成
}
