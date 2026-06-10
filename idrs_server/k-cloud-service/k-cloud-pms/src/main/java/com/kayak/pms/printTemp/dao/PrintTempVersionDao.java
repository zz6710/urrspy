package com.kayak.pms.printTemp.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.T81.model.T8ProdInfo;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 文档模板版本Dao
 * @author: WangZhenXin
 * @create: 2020-12-28 09:45
 * @memo 备注信息
 */
@Repository
public class PrintTempVersionDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempVersionDao.class);


    public String savePrintTempVersion(PrintTempVersion printTempVersion) throws Exception {
        return super.update("insert into t8_print_temp_version(id, t8_print_temp_id, version, temp_name, temp_html, remark, create_date, create_user_id, create_user_name, status,process_instance_id,risk_num,create_time) " +
                "VALUES($AUTOIDS{t8_print_temp_version},$S{t8PrintTempId},$S{version},$S{tempName},$S{tempHtml}, $S{remark}, $S{createDate}, $S{createUserId}, $S{createUserName}, '0',$S{processInstanceId},$S{riskNum},$S{createTime})", printTempVersion).getAutoId();
    }

    public SqlResult<PrintTempVersion> getPrintTempVersionByTempId(SqlParam<PrintTempVersion> param) throws Exception {
        param.setMakeSql(true);
        return super.findRows("select t.id,t.risk_num,t.t8_print_temp_id,t.temp_name,t.version,t.remark,t.create_date,t.create_time,t.create_user_name,t.status,t.expiration_date,t.effective_date \n" +
                "from t8_print_temp_version t \n" +
                "left join wf_process_instance t2 on t.process_instance_id = t2.id\n" +
                "where (t.process_instance_id is null or t.process_instance_id = '' \n" +
                "or t2.running_status = '9') order by substring(t.version,locate('.',t.version)+1)+0 desc", param);
    }

    public List<SqlRow> getNewestPrintTempVersion(String t8PrintTempId) throws Exception {
        return super.findRows("select t.version from t8_print_temp_version t where t.id in (select max(CONVERT(p.id,SIGNED))  id from t8_print_temp_version p where p.t8_print_temp_id = $S{t8PrintTempId})", t8PrintTempId);
    }

    public Integer updatePrintTempVersionStatus(String id,String effectiveDate) throws Exception {
        return super.update("update t8_print_temp_version t set t.status = '1',t.effective_date = '"+effectiveDate+"' where t.id= '"+id+"'").getEffect();
    }
    
    public Integer updatePrintTempVersion(String id,String remark) throws Exception {
        return super.update("update t8_print_temp_version t set t.remark =  '"+remark+"' where t.id= '"+id+"'").getEffect();
    }
    
    public Integer deletePrintTempVersionStatus(String id,String expirationDate) throws Exception {
        return super.update("update t8_print_temp_version t set t.status = '2',t.expiration_date = '"+expirationDate+"'  where t.id= '"+id+"'").getEffect();
    }

    public PrintTempVersion getPrintTempVersionById(String id,String processInstanceId) throws Exception{
        String sql = "select t.id, t.t8_print_temp_id, t.version, t.temp_name, t.temp_html, t.remark  " +
                "  from t8_print_temp_version t " +
                "  where t.id = $S{id} ";
        if(!"".equals(processInstanceId)){
            sql += " or t.process_instance_id = '" + processInstanceId + "'";
        }
        SqlRow sqlRow = super.findRow(sql, id);
        PrintTempVersion printTempVersion = new PrintTempVersion();
        printTempVersion.setId(sqlRow.getString("id"));
        printTempVersion.setT8PrintTempId(sqlRow.getString("t8_print_temp_id"));
        printTempVersion.setVersion(sqlRow.getString("version"));
        printTempVersion.setTempName(sqlRow.getString("temp_name"));
        printTempVersion.setTempHtml(sqlRow.getString("temp_html"));
        printTempVersion.setRemark(sqlRow.getString("remark"));
        return printTempVersion;
    }


    public Integer deleteWfBusiExtend(String processInstanceId) throws Exception{
        return super.update("DELETE FROM wf_busi_extend  WHERE process_instance_id = $S{processInstanceId}",processInstanceId).getEffect();
    }

    public Integer deleteWfTask(String processInstanceId) throws Exception{
        return super.update("DELETE FROM wf_task  WHERE process_instance_id = $S{processInstanceId}",processInstanceId).getEffect();
    }

    public Integer deleteWfTaskActor(String processInstanceId) throws Exception{
        return super.update("delete from wf_task_actor where task_id = (select id from wf_task where PROCESS_INSTANCE_ID = $S{processInstanceId})",processInstanceId).getEffect();
    }

    public Integer deleteWfProcessInstance(String processInstanceId) throws Exception{
        return super.update("DELETE FROM wf_process_instance  WHERE id = $S{processInstanceId}",processInstanceId).getEffect();
    }

    public List<PrintTempVersion>  getPrintTempVersionByParams(Map<String,Object> params) throws Exception {
        SqlParam<PrintTempVersion> param = new FetcherData<>(params,PrintTempVersion.class);
        param.setMakeSql(true);
        SqlResult<PrintTempVersion> sqlResult = super.findRows("select t.id,t.t8_print_temp_id,t.temp_name," +
                "t.version,t.temp_html,t.remark,t.create_date,t.create_user_name,t.status from t8_print_temp_version t", param);
        List<PrintTempVersion> printTempVersions = sqlResult.getRows();
        if (printTempVersions == null || printTempVersions.size()<1){
            throw new PromptException("查询无返回结果，请判断参数是否正确");
        }else {
            return printTempVersions;
        }
    }

    public List<PrintTempVersion>  getPrintTempVersionByParamsForCompare(Map<String,Object> params) throws Exception {
        SqlParam<PrintTempVersion> param = new FetcherData<>(params,PrintTempVersion.class);
        String sql = "select t.id,t.t8_print_temp_id,t.temp_name," +
                "t.version,t.temp_html,t.remark,t.create_date,t.create_user_name,t.status from t8_print_temp_version t where 1 = 1";
        if(param.getModel().getVersion()!="" && param.getModel().getVersion()!=null){
            sql = sql + " and version = '"+param.getModel().getVersion()+"'";
        }

//        if(Tools.isNotEmpty(param.getModel().getDistributorCode())&&param.getModel().getDistributorCode()!="" && param.getModel().getDistributorCode()!=null){
//            sql = sql + " and distributor_code = '"+param.getModel().getDistributorCode()+"'";
//        }
        if(param.getModel().getT8PrintTempId()!="" && param.getModel().getT8PrintTempId()!=null){
            sql = sql + " and t8_print_temp_id = '"+param.getModel().getT8PrintTempId()+"'";
        }

        SqlResult<PrintTempVersion> sqlResult = super.findRows(sql, param);
        List<PrintTempVersion> printTempVersions = sqlResult.getRows();
        if (printTempVersions == null || printTempVersions.size()<1){
            throw new PromptException("查询无返回结果，请判断参数是否正确");
        }else {
            return printTempVersions;
        }
    }

    public PrintTempVersion getProdDocumentByProdAndTempType(String prodDocMods,String documentType) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("prodDocMods",prodDocMods.toString());
        params.put("documentType",documentType);
        SqlRow row = this.findRow("select t.id, " +
                "       t.t8_print_temp_id, " +
                "       t.version, " +
                "       t.temp_name, " +
                "       t.temp_html, " +
                "       t.remark, " +
                "       t.status " +
                "from t8_print_temp_version t " +
                "         left join t8_print_temp t2 " +
                "                   on t.t8_print_temp_id = t2.id " +
                //"where t.id in ($U{prodDocMods}) " +
                "where t.id in ($S{prodDocMods}) " +
                "  and t2.temp_type = $S{documentType}", params);
        PrintTempVersion printTempVersion = new PrintTempVersion();
        printTempVersion.setId(row.getString("id"));
        printTempVersion.setT8PrintTempId(row.getString("t8_print_temp_id"));
        printTempVersion.setVersion(row.getString("version"));
        printTempVersion.setTempName(row.getString("temp_name"));
        printTempVersion.setTempHtml(row.getString("temp_html"));
        printTempVersion.setRemark(row.getString("remark"));
        printTempVersion.setStatus(row.getString("status"));
        return printTempVersion;
    }

    public PrintTempVersion getProdManualByT8ProdInfo(T8ProdInfo t8ProdInfo) throws Exception {
        SqlRow row = super.findRow("select t.id, " +
                "       t.t8_print_temp_id, " +
                "       t.version, " +
                "       t.temp_name, " +
                "       t.temp_html, " +
                "       t.remark, " +
                "       t.status " +
                "from t8_print_temp_version t " +
                "         left join t8_print_temp t2 " +
                "                   on t.t8_print_temp_id = t2.id " +
                "where t.id in ($U{prodDocMods}) " +
                "  and (case $S{prodMode} " +
                "           when '1' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.doc_type = '7' " +
                "                    when '2' " +
                "                        then t2.doc_type = '2' end) " +
                "           when '2' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.doc_type = '3' " +
                "                    when '2' " +
                "                        then t2.doc_type = '4' end) " +
                "           when '3' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.doc_type = '5' " +
                "                    when '2' " +
                "                        then t2.doc_type = '6' end) " +
                "           when '4' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.doc_type = '7' " +
                "                    when '2' " +
                "                        then t2.doc_type = '8' end) end) " +
                "  and (case $S{prodMode} " +
                "           when '1' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.temp_type = '70001' " +
                "                    when '2' " +
                "                        then t2.temp_type = '70002' end) " +
                "           when '2' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.temp_type = '60001' " +
                "                    when '2' " +
                "                        then t2.temp_type = '60002' end) " +
                "           when '3' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.temp_type = '50001' " +
                "                    when '2' " +
                "                        then t2.temp_type = '50002' end) " +
                "           when '4' " +
                "               then " +
                "               (case $S{raiseType} " +
                "                    when '1' " +
                "                        then " +
                "                        t2.temp_type = '40001' " +
                "                    when '2' " +
                "                        then t2.temp_type = '40002' end) end)", t8ProdInfo);
        PrintTempVersion printTempVersion = new PrintTempVersion();
        printTempVersion.setId(row.getString("id"));
        printTempVersion.setT8PrintTempId(row.getString("t8_print_temp_id"));
        printTempVersion.setVersion(row.getString("version"));
        printTempVersion.setTempName(row.getString("temp_name"));
        printTempVersion.setTempHtml(row.getString("temp_html"));
        printTempVersion.setRemark(row.getString("remark"));
        printTempVersion.setStatus(row.getString("status"));
        return printTempVersion;
    }




    //产品页面文档字模板关联
    public SqlResult<PrintTempVersion> getPrintTempVersionName(SqlParam<PrintTempVersion> param) throws Exception {
        return super.findRows("select * from \n" +
                "(select t.id,t.t8_print_temp_id,t.temp_name,t.version as doc_version ,t.status,t2.temp_type,distributor_code,t.remark\n" +
                "from t8_print_temp_version t\n" +
                "left join t8_print_temp t2 \n" +
                "on t.t8_print_temp_id= t2.id\n" +
                "union all\n" +
                "select t.id,t.t8_static_temp_id t8_print_temp_id,t.temp_name,t.version as doc_version ,t.status,t2.temp_type,distributor_code,t.remark\n" +
                "from t8_static_temp_version t\n" +
                "left join t8_static_temp t2\n" +
                "on t2.id = t.t8_static_temp_id) tt\n" +
                "where status='1' order by tt.doc_version desc", param);
    }

}
