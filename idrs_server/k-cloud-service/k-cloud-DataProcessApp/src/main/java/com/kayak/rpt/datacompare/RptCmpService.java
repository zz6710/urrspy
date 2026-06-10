package com.kayak.rpt.datacompare;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.cache.util.CacheUtil;
import com.kayak.common.SliceExecDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
import com.kayak.core.util.Tools;
import com.kayak.excel.action.ColumnWidthStyleStrategy;
import com.kayak.report.dao.BaseReportFileManageDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
@APIDefine(desc = "报表比对服务", model = RptCmp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RptCmpService {
    private static final Logger log = LoggerFactory.getLogger(RptCmpService.class);

    final private RptCmpDao rptCmpDao;

    private final  ComnDao comnDao;

    private final  SliceExecDao sliceExecDao;

    private final BaseReportFileManageDao BaseReportFileManageDao;

    @API(desc = "查询比对结果",auth = APIAuth.YES)
    public SqlResult<RptCmp> find(SqlParam<RptCmp> params) throws Exception {
        if (!StringUtils.isEmpty(params.getModel().getDealDate()) || !StringUtils.isEmpty(params.getModel().getReportDate())) {
            Map<String, Object> dParams = params.getParams();
            params.getModel().setReportDate(rptCmpDao.checkDataDate(dParams));
        }
        return rptCmpDao.find(params);
    }
    public void compare(String report_date, String table_name) throws Exception {
        HashMap cfg=rptCmpDao.getCfg(table_name);
        table_name= (String) cfg.get("table_name");
        Object coordinate_type = cfg.get("coordinate_type"); //报表维度 1:一维 2：二维
        if(coordinate_type != null && "2".equals(coordinate_type.toString())){
            //对二维报表中多余的数据进行删除操作 程晓鹏 2025.03.20 modify
            del2DImportData(table_name,report_date);
        }

        if("app_asset_regist_info".equals(table_name)){ //资产持仓登记
            investorCompare(table_name,report_date);
        }else{
            compare(report_date,table_name,cfg);
        }
    }

    /**
     * 将上传的二维报表数据进行删除
     * 删除条件为：上传的比系统数据多，并且值为0的数据
     * @param tableName 表名称
     * @param reportDate 报送日期
     * @return
     */
    private int del2DImportData(String tableName, String reportDate) throws Exception{
        int result = 0;
        Map<String,String> mapData = new HashMap<>();
        mapData.put("tableName",tableName);
        mapData.put("reportDate",reportDate);
        //由于MySQL的delete 不支持同表使用别名进行查询删除，因此采用先查询id，再进行删除的操作 程晓鹏 2025.03.20 modify
        String strSql = "select id from $U{tableName} t1 " +
                " where t1.sys_data_version = '0' and t1.report_date = $S{reportDate} " +
                " and (cast(t1.data_value as unsigned) = 0 or t1.data_value is null or t1.data_value = '') " +
                " and not exists ( " +
                "   select 1 from $U{tableName} t2 where t2.sys_data_status = '1' and t2.report_date = t1.report_date " +
                "   and t2.row_id = t1.row_id  and t2.column_id = t1.column_id " +
                " )";
        List<SqlRow> rows = comnDao.findRows(strSql, mapData);
        if(rows != null && rows.size() >0){
            StringBuilder sb = new StringBuilder();
            for(int i =0; i<rows.size(); i++){
                SqlRow row = rows.get(i);
                sb.append(row.getString("id"));
                if(i+1 != rows.size()){
                    sb.append(",");
                }
            }
            mapData.put("id",sb.toString());
            String strDel = "delete from $U{tableName} where find_in_set(id,$S{id}) and sys_data_version = '0' and report_date = $S{reportDate}";
            result = comnDao.update(strDel, mapData).getEffect();
        }
        log.info(tableName +" 导入的数据，日期："+reportDate+" 删除多余的数据量为：" + result);
        return result;
    }

    /**
     * 比对l
     */
    private void compare(String report_date,String table_name, HashMap cfg) throws Exception {
        //循环处理比对数据
        String pk_field= (String) cfg.get("pk_field");
        String pk_name= (String) cfg.get("pk_name");
        String cmp_field= (String) cfg.get("cmp_field");
        String rt_cmp_field= (String) cfg.get("rt_cmp_field");
        String cpm_name= (String) cfg.get("cpm_name");
        String left_condition= (String) cfg.get("left_condition");
        String right_condition= (String) cfg.get("right_condition");
        String sqlstr= (String) cfg.get("sqlstr");
        gennerateSql(report_date, table_name, pk_field,pk_name, cmp_field, rt_cmp_field,cpm_name ,left_condition,right_condition,sqlstr);

    }
    public void investorCompare(String table_name ,String report_date) throws Exception {
        String deleteExeid="";
        String insertExeid="";
        String insertExeid1="";
        String uploadTableName = "";
        String sqlStr5 = "",sqlStr6 = "";
      if("app_cust_register_info".equals(table_name)){
          deleteExeid="CPMINVEU01";
          insertExeid="CPMINVEU02";
          insertExeid1="CPMINVEU02_1";
          uploadTableName = "stg_s26_iis_holder_identify";
          sqlStr5 =ExeQuery.queryExeId("CPMEU07");
          sqlStr6 =ExeQuery.queryExeId("CPMEU08");
      }else if("app_cust_vol_register_info".equals(table_name)){
          deleteExeid="CPMINVEU03";
          insertExeid="CPMINVEU04";
          insertExeid1="CPMINVEU04_1";
          uploadTableName = "stg_s26_iis_holdingvolume_rp";
          sqlStr5 =ExeQuery.queryExeId("CPMEU09");
          sqlStr6 =ExeQuery.queryExeId("CPMEU10");
      }else if("app_cust_trans_info".equals(table_name)){
          deleteExeid="CPMINVEU05";
          insertExeid="CPMINVEU06";
          insertExeid1="CPMINVEU06_1";
          uploadTableName = "stg_s26_iis_holder_identify_trade";
          sqlStr5 =ExeQuery.queryExeId("CPMEU11");
          sqlStr6 =ExeQuery.queryExeId("CPMEU12");
      }else if ("app_asset_regist_info".equals(table_name)){ //资产持仓登记 程晓鹏 2025.03.14 add
          deleteExeid="CPMINVEU07";
          insertExeid="CPMINVEU08";
          insertExeid1="CPMINVEU08_1";
          sqlStr5 =ExeQuery.queryExeId("CPMEU13");
          sqlStr6 =ExeQuery.queryExeId("CPMEU14");
      }

        HashMap param= new HashMap();
        param.put("table_name",table_name);
        param.put("report_date",report_date);
        String version = rptCmpDao.getMaxVersion(param);
        param.put("version",version);
        if(version != null && version.length() >0){
            param.put("version_condition", " sys_data_version = '"+version+"' ");
        }else{
            param.put("version_condition", " sys_data_version = '1.0' ");
        }
        String deletesqlStr=ExeQuery.queryExeId(deleteExeid);
        String insertSqlStr=ExeQuery.queryExeId(insertExeid);
        String insertSqlStr1=ExeQuery.queryExeId(insertExeid1);
        comnDao.update(deletesqlStr, DataSourceProperty.PUB,param);
        int diff_count=sliceExecDao.exec(insertSqlStr,param);
        int diff_count1=sliceExecDao.exec(insertSqlStr1,param);
        // 查询uploadParam
        HashMap<String, Object> paramUpload = new HashMap<>();
        paramUpload.put("table_name",uploadTableName);
        String upload_count = "0";
        if(StringUtils.equalsAny(table_name, "app_asset_regist_info")){ //程晓鹏 2025.03.14 add
            upload_count = rptCmpDao.getUploadCount(param); //需要使用param这个Map
        }else if (StringUtils.equalsAny(uploadTableName,"stg_s26_iis_holdingvolume_rp","stg_s26_iis_holder_identify_trade")) {
            upload_count = rptCmpDao.getUploadCount1(paramUpload);
        } else {
            paramUpload.put("deal_date",report_date);
            upload_count = rptCmpDao.getUploadCount2(paramUpload);
        }
        HashMap logParam= new HashMap();
        // logParam.put("diff_count",diff_count+diff_count1);
        // logParam.put("table_name",table_name);
        // logParam.put("upload_count",upload_count);
        // logParam.put("report_date",report_date);

        logParam.put("table_name",table_name);
        //设置上传数量
        logParam.put("upload_count",upload_count);
        //设置系统存在的数量
        //获取系统存在的数量
        String sqlStr4=ExeQuery.queryExeId("CPMEU04");
        sqlStr4 = sqlStr4.replace("$S{version_condition}", String.valueOf(param.get("version_condition")));
        List<SqlRow> systemCountRows = comnDao.findRows(sqlStr4, DataSourceProperty.PUB, param);
        String systemCount = "0" ;
        if (systemCountRows.size() > 0 &&  systemCountRows.get(0).containsKey("system_count") && systemCountRows.get(0).get("system_count") != null) {
            systemCount = systemCountRows.get(0).getString("system_count");
        }

        //获取主键PK匹配上的数量
        String pkMatchCount = "0",allMatchCount= "0";
        if("app_asset_regist_info".equals(table_name)){ //当为资产持仓表时
            int diffCount = getDiffCount(table_name,report_date); //匹配不一致的数量
            System.out.println("app_asset_regist_info计算后，不一致的的数据量为： " + diffCount);
            int succCount = Integer.valueOf(systemCount) - diffCount; //成功匹配的数量
            logParam.put("pk_match_count",succCount);
            logParam.put("all_match_count",succCount);
            logParam.put("not_match_count",diffCount);
        }else{
            List<SqlRow> sqlStr5Rows = comnDao.findRows(sqlStr5,DataSourceProperty.PUB,param);
            log.info("sqlStr5Rows.size::"+sqlStr5Rows.size()+";;pk_match_count::"+sqlStr5Rows.get(0).get("pk_match_count"));
            for (SqlRow sqlStr5Row : sqlStr5Rows) {
                Set<String> strings = sqlStr5Row.keySet();
                log.info(strings.toString());
            }
            if (sqlStr5Rows.size() > 0 &&  sqlStr5Rows.get(0).containsKey("pk_match_count") && sqlStr5Rows.get(0).get("pk_match_count") != null  ) {
                pkMatchCount = sqlStr5Rows.get(0).getString("pk_match_count");
            }
            // String pkMatchCount = comnDao.findRows(sqlStr5,DataSourceProperty.PUB,param).get(0).getString("pk_match_count");
            //获取主键PK匹配上，并且所有条件都可以匹配上的数量
            List<SqlRow> sqlStr6Rows = comnDao.findRows(sqlStr6,DataSourceProperty.PUB,param);
            if (sqlStr6Rows.size() > 0 &&  sqlStr6Rows.get(0).containsKey("all_match_count") && sqlStr6Rows.get(0).get("all_match_count") != null  ) {
                allMatchCount = sqlStr6Rows.get(0).getString("all_match_count");
            }

            //设置pk主键匹配上的数据
            logParam.put("pk_match_count",pkMatchCount);
            //设置完全匹配的数量
            logParam.put("all_match_count",allMatchCount);
            //设置不匹配的数量
            logParam.put("not_match_count",Integer.parseInt(pkMatchCount) - Integer.parseInt(allMatchCount));
        }

        logParam.put("system_count",systemCount);
        //设置报告时间
        logParam.put("report_date",report_date);

        rptCmpDao.createCmpLog(logParam);
    }
    private void gennerateSql(String report_date, String table_name, String pk_field,String pk_name, String cmp_field, String rt_cmp_field ,String cpm_name,String left_condition,String right_condition,String sqlstr) throws Exception {
        String [] pk_v= pk_field.split(",");
        String [] pk_name_v= pk_name.split(",");
        String [] cmp_field_v= cmp_field.split(",");
        String [] rt_cmp_field_v= rt_cmp_field.split(",");
        String [] cpm_name_v= cpm_name.split(",");
        String lf_pk= "";
        String lf_pk_ifnull = "";
        String pk_lf_json= "json_object(";
        String rt_pk= "";
        String pk_rt_json= "json_object(";
        String lf_cmp= "";
        String rt_cmp= "";
        String compare_str= "";
        String matchStr = "";
        String lf_json="json_array(";
        String rt_json="json_array(";
        String separator;
        StringJoiner pkJoiner = new StringJoiner(" and ", "(", ")");

        for(int i=0;i<pk_v.length;i++){
            separator= (i==pk_v.length-1?"":",");
            String pk_v1=pk_v[i].toLowerCase().replace("ifnull(","").replace(")","");
            lf_pk+="lf."+pk_v1+separator;
            rt_pk+="rt."+pk_v1+separator;
            pk_lf_json+="'"+pk_name_v[i]+"',ifnull(lf."+pk_v1+",'')"+separator;
            pk_rt_json+="'"+pk_name_v[i]+"',ifnull(rt."+pk_v1+",'')"+separator;
            if (pk_v[i].toLowerCase().contains("ifnull")) {
                lf_pk_ifnull += pk_v[i].toLowerCase().replace("ifnull(","ifnull(lf.").replace(")",",'')")+separator;
                String target = pk_v[i].toLowerCase().replace("ifnull(","ifnull(lf.").replace(")",",'')") + " = " + pk_v[i].toLowerCase().replace("ifnull(","ifnull(rt.").replace(")",",'')");
                pkJoiner.add(target);
            } else {
                lf_pk_ifnull+="lf."+pk_v1+separator;
                pkJoiner.add("lf." + pk_v[i] + " = " + "rt." + pk_v[i]);
            }
        }
        pk_lf_json+=")";
        pk_rt_json+=")";
        compare_str+="(";
        matchStr+="(";
        for(int i=0;i<cmp_field_v.length;i++){
            separator= (i==cmp_field_v.length-1?"":",");
            lf_cmp+="lf."+cmp_field_v[i]+separator;
            rt_cmp+="rt."+rt_cmp_field_v[i]+separator;
            if(StringUtils.equalsAny(cmp_field_v[i],"data_value")) {
                compare_str+="ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(lf." + cmp_field_v[i] + ", '.')>0, concat(lf." + cmp_field_v[i] + ", '0'), lf." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')" +
                        "<>" +
                        "ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(rt." + cmp_field_v[i] + ", '.')>0, concat(rt." + cmp_field_v[i] + ", '0'), rt." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')" +
                        (i==cmp_field_v.length-1?"":" or ");
                matchStr+="ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(lf." + cmp_field_v[i] + ", '.')>0, concat(lf." + cmp_field_v[i] + ", '0'), lf." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')" +
                        "=" +
                        "ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(rt." + cmp_field_v[i] + ", '.')>0, concat(rt." + cmp_field_v[i] + ", '0'), rt." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')"+
                        (i==cmp_field_v.length-1?"":" and ");
            } else if(StringUtils.equalsAny(cmp_field_v[i],"prod_ccy","zon_clc_amt")) {
                compare_str+="ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(lf." + cmp_field_v[i] + ", '.')>0, concat(lf." + cmp_field_v[i] + ", '0'), lf." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')" +
                        "<>" +
                        "ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(rt." + cmp_field_v[i] + ", '.')>0, concat(rt." + cmp_field_v[i] + ", '0'), rt." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')" +
                        (i==cmp_field_v.length-1?"":" or ");
                matchStr+="ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(lf." + cmp_field_v[i] + ", '.')>0, concat(lf." + cmp_field_v[i] + ", '0'), lf." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')" +
                        "=" +
                        "ifnull(" +
                        "regexp_replace(regexp_replace(if(instr(rt." + cmp_field_v[i] + ", '.')>0, concat(rt." + cmp_field_v[i] + ", '0'), rt." + cmp_field_v[i] + "),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$','')" +
                        ",'')"+
                        (i==cmp_field_v.length-1?"":" and ");
            } else if(StringUtils.equalsAny(cmp_field_v[i],"ccy_and_pch_rdm")){
                compare_str+="(ifnull(regexp_replace(regexp_replace(substring_index(if(instr(lf." + cmp_field_v[i] + ", '.')>0, concat(lf." + cmp_field_v[i] + ", '0'), lf." + cmp_field_v[i] + "),',',2),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$',''),'')\n" +
                        " <> ifnull(regexp_replace(regexp_replace(substring_index(if(instr(rt." + cmp_field_v[i] + ", '.')>0, concat(rt." + cmp_field_v[i] + ", '0'), rt." + cmp_field_v[i] + "),',',2),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$',''),'')\n" +
                        " or abs(substring_index(lf." + cmp_field_v[i] + ",',',-1)-substring_index(rt." + cmp_field_v[i] + ",',',-1))/\n" +
                        "     if(substring_index(lf." + cmp_field_v[i] + ",',',-1)=0,1,substring_index(lf." + cmp_field_v[i] + ",',',-1))>0.01\n" +
                        " )" +
                        (i==cmp_field_v.length-1?"":" or ");
                matchStr+="(ifnull(regexp_replace(regexp_replace(substring_index(if(instr(lf." + cmp_field_v[i] + ", '.')>0, concat(lf." + cmp_field_v[i] + ", '0'), lf." + cmp_field_v[i] + "),',',2),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$',''),'')\n" +
                        " = ifnull(regexp_replace(regexp_replace(substring_index(if(instr(rt." + cmp_field_v[i] + ", '.')>0, concat(rt." + cmp_field_v[i] + ", '0'), rt." + cmp_field_v[i] + "),',',2),'\\\\.([0-9]*[1-9])0+','.$1'),'\\\\.0+$',''),'')\n" +
                        " and abs(substring_index(lf." + cmp_field_v[i] + ",',',-1)-substring_index(rt." + cmp_field_v[i] + ",',',-1))/\n" +
                        "     if(substring_index(lf." + cmp_field_v[i] + ",',',-1)=0,1,substring_index(lf." + cmp_field_v[i] + ",',',-1))<=0.01\n" +
                        " )" +
                        (i==cmp_field_v.length-1?"":" and ");
            }
            else{
                if(rt_cmp_field_v[i].contains("/")){//包含除法运算符时默认为数值类型，不进行非空判断
                    compare_str+="lf."+cmp_field_v[i]+" <> rt."+rt_cmp_field_v[i] + (i==cmp_field_v.length-1?"":" or ");
                    matchStr+="lf."+cmp_field_v[i]+" = rt."+rt_cmp_field_v[i] + (i==cmp_field_v.length-1?"":" and ");
                } else {
                    compare_str+="ifnull(lf."+cmp_field_v[i]+",'')<>ifnull(rt."+rt_cmp_field_v[i]+",'')"+ (i==cmp_field_v.length-1?"":" or ");
                    matchStr+="ifnull(lf."+cmp_field_v[i]+",'')=ifnull(rt."+rt_cmp_field_v[i]+",'')"+ (i==cmp_field_v.length-1?"":" and ");
                }
            }
            lf_json+="json_object('label','"+cpm_name_v[i]+"','value',lf."+cmp_field_v[i]+")"+separator;
            rt_json+="json_object('label','"+cpm_name_v[i]+"','value',rt."+rt_cmp_field_v[i]+")"+separator;
        }
        compare_str+=")";
        matchStr += ")";
        lf_json+=")";
        rt_json+=")";

        HashMap param= new HashMap();
        param.put("lf_pk",lf_pk);
        param.put("rt_pk",rt_pk);
        param.put("lf_cmp",lf_cmp);
        param.put("rt_cmp",rt_cmp);
        param.put("lf_json",lf_json);
        param.put("rt_json",rt_json);
        param.put("lf_pk_ifnull",lf_pk_ifnull);
        param.put("pk_lf_json",pk_lf_json);
        param.put("pk_rt_json",pk_rt_json);
        param.put("table_name",table_name);
        param.put("report_date",report_date);
        param.put("compare_str",compare_str);
        param.put("left_condition",left_condition);
        param.put("right_condition",right_condition);
        param.put("match_str",matchStr);
        param.put("pk_join",pkJoiner.toString());
        param.put("version_condition"," 1 = 1 ");//添加产品端4张报表版本号限制条件
        //查询最大sys_data_status='1'的最大版本号
        String version = rptCmpDao.getMaxVersion(param);
        param.put("version", version);

        if ("app_pbc_report_zg01,app_prod_issuance_regist_info,app_initial_sub_regist_info,".contains(String.valueOf(param.get("table_name")))) {
            param.put("right_condition", right_condition + " and rt.sys_data_version = '1.0' ");//CPMEU02 比对结果条件追加数据版本号筛选1.0版本
            param.put("version_condition"," sys_data_version = '1.0' ");//CPMEU06 比对匹配数量条件追加数据版本号筛选1.0版本
        }

        String delsqlStr=ExeQuery.queryExeId("CPMEU01");
        String sqlStr1=ExeQuery.queryExeId("CPMEU02");
        String sqlStr2=ExeQuery.queryExeId("CPMEU03");
        sqlStr1=sqlStr1.replace("$U{left_condition}", String.valueOf(param.get("left_condition")));
        sqlStr1=sqlStr1.replace("$U{right_condition}",String.valueOf(param.get("right_condition")));
        if (StringUtils.isNotEmpty(sqlstr)) {
            sqlStr1 = sqlstr;
        }

        HashMap logParam= new HashMap();
        comnDao.update(delsqlStr, DataSourceProperty.PUB,param);
        int diff_count=sliceExecDao.exec(sqlStr1,param);
        comnDao.update(sqlStr2, DataSourceProperty.PUB,param);
        String upload_count= rptCmpDao.getUploadCount(param);
        //获取系统存在的数量
        String sqlStr4=ExeQuery.queryExeId("CPMEU04");
        sqlStr4 = sqlStr4.replace("$S{version_condition}", String.valueOf(param.get("version_condition")));
        List<SqlRow> systemCountRows = comnDao.findRows(sqlStr4, DataSourceProperty.PUB, param);
        String systemCount = "0" ,pkMatchCount = "0",allMatchCount = "0";
        if (systemCountRows.size() > 0 &&  systemCountRows.get(0).containsKey("system_count") && systemCountRows.get(0).get("system_count") != null  ) {
            systemCount = systemCountRows.get(0).getString("system_count");
        }
        //String systemCount = comnDao.findRows(sqlStr4,DataSourceProperty.PUB,param).get(0).getString("system_count");
        //获取主键PK匹配上的数量
        String sqlStr5 =ExeQuery.queryExeId("CPMEU05");
        List<SqlRow> sqlStr5Rows = comnDao.findRows(sqlStr5,DataSourceProperty.PUB,param);
        log.info("sqlStr5Rows.size::"+sqlStr5Rows.size()+";;pk_match_count::"+sqlStr5Rows.get(0).get("pk_match_count"));
        for (SqlRow sqlStr5Row : sqlStr5Rows) {
            Set<String> strings = sqlStr5Row.keySet();
            log.info(strings.toString());
        }
        if (sqlStr5Rows.size() > 0 &&  sqlStr5Rows.get(0).containsKey("pk_match_count") && sqlStr5Rows.get(0).get("pk_match_count") != null  ) {
            pkMatchCount = sqlStr5Rows.get(0).getString("pk_match_count");
        }
       // String pkMatchCount = comnDao.findRows(sqlStr5,DataSourceProperty.PUB,param).get(0).getString("pk_match_count");
        //获取主键PK匹配上，并且所有条件都可以匹配上的数量
        String sqlStr6=ExeQuery.queryExeId("CPMEU06");
        sqlStr6 = sqlStr6.replace("$S{version_condition}", String.valueOf(param.get("version_condition")));
        List<SqlRow> sqlStr6Rows = comnDao.findRows(sqlStr6,DataSourceProperty.PUB,param);
        if (sqlStr6Rows.size() > 0 &&  sqlStr6Rows.get(0).containsKey("all_match_count") && sqlStr6Rows.get(0).get("all_match_count") != null  ) {
            allMatchCount = sqlStr6Rows.get(0).getString("all_match_count");
        }
        // String allMatchCount = rptCmpDao.getAllMatchCount(param);
        //String allMatchCount = comnDao.findRows(sqlStr6,DataSourceProperty.PUB,param).get(0).getString("all_match_count");

        logParam.put("table_name",table_name);
        //设置上传数量
        logParam.put("upload_count",upload_count);
        //设置系统存在的数量
        logParam.put("system_count",Integer.parseInt(systemCount));
        //设置pk主键匹配上的数据
        logParam.put("pk_match_count",Integer.parseInt(pkMatchCount));
        //设置完全匹配的数量
        logParam.put("all_match_count",Integer.parseInt(allMatchCount));
        //设置不匹配的数量
        logParam.put("not_match_count",Integer.parseInt(pkMatchCount) - Integer.parseInt(allMatchCount));
        //设置报告时间
        logParam.put("report_date",report_date);
        rptCmpDao.createCmpLog(logParam);
    }

    /**
     * 查询不一致的数据量
     * @param tableName 表名称
     * @param reportDate 报表日期
     * @return
     * @throws Exception
     */
    private int getDiffCount(String tableName, String reportDate) throws Exception{
        int result = 0;
        String strSql = "select lf_pk,rt_pk,lf_data,rt_data from base_rpt_cmp_result where table_name = '"+tableName+"' and report_date = '"+reportDate+"'";
        List<SqlRow> list = comnDao.findRows(strSql);
        for (int i = 0; i < list.size(); i++) {
            SqlRow data = list.get(i);

            List<String> _datas = new ArrayList<>();

            String keyvalue = "";
            String datavalue = "";
            boolean isDiff = false;//匹配数据差异时才展示具体内容

            Map lfpkMap = Tools.json2map(new JSONObject(data.getString("lf_pk")));
            Map lfdataMap = Tools.array2map(new JSONArray(data.getString("lf_data")));
            List lfList = Tools.json2list(new JSONArray(data.getString("lf_data")));

            Map rtpkMap = Tools.json2map(new JSONObject(data.getString("rt_pk")));
            Map rtdataMap = Tools.array2map(new JSONArray(data.getString("rt_data")));
            List rtList = Tools.json2list(new JSONArray(data.getString("rt_data")));
            String strDiffType = ""; //1 匹配数据差异; 2 上传多余系统; 3 系统多余上传 程晓鹏 2025.03.19 modify
            boolean lfflag = false;
            boolean rtflag = false;
            for (Object key : lfpkMap.keySet()) {
                if (!Tools.strIsEmpty(lfpkMap.get(key).toString())) lfflag = true;
            }
            for (Object key : rtpkMap.keySet()) {
                if (!Tools.strIsEmpty(rtpkMap.get(key).toString())) rtflag = true;
            }
            if (lfflag && rtflag) {
                ++result;
            }
        }
        return result;

    }


    @API(desc = "查询导入报表信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<RptCmp> findTableName(SqlParam<RptCmp> params) throws Exception {
        return rptCmpDao.findTableName(params);
    }
    @API(desc = "查询导入日志信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<RptCmp> findCmpLog(SqlParam<RptCmp> params) throws Exception {
        if (!StringUtils.isEmpty(params.getModel().getDealDate()) || !StringUtils.isEmpty(params.getModel().getReportDate())) {
            Map<String, Object> dParams = params.getParams();
            params.getModel().setReportDate(rptCmpDao.checkDataDate(dParams));
        }
        return rptCmpDao.findCmpLog(params);
    }

    @API(desc = "报表数据核对导出", operation = APIOperation.UPDATE,auth = APIAuth.YES)
    public void download(Map params, HttpServletResponse response) throws Exception {
        String property = System.getProperty("os.name");
        String temPath; //文件路径
        String tableName = (String)params.get("tableName"); //表名称
        String file_name = (String)params.get("tableName"); //文件名
        if (property.toLowerCase().startsWith("win")) {
            temPath = CacheUtil.getSystemParam("80000080007");
        } else {
            temPath = CacheUtil.getSystemParam("80000080006");
        }

        params.put("reportDate", rptCmpDao.checkDataDate(params));

        // 获取数据
        String  sqlStr= ExeQuery.queryExeId("CPMEQ04");
        List<SqlRow> result = rptCmpDao.findByMap(sqlStr, params);
        //List<RptCmp> datas = result;
        List<List<String>> excelHeaders = new ArrayList<>();
        List<String> head = new ArrayList<>();

        File dir=new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }
        // 生成临时文件
        String temExcel = temPath + "/" + file_name + ".xlsx";
        File temExcelFile = new File(temExcel);

        if(!temExcelFile.exists()){
            temExcelFile.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(temExcelFile);

        ExcelWriterSheetBuilder excelBuilder = EasyExcel.write(out)
                .registerWriteHandler(new ColumnWidthStyleStrategy()).sheet("报表数据核对结果");
        String strAssetRegistColumns = "【上传-金额】,【上传-公允价值】,【上传-数量】,【系统-金额】,【系统-公允价值】,【系统-数量】"; //资产持仓添加的自定义列
        List<String> headKeys = new ArrayList<>();
        if(!result.isEmpty()) {// 头部信息
            JSONObject json = new JSONObject(result.get(0).getString("lf_pk"));
            headKeys.add("**差异类型**");
            head.add("**差异类型**");
            excelHeaders.add(head);
            List<SqlRow> pklist = rptCmpDao.findPkNameByMap(params);
            if(!pklist.isEmpty()){
                String pkname = pklist.get(0).getString("pk_name");
                String[] pknames = pkname.split(",");
                for (String key : pknames) {
                    head = new ArrayList<>();
                    headKeys.add(key);
                    head.add(key);
                    excelHeaders.add(head);
                }
            }
            head = new ArrayList<>();
            headKeys.add("**上传数据与系统数据差异**");
            head.add("**上传数据与系统数据差异**");
            excelHeaders.add(head);

            if("app_asset_regist_info".equals(tableName)){ //当为资产持仓登记导出时，追加6列 程晓鹏 2025.03.19 add
                String[] arrAppendCols = strAssetRegistColumns.split(",");
                for(String col: arrAppendCols){
                    head = new ArrayList<>();
                    headKeys.add(col);
                    head.add(col);
                    excelHeaders.add(head);
                }
            }
        }
        // 添加头部信息
        excelBuilder.head(excelHeaders);

        // 写入数据
        List<List<String>> excelDatas = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            SqlRow data = result.get(i);

            List<String> _datas = new ArrayList<>();

            String keyvalue = "";
            String datavalue = "";
            boolean isDiff = false;//匹配数据差异时才展示具体内容

            Map lfpkMap = Tools.json2map(new JSONObject(data.getString("lf_pk")));
            Map lfdataMap = Tools.array2map(new JSONArray(data.getString("lf_data")));//.substring(1,data.getString("lf_data").length()-1).replace("}, {", ",")
            lfdataMap.putAll(lfpkMap);
            List lfList = Tools.json2list(new JSONArray(data.getString("lf_data")));

            Map rtpkMap = Tools.json2map(new JSONObject(data.getString("rt_pk")));
            Map rtdataMap = Tools.array2map(new JSONArray(data.getString("rt_data")));
            rtdataMap.putAll(rtpkMap);
            List rtList = Tools.json2list(new JSONArray(data.getString("rt_data")));
            String strDiffType = ""; //1 匹配数据差异; 2 上传多余系统; 3 系统多余上传 程晓鹏 2025.03.19 modify
            for (String headKey : headKeys) {
                if(headKey.equals("**差异类型**")){
                    boolean lfflag = false;
                    boolean rtflag = false;
                    for(Object key : lfpkMap.keySet()){
                        if(!Tools.strIsEmpty(lfpkMap.get(key).toString())) lfflag = true;
                    }
                    for(Object key : rtpkMap.keySet()){
                        if(!Tools.strIsEmpty(rtpkMap.get(key).toString())) rtflag = true;
                    }
                    if(lfflag && rtflag) {
                        _datas.add("匹配数据差异");
                        isDiff = true;
                        strDiffType = "1";
                    }
                    if(lfflag && !rtflag){
                        _datas.add("上传多于系统");
                        strDiffType = "2";
                    }
                    if(!lfflag && rtflag){
                        _datas.add("系统多于上传");
                        strDiffType = "3";
                    }

                }else if(headKey.equals("**上传数据与系统数据差异**") && isDiff){
                    for(int j=0; j < lfList.size(); j++){
                        if((!"".equals(String.valueOf(((Map)lfList.get(j)).get("value")).replaceAll("null", "")) ||
                                !"".equals(String.valueOf(((Map)rtList.get(j)).get("value")).replaceAll("null", ""))) &&
                                !((Map)lfList.get(j)).get("value").equals(((Map)rtList.get(j)).get("value"))){
                            if(StringUtils.isNotEmpty(datavalue)){// 多行时加入换行符
                                datavalue += ";\n";
                            }
                            // 取左边数据
                            String lfstr = String.valueOf(Tools.formatObjVal(((Map)lfList.get(j)).get("value"), -1));
                            // 左边数据为小数时取小数位数
                            int dit = lfstr.length() - (lfstr.indexOf(".") == -1 ? lfstr.length() : lfstr.indexOf(".")+1);
                            // 根据左边小数位数格式化右边数据
                            String rtstr = String.valueOf(Tools.formatObjVal(((Map)rtList.get(j)).get("value"), dit));
                            datavalue += ((Map)lfList.get(j)).get("label") + ": [" + lfstr + "] - " + "[" + rtstr + "]";
                        }
                    }
                    _datas.add(datavalue);
                }else if (tableName.equals("app_asset_regist_info") && strAssetRegistColumns.contains(headKey)){ //当为资产持仓追加的6列时，进行特殊处理 程晓鹏 2025.03.17 modify
                    String cellValue = getAssetRegistCellValue(strDiffType, headKey, lfdataMap, rtdataMap);
                    _datas.add(cellValue);
                }else{ // 主键信息
                    keyvalue = rtdataMap.get(headKey) == null ? "":rtdataMap.get(headKey).toString();
                    if(StringUtils.isBlank(keyvalue) || "null".equals(keyvalue)){
                        keyvalue = lfdataMap.get(headKey) == null ? "":lfdataMap.get(headKey).toString();
                    }
                    _datas.add(keyvalue);
                }
            }
            excelDatas.add(_datas);
        }
        excelBuilder.doWrite(excelDatas);

        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try{
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("filename",file_name);
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file_name, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setCharacterEncoding("UTF-8");
            File localFile = new File(temExcel);
            response.addHeader("Content-Length",String.valueOf(localFile.length()));
            fileInputStream = new FileInputStream(localFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }
        }catch(Exception ex){
            throw ex;
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }
        }
    }

    /**
     * 获取导出资产持仓添加6列的单元格数据值
     * @param type 类型 1 匹配数据差异; 2 上传多余系统; 3 系统多余上传
     * @param headKey 添加列名称 【上传-金额】,【上传-公允价值】,【上传-数量】,【系统-金额】,【系统-公允价值】,【系统-数量】
     * @param lfdataMap 上传核对的数据Map
     * @param rtdataMap 系统的数据Map
     * @return
     */
    private String getAssetRegistCellValue(String type, String headKey, Map lfdataMap, Map rtdataMap){
        String result = "";
        if("2".equals(type)){ //2 上传多余系统
            if("【上传-金额】".equals(headKey)){
                result = getValue(lfdataMap, "金额");
            }else if("【上传-公允价值】".equals(headKey)){
                result = getValue(lfdataMap, "公允价值");
            }else if("【上传-数量】".equals(headKey)){
                result = getValue(lfdataMap, "数量");
            }else{
                ;
            }
        }else if ("3".equals(type)){ //3 系统多余上传
            if("【系统-金额】".equals(headKey)){
                result = getValue(rtdataMap, "金额");
            }else if("【系统-公允价值】".equals(headKey)){
                result = getValue(rtdataMap, "公允价值");
            }else if("【系统-数量】".equals(headKey)){
                result = getValue(rtdataMap, "数量");
            }else{
                ;
            }
        }else if ("1".equals(type)){ //1 匹配数据差异
            if("【上传-金额】".equals(headKey)){
                result = getValue(lfdataMap, "金额");
            }else if("【上传-公允价值】".equals(headKey)){
                result = getValue(lfdataMap, "公允价值");
            }else if("【上传-数量】".equals(headKey)){
                result = getValue(lfdataMap, "数量");
            }else if("【系统-金额】".equals(headKey)){
                result = getValue(rtdataMap, "金额");
            }else if("【系统-公允价值】".equals(headKey)){
                result = getValue(rtdataMap, "公允价值");
            }else if("【系统-数量】".equals(headKey)){
                result = getValue(rtdataMap, "数量");
            }else{
                ;
            }
        }else{
            ;
        }
        return result;
    }

    /**
     * 获取值
     * @param map 数据Map
     * @param key 查询的键
     * @return
     */
    private String getValue(Map map, String key){
        String result = "";
        Object obj = map.get(key);
        if(obj != null){
            result = Tools.formatObjVal(obj,-1).toString();
        }
        return result;
    }
}
