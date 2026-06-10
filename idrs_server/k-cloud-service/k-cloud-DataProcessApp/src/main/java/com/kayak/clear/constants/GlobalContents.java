package com.kayak.clear.constants;

import java.math.BigDecimal;

/**
 * 全局常量类
 *
 * @author kkws_admin
 */
public class GlobalContents {

    //region 基本返回码信息
    /** 默认错误信息 */
    public static final String ERROR_RETURN_MSG = "系统处理出错";
    /** 返回码-成功 */
    public static final String RTN_CODE_SUCCESS = "0000";
    /** 返回描述-处理成功 */
    public static final String RTN_DESC_SUCCESS = "处理成功";
    /** 返回描述-查询成功 */
    public static final String RTN_SEARCH_SUCCESS = "查询成功";
    /** 返回码-失败 */
    public static final String RTN_CODE_FAILURE = "9999";
    /** 返回描述-处理失败 */
    public static final String RTN_DESC_FAILURE = "处理失败";
    /** 返回码-系统错误 */
    public static final String RTN_CODE_SYS_ERROR = "999999";
    /** 返回描述-系统错误 */
    public static final String RTN_DESC_SYS_ERROR = "系统错误";
    /** 返回描述-系统错误 */
    public static final String RTN_DESC_TRANS_ERROR = "查询数据量超过50000条";
    /** 返回给渠道的成功码 */
    public static final String HEAD_RTN_CODE_SUCCESS = "0000";
    /** 正在执行中  */
    public static final String HEAD_RTN_CODE_IMPLEMENT = "处理中";

    //endregion

    //region 通用状态
    /** 0-否；1-是 */
    public static final String YES = "1";
    /** 0-否；1-是 */
    public static final String NO = "0";
    /** -1 表示不限制-String */
    public static final String UNLIMITED = "-1";
    /** -1 表示不限制-BigDecimal */
    public static final BigDecimal UNLIMITEDBigD = new BigDecimal(-1);
    /** 判断数字 -1 */
    public static boolean isUNLIMITEDBigD(BigDecimal b){
        return UNLIMITEDBigD.compareTo(b) == 0;
    }


    /** 男 */
    public static final String MAN = "1";
    /** 女 */
    public static final String WOMAN = "2";
    public static final String CONCAT_STR = "\u0001";
    //endregion

    /** 个人 */
    public static final String PERSONAL = "1";
    /** 机构 */
    public static final String MECHANISM = "0";
    /** 产品 */
    public static final String PRODUCT = "2";

    //region redis相关
    /** Redis 操作日志执行状态-未知 */
    public static final String REDIS_LOG_STATUS_UNKONW = "0";
    /** Redis 操作日志执行状态-成功 */
    public static final String REDIS_LOG_STATUS_SUCCESS = "1";
    /** Redis 操作日志调整标志-加 */
    public static final String REDIS_LOG_OPRATE_FLAG_ADD = "0";
    /** Redis 操作日志调整标志-减 */
    public static final String REDIS_LOG_OPRATE_FLAG_SUB = "1";
    /** Redis LUA脚本操作结果-成功 */
    public static final String REDIS_LUA_OPRATE_SUCCESS = "success";
    /** Redis LUA脚本操作结果-失败 */
    public static final String REDIS_LUA_OPRATE_FAIL = "fail";
    /** Redis Key的分隔符 */
    public static final String REDIS_KEY_SEPARATOR = ":";
    /** Redis 操作脚本多机构Key的分隔符 */
    public static final String REDIS_LIST_KEY_SEPARATOR = "-";
    /** Redis 错误时返回码(带有key值得返回码，这样需要解析key值拼接错误信息) */
    public static final String REDIS_FAIL_ERRORCODE = "DQ01";
    /** TA系统标识 */
    public static final String TA_MENU_ID = "MA";
    /** 系统工作日 */
    public static final String PGMTYPE_SYSTEM = "1";

    /** 产品工作日 */
    public static final String PGMTYPE_WORKDAY= "2";

    /** 销售商工作日 */
    public static final String PGMTYPE_WORKDAY_DIS= "3";
    
    /** 系统工作日方案名 */
    //public static final String DEFAULT_SYSTEM_PGMNO = "00001";
    //modify by 2019-12-11 21:31:58 全部使用 000000 为系统工作日方案
    public static final String DEFAULT_SYSTEM_PGMNO = "000000";
    /** TA系统工作日方案名 */
    public static final String TA_SYSTEM_PGMNO = "000000";
    //endregion

    //region 文件相关
    /** 文件路径间隔*/
    public static final String FILE_PATH_LINUX_SEPARATOR = "/";
    /** 文件路径间隔*/
    public static final String FILE_PATH_WINDOWS_SEPARATOR = "\\";
    //endregion

    //region 批量任务执行相关
    /**任务执行优先级默认值--0000优先级最高*/
    public static final String EXEC_ORDER = "0000";
    //endregion
    // 默认销售商代码
    public static final String DEFAULT_DISTRIBUTOR_CODE = "CQ";


    /**
     * 资管导入任务组代码
     */
    public static final String ZG_IMP_GROUP_CODE = "IG0000";

    /*
    * 回滚任务id
    * */
    public static final String ROLLBACK_TASK_ID = "S000";

    /**
     * 批量任务失败检测: 失败数量标识
     */
    public static final String MUTI_BATCH_FAIL_NUMBER_FLAG = "FAILURE";

    /**
     * 批量任务失败检测: 总数标识
     */
    public static final String MUTI_BATCH_TOTAL_NUMBER_FLAG = "TOTAL";
}
