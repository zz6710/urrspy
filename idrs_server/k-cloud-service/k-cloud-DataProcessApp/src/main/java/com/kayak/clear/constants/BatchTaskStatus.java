package com.kayak.clear.constants;

/**
 * 批量任务执行状态<br/>
 * Task 级任务 与 Step
 *
 * @author Mosy
 * @date 2019-01-25
 **/
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

    /** 分片任务执行失败 */
    public static final String BLOCK = "B";

    /** 任务回滚中 */
    public static final String ROLL_BACK = "R";

    /** 卡批 */
    public static final String CARD = "B";

    /** 任务执行预展示表，初始化状态 */
    public static final String DISPLAY_INIT = "Z";

    /** 是否执行成功 */
    public static boolean isSuccess(String status) {
        return SUCCESS.equals(status);
    }

    /** 是否执行中 */
    public static boolean isExecution(String status) {
        return EXECUTION.equals(status);
    }

    /** 是否分片任务执行中 */
    public static boolean isSliceExecution(String status) {
        return SLICE_EXECUTION.equals(status);
    }

    /** 是否为分片任务执行失败 */
    public static boolean isSliceFailed(String status) {
        return SLICE_FAILED.equals(status);
    }

    /** 是否执行失败 */
    public static boolean isFailed(String status) {
        return FAILED.equals(status);
    }

    /** 是否为占用状态 */
    public static boolean isLoot(String status) {
        return LOOT.equals(status);
    }

    /** 是否为待调用状态 */
    public static boolean isToExec(String status) {
        return TO_EXEC.equals(status);
    }

    /**
     * 是否可执行,可执行状态如下<br/>
     * <pre>
     * (未执行)   NON_EXECUTION
     * (占用中)   LOOT
     * (执行失败) FAILED
     * </pre>
     */
    public static boolean isExecutable(String status) {
        return LOOT.equals(status) || NON_EXECUTION.equals(status) || TO_EXEC.equals(status) || FAILED.equals(status) || SLICE_FAILED.equals(status);
    }

    /**
     * 是否为正在执行中状态
     * <pre>
     * (占用中)         LOOT
     * (执行中)         EXECUTION
     * (分片任务执行中)   SLICE_EXECUTION
     * (待调用)         TO_EXEC
     * </pre>
     */
    public static boolean taskExecution(String status){
        return LOOT.equals(status) || EXECUTION.equals(status) || SLICE_EXECUTION.equals(status) || TO_EXEC.equals(status);
    }


    /**
     * 任务是为完成状态(非执行)
     * <pre>
     * (未执行)         NON_EXECUTION
     * (执行成功)       SUCCESS
     * (跳过)          SKIP
     * (执行失败)       FAILED
     * (终止)          TERMINATION
     * (分片任务执行失败) SLICE_FAILED
     * </pre>
     *
     * @param status 任务状态
     */
    public static boolean taskFinish(String status) {
        return NON_EXECUTION.equals(status) || SUCCESS.equals(status) || FAILED.equals(status) || SKIP.equals(status) || TERMINATION.equals(status) || SLICE_FAILED.equals(status);
    }

    /**
     * 是否需要发送短信
     *
     * @param status 状态
     * @return true-发送 false-不发送
     */
    public static boolean sendMessage(String status) {
        return BatchTaskStatus.FAILED.equals(status) || BatchTaskStatus.SUCCESS.equals(status) || BatchTaskStatus.SLICE_FAILED.equals(status) || BatchTaskStatus.CARD.equals(status);
    }

}
