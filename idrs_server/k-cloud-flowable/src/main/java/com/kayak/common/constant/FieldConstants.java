package com.kayak.common.constant;

/**
 * 工作流字段常量
 *
 * @author yuanjinqiao
 * @date 20220921
 */
public class FieldConstants {
    /**
     * 流程key
     */
    public static final String PROCESS_KEY = "processKey";
    /**
     * 流程定义id
     */
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    /**
     * 流程实例id
     */
    public static final String PROCESS_INSTANCE_ID = "processInstanceId";

    /**
     * 流程状态
     */
    public static final String PROCESS_STATUS = "processStatus";

    /**
     * 表单字段显示json(表单回显用)
     */
    public static final String LABEL_INFO = "_wfLabelJsonInfo";

    /**
     * 业务表名（入库流程用）
     */
    public static final String BUSI_TABLE_NAME = "_wfBusiTableName";

    /**
     * 业务表主键（入库流程用）
     */
    public static final String BUSI_TABLE_PRIMARY_KEY = "_wfBusiTablePrimaryKey";

    /**
     * 请求参数唯一key（用于发起工作流时，进行表单参数校验）
     */
    public static final String BUSI_UN_KEY = "_wfBusiUnKey";

    /**
     * 回调server/IP:PORT
     */
    public static final String SERVER = "_wfServer";

    /**
     * 回调url
     */
    public static final String URL = "_wfUrl";

    /**
     * 流程userid
     */
    public static final String USERID = "_wfUserid";

    /**
     * 是否回调请求（用于回调时，不进行工作流拦截，造成死循环）
     */
    public static final String BUSI_CALL_BACK = "_wfBusiCallBack";

    /**
     * 请求参数类型
     */
    public static final String CONTENT_TYPE = "_wfContentType";

}
