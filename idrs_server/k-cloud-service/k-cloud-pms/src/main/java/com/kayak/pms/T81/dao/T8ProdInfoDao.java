package com.kayak.pms.T81.dao;

import com.google.common.base.Strings;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.T81.model.T8ProdInfo;
import com.kayak.pms.prod.model.T8ProdStandBook;
import com.kayak.pms.prod.model.T8ProdSync;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class T8ProdInfoDao extends ComnDao {


    public SqlResult<T8ProdInfo> findT8ProdInfos(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("SELECT t.id,t.prod_desc,t.is_share_sort,t.is_series_meeting,t.prod_mode,t.other_risk,t.bnote_remit_flag,t.invest_direction,t.prod_trait,t.prod_mode_id," +
                "   t.prod_series,t.prod_code,t.prod_name,t.prod_brand,t.regist_code,t.is_originality,t.originality_id,t.prod_risk_level,t.prod_cur," +
                "   t.netprice,t.raise_type,t.income_type,t.prod_classify,t.manager_code,t.publish_explain,t.filing_status,t.filing_materials_status," +
                "   t.prod_status,t.prod_son_status,t.approval_status,t.crt_date,t.crt_time,t.crt_user,t.upd_date,t.upd_time,t.upd_user,t.prod_son_series," +
                "   t2.end_date,t2.establish_date,t.product_term,prod_company," +
                "   t.t8_spare_column_one,t.t8_spare_column_two,t.t8_spare_column_three,t.t8_spare_column_four,t.t8_spare_column_five," +
                " perf.base_type " +
                " FROM t8_prod_info t " +
                " left join t8_prod_calendar t2 on t2.t8_prod_info_id = t.id " +
                " left join t8_prod_performance perf on t.id = perf.t8_prod_info_id where t.is_recycle_code !='1' or t.is_recycle_code is null " +
                "order by t.id desc", params);
    }

    public SqlResult<Map<String, Object>> findT8ProdInfos(Map<String, Object> parameters) throws Exception {

        /*String sql = "SELECT t.id,t.prod_desc,t.prod_mode,t.other_risk,t.bnote_remit_flag,t.invest_direction,t.prod_trait,t.prod_mode_id," +
                "t.prod_series,t.prod_code,t.prod_name,t.prod_brand,t.regist_code,t.is_originality,t.originality_id,t.prod_risk_level,t.prod_cur," +
                "t.netprice,t.raise_type,t.income_type,t.prod_classify,t.manager_code,t.publish_explain,t.filing_status,t.filing_materials_status," +
                "t.prod_status,t.prod_son_status,t.approval_status,t.crt_date,t.crt_time,t.crt_user,t.upd_date,t.upd_time,t.upd_user," +
                "t2.end_date,t2.establish_date,t.product_term,prod_company," +
                "t.t8_spare_column_one,t.t8_spare_column_two,t.t8_spare_column_three,t.t8_spare_column_four,t.t8_spare_column_five FROM t8_prod_info t " +
                "left join t8_prod_calendar t2 on t2.t8_prod_info_id = t.id where 1 = 1";*/

        String sql = "SELECT*FROM ( "
                + "SELECT IF (count(tps.prod_code)> 0,'1','0') AS is_have,temp.*FROM ( "
                + "SELECT t.id,t.prod_desc,t.is_recycle_code,t.prod_mode,t.other_risk,t.bnote_remit_flag,t.invest_direction,t.prod_trait,t.prod_mode_id,t.prod_series,t.prod_code," +
                    "t.prod_name,t.prod_brand,t.regist_code,t.is_originality,t.originality_id,t.prod_risk_level,t.prod_cur,t.netprice,t.raise_type,t.income_type," +
                    "t.prod_classify,t.manager_code,t.publish_explain,t.filing_status,t.filing_materials_status,t.prod_status,t.prod_son_status,t.approval_status," +
                    "t.crt_date,t.crt_time,t.crt_user,t.upd_date,t.upd_time,t.upd_user,t2.end_date,t2.establish_date,t.product_term,prod_company," +
                    "t.t8_spare_column_one,t.t8_spare_column_two,t.t8_spare_column_three,t.t8_spare_column_four,t.t8_spare_column_five FROM t8_prod_info t " +
                    "LEFT JOIN t8_prod_calendar t2 ON t2.t8_prod_info_id=t.id WHERE 1=1 ";

        if(StringUtils.isNotBlank((String) parameters.get("prodCode"))){
            sql = sql + " and t.prod_code = '"+parameters.get("prodCode")+"'";
        }
        if (StringUtils.isNotBlank((String) parameters.get("prodName"))) {
            sql += " and t.prod_name like '%" + parameters.get("prodName") + "%'";
        }
        if(StringUtils.isNotBlank((String) parameters.get("prodStatus"))){
            sql = sql + " and t.prod_status = '"+parameters.get("prodStatus")+"'";
        }
        sql = sql + ") temp LEFT JOIN t8_prod_supplementary tps ON temp.prod_code=tps.prod_code GROUP BY temp.prod_code) temp6 where 1=1 ";
        if(StringUtils.isNotBlank((String) parameters.get("isHave"))){
            if("0".equals(parameters.get("isHave"))){
                sql = sql + " and temp6.is_have='0'";
            }else{
                sql = sql + " and temp6.is_have='1'";
            }
        }
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                sql = sql + " and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null )";
            }else{
                sql = sql + " and temp6.is_recycle_code ='"+parameters.get("isRecycleCode")+"'";
            }
        }else{
            sql = sql + " and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null )";
        }
        //账户类型
        String params =(String) parameters.get("accountType");
        List<String> collect = new ArrayList<>();
        if(StringUtils.isNotBlank(params)){
             collect = Stream.of(params.split(",")).collect(Collectors.toList());
        }

        if("1".equals(parameters.get("isMaintainAccount"))){//是
            if(StringUtils.isNotBlank((String)parameters.get("accountType"))){
                for (String str :collect){
                    sql = sql +"AND exists (SELECT (1) FROM t8_prod_account_info_correlation tpaic  left join  t8_prod_account_info tpai on tpai.id = tpaic.t8_prod_account_info_id WHERE tpai.account_type = '"+str+"'  and temp6.prod_code = tpaic.prod_code ) ";
                }
            } else {
                sql = sql + " and exists (select (1) from t8_prod_account_info_correlation tpaic where temp6.prod_code = tpaic.prod_code) ";
            }
        } else if ("0".equals(parameters.get("isMaintainAccount"))) {//否
            if(StringUtils.isNotBlank((String)parameters.get("accountType"))){
                for (String str :collect){
                    sql = sql +"AND not exists (SELECT DISTINCT prod_code FROM t8_prod_account_info_correlation tpaic  left join  t8_prod_account_info tpai on tpai.id = tpaic.t8_prod_account_info_id WHERE tpai.account_type = '"+str+"' and temp6.prod_code = tpaic.prod_code ) ";

                }
            } else {
                sql = sql + " and not exists (select (1) from t8_prod_account_info_correlation tpaic where temp6.prod_code = tpaic.prod_code) ";
            }
        } else {//全部
            if(StringUtils.isNotBlank((String)parameters.get("accountType"))) {
                for (String str : collect) {
                    sql = sql + "AND temp6.prod_code in (SELECT DISTINCT prod_code FROM t8_prod_account_info_correlation tpaic  left join  t8_prod_account_info tpai on tpai.id = tpaic.t8_prod_account_info_id WHERE tpai.account_type = '" + str + "' ) ";
                }
            }
        }

        sql = sql + " order by temp6.id desc";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
    }

    public SqlResult<Map<String, Object>> findFilingT8ProdInfos(Map<String, Object> parameters) throws Exception {
        String sql = "SELECT t.id,t.prod_mode,t.other_risk,t.bnote_remit_flag,t.invest_direction,t.prod_trait,t.prod_mode_id,t.prod_series," +
                "t.prod_code,t.prod_name,t.prod_brand,t.regist_code,t.is_originality,t.originality_id,t.prod_risk_level,t.prod_cur," +
                "t.netprice,t.raise_type,t.income_type,t.prod_classify,t.manager_code,t.publish_explain,t.filing_status,t.filing_materials_status," +
                "t.prod_status,t.prod_son_status,t.approval_status,t.product_term,t.prod_company,t.crt_date,t.crt_time,t.crt_user," +
                "t.upd_date,t.upd_time,t.upd_user,t.apply_regist_date,t.issue_regist_date,t.t8_spare_column_one,t.t8_spare_column_two," +
                "t.t8_spare_column_three,t.t8_spare_column_four,t.t8_spare_column_five,tpda.approval_advice,'20' as document_type  " +
                " FROM t8_prod_info t LEFT JOIN t8_prod_document_attachment tpda on t.id=tpda.parent_id and tpda.attachment_type='20' " +
                " where CONVERT (t.prod_son_status,SIGNED) >= 6  and t.prod_son_status <> 10 ";
       if (StringUtils.isNotBlank((String)parameters.get("prodName"))) {
           sql = sql + " and t.prod_name like '%" + parameters.get("prodName") + "%'";
       }
        if(StringUtils.isNotBlank((String)parameters.get("prodCode"))){
            sql = sql + " and t.prod_code = '"+parameters.get("prodCode")+"'";
        }
        if(StringUtils.isNotBlank((String)parameters.get("filingMaterialsStatus"))){
            if("2".equals(parameters.get("filingMaterialsStatus"))){
                sql = sql + " and t.filing_materials_status = '"+parameters.get("filingMaterialsStatus")+"'";
            }else{
                sql = sql + " and (t.filing_materials_status = '"+parameters.get("filingMaterialsStatus")+"' or t.filing_materials_status = '')";
            }
        }
        if(StringUtils.isNotBlank((String)parameters.get("filingStatus"))){
            if("2".equals(parameters.get("filingStatus"))){
                sql = sql + " and t.filing_status = '"+parameters.get("filingStatus")+"'";
            }else{
                sql = sql + " and (t.filing_status = '"+parameters.get("filingStatus")+"' or t.filing_status = '')";
            }
        }
        sql = sql + " order by t.id desc";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters,this);

    }

    //查询子系列是否过会,count>0,则产品子系列上会通过
    public int findIsSeries(String t8SeriesId) throws Exception {
        String sql=" select count(*) count from t8_create_meeting_prod where t8_series_id = $S{t8SeriesId} and meeting_result = '2'";
        return super.findRow(sql,t8SeriesId).getInteger("count");
    }

    public T8ProdInfo findTaProdInfo(String id) throws Exception {
        return super.findRows(T8ProdInfo.class, "SELECT id ,prod_code,prod_name,prod_status,is_share_sort FROM t8_prod_info where id = $S{id} ", 0, id).get(0);
    }

    //报告主体文件
    public SqlResult<Map<String, Object>> findOtherFilingT8ProdInfos(Map<String, Object> parameters) throws Exception {

    	  String sql = "select * from (SELECT IF (count(t.version)> 0,'1','0') AS isHave,temp1.* FROM ( "
    	            + "SELECT a.id,a.prod_code,a.is_recycle_code,a.prod_name,a.prod_status,a.other_filing_status,IFNULL(b.doc_type,'10105') document_type,b.id t8_spare_column_one, "
    	            + "c.id t8_spare_column_two "
    	            + "FROM t8_prod_info a LEFT JOIN t8_prod_doc_info b ON a.id=b.t8_prod_info_id AND b.doc_type " +
    	                "IN ('10005','20005','30005','40005','50005','60005','70005','10105') LEFT JOIN t8_prod_doc_info c ON a.id=c.t8_prod_info_id " +
    	                "AND c.doc_type IN ('10004','20004','30004','40004','50004','60004','70004','10104') " +
    	                "WHERE 1=1";
        if(Tools.isNotEmpty((String) parameters.get("prodName"))){
            sql = sql + " and prod_name like '%"+parameters.get("prodName")+"%' ";
        }
        if(Tools.isNotEmpty((String) parameters.get("prodCode"))){
            sql = sql + " and prod_code = '"+parameters.get("prodCode")+"'";
        }
        sql = sql + ") temp1 LEFT JOIN t8_prod_document_version t ON t.prod_code=temp1.prod_code AND t.document_type " +
                "IN ('10004','10005','20004','20005','30004','30005','40004','40005','50004','50005','60004','60005','70004','70005','10104','10105') " +
                "LEFT JOIN sys_dict_item t1 ON t1.dict='t8_temp_type' AND t1.itemkey=t.document_type GROUP BY temp1.prod_code";
        sql = sql + " order by temp1.id desc ) temp6 where 1=1 ";
        if (StringUtils.isNotEmpty((String)parameters.get("isHave"))) {
            sql = sql + " and temp6.isHave='"+parameters.get("isHave")+"'";
        }
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                sql = sql +" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null )";
            }else{
                sql = sql +" and temp6.is_recycle_code ='"+parameters.get("isRecycleCode")+"'";
            }
        }else{
            sql = sql + " and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null )";
        }
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
    }
    //可行性报告文件
    public List<T8ProdInfo> findOtherFilingT8ProdInfos2(String prodCode) throws Exception {
        String sql = "SELECT d.doc_type document_type  from t8_prod_info p LEFT JOIN t8_prod_doc_info d on p.id=d.t8_prod_info_id where d.doc_type in('10004','20004','30004','40004','50004','60004','70004','10104')  and  prod_code=$S{prodCode}";

        sql = sql + " order by p.id desc";
        return super.findRows(T8ProdInfo.class,sql,0, prodCode);
    }

    //消保审核表
    public List<T8ProdInfo> findOtherFilingT8ProdInfos3(String prodCode) throws Exception {
        String sql = "SELECT d.doc_type document_type from t8_prod_info p left join t8_prod_doc_info d on p.id=d.t8_prod_info_id where d.doc_type in ('10008','20008','30008','40008','50008','60008','70008','10108') and  p.prod_code=$S{prodCode}";

        return super.findRows(T8ProdInfo.class,sql,0, prodCode);
    }

    public T8ProdInfo findT8ProdInfos(String prodCode) throws Exception {
        return super.findRow(T8ProdInfo.class, "SELECT prod_mode,other_risk,bnote_remit_flag,invest_direction,prod_trait,prod_mode_id,prod_series,prod_code,prod_name,prod_brand,regist_code,is_originality,originality_id,prod_risk_level,prod_cur,netprice,raise_type,income_type,prod_classify,manager_code,publish_explain,filing_status,filing_materials_status,prod_status,prod_son_status,approval_status,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user, " +
                " product_term,prod_company,t8_spare_column_one,t8_spare_column_two,t8_spare_column_three,t8_spare_column_four,t8_spare_column_five FROM t8_prod_info where prod_code=$S{prodCode}", 0, prodCode);
       /* return super.findRows(T8ProdInfo.class,"SELECT prod_mode,other_risk,bnote_remit_flag,invest_direction,prod_trait,prod_mode_id,prod_series,prod_code,prod_name,prod_brand,regist_code,is_originality,originality_id,prod_risk_level,prod_cur,netprice,raise_type,income_type,prod_classify,manager_code,publish_explain,filing_status,filing_materials_status,prod_status,prod_son_status,approval_status,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user," +
                        "product_term,prod_company,t8_spare_column_one,t8_spare_column_two,t8_spare_column_three,t8_spare_column_four,t8_spare_column_five FROM t8_prod_info where prod_code=$S{prodCode}",
                0, prodCode);*/
    }

    public List<SqlRow> isExistsProdCount(T8ProdInfo params) throws Exception {
        return super.findRows("SELECT prod_code,prod_name FROM t8_prod_info where prod_code=$S{prodCode} or prod_name = $S{prodName}",params);
    }

    public List<T8ProdInfo> findT8ProdRegistCode(String prodCode) throws Exception {
        return super.findRows(T8ProdInfo.class,"SELECT prod_code,prod_name,regist_code FROM t8_prod_info where prod_code=$S{prodCode} and regist_code is not null and regist_code !=''",
                0, prodCode);
    }

    public SqlResult<T8ProdInfo> isExistsProdCount(SqlParam<T8ProdInfo> t8ProdInfo) throws Exception {

        return super.findRows("SELECT prod_code,prod_name FROM t8_prod_info WHERE prod_code = $S{prodCode} or  prod_name = $S{prodName}", t8ProdInfo);

    }

    public SqlResult<T8ProdInfo> isExistsSeries(SqlParam<T8ProdInfo> t8ProdInfo) throws Exception {
    return super.findRows(
        "SELECT " +
                " tcmp.t8_series_id AS prod_son_series, " +
                " tcr.series_explain AS series_explain " +
                " FROM t8_create_meeting_prod tcmp LEFT JOIN t8_create_relation tcr " +
                " ON tcr.t8_create_meeting_id = tcmp.t8_create_meeting_id " +
                " WHERE tcmp.t8_series_id = $S{t8ProdSeriesId} " +
                " AND tcr.t8_prod_series_id = $S{t8ProdSeriesId} " +
                " AND tcmp.meeting_result='2' AND tcmp.t8_prod_info_id = ''",
        t8ProdInfo);
    }

    public SqlResult<T8ProdInfo> isProdParamsCount(SqlParam<T8ProdInfo> t8ProdInfo) throws Exception {

        return super.findRows("select count(t1.assembly_id) approval_Status from  t8_prod_assembly t1 \n" +
                "left join t8_prod_progress_record t2 on \n" +
                "t1.assembly_id = t2.assembly_id and t1.prod_code = t2.prod_code\n" +
                "where t1.prod_code = $S{prodCode} and (t2.crt_user = '' or t2.crt_user is null ) and t2.hidden = '0'", t8ProdInfo);

    }


    public SqlResult<T8ProdInfo> isExistsProdCountUpt(SqlParam<T8ProdInfo> t8ProdInfo) throws Exception {

        return super.findRows("SELECT prod_name,prod_code FROM t8_prod_info " +
                "WHERE (prod_name = $S{prodName} or prod_code = $S{prodCode}) and id <> $S{id} ", t8ProdInfo);

    }

    //更新 产品状态为 上会通过
    public int updateProdStatus(String prodCode) throws Exception {
        return super.update("update t8_prod_info set prod_status ='2',prod_son_status = '4' where prod_code = $S{prodCode} and prod_status <'3'", prodCode).getEffect();
    }

    //更新 产品 有系列过会
    public int updateIsSeriesMeeting(String prodCode) throws Exception {
        return super.update("update t8_prod_info set is_series_meeting = '1' where prod_code = $S{prodCode}", prodCode).getEffect();
    }

    public SqlResult<Map<String, Object>> findT8ProdInfoMeetingConfirms(Map<String, Object> parameters) throws Exception {
        String sql = "SELECT tt.id,tt.prod_mode,tt.other_risk,tt.bnote_remit_flag,tt.invest_direction,tt.prod_trait,tt.prod_mode_id,tt.prod_series, \n" +
                "                tt.prod_code,tt.prod_name,tt.prod_brand,tt.regist_code,tt.is_originality,tt.originality_id,tt.prod_risk_level,tt.prod_cur,tt.netprice,tt.raise_type, \n" +
                "                tt.income_type,tt.prod_classify,tt.manager_code,tt.publish_explain,tt.filing_status,tt.filing_materials_status,tt.prod_status,tt.prod_son_status, \n" +
                "                tt.approval_status,tt.crt_date,tt.crt_time,tt.crt_user,tt.upd_date,tt.upd_time,tt.upd_user,t2.crt_date declara_crt_date,su.username declara_crt_user, \n" +
                "t4.establish_date,t4.end_date," +
                "t3.crt_date issue_crt_date,t3.crt_user_name issue_crt_user\n" +
                "FROM t8_prod_info tt left join (  \n" +
                "                select count(t1.prod_code) count1,count(t2.prod_code) count2,max(t1.prod_code) prod_code  \n" +
                "                from t8_prod_assembly t1   \n" +
                "                left join t8_prod_progress_record t2 on t1.prod_code = t2.prod_code and t1.assembly_id = t2.assembly_id  \n" +
                "                GROUP BY t1.prod_code  \n" +
                "                ) t  \n" +
                "on tt.prod_code = t.prod_code \n" +
                "left join t8_prod_declara t2 on tt.id = t2.t8_prod_info_id \n" +
                "left join t8_prod_issue_info t3 on tt.id = t3.t8_prod_info_id \n" +
                "left join t8_prod_calendar t4 on tt.id = t4.t8_prod_info_id " +
                "left join sys_user su on t2.upd_user = su.userid or t2.upd_user = su.username " +
                "                where t.count1 = t.count2 "
               +  " and tt.prod_son_status+0 >= 4 and tt.prod_son_status+0 != 5 "//创设已确认 参数填写完成且参数已经审批
                ;
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                sql = sql +" and (tt.is_recycle_code != '1' or tt.is_recycle_code is null )";
            }else{
                sql = sql + " and tt.is_recycle_code ='"+parameters.get("isRecycleCode")+"'";
            }
        }else{
            sql = sql +" and (tt.is_recycle_code != '1' or tt.is_recycle_code is null )";
        }
        if (StringUtils.isNotBlank((String)parameters.get("prodName"))) {
                    sql += " and tt.prod_name like '%" + parameters.get("prodName") + "%'";
                }
        if(Tools.isNotEmpty((String)parameters.get("prodCode"))){
            sql = sql +"AND tt.prod_code = '"+parameters.get("prodCode")+"' ";
        }

        if(Tools.isNotEmpty((String)parameters.get("prodStatus"))){
            sql = sql +"AND tt.prod_status = '"+parameters.get("prodStatus")+"' ";
        }
        if(Tools.isNotEmpty((String)parameters.get("prodMode"))){
            sql = sql +"AND tt.prod_mode = '"+parameters.get("prodMode")+"' ";
        }
        sql = sql +" order by id desc";

        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
    }



    public SqlResult<Map<String, Object>> findT8ProdInfoAdjustConfirms(Map<String, Object> parameters) throws Exception {
        String sql = "SELECT tt.id,tt.prod_mode,tt.other_risk,tt.bnote_remit_flag,tt.invest_direction,tt.prod_trait,tt.prod_mode_id,tt.prod_series, \n" +
                "                tt.prod_code,tt.prod_name,tt.prod_brand,tt.regist_code,tt.is_originality,tt.originality_id,tt.prod_risk_level,tt.prod_cur,tt.netprice,tt.raise_type, \n" +
                "                tt.income_type,tt.prod_classify,tt.manager_code,tt.publish_explain,tt.filing_status,tt.filing_materials_status,tt.prod_status,tt.prod_son_status, \n" +
                "                tt.approval_status,tt.crt_date,tt.crt_time,tt.crt_user,tt.upd_date,tt.upd_time,tt.upd_user,t2.upd_date declara_crt_date,su.username declara_crt_user, \n" +
                "t4.establish_date,t4.end_date," +
                "t3.crt_date issue_crt_date,t3.crt_time issue_crt_time,t3.crt_user_name issue_crt_user\n" +
                "FROM t8_prod_info tt left join (  \n" +
                "                select count(t1.prod_code) count1,count(t2.prod_code) count2,max(t1.prod_code) prod_code  \n" +
                "                from t8_prod_assembly t1   \n" +
                "                left join t8_prod_progress_record t2 on t1.prod_code = t2.prod_code and t1.assembly_id = t2.assembly_id  \n" +
                "                GROUP BY t1.prod_code  \n" +
                "                ) t  \n" +
                "on tt.prod_code = t.prod_code \n" +
                "left join t8_prod_declara t2 on tt.id = t2.t8_prod_info_id \n" +
                "left join t8_prod_issue_info t3 on tt.id = t3.t8_prod_info_id \n" +
                "left join t8_prod_calendar t4 on tt.id = t4.t8_prod_info_id " +
                "left join sys_user su on t2.upd_user = su.userid or t2.upd_user = su.username " +
                "                where t.count1 = t.count2 "
                +  "  and tt.prod_son_status+0 != 10  and tt.prod_son_status+0 != 9 ";

            if("0".equals(parameters.get("isCompleteConfirm"))){
                sql = sql + " and tt.prod_status <5 ";
            } else if ("1".equals(parameters.get("isCompleteConfirm"))) {
                sql = sql + " and tt.prod_status >= 5 ";
            } else {
            }

        if(StringUtils.isNotBlank((String)parameters.get("prodCode"))){
            sql = sql +"AND tt.prod_code = '"+parameters.get("prodCode")+"' ";
        }
        if (StringUtils.isNotBlank((String)parameters.get("prodName"))) {
            sql = sql + " and tt.prod_name like '%" + parameters.get("prodName") + "%'";
        }
        if(StringUtils.isNotBlank((String)parameters.get("prodStatus"))){
            sql = sql +"AND tt.prod_status = '"+parameters.get("prodStatus")+"' ";
        }
        if(StringUtils.isNotBlank((String)parameters.get("prodMode"))){
            sql = sql +"AND tt.prod_mode = '"+parameters.get("prodMode")+"' ";
        }
        sql = sql +" order by id desc";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
    }

    public SqlResult<Map<String, Object>> findT8ProdInfoDeclareConfirms(Map<String, Object> parameters) throws Exception {
        String sql = "SELECT tt.id,tt.prod_mode,tt.other_risk,tt.bnote_remit_flag,tt.invest_direction,tt.prod_trait,tt.prod_mode_id,tt.prod_series, \n" +
                "tt.prod_code,tt.prod_name,tt.prod_brand,tt.regist_code,tt.is_originality,tt.originality_id,tt.prod_risk_level,tt.prod_cur,tt.netprice,tt.raise_type, \n" +
                "tt.income_type,tt.prod_classify,tt.manager_code,tt.publish_explain,tt.filing_status,tt.filing_materials_status,tt.prod_status,tt.prod_son_status, \n" +
                "tt.approval_status,tt.crt_date,tt.crt_time,tt.crt_user,tt.upd_date,tt.upd_time,tt.upd_user,t2.upd_date declara_crt_date,t2.upd_time declara_crt_time ,su.username declara_crt_user, \n" +
                "t4.establish_date,t4.end_date, t3.crt_date issue_crt_date,t3.crt_user_name issue_crt_user\n" +
                "FROM t8_prod_info tt left join (  \n" +
                "      select count(t1.prod_code) count1,count(t2.prod_code) count2,max(t1.prod_code) prod_code  \n" +
                "      from t8_prod_assembly t1   \n" +
                "      left join t8_prod_progress_record t2 on t1.prod_code = t2.prod_code and t1.assembly_id = t2.assembly_id  \n" +
                "      GROUP BY t1.prod_code) t  \n" +
                "on tt.prod_code = t.prod_code \n" +
                "left join t8_prod_declara t2 on tt.id = t2.t8_prod_info_id \n" +
                "left join t8_prod_issue_info t3 on tt.id = t3.t8_prod_info_id \n" +
                "left join t8_prod_calendar t4 on tt.id = t4.t8_prod_info_id \n" +
                "left join sys_user su on t2.upd_user = su.userid or t2.upd_user = su.username\n" +
                "where t.count1 = t.count2 ";
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                sql = sql +" and (tt.is_recycle_code != '1' or tt.is_recycle_code is null )";
            }else{
                sql = sql + " and tt.is_recycle_code ='"+parameters.get("isRecycleCode")+"'";
            }
        }else{
            sql = sql +" and (tt.is_recycle_code != '1' or tt.is_recycle_code is null )";
        }
        if(Tools.isNotEmpty((String)parameters.get("isConfirm"))){
            if("0".equals(parameters.get("isConfirm"))){
                sql = sql +"AND tt.prod_status+0<3 ";
            }else{
                sql = sql +"AND tt.prod_status+0>=3 ";
            }
        }else{
            sql = sql +" and tt.prod_son_status+0 > 5  ";
        }
        if(Tools.isNotEmpty((String)parameters.get("prodCode"))){
            sql = sql +"AND tt.prod_code = '"+parameters.get("prodCode")+"' ";
        }
        if (StringUtils.isNotBlank((String)parameters.get("prodName"))) {
            sql = sql + " and tt.prod_name like '%" + parameters.get("prodName")+ "%' ";
        }
        if(Tools.isNotEmpty((String)parameters.get("prodStatus"))){
            sql = sql +"AND tt.prod_status = '"+parameters.get("prodStatus")+"' ";
        }
        if(Tools.isNotEmpty((String)parameters.get("prodMode"))){
            sql = sql +"AND tt.prod_mode = '"+parameters.get("prodMode")+"' ";
        }
        sql = sql +" order by id desc";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
    }



    public SqlResult<Map<String, Object>> findT8ProdInfoAdjust(Map<String, Object> parameters) throws Exception {
        String sql = "SELECT tt.id,tt.prod_mode,tt.is_recycle_code,tt.other_risk,tt.bnote_remit_flag,tt.invest_direction,tt.prod_trait,tt.prod_mode_id,tt.prod_series, \n" +
                "                tt.prod_code,tt.prod_name,tt.prod_brand,tt.regist_code,tt.is_originality,tt.originality_id,tt.prod_risk_level,tt.prod_cur,tt.netprice,tt.raise_type, \n" +
                "                tt.income_type,tt.prod_classify,tt.manager_code,tt.publish_explain,tt.filing_status,tt.filing_materials_status,tt.prod_status,tt.prod_son_status, \n" +
                "                tt.approval_status,tt.crt_date,tt.crt_time,tt.crt_user,tt.upd_date,tt.upd_time,tt.upd_user,t2.upd_date declara_crt_date,t2.upd_user declara_crt_user, \n" +
                "t4.establish_date,t4.end_date," +
                "t3.crt_date issue_crt_date,t3.crt_user_name issue_crt_user\n" +
                "FROM t8_prod_info tt left join (  \n" +
                "                select count(t1.prod_code) count1,count(t2.prod_code) count2,max(t1.prod_code) prod_code  \n" +
                "                from t8_prod_assembly t1   \n" +
                "                left join t8_prod_progress_record t2 on t1.prod_code = t2.prod_code and t1.assembly_id = t2.assembly_id  \n" +
                "                GROUP BY t1.prod_code  \n" +
                "                ) t  \n" +
                "on tt.prod_code = t.prod_code \n" +
                "left join t8_prod_declara t2 on tt.id = t2.t8_prod_info_id \n" +
                "left join t8_prod_issue_info t3 on tt.id = t3.t8_prod_info_id \n" +
                "left join t8_prod_calendar t4 on tt.id = t4.t8_prod_info_id" +
                "                where t.count1 = t.count2 and tt.prod_son_status >1 " +
                "and tt.prod_son_status <= 17 and tt.prod_son_status not in ( 13,9,5) ";
        if(StringUtils.isNotBlank((String) parameters.get("prodCode"))){
            sql = sql +"AND tt.prod_code = '"+parameters.get("prodCode")+"' ";
        }
        if (StringUtils.isNotBlank((String) parameters.get("prodName"))) {
            sql = sql + " and tt.prod_name like '%" + parameters.get("prodName") + "%'";
        }
        if(StringUtils.isNotBlank((String) parameters.get("prodMode"))){
            sql = sql +"AND tt.prod_mode = '"+parameters.get("prodMode")+"' ";
        }
        if(StringUtils.isNotBlank((String) parameters.get("prodStatus"))){
            sql = sql +"AND tt.prod_status = '"+parameters.get("prodStatus")+"' ";
        }
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                sql = sql +" and (tt.is_recycle_code != '1' or tt.is_recycle_code is null )";
            }else{
                sql = sql + " and tt.is_recycle_code ='"+parameters.get("isRecycleCode")+"'";
            }
        }else{
            sql = sql +" and (tt.is_recycle_code != '1' or tt.is_recycle_code is null )";
        }
        sql = sql +" order by id desc";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
    }


    public String addT8ProdInfo(T8ProdInfo params) throws Exception {
        return super.update("INSERT INTO t8_prod_info(id,prod_desc,is_share_sort,is_series_meeting,prod_mode_id,prod_mode,product_term,prod_company,t8_spare_column_one,t8_spare_column_two,t8_spare_column_three,t8_spare_column_four,t8_spare_column_five,other_risk,bnote_remit_flag,invest_direction,prod_trait,prod_series,prod_code,prod_name,prod_brand,regist_code,is_originality,originality_id,prod_risk_level,prod_cur,netprice,raise_type,income_type,prod_classify,manager_code,publish_explain,filing_status,filing_materials_status,prod_status,prod_son_status,approval_status,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user,prod_son_series) " +
                        "VALUES($AUTOIDS{id},$S{prodDesc},$S{isShareSort},$S{isSeriesMeeting},$S{prodModeId},$S{prodMode},$S{productTerm},$S{prodCompany},$S{t8SpareColumnOne},$S{t8SpareColumnTwo},$S{t8SpareColumnThree},$S{t8SpareColumnFour},$S{t8SpareColumnFive},$S{otherRisk},$S{bnoteRemitFlag},$S{investDirection},$S{prodTrait},$S{prodSeries},$S{prodCode},$S{prodName},$S{prodBrand},$S{registCode},$S{isOriginality},$S{originalityId},$S{prodRiskLevel},$S{prodCur},$S{netprice},$S{raiseType},'3',$S{prodClassify},$S{managerCode},$S{publishExplain},$S{filingStatus},$S{filingMaterialsStatus},$S{prodStatus},$S{prodSonStatus},$S{approvalStatus},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{prodSonSeries})",
                params).getAutoId();

    }


    public List<SqlRow> getId(String prodCode,String prodName) throws Exception {
        String sql = "select id from t8_prod_info where prod_code='"+prodCode+"' and prod_name='"+prodName+"'";
        return findRows(sql);
    }

    public int updateT8ProdInfo(T8ProdInfo params) throws Exception {
        super.update("UPDATE t8_prod_info SET prod_mode_id=$S{prodModeId} ,prod_desc=$S{prodDesc},is_share_sort=$S{isShareSort},prod_mode=$S{prodMode} ,other_risk=$S{otherRisk},bnote_remit_flag=$S{bnoteRemitFlag},invest_direction=$S{investDirection},prod_trait=$S{prodTrait}," +
                        "product_term=$S{productTerm},prod_company=$S{prodCompany},t8_spare_column_one=$S{t8SpareColumnOne},t8_spare_column_two=$S{t8SpareColumnTwo},t8_spare_column_three=$S{t8SpareColumnThree},t8_spare_column_four=$S{t8SpareColumnFour},t8_spare_column_five=$S{t8SpareColumnFive}," +
                        "prod_series=$S{prodSeries} ,prod_name=$S{prodName} ,prod_brand=$S{prodBrand} ,regist_code=$S{registCode} ,is_originality=$S{isOriginality} ,originality_id=$S{originalityId} ,prod_risk_level=$S{prodRiskLevel} ,prod_cur=$S{prodCur} ,netprice=$S{netprice} ,raise_type=$S{raiseType} ,income_type=$S{incomeType} ,prod_classify=$S{prodClassify} ,manager_code=$S{managerCode} ,publish_explain=$S{publishExplain} ,filing_status=$S{filingStatus} ,filing_materials_status=$S{filingMaterialsStatus} ,prod_status=$S{prodStatus} ,prod_son_status=$S{prodSonStatus} ,approval_status=$S{approvalStatus} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser} ,prod_son_series=$S{prodSonSeries}  WHERE  id=$S{id} AND prod_code=$S{prodCode} ",
                params);
        return 1;
    }

    public int updateT8ProdInfoRiskLevel(T8ProdInfo params) throws Exception {
            super.update("UPDATE t8_prod_info SET prod_risk_level=$S{prodRiskLevel},risk_score=$S{riskScore} WHERE  id=$S{id} ",
                    params);
        return 1;
    }

    public UpdateResult deleteT8ProdInfo(SqlParam<T8ProdInfo> params) throws Exception {
        return super.update("DELETE FROM t8_prod_info WHERE  id=$S{id} AND prod_code=$S{prodCode} ",
                params.getModel());
    }

    //产品创设判断数据是否存在
    public Integer findT8ProdCalendarCounts(SqlParam<T8ProdInfo> params) throws Exception {
        //return super.findRows("SELECT count(*) FROM t8_prod_calendar", params);
        SqlRow sqlRow = super.findRow("select count(*) con " +
                "FROM t8_prod_info where id=$S{id} ", params.getModel());
        return sqlRow.getInteger("con");
    }


    //获取开放式产品信息



    //产品开放日信息调整grid
    public SqlResult<Map<String, Object>> findT8ProdWorkday(Map<String, Object> parameters) throws Exception {
        String sql = "SELECT t1.id,t1.prod_mode,t1.prod_series,t1.is_recycle_code,t1.prod_code,t1.prod_name,t1.prod_brand,t1.prod_status, " +
                "t2.apply_end_date,t2.apply_start_date, t2.liquidate, "+
                "t2.establish_date,t2.open_start_date,t2.open_end_date,t2.end_date," +
                "t1.prod_son_status,t2.pgmno ,t3.pgmname,series_name " +
                "from  t8_prod_info t1  left join t8_prod_calendar t2 on t1.prod_code = t2.prod_code  " +
                "left join sys_workday_pgm t3 on t2.pgmno = t3.pgmno " +
                "left join ods_amng_prod_series t5 on t1.prod_series = t5.series_code where t1.prod_mode != '1' " +
                "and t2.establish_date is not null and open_start_date is not null ";
        StringBuilder builder = new StringBuilder(sql);

        if (StringUtils.isNotBlank((String) parameters.get("prodName"))) {
            builder.append(" and t1.prod_name like '%" + parameters.get("prodName") + "%' ");
        }
        if(StringUtils.isNotBlank((String)parameters.get("prodCode"))){
            builder.append("and t1.prod_code = $S{prodCode}");
        }
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                builder.append(" and (t1.is_recycle_code != '1' or t1.is_recycle_code is null ) ");
            }else{
                builder.append(" and t1.is_recycle_code ='"+parameters.get("isRecycleCode")+"' ");
            }
        }else{
            builder.append(" and (t1.is_recycle_code != '1' or t1.is_recycle_code is null ) ");
        }
        return SqlUtils.sqlPackage(builder.toString(), DataSourceProperty.PUB, parameters, this);
    }



    //获取系列
    public SqlResult<T8ProdInfo> getProdSeries(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("SELECT id t8_prod_series_id, series_code,series_name FROM ods_amng_prod_series where son_flag = '0'", params);
    }
    //获取资管系列
    public SqlResult<T8ProdInfo> getNewProdSeries(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("SELECT distinct prod_ser_cd series_code,prod_ser_nm series_name  FROM APP_PRD_BAS_INF where 1=1", params);
    }

    //获取子系列
    public SqlResult<T8ProdInfo> getProdSonSeries(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("SELECT id t8_prod_series_id, series_code,series_name FROM ods_amng_prod_series WHERE parent_code = $S{parentCode} and son_flag = '1'", params);
    }

    //获取子系列1
    public SqlRow getProdSonSeries1(String prodCode) throws Exception {
        return super.findRow("SELECT prod_son_series FROM t8_prod_info WHERE prod_code = $S{prodCode}", prodCode);
    }

    //获取产品状态
    public int getProdStatus(T8ProdInfo params) throws Exception {
        String sql = "SELECT prod_status FROM t8_prod_info where prod_code = $S{prodCode}";
        return super.findRow(sql, params).getInteger("prod_status");
    }

    //产品是否系列过会
    public int findIsSeriesMeeting(String prodCode) throws Exception {
        String sql = "SELECT count(*) count FROM t8_prod_info where prod_code = $S{prodCode} and is_series_meeting ='1'";
        return super.findRow(sql, prodCode).getInteger("count");
    }

    public SqlRow getProdCode(String t8ProdInfoId) throws Exception {
        return super.findRow("SELECT prod_code FROM t8_prod_info where id = $S{id}",t8ProdInfoId);
    }






    //申报登记，sql重新写
    public SqlResult<Map<String, Object>> findProdInfoByLike(Map<String, Object> params) throws Exception {
        String sql = "select a.id,a.prod_code,a.prod_name,a.prod_status,a.risk_score,a.risk_score_status,a.prod_risk_level " +
                "from t8_prod_info a left join t8_prod_days b on a.prod_code = b.prod_code where 1=1 ";
        StringBuilder builder = new StringBuilder(sql);
        if(StringUtils.isNotBlank((String)params.get("prodCode"))){
            builder.append("and a.prod_code = $S{prodCode}");
        }
        if(StringUtils.isNotBlank((String)params.get("prodName"))){
            builder.append("and a.prod_name like '%$U{prodName}%'");
        }
        if(StringUtils.isNotBlank((String)params.get("riskScoreStatus"))){
            builder.append("and a.risk_score_status = $S{riskScoreStatus}");
        }
        if(StringUtils.isNotBlank((String)params.get("prodRiskLevel"))){
            builder.append("and a.prod_risk_level = $S{prodRiskLevel}");
        }
        SqlResult<Map<String, Object>> mapSqlResult = SqlUtils.sqlPackage(builder.toString(), DataSourceProperty.PUB, params, this);
        return mapSqlResult;
    }


    public int updateFilingStatus(SqlParam<T8ProdInfo> params) throws Exception {
        return super.update("update t8_prod_info set filing_status='2' where prod_code=$S{prodCode}", params.getModel()).getEffect();
    }

    /**
     * 功能：修改其他报备材料确认状态
     * @param prodCode
     * @return
     * @throws Exception
     */
    public int updateOtherFilingStatus(String prodCode) throws Exception {
        return super.update("update t8_prod_info set other_filing_status='2' where prod_code=$S{prodCode}", prodCode).getEffect();
    }

    public SqlResult<Map<String, Object>> findProdInfoByByCodeAndDate(SqlParam<Map<String, Object>> params) throws Exception {
        String sql = "select a.prod_code,a.prod_name,a.prod_status,a.filing_status," +
                "a.filing_materials_status from t8_prod_info a left join t8_prod_days b on a.prod_code = b.prod_code where 1=1 ";
        StringBuilder builder = new StringBuilder(sql);
        if(StringUtils.isNotBlank((String)params.getParams().get("prodCode"))){
            builder.append("and a.prod_code = $S{prodCode}");
        }
        if(StringUtils.isNotBlank((String)params.getParams().get("prodName"))){
            builder.append("and a.prod_name like concat('%',$S{prodName},'%')");
        }
        if (StringUtils.isNotBlank((String)params.getParams().get("establishDate"))){
            builder.append("and b.establish_date = $S{establishDate}");
        }
        if (StringUtils.isNotBlank((String)params.getParams().get("endDate"))){
            builder.append("and b.end_date = $S{endDate}");
        }
        SqlResult<Map<String, Object>> mapSqlResult = SqlUtils.sqlPackage(builder.toString(), DataSourceProperty.PUB, params, this);
        return mapSqlResult;
    }
    //申报登记结果维护页面查询产品信息，仅查询子状态为申报准备以后的产品信息 20210129  修改时间20210515
    public SqlResult<Map<String, Object>> findT8ProdRegistCodeInfos(Map<String, Object> parameters) throws Exception {
        StringBuffer sql = new StringBuffer("  select prod.prod_code," +
                "             prod.id," +
                "             prod.prod_name," +
                "             prod.regist_code ," +
                "             prod.raise_type," +
                "             prod.prod_status," +
                "             prod.prod_son_status" +
                "      from t8_prod_info prod " +
                "     where 1 = 1 ");

        if(StringUtils.isNotBlank((String)parameters.get("prodCode"))) {
            sql.append(" and prod.prod_code = $S{prodCode}");
        }
        if(StringUtils.isNotBlank((String)parameters.get("prodName"))) {
            sql.append(" and prod.prod_name like '%$U{prodName}%'");
        }
        if(StringUtils.isNotBlank((String)parameters.get("registCode"))) {
            sql.append(" and prod.regist_code like '%$U{registCode}%'");
        }
        if (StringUtils.isNotEmpty((String)parameters.get("isHave"))) {
            if("0".equals(parameters.get("isHave"))){
                sql.append(" and prod.regist_code =''");
            }else{
                sql.append(" and prod.regist_code !=''");
            }
        }
        return SqlUtils.sqlPackage(sql.toString(), DataSourceProperty.PUB, parameters, this);
    }
    //产品状态调整页面产品信息查询，仅查询状态为成立前与已终止的产品  rennannan 20210201
    public SqlResult<Map<String, Object>> findAdjustProdList(Map<String, Object> parameters) throws Exception {
        StringBuffer sql= new StringBuffer("   select t.prod_code," +
                "             t.prod_name, " +
                "             t.prod_status," +
                "             t.prod_son_status," +
                "             t.id," +
                "             t.is_recycle_code" +
                "        from t8_prod_info t " +
                "       where t.prod_status in('1','2','3','4','5','9')");  //仅查询状态为1创设2上会3申报4参数已确认5发行的产品信息9已终止
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                sql.append(" and (t.is_recycle_code != '1' or t.is_recycle_code is null )");
            }else{
                sql.append(" and t.is_recycle_code ='"+parameters.get("isRecycleCode")+"'");
            }
        }else{
            sql.append(" and (t.is_recycle_code != '1' or t.is_recycle_code is null )");
        }
        if (StringUtils.isNotBlank((String) parameters.get("prodName"))) {
            sql.append(" and t.prod_name like '%" + parameters.get("prodName") + "%' ");
        }
        if(StringUtils.isNotBlank((String) parameters.get("prodCode"))){
            sql.append(" and t.prod_code='"+parameters.get("prodCode")+"'");
        }
        return SqlUtils.sqlPackage(sql.toString(), DataSourceProperty.PUB, parameters, this);
    }
    //申报登记结果维护 rennannan 20210129
    public int updateT8ProdRegisCodeAndStatus(Map<String, Object> parameters) throws Exception{
        String sql="update t8_prod_info " +
                "    set  prod_son_status='"+parameters.get("prodSonStatus")+"'," +
                "         regist_code = '"+parameters.get("registCode")+"' "+
                "    where prod_code = '"+parameters.get("prodCode")+"'";

        return super.update(sql).getEffect();
    }

    public List<Map<String, Object>> findT8ProdRegis(Map<String, Object> parameters) throws Exception {

        String sql = "select regist_code from  t8_prod_info where 1=1 ";
        if(StringUtils.isNotBlank((String)parameters.get("registCode"))){
            sql = sql +" and regist_code = $S{registCode}";
        }
        if(StringUtils.isNotBlank((String)parameters.get("prodCode"))){
            sql = sql +" and prod_code != $S{prodCode}";
        }

        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this).getRows();
    }

    /**
     * 功能：查询流程是否开启
     * 作者：rennannan
     * 日期：20210331
     *
     * @return
     */
    public int getProdProcessOpen(SqlParam<T8ProdInfo> params) throws Exception {
        String sql = "select count(1) cou from t8_prod_task_nodes_info where prod_code = $S{prodCode}";
        return super.findRow(sql, params.getModel()).getInteger("cou");
    }



    //产品状态调整 rennannan 20210129
    public int updateT8ProdInfoStatus(T8ProdInfo params) throws Exception {
        String sql = "update t8_prod_info " +
                "   set prod_status =$S{prodStatus}, " +
                "      prod_son_status=$S{prodSonStatus} ";
        if("1".equals(params.getIsRecycleCode())){
            sql = sql +",is_recycle_code='1' ";
        }

        sql = sql + "    where prod_code = $S{prodCode}";
        return super.update(sql, params).getEffect();
    }

    /**
     * 功能：根据id修改产品状态
     * 作者：rennannan
     * 日期：20210805
     *
     * @param params
     * @return
     * @throws Exception
     */
    public int updateProdStatusById(T8ProdInfo params) throws Exception {
        String sql = "update t8_prod_info " +
                "   set prod_status =$S{prodStatus}, " +
                "      prod_son_status=$S{prodSonStatus} " +
                "    where id = $S{id}";
        return super.update(sql, params).getEffect();
    }

    /**
     * 功能：一次报备确认
     * 作者：rennannan
     * 日期：20210316
     *
     * @param params
     * @return
     * @throws Exception
     */
    public int updateStatusAndDate(T8ProdInfo params) throws Exception {
        String sql = "update t8_prod_info " +
                "      set prod_status =$S{prodStatus}, " +
                "          prod_son_status=$S{prodSonStatus}," +
                "        apply_regist_date=$S{applyRegistDate}," +
                "        filing_status='2'" +
                "    where prod_code = $S{prodCode}";
        return super.update(sql,params).getEffect();
    }
    /**
     * 功能：二次报备确认
     * 作者：rennannan
     * 日期：20210316
     * @return
     * @throws Exception
     */
    public int updateStatusAndIssueDate(Map<String,Object> map)throws Exception{
        String sql="update t8_prod_info " +
                "      set prod_status =$S{prodStatus}, " +
                "          prod_son_status=$S{prodSonStatus}," +
                "        issue_regist_date=$S{issueRegistDate}" +
                "    where prod_code = $S{prodCode}";
        return super.update(sql,map).getEffect();
    }

    //修改其他报备材料确认状态
    public int updateT8ProdInfoStatusForOther(T8ProdInfo params) throws Exception{
        String sql="update t8_prod_info " +
                "   set other_filing_materials_status ='2' " +
                "    where prod_code = $S{prodCode}";
        return super.update(sql,params).getEffect();
    }

    //cha产品文档，
    public T8ProdInfo getProdInfoByProdCode(String prodCode) throws Exception {
        SqlRow sqlRow = this.findRow("select t.id,t.prod_name, " +
                "       t.prod_code, " +
                "       t.raise_type, " +
                "       t.prod_mode, " +
                "       t.prod_doc_mods, " + "" +
                "       t.is_share_sort " +
                "from t8_prod_info t " +
                "where t.prod_code = $S{prodCode}", prodCode);
        T8ProdInfo t8ProdInfo = new T8ProdInfo();
        t8ProdInfo.setId(sqlRow.getString("id"));
        t8ProdInfo.setProdName(sqlRow.getString("prod_name"));
        t8ProdInfo.setProdCode(sqlRow.getString("prod_code"));
        t8ProdInfo.setRaiseType(sqlRow.getString("raise_type"));
        t8ProdInfo.setProdMode(sqlRow.getString("prod_mode"));
        t8ProdInfo.setProdDocMods(sqlRow.getString("prod_doc_mods"));
        t8ProdInfo.setIsShareSort(sqlRow.getString("is_share_sort"));
        return t8ProdInfo;
    }

    public T8ProdInfo getProdInfoByCode(String prodCode) throws Exception {
        SqlRow sqlRow = this.findRow("select t.PROD_NM prod_name, " +
                "       t.PROD_CD prod_code, " +
                "       t.PROD_CLC_MTH raise_type, " +
                "       t.PROD_FORM prod_mode " +

                "from app_prd_bas_inf t " +
                "where t.PROD_CD = $S{prodCode}", prodCode);
        T8ProdInfo t8ProdInfo = new T8ProdInfo();
        t8ProdInfo.setProdName(sqlRow.getString("prod_name"));
        t8ProdInfo.setProdCode(sqlRow.getString("prod_code"));
        t8ProdInfo.setRaiseType(sqlRow.getString("raise_type"));
        t8ProdInfo.setProdMode(sqlRow.getString("prod_mode"));

        return t8ProdInfo;
    }

    //说明书
    public SqlResult<Map<String, Object>> findProdManualListByProdCodeOrProdName(SqlParam<T8ProdInfo> params) throws Exception {
        String sql = "\t\tselect t.prod_code,t.id,t.prod_name,  \n" +
                "\t\t\t\t\t t2.establish_date,t2.end_date,t.prod_status,  \n" +
                "\t\t\t\t\t IFNULL(t3.doc_type,(case when t.prod_mode='1' and t.raise_type='01' then '10001' \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhen t.prod_mode='1' and t.raise_type='02' then '20001'  \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhen t.prod_mode='2' and t.raise_type='01' then '50001' \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhen t.prod_mode='2' and t.raise_type='02' then '60001' \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhen t.prod_mode='3' and t.raise_type='01' then '30001' \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhen t.prod_mode='3' and t.raise_type='02' then '40001' \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhen t.prod_mode='4'  then '70001' \n" +
                "\t\t\t\t\t                          else '' end) ) as document_type,  \n" +
                "\t\t\t\t\t IFNULL(t3.doc_type,0) has_template \n" +
                "\t\t\tfrom t8_prod_info t  \n" +
                " left join t8_prod_calendar t2 on t.prod_code = t2.prod_code  \n" +
                " left join t8_prod_doc_info t3 on t.id = t3.t8_prod_info_id  \n" +
                "      and t3.doc_type in ('10001', '20001', '30001', '40001', '50001', '60001', '70001')" +
                "where 1=1 ";
        if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
            sql = sql + " and t.prod_code = $S{prodCode}";
        }
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
    }


    public SqlResult<SqlRow> getProdNameByProdCode(SqlParam<T8ProdInfo> params) throws Exception {
        Map<String, Object> objectMap = params.getParams();
        SqlResult<SqlRow>  sqlResult = new SqlResult<>();
        List<SqlRow> sqlRowList = super.findRows("select t.prod_mode prodMode,t.prod_name prodName,t.id, t3.base_type baseType from t8_prod_info t left join t8_prod_performance t3 on t3.prod_code = t.prod_code where t.prod_code=$S{prodCode}", objectMap);
        sqlResult.setRows(sqlRowList);
        sqlResult.setResults(sqlRowList.size());
        sqlResult.setDesensitized(false);
        return sqlResult;
    }


    public SqlResult<Map<String, Object>> findProdListByProdCodeOrProdName(SqlParam<T8ProdInfo> params) throws Exception {
        Map<String, Object> objectMap = params.getParams();
        String sql = "select t.prod_code       prodCode, " +
                "       t.prod_name       prodName, " +
                "       t2.establish_date establishDate, " +
                "       t2.end_date       endDate, " +
                "       t.prod_status     prodStatus, " +
                "       t4.temp_type      documentType " +
                "from t8_prod_info t " +
                "         left join t8_prod_calendar t2 " +
                "                   on t.prod_code = t2.prod_code " +
                "         left join t8_print_temp_version t3 " +
                "                   on find_in_set(t3.id, t.prod_doc_mods) " +
                "         left join t8_print_temp t4 " +
                "                   on t3.t8_print_temp_id = t4.id " +
                "where 1 = 1";
        if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
            sql = sql + " and t.prod_code like '%" + params.getModel().getProdCode() + "%' ";
        }
        if (StringUtils.isNotEmpty(params.getModel().getProdName())) {
            sql = sql + " and t.prod_name like '%" + params.getModel().getProdName() + "%' ";
        }
        SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, objectMap, this);
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, objectMap, this);
    }


    public SqlResult<Map<String, Object>> findProdListByProdCodeOrProdName2(SqlParam<T8ProdInfo> params) throws Exception {
        Map<String, Object> objectMap = params.getParams();
        String sql = "SELECT\n" +
        		 "\tt.id t8ProdInfoId,\n" +
                "\tt.prod_code prodCode,\n" +
                "\tt.prod_name prodName,\n" +
                "\tt.prod_status prodStatus,\n" +
                "\tt4.temp_type documentType, \n" +
                "\tt4.doc_type \tdocType, \n" +
                "\tt4.id tempId \n" +
                "FROM\n" +
                "\tt8_prod_info t\n" +
                "\tLEFT JOIN t8_print_temp t4 ON find_in_set( t4.id, t.prod_doc_mods ) \n" +
                "WHERE\n" +
                "\t1 = 1";
        if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
            sql = sql + " and t.prod_code ='" + params.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotEmpty(params.getModel().getProdName())) {
            sql = sql + " and t.prod_name like '%" + params.getModel().getProdName() + "%' ";
        }
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, objectMap, this);
    }


    //GD   产品基本信息
    //创意
    public SqlResult<T8ProdInfo> getOriginality(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("SELECT id originality_id, originality_name FROM t8_prod_creative_project where status ='1'", params);
    }

    //新增产品基础信息
    public int addProdInfo(SqlParam<T8ProdInfo> params) throws Exception {

        return super.update(
                "insert into t8_prod_info (id,prod_mode_id,prod_code,prod_name,prod_name_short,prod_name_es,prod_series,regist_code,prod_mode,sale_type,prod_cur,bnote_remit_flag,prod_risk_level,prod_status, " +
                        "is_originality,originality_id,prod_classify,prod_doc_mods,approval_status,faceprice,netprice,pgmno,crt_date,crt_time,crt_user)  " +
                        "VALUES ($AUTOIDS{id},$S{prodModeId},$S{prodCode},$S{prodName},$S{prodNameShort},$S{prodNameEs},$S{prodSeries},$S{registCode},$S{prodMode},$S{saleType},$S{prodCur},$S{bnoteRemitFlag},$S{prodRiskLevel},$S{prodStatus}, " +
                        "$S{isOriginality},$S{originalityId},$S{prodClassify},$S{prodDocMods},$S{approvalStatus},$S{faceprice},$S{netprice},$S{pgmno},$S{crtDate},$S{crtTime},$S{crtUser})",
                params.getModel()).getEffect();
    }

    //修改产品信息
    public int updateProdInfo(SqlParam<T8ProdInfo> params) throws Exception {

        return super.update(
                "UPDATE T8_PROD_INFO SET prod_mode_id=$S{prodModeId} ,prod_code=$S{prodCode} ,prod_name=$S{prodName} ,prod_name_short=$S{prodNameShort} ," +
                        "regist_code=$S{registCode} , prod_mode=$S{prodMode} ,sale_type=$S{saleType} ," +
                        "prod_cur=$S{prodCur} ,bnote_remit_flag=$S{bnoteRemitFlag} ,prod_risk_level=$S{prodRiskLevel} , " +
                        "prod_status=$S{prodStatus} ,is_originality=$S{isOriginality} ,originality_id=$S{originalityId} " +
                        ",prod_classify=$S{prodClassify} ,prod_doc_mods=$S{prodDocMods} , approval_status=$S{approvalStatus} ," +
                        "faceprice=$S{faceprice} ,netprice=$S{netprice}, raise_type=$S{raiseType} ," +
                        "is_quota_limit=$S{isQuotaLimit} ,quota_limit_up=$S{quotaLimitUp} ,expe_scale=$S{expeScale} ," +
                        "min_scale=$S{minScale}, max_scale=$S{maxScale} ,control_sign=$S{controlSign} ," +
                        "is_realtime_re=$S{isRealtimeRe} ,work_time_start=$S{workTimeStart} ,work_time_end=$S{workTimeEnd} , " +
                        "realtime_pledge_ratio=$S{realtimePledgeRatio} ,realtime_fee=$S{realtimeFee} ,perf_method_max=$S{perfMethodMax} ," +
                        "perf_method_min=$S{perfMethodMin} ,approval_status=$S{approvalStatus}, pgmno = $S{pgmno}, " +
                        "upt_date=$S{uptDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser} " +
                        "where id=$S{id}",
                params.getModel()).getEffect();
    }


    //新增募集信息
    public int addProdRaise(SqlParam<T8ProdInfo> params) throws Exception {

        return super.update(
                "UPDATE T8_PROD_INFO SET  " +
                        "raise_type=$S{raiseType} ,is_quota_limit=$S{isQuotaLimit} ,quota_limit_up=$S{quotaLimitUp} ,expe_scale=$S{expeScale} ,min_scale=$S{minScale}, " +
                        "max_scale=$S{maxScale} ,control_sign=$S{controlSign} ,is_realtime_re=$S{isRealtimeRe} ,work_time_start=$S{workTimeStart} ,work_time_end=$S{workTimeEnd} , " +
                        "realtime_pledge_ratio=$S{realtimePledgeRatio} ,realtime_fee=$S{realtimeFee} ,perf_method_max=$S{perfMethodMax} ,perf_method_min=$S{perfMethodMin} ,approval_status=$S{approvalStatus} , " +
                        "upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser} " +
                        "where PROD_CODE=$S{prodCode} ",
                params.getModel()).getEffect();
    }



    public SqlResult<Map<String, Object>> findEscrowAgreementByProdName(SqlParam<T8ProdInfo> params) throws Exception {
        String sql = "select t.prod_code       prodCode, " +
                "       t.prod_name       prodName, " +
                "       t.prod_status     prodStatus, " +
                "       t4.temp_type      documentType " +
                "from t8_prod_info t " +
                "         left join t8_prod_calendar t2 " +
                "                   on t.prod_code = t2.prod_code " +
                "         left join t8_print_temp_version t3 " +
                "                   on find_in_set(t3.id, t.prod_doc_mods) " +
                "         left join t8_print_temp t4 " +
                "                   on t3.t8_print_temp_id = t4.id " +
                "where t4.temp_type in ('10002','20002','30002','40002','50002','60002','70002') ";
        if (StringUtils.isNotEmpty(params.getModel().getProdName())) {
            sql = sql + " and t.prod_name like '%" + params.getModel().getProdName() + "%' ";
        }
        SqlResult<Map<String, Object>> mapSqlResult = SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
        return mapSqlResult;
    }



    public SqlResult<Map<String, Object>> findDocInfo(Map<String, Object> parameters) throws Exception {

    	String sql = "SELECT*FROM (SELECT IF (count(t.version)> 0,'1','0') AS is_have,temp.*FROM ( "
                 + "SELECT prod.prod_code,prod.prod_name,prod.id t8_prod_info_id,doc.id,prod.is_recycle_code,IFNULL(doc.doc_type,'10103') AS document_type,doc.t8_print_temp_version_id," +
                     "doc.doc_desc,dis.DISTRIBUTOR_CODE,dis.DISTRIBUTOR_NAME,IFNULL(doc.doc_type,0) has_template FROM t8_prod_info prod LEFT JOIN t8_prod_calendar c " +
                     "ON prod.prod_code=c.prod_code JOIN t8_prod_sale sale ON prod.id=sale.t8_prod_info_id JOIN t8_distributor_info dis ON " +
                     "FIND_IN_SET(dis.DISTRIBUTOR_CODE,sale.distributor_code) LEFT JOIN t8_prod_doc_info doc ON prod.id=doc.t8_prod_info_id " +
                     "AND dis.DISTRIBUTOR_CODE=doc.DISTRIBUTOR_CODE AND doc.doc_type IN ('10003','20003','30003','40003','50003','60003','70003','10103') WHERE 1=1 ";
        StringBuilder builder = new StringBuilder(sql);
        if (StringUtils.isNotBlank((String) parameters.get("prodCode"))) {
            builder.append(" and prod.prod_code = $S{prodCode}");
        }
        if (StringUtils.isNotBlank((String) parameters.get("prodName"))) {
            builder.append(" and prod.prod_name like '%" + parameters.get("prodName") + "%'");
        }
        if (StringUtils.isNotBlank((String) parameters.get("distributorCode"))) {
            builder.append(" and dis.distributor_code = $S{distributorCode}");
        }
        if (StringUtils.isNotBlank((String) parameters.get("queryStartDate"))) {
            builder.append(" and c.establish_date >= $S{queryStartDate}");
        }
        if(StringUtils.isNotBlank((String)parameters.get("queryEndDate"))){
            builder.append(" and c.establish_date <= $S{queryEndDate}");
        }
        builder.append(") temp LEFT JOIN t8_prod_document_version t ON temp.prod_code=t.prod_code AND (temp.document_type=t.document_type or t.document_type='10103') \n"
                + "\tLEFT JOIN sys_dict_item t1 ON t1.dict = 't8_temp_type' \n"
                + "\tAND t1.itemkey = t.document_type\n"
                + "\tLEFT JOIN t8_distributor_info t2 ON t2.distributor_code = t.distributor_code\n"
                + "\tLEFT JOIN t8_trutee_info t3 ON t3.id = t.t8_trutee_info_id\n"
                + "\tgroup by temp.prod_code)temp6 where 1=1 ");
        if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
            if("0".equals(parameters.get("isRecycleCode"))){
                builder.append(" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null )");
            }else{
                builder.append(" and temp6.is_recycle_code ='"+parameters.get("isRecycleCode")+"'");
            }
        }else{
            builder.append(" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null )");
        }
        if (StringUtils.isNotBlank((String) parameters.get("isHave"))) {
            if("0".equals(parameters.get("isHave"))){
                builder.append("and temp6.is_have='0'");
            }else{
                builder.append("and temp6.is_have='1'");
            }
        }
        return SqlUtils.sqlPackage(builder.toString(), DataSourceProperty.PUB, parameters, this);
    }

    public SqlRow findProdInfo(Map<String, Object> params) throws Exception  {

        return super.findRow("SELECT p.prod_name as prodName,p.prod_code as internalIdentCode,p.income_type as 'revenueType',"
                + "	case p.prod_mode "
                + "	when '1' then '01' "
                + "	else '03' "
                + " end AS 'productOperationMode',"
                + "case p.prod_mode "
                + "	when '4' then '01' "
                + "	else '02' "
                + " end as 'isCashManagement',"
                + "p.raise_type AS 'prodRaiseMethod' ,"
                + "p.prod_classify as 'prodInvestNature',"
                + "p.prod_cur as 'raisedCurrency',"
                + "p.prod_cur as 'currencyCashIncome',"
                + "p.prod_risk_level as'investorRiskPreference',"
                + "p.prod_risk_level as 'prodRiskLevel',"
                + "p.prod_brand as 'prodBrand',"
                + "c.apply_start_date as 'raiseDateStart',"
                + "c.apply_end_date as 'raiseDateend',"
                + "c.product_term as 'prodTerm',"
                + "f.base_rate as 'performanceBenchmark',"
                + "s.max_raise_amt as'planFundRaiseAmount',"
                + "case  s.prod_sale_custom "
                + "when '3' then '01' "
                + "else '02' "
                + "end 'isFinancialIndustry',"
                + "t.t8_trutee_info_id ,"
                + "t.t8_out_trutee_info_id "
                + "FROM t8_prod_info p "
                + "LEFT JOIN t8_prod_calendar c "
                + "	on p.id = c.t8_prod_info_id "

                +"LEFT JOIN t8_prod_sale s"
                + "	on p .id = s.t8_prod_info_id "
                +"LEFT JOIN t8_prod_performance f "
                + "	on p .id =f.t8_prod_Info_id "
                +"LEFT JOIN t8_prod_trutee_bank t "
                + " on p.id = t.t8_prod_info_id "
                + "where p.id= $S{prodId}", params);
    }

    public int updateRiskScore(SqlParam<T8ProdInfo> param) throws Exception {
        return super.update("update t8_prod_info set prod_risk_level = $S{prodRiskLevel} where prod_code = $S{prodCode}", param.getModel()).getEffect();
    }


    public SqlResult<T8ProdInfo> getProdInfos(SqlParam<T8ProdInfo> param) throws Exception {
        T8ProdInfo t8ProdInfo = param.getModel();
        if(t8ProdInfo!=null&&t8ProdInfo.getProdCode()!=null) {
            return super.findRows("SELECT p.id,p.prod_code,p.prod_name,c.establish_date from t8_prod_info p LEFT JOIN t8_prod_calendar c on p.id= c.t8_prod_info_id where p.prod_code = $S{prodCode}", param);
        }else {
            return super.findRows("SELECT p.id,p.prod_code,p.prod_name,c.establish_date from t8_prod_info p LEFT JOIN t8_prod_calendar c on p.id= c.t8_prod_info_id ", param);
        }

    }

    public SqlResult<T8ProdInfo> getProdInfosZG(SqlParam<T8ProdInfo> param) throws Exception {
        T8ProdInfo t8ProdInfo = param.getModel();
        if(t8ProdInfo!=null&&t8ProdInfo.getProdCode()!=null) {
            return super.findRows("SELECT p.prod_cd prod_code,p.prod_nm prod_name from APP_PRD_BAS_INF p where p.prod_cd = $S{prodCode}", DataSourceProperty.PUB, param);
        }else {
            return super.findRows("SELECT p.prod_cd prod_code,p.prod_nm prod_name from APP_PRD_BAS_INF p ",  DataSourceProperty.PUB,param);
        }

    }


    public SqlResult<T8ProdInfo> getProdInfosAndCode(SqlParam<T8ProdInfo> param) throws Exception {
        T8ProdInfo t8ProdInfo = param.getModel();
        if(t8ProdInfo!=null&&t8ProdInfo.getProdCode()!=null) {
            return super.findRows("SELECT  p.prod_cd prod_code ,p.prod_nm prod_name from APP_PRD_BAS_INF p  where p.prod_cd = $S{prodCode}", DataSourceProperty.PUB, param);
        }else {
            return super.findRows("SELECT  p.prod_cd prod_code ,p.prod_nm prod_name from APP_PRD_BAS_INF p ", DataSourceProperty.PUB, param);
        }

    }

    public List<SqlRow> getProd_assembly_info(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("select i.assembly_id,i.assembly_desc,y.prod_code,su.username crt_user,r.crt_date,r.crt_time \n" +
                "from t8_prod_assembly_info i \n" +
                "left join (select t.assembly_id,t.assembly_desc,a.prod_code \n" +
                "  from t8_prod_assembly_info t \n" +
                "  left join t8_prod_assembly a on a.assembly_id=t.assembly_id \n" +
                "  where a.prod_code='"+params.getParams().get("prodCode")+"' and a.menu_items_type=1) y \n" +
                "on y.assembly_id=i.assembly_id \n" +
                "left join t8_prod_progress_record r \n" +
                "on r.prod_code=y.prod_code and r.assembly_id=y.assembly_id \n" +
                "left join sys_user su on r.crt_user = su.userid\n" +
                "where i.assembly_type='1' order by i.assembly_sort");
    }

    public List<SqlRow> findProdSeries(String prodSeries) throws Exception {
        return super.findRows("select prod_series from t8_prod_info where prod_series ='"+prodSeries+"' ");
    }

    public List<SqlRow> findProdDistributorCode(String distributorCode) throws Exception {
        return super.findRows("SELECT\r\n" +
        		"	distributor_code\r\n" +
        		"FROM\r\n" +
        		"	t8_prod_sale\r\n" +
        		"WHERE\r\n" +
        		"	 FIND_IN_SET('"+distributorCode+"',distributor_code)");
    }

    //获取产品方案信息
    public SqlResult<Map<String, Object>> getCreatePlan(Map<String, Object> params) throws Exception {
        String sql = "select * from (SELECT\r\n" +
                "		t.id,\r\n" +
                "		t.prod_code,\r\n" +
                "		t.prod_name,\r\n" +
                "		t.prod_status,\r\n" +
                "		t.raise_type,\r\n" +
                "		s.series_name,\r\n" +
                " IFNULL (\n" +
                " d.doc_type,'10107') document_type,  "+
                "ifnull(d.doc_type,0) as is_show " +
                "FROM\r\n" +
                "	t8_prod_info t\r\n" +
                "LEFT JOIN ods_amng_prod_series s ON t.prod_series = s.series_code\r\n" +
                "LEFT JOIN t8_prod_doc_info d ON t.id = d.t8_prod_info_id and d.doc_type in ( '10007', '20007', '30007','40007', '50007','60007','70007','10107' ) where 1=1  \r\n";

        if (params.get("isRecycleCode") != null && params.get("isRecycleCode") != "") {
            if("0".equals(params.get("isRecycleCode"))){
                sql = sql +" and (t.is_recycle_code != '1' or t.is_recycle_code is null )";
            }else{
                sql = sql + " and t.is_recycle_code ='"+params.get("isRecycleCode")+"'";
            }
        }else{
            sql = sql +" and (t.is_recycle_code != '1' or t.is_recycle_code is null )";
        }
        if(params.get("prodCode")!=""&&params.get("prodCode")!=null){
            sql = sql + " and t.prod_code= '"+params.get("prodCode")+"'";
        }
        if (StringUtils.isNotBlank((String)params.get("prodName"))) {
                    sql += " and t.prod_name like '%" + params.get("prodName") + "%'";
        }
        //新加条件查询 系列名称
        if(params.get("seriesName")!=""&&params.get("seriesName")!=null){
            sql = sql + " and s.series_name= '"+params.get("seriesName")+"'";
        }//募集方式
        if(params.get("raiseType")!=""&&params.get("raiseType")!=null){
            sql = sql + " and t.raise_type= '"+params.get("raiseType")+"'";
        }//产品状态
        if(params.get("prodStatus")!=""&&params.get("prodStatus")!=null){
            sql = sql + " and t.prod_status= '"+params.get("prodStatus")+"'";
        }

        if("0".equals(params.get("isExistPlan"))){

            sql = sql + " )temp where temp.document_type IN('10007', '20007', '30007','40007', '50007','60007','70007','10107') " +
                        " and not exists (SELECT 1 from t8_prod_document_version tpd where tpd.prod_code = temp.prod_code and tpd.document_type =temp.document_type) ";

        } else if("1".equals(params.get("isExistPlan"))){

            sql = sql + " )temp where temp.document_type IN('10007', '20007', '30007','40007', '50007','60007','70007','10107') " +
                        " and exists (SELECT 1 from t8_prod_document_version tpd where tpd.prod_code = temp.prod_code and tpd.document_type =temp.document_type) ";

        } else{
            sql = sql + " )temp where temp.document_type IN('10007', '20007', '30007','40007', '50007','60007','70007','10107')";

        }

        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
    }

