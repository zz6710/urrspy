package com.kayak.pms.global.constants;

/**
 * 文件名: ClearExecStatus.java
 * 描述:  清算任务执行表执行状态
 * 创建人: zengzt
 * 创建时间:2020年4月26日下午5:41:29
 */
public class BatchTaskStatus {

    /** 未执行 */
    public static final String NON_EXECUTION = "0";
    /** 占用中 */
    public static final String LOOT = "1";
    /** 执行中 */
    public static final String EXECUTION = "2";
    /** 分片任务执行中 */
    public static final String SLICE_EXECUTION = "3";
    /** 待调用 */
    public static final String TO_EXEC = "4";
    /** 执行成功 */
    public static final String SUCCESS = "5";
    /** 执行失败 */
    public static final String FAILED = "6";
    /** 跳过执行 */
    public static final String SKIP = "7";
    /** 任务终止 */
    public static final String TERMINATION = "8";
    /** 分片任务执行失败 */
    public static final String SLICE_FAILED = "9";
    /** 任务回滚中 */
    public static final String ROLL_BACK = "R";

    /** 任务执行预展示表，初始化状态 */
    public static final String DISPLAY_INIT = "Z";

	
}
