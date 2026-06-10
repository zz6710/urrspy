package com.kayak.rpt.zz.manage.util;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//axin
//中债直连全局参数
@Slf4j
public class CheckDataParams {

    @Autowired
    public ComnDao comnDao = SysBeans.getBean("comnDao");

    public final static  String tCode = "ZZ";	//默认中债
    public final static String CREATERFLAG = "FCODE"; // 文件名格式:创建者
    public final static String PROCDATEFLAG = "DATE"; // 文件名格式:日期
    public final static String FILETYPEFLAG = "TP"; // 文件名格式:文件类型
    public final static String FILESERNO = "SN"; // 文件名格式:序列号


    public final static String DICT_PRE = "tr_"; // 字典前缀
    public final static String LOADFILE_SPLITER = "|"; // 可用于load的数据文件分隔符
    public final static String LINE_ENDERSTR = "\n"; // 文件换行符
    public final static String INSTANT_DATE = "I"; //时间背景:时点

    /**
     * 数值单位类型
     */
    public final static String UNIT_CUR = "Y"; // 数值单位:人民币
    public final static String UNIT_PERSON = "P"; // 数值单位:人
    public final static String UNIT_MONTH = "M"; // 数值单位:月
    public final static String UNIT_DAY = "D"; // 数值单位:日
    public final static String UNIT_PRODUCT = "J"; // 数值单位:只
    public final static String UNIT_COUNT = "O"; // 数值单位:次
    public final static String UNIT_MONTHPERCOUNT = "E"; // 数值单位:个月每次
    public final static String GIVEN_CUR = "U"; // 数值单位:非人民币

    /**
     * 数据项字段类型
     */
    public final static String FLDTYPE_TUPLE = "T"; // 字段类型:多选字符,默认逗号分割
    public final static String FLDTYPE_TUPLE_T = "TT"; // 字段类型:多条多选字符,默认分号分割加逗号分割
    public final static String FLDTYPE_TUPLE_TF = "TF"; // 字段类型:T字节点类型，申报登记带多文件字段
    public final static String FLDTYPE_NUMBER = "N"; // 字段类型:数值
    public final static String DATE_RANG = "Z"; // 字段类型:日期类型yyyy-MM-dd
    public final static String REGISTER_SERNO = "A"; // 字段类型:登记流水号


    public static int errInt = 0;
    public static String workDate = "";// 当前系统工作日
    public static String workTime = "";// 系统报送时间（切日）
    public static String preWorkDate = ""; //上个工作日
    public static String nextWorkDate = ""; //下个工作日

    public static String preSysDate = ""; //上个自然日
    public static String nextSysDate = ""; //下个自然日
    public static String sysDate = ""; // 当前系统日期
    public static String sysTime = "" ;// 当前系统时间

    public static String localfilePath = "";// 本地文件地址
    public static String impfilePath = "";// 导入文件存放路径
    public static String divLimit = "100000";//直连报送单文件数据量,默认100000


    public static Map<String, List<String>> dict_map_check = new HashMap<>(); // 存放字典值字符串，用于校验合法性
    public static Map<String, List<String>> dict_mapex = new HashMap<>(); // 存放字典值字符串，用于校验合法性
    public static Map<String, List<String>> dict_map_vol = new HashMap<>(); // 存放字典值字符串，用于校验合法性202

    public static Map<String, Map<String,String>> dict_name = new HashMap<>();//进行字典名称转换使用

    public static Map<String, String> dict_map_check_all = new HashMap<>(); // 存放(字典值+空格+字符串)，用于校验合法性
    public static Map<String, String> dict_map_check_value = new HashMap<>(); // 存放(字典值)，用于校验合法性

    //系统字典值sys_dict获取
    public static Map<String, Map<String,String>> dict_name_sys = new HashMap<>();



    public static String whiteregex = "";
    public static String whitereForCode = "";//不包含漢字的白名單

    //是否需要前置指标校验通过
    public static String appCheck = "";
    public static String connFlag = "";	//是否与理财登记中心对接

