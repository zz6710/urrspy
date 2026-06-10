package com.kayak.rpt.email.util;

public interface EmailDict {
     String SUCCESS = "SUCCESS";
     String CHARSET_DEFAULT = "UTF-8";

     interface Status{
         //成功
         String SUCCESS = "1";
         //失败
         String FAIL = "2";
    }

    /**
     * 邮件模板启用状态 0 未启用  1 已启用
     */
    interface EffectStatus{
        //启用
        String ON_1 = "1";
        //未启用/已停用
        String OFF_0 = "0";
    }
    interface EffectStatusName{
        //启用
        String ON_1 = "启用";
        //未启用/已停用
        String OFF_0 = "停用";
    }

    /**
     * 邮件日志 数据状态
     */
    interface DataStatus{
         //待处理
         String NEW = "A";
        //有效
        String USE = "E";
        //删除、撤销
        String DELETE = "D";
        //流程中
        String FLOW = "F";
        //待定
        String NO_DEFINE = "P";
        //中止
        String STOP = "S";
    }

    interface SendStatus{
        //成功
        String SUCCESS_00 = "1";
        //准备发送、待发送
        String PREPARE_00 = "00";
        //失败
        String FAIL_02 = "0";
    }

    /**
     * 邮件类型
     */
    interface EmailType{
        //通知邮件
        String NORMAL_M1 = "M1";
        //报告发布邮件
        String REPORT_PUBLISH_M2 = "M2";
        //托管行审批邮件
        String BEFORE_REPORT_M3 = "M3";
        //发行登记表
        String AFTER_REPORT_M4 = "M4";
    }

    /**
     * 
     *
     */
    interface EmailTypeName{
        //普通通知
        String NORMAL_M1_NAME = "普通邮件";
        //报告发布通知
        String REPORT_PUBLISH_M2_NAME = "报告发布";
        //报送前异常数据通知
        String BEFORE_REPORT_M3_NAME = "报送前异常数据通知";
        //报送后异常数据通知
        String AFTER_REPORT_M4_NAME = "报送后异常数据通知";
    }

    /**
     * 文件路径 类型
     */
    interface FilePathType{
        //通用文档目录
        String DEFAULT_PATH = "xpTemp";
        //邮件附件打包目录
        String EMAIL_PATH = "email";
        //系统文件上传压缩包目录
        String SYSTEM_FILE_PATH = "sys";
        //对接理财文件目录
        String LCD_PATH = "lc";
        //onlyOffice预览本地目录
        String ONLY_OFFICE_PATH = "onlyOffice";
        //系统获取备案通知书本地目录
        String CISP_PATH = "bak";
        //托管行电子对账文件目录
        String SZT_PATH = "szt";
        //文档模板目录
        String MODEL_PATH = "model";
    }

    /**
     * 报送  频率类型
     */
    interface RegularReportType{
        //月报
        String MONTH_REPORT = "101";
        //季报
        String QUARTER_REPORT = "102";
        //半年报
        String HALF_YEAR_REPORT = "103";
        //年报
        String YEAR_REPORT = "104";
    }

    /**
     * 发邮件相关  系统参数键值  服务器地址及用户名密码
     */
    interface EmailHostParams{
        //服务器ip
        String SEND_HOST = "email_host_01";
        //服务器端口
        String SEND_PORT = "email_port_02";
        //发件人邮箱（用户名）
        String SEND_USER = "email_send_acct_03";
        //发件人密码
        String SEND_SECRET = "email_send_secret_04";
        //发件人邮箱（用户名）
        String SEND_USER_NAME = "email_send_name_05";
        //关联的moduleId 默认值 配置的时候需要注意
        String MODULEID_VALUE = "0";
    }


    /**
     * 充当系统配置的主键值，单个附件文件的-可根据配置获取附件文件名
     */
    interface EmailBizTableFileName{
        //监管报送平台打标为房地产行业的债券投资明细  对应文件名 ”截至XXXX年XX月末监管报送平台打标为房地产行业的债券投资明细“ (后缀 目前为.xlxs)
        String APP_PROD_BOND_PROPERTY_INFO = "email_biz_filename_01";
        //文件头  "债券wind代码,债券持仓数量,成本,发行人,额度占用方,是否投向房地产行业,产品名称/委外专户名称,投资估值表比例,券面金额（万元）"
        String APP_PROD_BOND_PROPERTY_INFO_CLOUNMS = "email_biz_filename_cloumns_01";
    }


    /**
     * S3 类型配置 固定值  文件发邮件相关
     */
    interface ConfigContentType{
        String S3_COMMON="S3_COMMON";
    }

    /**
     * 符号 标识
     */
    interface SymbolType{
         String T_POINT = ".";
         String T_COMMA = ",";
         String T_SEPARATOR = "/";
         String T_D_SEPARATOR = "\\";
         String T_BOTTOM_LINE = "_";
         String T_MIDDLE_LINE = "-";
         String T_BLANK_STR = " ";
         String T_INIT_STR = "";
         String T_SEMICOLON = ";";
         String T_COLON = ":";
         String T_CHANGE_LINE_SIGN = "\n";
         String T_SIGN_COMMA_SAME = "、";
         String T_SINGLE_SEP = "#";
    }

    /**
     *  任务 业务类型   根据任务类型判断，要走哪个分支的取数业务规则
     */
    interface EmailBizType{
        // 打标为房地产行业的债券明细数据  TODO 可对应业务表
        String TYPE_01 = "APP_PROD_BOND_PROPERTY_INFO";

        String TYPE_02 = "";

        String TYPE_03 = "";


    }
}