//记录表
    public SqlResult<T8ProdInfo> findT8ProdInfoHiss(SqlParam<T8ProdInfo> params) throws Exception {
        return super.findRows("SELECT id,t8_prod_adjust_id,prod_mode,prod_mode_id,prod_series,prod_code,prod_name,prod_brand,regist_code,is_originality,originality_id,prod_risk_level,prod_cur,netprice,raise_type,income_type,prod_classify,manager_code,publish_explain,prod_status,filing_status,filing_materials_status,risk_score,risk_score_status,prod_son_status,prod_doc_mods,distributor_code,approval_status,t8_prod_account_info_id,bnote_remit_flag,prod_trait,invest_direction,other_risk,crt_date,crt_time,crt_user," +
                "product_term,prod_company,t8_spare_column_one,t8_spare_column_two,t8_spare_column_three,t8_spare_column_four,t8_spare_column_five FROM t8_prod_info_his", params);
    }


    public UpdateResult addT8ProdInfoHis(T8ProdInfo params) throws Exception {
        return super.update("INSERT INTO t8_prod_info_his(id,prod_mode,product_term,prod_company,t8_spare_column_one,t8_spare_column_two,t8_spare_column_three,t8_spare_column_four,t8_spare_column_five,t8_prod_adjust_id,prod_mode_id,prod_series,prod_code,prod_name,prod_brand,regist_code,is_originality,originality_id,prod_risk_level,prod_cur,netprice,raise_type,income_type,prod_classify,manager_code,publish_explain,prod_status,filing_status,filing_materials_status,risk_score,risk_score_status,prod_son_status,prod_doc_mods,distributor_code,approval_status,t8_prod_account_info_id,bnote_remit_flag,prod_trait,invest_direction,other_risk,crt_date,crt_time,crt_user) " +
                        "VALUES($AUTOIDS{id},$S{prodMode},$S{productTerm},$S{prodCompany},$S{t8SpareColumnOne},$S{t8SpareColumnTwo},$S{t8SpareColumnThree},$S{t8SpareColumnFour},$S{t8SpareColumnFive},$S{t8ProdAdjustId},$S{prodModeId},$S{prodSeries},$S{prodCode},$S{prodName},$S{prodBrand},$S{registCode},$S{isOriginality},$S{originalityId},$S{prodRiskLevel},$S{prodCur},$S{netprice},$S{raiseType},$S{incomeType},$S{prodClassify},$S{managerCode},$S{publishExplain},$S{prodStatus},$S{filingStatus},$S{filingMaterialsStatus},$S{riskScore},$S{riskScoreStatus},$S{prodSonStatus},$S{prodDocMods},$S{distributorCode},$S{approvalStatus},$S{t8ProdAccountInfoId},$S{bnoteRemitFlag},$S{prodTrait},$S{investDirection},$S{otherRisk},$S{crtDate},$S{crtTime},$S{crtUser})",
                params);
    }




    public List<SqlRow> findDictItemOnly(String dict,String itemKey) throws Exception {
    	if(itemKey.contains(","))
    		return super.findRows("SELECT * FROM sys_dict_item WHERE dict = '"+dict+"' and itemkey in ("+itemKey+")");
    	return super.findRows("SELECT * FROM sys_dict_item WHERE dict = '"+dict+"' and itemkey= '"+itemKey+"'");
    }

    public List<SqlRow> findProdBaseInfo(String ruleId) throws Exception {
        return super.findRows("select channel_ids,disclosure_mod_version_id,notice_title from idb_disclosure_prod_rule where id ="+ruleId+"'");
    }


    public List<SqlRow> findProdDistributor(String prodCode) throws Exception{

    	return super.findRows("SELECT CONCAT(d.distributor_code,\"-\",d.distributor_name) distributor_code from t8_distributor_info  d " +
    			" LEFT JOIN t8_prod_sale s on FIND_IN_SET(d.DISTRIBUTOR_CODE,s.distributor_code) " +
    			" where s.prod_code = '"+prodCode+"'");
    }

    public List<SqlRow> findProdSubscriptionFee(String prodCode) throws Exception{
    	return super.findRows("SELECT A.fee_type_deal,A.cust_type_deal,A.charging_index_deal,A.charging_method,A.base_fee_rate,A.max_cost,A.min_cost,A.is_amt_segment FROM t8_fee_deal A " +
    			" where A.prod_code = '"+prodCode+"' and A.fee_type_deal='1'");
    }

    public List<SqlRow> findProdApplyFee(String prodCode) throws Exception{
    	return super.findRows("SELECT A.fee_type_deal fee_type_deal1,A.cust_type_deal cust_type_deal1,A.charging_index_deal charging_index_deal1,A.charging_method charging_method1,A.base_fee_rate base_fee_rate1,A.max_cost max_cost1,A.min_cost min_cost1,A.is_amt_segment is_amt_segment1 FROM t8_fee_deal A " +
    			" where A.prod_code = '"+prodCode+"' and A.fee_type_deal='2'");
    }

    public List<SqlRow> findProdRedeemFee(String prodCode) throws Exception{
    	return super.findRows("SELECT A.fee_type_deal fee_type_deal2,A.cust_type_deal cust_type_deal2,A.charging_index_deal charging_index_deal2,A.charging_method charging_method2,A.base_fee_rate base_fee_rate2,A.max_cost max_cost2,A.min_cost min_cost2,A.is_amt_segment is_amt_segment2 FROM t8_fee_deal A " +
    			" where A.prod_code = '"+prodCode+"' and A.fee_type_deal='3'");
    }


    public SqlResult<Map<String, Object>> findProdDividendInfo(SqlParam<T8ProdInfo> params) throws Exception {
        String sql = "select a.id, a.prod_code, a.prod_name, b.bonus_type,c.establish_date,c.pgmno \n" +
                "from t8_prod_info a\n" +
                "         left join t8_prod_bonus b on a.prod_code = b.prod_code " +
                " left join t8_prod_calendar c on a.id=c.t8_prod_info_id  where a.prod_code=$S{prodCode}";

        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
    }

    //查询产品台账
    public SqlResult<T8ProdStandBook> findProdStandBook(SqlParam<T8ProdStandBook> params) throws Exception {
        String sql = "SELECT p.id, s.series_name ,p.prod_code,p.prod_name,p.market_value,p.prod_classify,c.establish_date ," +
                "c.end_date,p.prod_mode,c.product_term,c.cycle_open_term,c.cycle_open_type ,p.prod_risk_level,tpi.valuation_method," +
                "p.raise_type,ts.prod_sale_custom ,ts.min_subs_person,ts.min_subs_mechanism,ts.min_subs_interbank," +
                "pf.base_rate,p.invest_direction,p.prod_trait,c.liquidate,c.liquidate_type,b.bonus_type,b.bonus_frequency," +
                "(select group_concat(sysUser.username) from  t8_prod_user users join sys_user sysUser on users.userid_a = sysUser.userid where users.t8_prod_info_id=p.id and users.role_id='14') invest_manage_name," +
                " (select group_concat(sysUser.username) from  t8_prod_user users join sys_user sysUser on users.userid_a = sysUser.userid where users.t8_prod_info_id=p.id and users.role_id='3') prod_manage_name," +
                "p.regist_code " +
                " from t8_prod_info p " +
                " LEFT JOIN t8_prod_invest tpi " +
                " on p.prod_code = tpi.prod_code " +
                " LEFT JOIN t8_prod_calendar c " +
                " on p.prod_code = c.prod_code " +
                " LEFT JOIN ods_amng_prod_series s  " +
                "        on p.prod_series = s.series_code\r\n" +
                " LEFT JOIN t8_prod_sale ts " +
                "        on p.prod_code = ts.prod_code  " +
                " LEFT JOIN t8_prod_performance pf  " +
                "        on p.prod_code = pf.prod_code  " +
                " LEFT JOIN t8_prod_bonus b " +
                "        on p.prod_code = b.prod_code " +
                " where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
            sql += " and p.prod_name like '%" + params.getModel().getProdName() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql += " and p.prod_code = $S{prodCode}";
        }
        //到期日区间
        if (StringUtils.isNotBlank(params.getModel().getExpireStartDate())) {
            sql += " and  c.end_date >= $S{expireStartDate}";
        }
        if (StringUtils.isNotBlank(params.getModel().getExpireEndDate())) {
            sql += " and  c.end_date <= $S{expireEndDate}";
        }
        //成立日区间
        if (StringUtils.isNotBlank(params.getModel().getEstablishStartDate())) {
            sql += " and  c.establish_date >= $S{establishStartDate}";
        }
        if (StringUtils.isNotBlank(params.getModel().getEstablishEndDate())) {
            sql += " and  c.establish_date <= $S{establishEndDate}";
        }
        //产品系列
        if (StringUtils.isNotBlank(params.getModel().getSeriesCode())) {
            sql += " and  s.series_code = $S{seriesCode}";
        }
        //开放类型
        if (StringUtils.isNotBlank(params.getModel().getProdMode())) {
            sql += " and  p.prod_mode = $S{prodMode}";
        }
        //产品类型
        if (StringUtils.isNotBlank(params.getModel().getProdClassify())) {
            sql += " and  p.prod_classify = $S{prodClassify}";
        }
        //销售对象
        if (StringUtils.isNotBlank(params.getModel().getProdSaleCustom())) {
            sql += " and  find_in_set($S{prodSaleCustom},ts.prod_sale_custom)";
        }
        //产品经理id
        if (StringUtils.isNotEmpty(params.getModel().getProdManageId())) {
            sql += " and exists( select 1 from t8_prod_user prodUser where prodUser.t8_prod_info_id = p.id and prodUser.role_id='3' and userid_a=$S{prodManageId})";
        }
        //投资经理id
        if (StringUtils.isNotEmpty(params.getModel().getInvestManageId())) {
            sql += " and exists( select 1 from t8_prod_user prodUser where prodUser.t8_prod_info_id = p.id and prodUser.role_id='14' and userid_a=$S{investManageId})";
        }
        //募集方式
        if (StringUtils.isNotEmpty(params.getModel().getRaiseType())) {
            sql += " and p.raise_type=$S{raiseType}";
        }
        //分红方式
        if (StringUtils.isNotEmpty(params.getModel().getBonusType())) {
            sql += " and b.bonus_type=$S{bonusType}";
        }
        //风险等级
        if (StringUtils.isNotEmpty(params.getModel().getProdRiskLevel())) {
            sql += " and p.prod_risk_level=$S{prodRiskLevel}";
        }
        //开放频率单位
        if (StringUtils.isNotEmpty(params.getModel().getCycleOpenType())) {
            sql += " and (c.cycle_open_type = $S{cycleOpenType} and p.prod_mode != '1')";
        }
        //开放频率
        if (StringUtils.isNotEmpty(params.getModel().getCycleOpenTerm())) {
            sql += " and (c.cycle_open_term=$S{cycleOpenTerm} and p.prod_mode != '1')";
        }
        //开放开始日期、结束日期
        if (StringUtils.isNotEmpty(params.getModel().getOpenStartDate()) && StringUtils.isNotEmpty(params.getModel().getOpenEndDate())) {
            sql += " and exists(select 1 from t8_prod_days days where days.prod_code = p.prod_code and days.date_type='1' and change_date>=$S{openStartDate} and change_date<=$S{openEndDate})";
        }
        if (params.getModel().getIsRecycleCode() != null && params.getModel().getIsRecycleCode() != "") {
            if ("0".equals(params.getModel().getIsRecycleCode())) {
                sql = sql + " and (p.is_recycle_code != '1' or p.is_recycle_code is null ) ";
            } else {
                sql = sql + " and p.is_recycle_code ='" + params.getModel().getIsRecycleCode() + "' ";
            }
        } else {
            sql = sql + " and (p.is_recycle_code != '1' or p.is_recycle_code is null ) ";
        }
        //产品登记编码
        if (StringUtils.isNotEmpty(params.getModel().getRegistCode())) {
            sql += " and p.regist_code=$S{registCode}";
        }
        SqlResult<T8ProdStandBook> sqlRowSqlResult = super.findRows(sql, params);
        return sqlRowSqlResult;

    }

    /**
     * 功能：根据产品代码与当前系统日期查询产品最近预约申购起始日、结束日、开放日
     * 作者：rennannan
     * 日期：20210401
     *
     * @param prodCode 产品代码   startDate开始日期  endDate结束日期
     * @return
     * @throws Exception
     */
    public SqlRow getDate(String prodCode, String startDate, String endDate) throws Exception {
        String nowDate = DateUtil.getNowDate();
        if (StringUtils.isNotEmpty(startDate)) {
            nowDate = startDate;
        }
        StringBuilder sql = new StringBuilder("select prod.prod_code,days.change_date openDate,days.sub_start_date subStartDate,days.sub_end_date subEndDate \n" +
                "\t\t   from t8_prod_info prod\n" +
                "  left join t8_prod_days days\n" +
                " on prod.prod_code = days.prod_code\n" +
                "\t\t\t where prod.prod_code = '" + prodCode + "'\n" +
                "\t\t\t   and DATEDIFF(days.change_date,'" + nowDate + "')>=0");
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append(" and days.change_date<='" + endDate + "'");
        }
        sql.append("\t\t\t order by days.change_date\n" +
                "\t\t\t limit 1");
        return super.findRow(sql.toString(), prodCode);

    }

    public int updateStandBook(SqlParam<T8ProdStandBook> param) throws Exception {
        return super.update("update t8_prod_info set prod_trait = $S{prodTrait} ,invest_direction = $S{investDirection} ,market_value = $S{marketValue}"
                + "  where  prod_code = $S{prodCode}", param.getModel()).getEffect();
    }

    public String getProdMode(String prodCode) throws Exception {

    	List<SqlRow> list = super.findRows("SELECT  case r.open_mod   when '01' then \"固定周期\" when '02'  then \"不定期开放\" end  as open_mod  from t8_prod_issue_register r where r.prod_code = '"+prodCode+"'");
    	if(!CollectionUtils.isEmpty(list))
    		return list.get(0).getString("open_mod");
    	return null;
    }

    /**
     * 产品排期所需数据查询
     */
    public SqlRow prodScheduleInfo(String prodCode) throws Exception {
        String sql = "select a.id,a.prod_son_status,a.prod_code,a.prod_mode,b.distributor_code, c.apply_start_date,c.apply_end_date from t8_prod_info a left join t8_prod_sale b on a.prod_code = b.prod_code left join t8_prod_calendar c on a.prod_code = c.prod_code where a.prod_code = '" + prodCode + "'";
        return super.findRow(sql, prodCode);

    }

    public SqlResult<T8ProdInfo> getIdExcludeOtherProdRuleId(Map params) throws Exception {
        //查询信披规则表中设置的个性化产品
        List<SqlRow> rows = super.findRows("select t8_prod_info_ids id from idb_disclosure_rule where disclosure_type = $S{disclosureType} and disclosure_son_type = $S{disclosureSonType} and channel_apply_type = '1'", params);
       //保存已经设定过单产品规则的产品
        HashSet<String> idSet = new HashSet<>();
        for (int i= 0; i < rows.size(); i++) {
            String ids = rows.get(i).getString("id");
            if (StringUtils.isNotEmpty(ids)) {
                String[] split = ids.split(",");
                for (int j = 0; j < split.length; j++) {
                    if (StringUtils.isNotEmpty(split[j])) {
                        idSet.add(split[j]);
                    }
                }
            }
        }

        //得到当期选择的产品
        String currentProdIds = (String)params.get("currentProdIds");
        if (StringUtils.isNotBlank(currentProdIds)) {
          String[] split = currentProdIds.split(",");
          if (split != null && split.length > 0) {
              //修改信披规则时,需要过滤当前回显的产品
              for (int k = 0; k < split.length; k++) {
                  idSet.remove(split[k]);
              }
          }
        }
        //去除已经存在的产品规则
        String sqlStr = "select t8_prod_info_id id from idb_disclosure_prod_rule where disclosure_type = $S{disclosureType} and disclosure_son_type = $S{disclosureSonType} and channel_apply_type = '1' ";
        if(!Strings.isNullOrEmpty(currentProdIds)) {
        	sqlStr =" select t8_prod_info_id id from idb_disclosure_prod_rule where disclosure_type = $S{disclosureType} and disclosure_son_type = $S{disclosureSonType} and channel_apply_type = '1' and t8_prod_info_id not in ("+currentProdIds+")";
        }
        List<SqlRow> prodRules = super.findRows( sqlStr, params);
        prodRules.forEach((prodrule)->{ idSet.add(prodrule.getString("id"));});
        String join = StringUtils.join(idSet, ',');
        params.put("id", join);
        FetcherData<T8ProdInfo> fetcherData = new FetcherData<>(params, T8ProdInfo.class);
        StringBuilder sql = new StringBuilder("SELECT a.id,a.prod_code ,a.prod_name  from t8_prod_info a where 1=1 ");
        //排除已经设定过单产品规则的产品
        if (StringUtils.isNotBlank(join)) {
            sql.append(" and a.id not in ($U{id})");
        }
        return super.findRows(sql.toString(), fetcherData);
    }

    /**
     * 功能：渠道规则查询未被选中过的产品
     * 作者：rennannan
     * 日期：20210611
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8ProdInfo> getProdByExistsProdIds(Map params) throws Exception {
        //查询渠道规则表中的选中的产品
        List<SqlRow> rows = super.findRows("select t8_prod_info_ids from idb_disclosure_channel_rule  where disclosure_type =$S{disclosureType} and disclosure_son_type =$S{disclosureSonType} and t8_prod_info_ids!= ''", params);
        //保存已经设定过单产品规则的产品
        HashSet<String> idSet = new HashSet<>();
        for (int i = 0; i < rows.size(); i++) {
            String ids = rows.get(i).getString("t8_prod_info_ids");
            String[] split = ids.split(",");
            for (int j = 0; j < split.length; j++) {
                idSet.add(split[j]);
            }
        }

        //得到当期选择的产品
        String currentProdIds = (String) params.get("currentProdIds");

        if (StringUtils.isNotBlank(currentProdIds)) {
            String[] split = currentProdIds.split(",");
            if (split != null && split.length > 0) {
                //修改渠道规则时,需要过滤当前选中的产品
                for (int k = 0; k < split.length; k++) {
                    idSet.remove(split[k]);
                }
            }
        }
        String join = StringUtils.join(idSet, ',');
        params.put("id", join);
        FetcherData<T8ProdInfo> fetcherData = new FetcherData<>(params, T8ProdInfo.class);
        String sql = "select prod.id,prod.prod_code,prod.prod_name from t8_prod_info prod where 1=1  ";
        //排除已经设定过单产品规则的产品
        if (StringUtils.isNotBlank(join)) {
            sql += " and prod.id not in ($U{id})";
        }
        return super.findRows(sql, fetcherData);
    }

    public SqlRow findProdInfoById(String id) throws Exception  {

        return super.findRow(" select prod.id,prod.prod_code,prod.prod_name from t8_prod_info prod  where id = $S{id}", id);
    }

    public SqlResult<Map<String, Object>> getSeriesName(Map<String, Object> params) throws Exception {
        String sql = "select * from (SELECT\r\n" +
                "		t.id,\r\n" +
                "		t.prod_code,\r\n" +
                "		t.prod_name,\r\n" +
                "		t.prod_status,\r\n" +
                "		t.raise_type,\r\n" +
                "		s.series_name,\r\n" +
                " IFNULL (\n" +
                " d.doc_type,(\n" +
                " CASE\n" +
                "\n" +
                " WHEN t.prod_mode = '1' \n" +
                " AND t.raise_type = '01' THEN\n" +
                " '10007' \n" +
                " WHEN t.prod_mode = '1' \n" +
                " AND t.raise_type = '02' THEN\n" +
                " '20007' \n" +
                " WHEN t.prod_mode = '2' \n" +
                " AND t.raise_type = '01' THEN\n" +
                " '50007' \n" +
                " WHEN t.prod_mode = '2' \n" +
                " AND t.raise_type = '02' THEN\n" +
                " '60007' \n" +
                " WHEN t.prod_mode = '3' \n" +
                " AND t.raise_type = '01' THEN\n" +
                " '30007' \n" +
                " WHEN t.prod_mode = '3' \n" +
                " AND t.raise_type = '02' THEN\n" +
                "\t'40007' \n" +
                "\tWHEN t.prod_mode = '4' THEN\n" +
                "\t'70007' ELSE '' \n" +
                "END \n" +
                ")) document_type,  "+
                "ifnull(d.doc_type,0) as is_show " +
                "FROM\r\n" +
                "	t8_prod_info t\r\n" +
                "LEFT JOIN ods_amng_prod_series s ON t.prod_series = s.series_code\r\n" +
                "LEFT JOIN t8_prod_doc_info d ON t.id = d.t8_prod_info_id and d.doc_type in ( '10007', '20007', '30007','40007', '50007','60007','70007' ) where 1=1  \r\n";
        if(params.get("prodCode")!=""&&params.get("prodCode")!=null){
            sql = sql + " and t.prod_code= '"+params.get("prodCode")+"'";
        }//新加条件查询 系列名称
        if(params.get("seriesName")!=""&&params.get("seriesName")!=null){
            sql = sql + " and s.series_name= '"+params.get("seriesName")+"'";
        }//募集方式
        if(params.get("raiseType")!=""&&params.get("raiseType")!=null){
            sql = sql + " and t.raise_type= '"+params.get("raiseType")+"'";
        }//产品状态
        if(params.get("prodStatus")!=""&&params.get("prodStatus")!=null){
            sql = sql + " and t.prod_status= '"+params.get("prodStatus")+"'";
        }
        sql = sql + " )temp where temp.document_type IN('10007', '20007', '30007','40007', '50007','60007','70007') GROUP BY series_name";

        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
    }

    //查询数据库字段拼接sql
    public List<SqlRow> findDataByTableName(T8ProdSync t8ProdSync) throws Exception {
        return super.findRows("select "+t8ProdSync.getFieldName()+" from "+t8ProdSync.getTableName()+"");
    }

    //通过username获取用户所属角色表
    public List<SqlRow> getUserRoleInfo(String username) throws Exception {
        String sql = "select su.username,sur.userid,sur.roleid from sys_user su join sys_user_role sur on su.userid=sur.userid where su.userid='"+username+"'";
        return findRows(sql);
    }

    //通过产品代码查询创建人,产品id,创建人姓名
    public SqlRow findProdInfoByCode(String prodCode) throws Exception  {
        return super.findRow("select tpi.id,tpi.prod_code,tpi.prod_name,su.userid,su.username from t8_prod_info tpi left join sys_user su on tpi.crt_user=su.userid where tpi.prod_code = '"+prodCode+"'", prodCode);
    }

	public SqlRow findProdDoc(String prodCode,String documentType) throws Exception {

		return super.findRow("select count(1) count from t8_prod_document_version where prod_code = '"+prodCode+"' and document_type in ("+documentType+")",null);
	}

	public T8ProdInfo queryProdInfoByCode(String prodCode)throws Exception {

		return super.findRow(T8ProdInfo.class, "select id , prod_mode from t8_prod_info where prod_code ='"+prodCode+"'", 0, null);

	}

    /**
     * 通过产品代码判断产品是否存在
     * @param prodCode
     * @return
     * @throws Exception
     */
	public SqlRow isExistProd(String prodCode) throws Exception {
        return super.findRow("select id from t8_prod_info where prod_code = $S{prodCode}", prodCode);
    }
}

