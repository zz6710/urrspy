package com.kayak.common.constant;

/**
 * 流程常量信息
 *
 * @author yuanjinqiao
 * @date 2022/10/17 22:46
 */
public class ProcessConstants {

    public static final String SUFFIX = ".bpmn";

    /**
     * 参数缓存
     */
    public static final String PARAM_CACHE = "paramCache";

    /**
     * 动态数据
     */
    public static final String DATA_TYPE = "dynamic";

    /**
     * 单个审批人
     */
    public static final String USER_TYPE_ASSIGNEE = "assignee";

    /**
     * 候选人
     */
    public static final String USER_TYPE_USERS = "candidateUsers";

    /**
     * 审批组
     */
    public static final String USER_TYPE_ROUPS = "candidateGroups";

    /**
     * 单个审批人
     */
    public static final String PROCESS_APPROVAL = "approval";

    /**
     * 会签人员
     */
    public static final String PROCESS_MULTI_INSTANCE_USER = "userList";

    /**
     * nameapace
     */
    public static final String NAMASPASE = "http://flowable.org/bpmn";

    /**
     * 会签节点
     */
    public static final String PROCESS_MULTI_INSTANCE = "multiInstance";

    /**
     * 自定义属性 dataType
     */
    public static final String PROCESS_CUSTOM_DATA_TYPE = "dataType";

    /**
     * 自定义属性 候选人，候选组 流程参数
     */
    public static final String CANDIDATE_PARAM = "candidateParam";

    /**
     * 自定义属性 回调报文校验
     */
    public static final String PROCESS_CALLBACK_VALIDATE = "validate";

    /**
     * 自定义属性 回调报文校验
     */
    public static final String ENV = "env";
    /**
     * 自定义属性 审批表单类型
     */
    public static final String PROCESS_FORM_TYPE = "formType";

    /**
     * 自定义属性 业务表单类型
     */
    public static final String PROCESS_BUSI_FORM_TYPE = "busiFormType";

    /**
     * 自定义属性 业务表单key
     */
    public static final String PROCESS_BUSI_FORM_KEY = "busiFormKey";

    /**
     * 自定义属性 开启附件上传
     */
    public static final String PROCESS_ENABLE_FILE_UPLOAD = "enableFileUpload";

    /**
     * 自定义属性 候选人,候选组的中文
     */
    public static final String CANDIDATE_TEXT = "text";

    /**
     * 自定义属性 角色类型
     */
    public static final String ROLE_TYPE = "roleType";

    /**
     * 自定义属性 按钮配置
     */
    public static final String BTNS = "btns";

    /**
     * 自定义属性 可驳回节点
     */
    public static final String REJECT_TASKS = "rejectTasks";

    /**
     * 自定义属性 拒绝回调
     */
    public static final String REFUSE_CALLBACK = "refuseCallback";

    /**
     * 自定义属性 是否重复审批
     */
    public static final String REPEAT_APPROVED = "repeatApproved";

    /**
     * 自定义属性 抄送表单类型
     */
    public static final String COPY_FORM_TYPE = "copyFormType";

    /**
     * 自定义属性 抄送表单key
     */
    public static final String COPY_FORM_KEY = "copyFormKey";

    /**
     * 自定义属性 抄送用户
     */
    public static final String COPY_USERS = "copyUsers";

    /**
     * 自定义属性 抄送角色
     */
    public static final String COPY_ROLES = "copyRoles";

    /**
     * 自定义属性 抄送动态审批人参数
     */
    public static final String COPY_USER_PARAM = "copyUserParam";
}