    public static String trytimes = "3";	//轮询次数
    public static String sleeptime = "300";	//每次轮询等待时间

    public static String bankCode = "Z70069";// 银行编号

    public static String readLimit = "10000"; //分页处理

    public void initData() throws Exception {
       String taskDate = SysUtil.getSystemParamsByParaid("app_workDate");
       this.initVar(taskDate);
    }

    /**
     * 初始化 不包含数据字典，只有白名单和银行编号
     * @throws Exception
     */
    public void initDataNoDict() throws Exception {
        bankCode = SysUtil.getSystemParamsByParaid("80000047"); // 银行编号
        //初始化正则表达式
        StringBuffer whiteregexsb = new StringBuffer("^[");
        List<SqlRow> wrs = comnDao.findRows("select t.white_unicode from base_white_unicode t");
        if(wrs==null){
            log.info("字符白名单表数据为空，不进行字符白名单校验");
        }else{
            for (SqlRow sqlRow:wrs) {
                whiteregexsb.append(sqlRow.getString("white_unicode"));
            }
            whiteregexsb.append("]+$");
            CheckDataParams.whiteregex = whiteregexsb.toString();
            log.info("校验白名单字符的正则表达式为"+ CheckDataParams.whiteregex);
        }
        CheckDataParams.whitereForCode = getWhiteregexforcode();
    }

    public void initVar(String taskDate) throws Exception {
        //初始化地址
        localfilePath = SysUtil.getSystemParamsByParaid("app_localpath"); // 本地文件地址
        impfilePath = SysUtil.getSystemParamsByParaid("app_imppath"); // 导入文件存放路径
        workDate = taskDate;
        workTime = SysUtil.getSystemParamsByParaid("app_workTime");
        preWorkDate = DateUtil.add(workDate, "yyyyMMdd", -1);
        nextWorkDate = DateUtil.add(workDate, "yyyyMMdd", 1);

        sysDate = SysUtil.getSysDynamicParamValue("SYSDATE").toString(); //系统当前日期
        sysTime = SysUtil.getSysDynamicParamValue("SYSTIME").toString();	//系统时间


        connFlag = SysUtil.getSystemParamsByParaid("app_conn_flag");//是否与理财登记中心对接
        //是否需要前置指标校验通过
        appCheck = SysUtil.getSystemParamsByParaid("app_check");
        bankCode = SysUtil.getSystemParamsByParaid("80000047"); // 银行编号

        trytimes = SysUtil.getSystemParamsByParaid("app_getmsg_times");//轮询次数
        sleeptime = SysUtil.getSystemParamsByParaid("app_getmsg_sleep");//每次轮询等待时间

        readLimit = SysUtil.getSystemParamsByParaid("app_limit_pagenum"); //分页处理
        divLimit = SysUtil.getSystemParamsByParaid("app_div_cnt");//直连三期报送单文件数据量

        //初始化正则表达式
        StringBuffer whiteregexsb = new StringBuffer("^[");
        List<SqlRow> wrs = comnDao.findRows("select t.white_unicode from base_white_unicode t");
        if(wrs==null){
            log.info("字符白名单表数据为空，不进行字符白名单校验");
        }else{
            for (SqlRow sqlRow:wrs) {
                whiteregexsb.append(sqlRow.getString("white_unicode"));
            }
            whiteregexsb.append("]+$");
            CheckDataParams.whiteregex = whiteregexsb.toString();
            log.info("校验白名单字符的正则表达式为"+ CheckDataParams.whiteregex);
        }
        CheckDataParams.whitereForCode = getWhiteregexforcode();
        //初始化数据字典值
        initDict();
    }

