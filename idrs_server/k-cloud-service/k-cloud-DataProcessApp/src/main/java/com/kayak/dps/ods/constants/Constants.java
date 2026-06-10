package com.kayak.dps.ods.constants;


/**
 * 常量
 */
public class Constants {

    /** 文件状态 - 处理中 */
    public static final String FILE_STATE_01 = "01";
    /** 文件状态 - 处理失败 */
    public static final String FILE_STATE_02 = "02";
    /** 文件状态 - 处理成功 */
    public static final String FILE_STATE_03 = "03";


    /** 文件类型 - DBF */
    public static final String FILE_TYPE_DBF = "DBF";
    /** 文件类型 - TXT */
    public static final String FILE_TYPE_TXT = "TXT";
    /** 文件类型 - DAT */
    public static final String FILE_TYPE_DAT = "DAT";
    /** 文件类型 - TXT 编码格式 - utf-8*/
    public static final String FILE_TYPE_TXT_UTF8 = "TXT_UTF8";
    /** 文件类型 - XML */
    public static final String FILE_TYPE_XML = "XML";


    // 接口类型 - 估值
    public static final String PORT_TYPE_1 = "1";
    // 接口类型 - 投资交易
    public static final String PORT_TYPE_2 = "2";
    // 接口类型 - TA
    public static final String PORT_TYPE_3 = "3";
    // 接口类型 - WIND
    public static final String PORT_TYPE_4 = "4";
    // 接口类型 - ZG
    public static final String PORT_TYPE_5 = "5";
    // 接口类型 - 产品管理系统
    public static final String PORT_TYPE_7 = "7";
    // 接口类型 - 财汇金融数据平台
    public static final String PORT_TYPE_8 = "8";
    // 接口类型 - 数据中台
    public static final String PORT_TYPE_9 = "9";
    // 接口类型 - 数据中台
    public static final String PORT_TYPE_10 = "10";
    // 接口类型 - 委外估值
    public static final String PORT_TYPE_11 = "11";

    // 配置参数类型 - 估值
    public static final String CONFIG_TYPE_GZ = "GZ";
    // 配置参数类型 - TA
    public static final String CONFIG_TYPE_TA = "TA";
    // 配置参数类型 - WIND
    public static final String CONFIG_TYPE_JY = "JY";
    // 配置参数类型 - WIND
    public static final String CONFIG_TYPE_WIND = "WIND";
    // 配置参数类型 - ZG
    public static final String CONFIG_TYPE_ZG = "ZG";
    // 配置参数类型 - 估值回传
    public static final String CONFIG_TYPE_GZHC = "GZHC";
    // 配置参数类型 - 交易系统回传
    public static final String CONFIG_TYPE_JYHC = "JYHC";
    // 配置参数类型 - TA回传
    public static final String CONFIG_TYPE_TAHC = "TAHC";
    // 配置参数类型 - WIND回传
    public static final String CONFIG_TYPE_WINDHC = "WINDHC";
    // 配置参数类型 - ZG回传
    public static final String CONFIG_TYPE_ZGHC = "ZGHC";
    // 配置参数类型 - 绩效回传
    public static final String CONFIG_TYPE_JXHC = "JXHC";
    // 配置参数类型 - 产品管理系统回传
    public static final String CONFIG_TYPE_CPHC = "CPHC";
    // 配置参数类型 - 产品管理系统文件推送--推送给产管系统的数据使用
    public static final String CONFIG_TYPE_CPTS = "CPTS";
    // 配置参数类型 - 财汇金融文件推送
    public static final String CONFIG_TYPE_CHJR = "CHJR";
    // 配置参数类型 - 数据中台参数
    public static final String CONFIG_TYPE_SJZT = "SJZT";
    // 配置参数类型 - CISP参数
    public static final String CONFIG_TYPE_CISP = "CISP_RECV";
    // 配置参数类型 - CISP参数
    public static final String CONFIG_TYPE_CISP_SEND = "CISP_SEND";
    // 配置参数类型 - 数据中台发送参数
    public static final String CONFIG_TYPE_SJZT_SEND = "SJZT_SEND";
    // 配置参数类型 - 委外估值
    public static final String CONFIG_TYPE_WWGZ = "WWGZ";
    /** 常量 - 是 */
    public static final String Y = "Y";
    /** 常量 - 否 */
    public static final String N = "N";


    /** 估值文件服务器参数KEY */
    public static final String SFTP_IP = "SFTP_IP";
    public static final String SFTP_PORT = "SFTP_PORT";
    public static final String USERNAMES = "USERNAMES";
    public static final String PASSWORD = "PASSWORD";
    public static final String REMOTE_PATH = "REMOTE_PATH";
    public static final String LOCAL_PATH = "LOCAL_PATH";
    public static final String IS_UPLOAD = "IS_UPLOAD";
    public static final String IS_DOWNLOAD = "IS_DOWNLOAD";
    public static final String IS_FULL_MATCHING = "IS_FULL_MATCHING";
    public static final String ODS_FA_GZB = "ods_fa_gzb";
    public static final String ODS_FA_GZB_1 = "ods_fa_gzb_1";
    public static final String ODS_FA_VOUCHERS = "ODS_FA_VOUCHERS";
    public static final String ODS_FA_VOUCHERS_1 = "ODS_FA_VOUCHERS_1";
    public static final String FILE_NAME= "FILE_NAME";
    public static final String PORT_FILE_NAME = "port_address";
    public static final String PORT_SKIP_FILE = "skip_no_file";

    public static final String RCV_FILE_NAME_1 = "CSP_FILE_NAME1";
    public static final String RCV_FILE_NAME_2 = "CSP_FILE_NAME2";

    /*报送数据生成方式*/
    public static final String REPORT_DATA_TYPE_CAL = "1";//计算
    public static final String REPORT_DATA_TYPE_SUM = "2";//合计项

//    public static final String IS_DOWNLOAD = "IS_DOWNLOAD";
//    public static final String IS_DOWNLOAD = "IS_DOWNLOAD";
//    public static final String IS_DOWNLOAD = "IS_DOWNLOAD";

}
