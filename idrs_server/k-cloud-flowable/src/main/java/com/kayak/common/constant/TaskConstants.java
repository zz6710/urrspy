package com.kayak.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanjinqiao
 * @createTime 2022/4/24 13:24
 */
public class TaskConstants {

    /**
     * 流程发起人
     */
    public static final String PROCESS_INITIATOR = "initiator";

    /**
     * 申请任务id
     */
    public static final String APPLY_TASK_ID = "applyTask";

    /**
     * 申请任务名字
     */
    public static final String APPLY_TASK_NAME = "申请";
    /**
     * 任务拒绝人数
     */
    public static final String MULTI_REFUSE_COUNT = "multiRefuseCount";

    /**
     * 任务通过人数
     */
    public static final String MULTI_PASS_COUNT = "multiPassCount";

    /**
     * 审核通过的人
     */
    public static final String PASS_USER_LIST = "passUserList";

    /**
     * 会签任务指定的审批人
     */
    public static final String ASSIGNEE = "assignee";

    /**
     * 发起流程时指定的审批人员
     */
    public static final String TASK_APPROVER = "_wfTaskApprover";

    /**
     * 发起流程时指定的抄送人
     */
    public static final String COPY_USER = "_wfCopyUser";

    public static List<String> getAllConstant() throws Exception {
        List<String> list = new ArrayList<>();
        for (Field field : TaskConstants.class.getDeclaredFields()) {
            list.add(field.get(null).toString());
        }
        return list;
    }
}