    /**
     * 不包含汉字的校验白名单字符的正则表达式
     * @return
     * @throws Exception
     */
    public String getWhiteregexforcode() throws Exception {
        //初始化正则表达式
        StringBuffer whiteregexsb = new StringBuffer("^[");
        List<SqlRow> wrs = comnDao.findRows("select t.white_unicode from base_white_unicode t where t.id in (4,5,6,7,8,9,10,11,12,13,14,15)");
        if(wrs==null){
            log.info("字符白名单表数据为空，不进行字符白名单校验");
        }else{
            for (SqlRow sqlRow:wrs) {
                whiteregexsb.append(sqlRow.getString("white_unicode"));
            }
            whiteregexsb.append("]+$");
            log.info("不包含汉字的校验白名单字符的正则表达式为"+whiteregexsb);
        }
        return whiteregexsb.toString();
    }


    public void initDict() throws Exception {

//        String sql = "select out_value v , dict from base_ex_map where moduleid='R' order by dict ";
//        List<SqlRow> sqlRows = comnDao.findRows(sql);
//        CheckDataParams.dict_map_vol = paramToList(sqlRows);


        String sql = "select dict,itemkey v,itemval k from sys_dict_item  order by dict";
        List<SqlRow> sqlRows = comnDao.findRows(sql);
        CheckDataParams.dict_map_check = paramToList(sqlRows);

        //值转换
        sql = "select dict,itemkey k,itemval v from sys_dict_item  order by dict";
        sqlRows = comnDao.findRows(sql);
        CheckDataParams.dict_name_sys= paramToMap(sqlRows);
        CheckDataParams.dict_map_check_value = paramToString(sqlRows);

//        sql = "select dict,out_value || ' '||dictname v from base_ex_map where moduleid='R' order by dict ";
//        sqlRows = comnDao.findRows(sql);
//        CheckDataParams.dict_mapex = paramToList(sqlRows);
//
//
//
//        Map<String, String> dict_nm = new HashMap<>();
//        sql = "select dict ,dictname v, out_value k from base_ex_map where moduleid='R' order by dict";
//        sqlRows = comnDao.findRows(sql);
//        CheckDataParams.dict_name= paramToMap(sqlRows);

        sql = "select dict,concat(itemkey,' ',itemval) v from sys_dict_item  order by dict";
        sqlRows = comnDao.findRows(sql);
        CheckDataParams.dict_map_check_all = paramToString(sqlRows);

    }


    public Map<String, List<String>> paramToList(List<SqlRow> sqlRow){
        Map<String ,List<String>> listToMap = new HashMap<>();
        List<String> str = sqlRow.stream().map(map -> map.getString("dict")).distinct().collect(Collectors.toList());
        for (String dict : str ) {
            List<String> list = new ArrayList<>();
            for (SqlRow sr : sqlRow) {
                String dict1 = sr.getString("dict");
                if (dict.equals(dict1)){
                    list.add(sr.getString("v").replace("（","(").replace("）",")"));
                }
            }
            listToMap.put(dict,list);
        }
        return listToMap ;
    }

    /**
     * 兼容多选
     * @param sqlRow
     * @return
     */
    public Map<String, String> paramToString(List<SqlRow> sqlRow){
        Map<String ,String> stringToMap = new HashMap<>();
        List<String> str = sqlRow.stream().map(map -> map.getString("dict")).distinct().collect(Collectors.toList());
        for (String dict : str ) {
            StringBuffer stringBuffer = new StringBuffer();
            for (SqlRow sr : sqlRow) {
                String dict1 = sr.getString("dict");
                if (dict.equals(dict1)){
                    stringBuffer.append(sr.getString("v").replace("（","(").replace("）",")")+",");

                }
            }
            stringToMap.put(dict,stringBuffer.toString());
        }
        return stringToMap ;
    }



    public Map<String, Map<String,String>> paramToMap(List<SqlRow> sqlRow){
        Map<String, Map<String,String>> listToMap = new HashMap<>();

        List<String> str = sqlRow.stream().map(map -> map.getString("dict")).distinct().collect(Collectors.toList());
        for (String dict : str ) {
            Map<String,String> m = new HashMap<>();
            for (SqlRow sr : sqlRow) {
                String dict1 = sr.getString("dict");
                if (dict.equals(dict1)){
                    m.put(sr.getString("k"), sr.getString("v"));
                }
            }
            listToMap.put(dict,m);
        }
        return listToMap ;
    }


}
