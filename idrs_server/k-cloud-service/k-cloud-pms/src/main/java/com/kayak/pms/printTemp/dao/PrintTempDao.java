package com.kayak.pms.printTemp.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.basePublish.model.DisclosureModVersion;
import com.kayak.pms.printTemp.model.PrintTemp;
import com.kayak.pms.printTemp.model.StaticTemp;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: k-cloud
 * @description: 文档模板信息Dao
 * @author: WangZhenXin
 * @create: 2020-12-26 11:50
 * @memo 备注信息
 */
@Repository
public class PrintTempDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempDao.class);
    /**
     * @Description: 当不传doctype时需要查询当前类型所有的模版
     * @Author: XIEZEDONG🐼
     * @Date: 2021/9/3 14:35 
     * @param params: 
     * @return: com.kayak.core.sql.SqlResult<com.kayak.pms.printTemp.model.PrintTemp>
     */
    public SqlResult<PrintTemp> find(SqlParam<PrintTemp> params) throws Exception {
        String sql="";
        if(StringUtils.isNotEmpty(params.getModel().getDocType())&&StringUtils.isNotEmpty(params.getModel().getTempType())){
            //手动拼接sql
            params.setMakeSql(false);
            String tempType = params.getModel().getTempType();
            String docType = params.getModel().getDocType();
            List<String> tempTypeList=new ArrayList<>();
            String text="";
            //拿到文档类型
            List<SqlRow> tempTypeByDocType = this.getTempTypeByDocType("");
            if(CollectionUtil.isNotEmpty(tempTypeByDocType)&&StringUtils.isNotEmpty(tempType)){
                for (SqlRow sqlRow : tempTypeByDocType) {
                    Object value = sqlRow.get("value");
                    if(value!=null&&StringUtils.isNotEmpty(value.toString())){
                        if(value.toString().equals(tempType)){
                            Object text1 = sqlRow.get("text");
                            if(text1!=null&&StringUtils.isNotEmpty(text1.toString())){
                                text=text1.toString();
                            }
                        }
                    }
                }

                List<SqlRow> tempTypeByDocType2 = this.getTempTypeByDocType(docType);
                //拿这个类型下所有的tempType
                for (SqlRow sqlRow : tempTypeByDocType2) {
                    Object text1 = sqlRow.get("text");
                    if(text1!=null&&StringUtils.isNotEmpty(text1.toString())){
                        if(text1.toString().equals(text)){
                            Object value = sqlRow.get("value");
                            if(value!=null&&StringUtils.isNotEmpty(value.toString())){
                                tempTypeList.add(value.toString());
                            }
                        }
                    }
                }

            }
            sql="SELECT\n" +
                    "\t\tt.id,\n" +
                    "\t\tt.temp_type,\n" +
                    "\t\tt.temp_name,\n" +
                    "\t\tt.doc_type,\n" +
                    "\t\tt.remark,\n" +
                    "\t\tt.distributor_code,\n" +
                    "\t\tt.t8_trutee_info_id,\n" +
                    "\t\tt.create_date,\n" +
                    "\t\tt.update_date \n" +
                    "\tFROM\n" +
                    "\t\tt8_print_temp t\n" +
                    "\tWHERE 1=1\n" +
                    "\t\tAND t.temp_type IN(";
            if(CollectionUtil.isNotEmpty(tempTypeList)){
                for (int i = 0; i < tempTypeList.size(); i++) {
                    sql+=tempTypeList.get(i);
                    if(i!=tempTypeList.size()-1){
                        sql+=",";
                    }
                }
            }
            sql+=") ORDER BY t.create_date DESC ";


        }else if(StringUtils.isEmpty(params.getModel().getDocType())&&StringUtils.isEmpty(params.getModel().getTempType())||StringUtils.isNotEmpty(params.getModel().getDocType())){
            sql="select " +
                    "    t.id, " +
                    "    t.temp_type, " +
                    "    t.temp_name, " +
                    "    t.doc_type, " +
                    "    t.remark, " +
                    "    t.distributor_code, " +
                    "    t.t8_trutee_info_id, " +
                    "    t.create_date, " +
                    "    t.update_date " +
                    " from t8_print_temp t " +
                    "where 1=1 order by t.create_date desc ";
        } else if(StringUtils.isEmpty(params.getModel().getDocType())&&StringUtils.isNotEmpty(params.getModel().getTempType())){
            //手动拼接sql
            params.setMakeSql(false);
            String tempType = params.getModel().getTempType();
            List<String> tempTypeList=new ArrayList<>();
            String text="";
            //拿到文档类型
            List<SqlRow> tempTypeByDocType = this.getTempTypeByDocType("");
            if(CollectionUtil.isNotEmpty(tempTypeByDocType)&&StringUtils.isNotEmpty(tempType)){
                for (SqlRow sqlRow : tempTypeByDocType) {
                    Object value = sqlRow.get("value");
                    if(value!=null&&StringUtils.isNotEmpty(value.toString())){
                    if(value.toString().equals(tempType)){
                        Object text1 = sqlRow.get("text");
                        if(text1!=null&&StringUtils.isNotEmpty(text1.toString())){
                            text=text1.toString();
                        }
                    }
                    }
                }
                //拿这个类型下所有的tempType
                for (SqlRow sqlRow : tempTypeByDocType) {
                    Object text1 = sqlRow.get("text");
                    if(text1!=null&&StringUtils.isNotEmpty(text1.toString())){
                        if(text1.toString().equals(text)){
                            Object value = sqlRow.get("value");
                            if(value!=null&&StringUtils.isNotEmpty(value.toString())){
                                tempTypeList.add(value.toString());
                            }
                        }
                    }
                }

            }
            sql="SELECT\n" +
                    "\t\tt.id,\n" +
                    "\t\tt.temp_type,\n" +
                    "\t\tt.temp_name,\n" +
                    "\t\tt.doc_type,\n" +
                    "\t\tt.remark,\n" +
                    "\t\tt.distributor_code,\n" +
                    "\t\tt.t8_trutee_info_id,\n" +
                    "\t\tt.create_date,\n" +
                    "\t\tt.update_date \n" +
                    "\tFROM\n" +
                    "\t\tt8_print_temp t\n" +
                    "\tWHERE 1=1\n" +
                    "\t\tAND t.temp_type IN(";
            if(CollectionUtil.isNotEmpty(tempTypeList)){
                for (int i = 0; i < tempTypeList.size(); i++) {
                    sql+=tempTypeList.get(i);
                    if(i!=tempTypeList.size()-1){
                        sql+=",";
                    }
                }
            }
            sql += ") ORDER BY t.create_date DESC ";


        }
        return super.findRows(sql, params);
    }

    /**
     * 功能：根据模板id查询该模板下最大版本的创建时间与状态
     *
     * @param printId
     * @return
     * @throws Exception
     */
    public PrintTemp findPrintTempVersionsByPrintId(String printId) throws Exception {
        SqlParam<PrintTemp> param = new FetcherData<>(new HashMap<>(), PrintTemp.class);
        return super.findRow(PrintTemp.class, "SELECT create_date temp_version_update_date,create_time temp_version_update_time,`status` from t8_print_temp_version  where id = (SELECT id FROM ( SELECT max(id+0) id, max(version) version FROM t8_print_temp_version WHERE t8_print_temp_id ='" + printId + "' ) t )", 0, param);
    }

    /**
     * @Description: 当文档类型为空的时候查询所有类型字典
     * @Author: XIEZEDONG🐼
     * @Date: 2021/9/3 10:03
     * @param doc_type:文档类型
     * @return: java.util.List<com.kayak.core.sql.SqlRow>
     */
    public List<SqlRow> getTempTypeByDocType(String doc_type) throws Exception {
        String sql="";
        if(StringUtils.isNotEmpty(doc_type)){
            sql="select itemkey value, " +
                    "       itemval text " +
                    "  from sys_dict_item " +
                    " where dict = (CASE $S{doc_type} " +
                    "         WHEN '1' THEN " +
                    "          't8_temp_type_fb_gm' " +
                    "         WHEN '2' THEN " +
                    "          't8_temp_type_fb_sm' " +
                    "         WHEN '3' THEN " +
                    "          't8_temp_type_zq_gm' " +
                    "         WHEN '4' THEN " +
                    "          't8_temp_type_zq_sm' " +
                    "         WHEN '5' THEN " +
                    "          't8_temp_type_tt_gm' " +
                    "         WHEN '6' THEN " +
                    "          't8_temp_type_tt_sm' " +
                    "         WHEN '7' THEN " +
                    "          't8_temp_type_hb' " +
                    "         WHEN '9' THEN " +
                    "          't8_temp_type_qt' " +
                    "         WHEN '10' THEN " +
                    "          't8_temp_type_ty' " +
                    "       END) " +
                    " order by itemkey+0";
        }else {
            sql="SELECT\n" +
                    "\titemkey value,\n" +
                    "\titemval text \n" +
                    "FROM\n" +
                    "\tsys_dict_item \n" +
                    "WHERE\n" +
                    "\tdict IN ( 't8_temp_type_fb_gm', 't8_temp_type_fb_sm', 't8_temp_type_zq_gm', 't8_temp_type_zq_sm', 't8_temp_type_tt_gm', 't8_temp_type_tt_sm', 't8_temp_type_hb', 't8_temp_type_qt', 't8_temp_type_ty' ) \n" +
                    "ORDER BY\n" +
                    "\titemkey + 0";
        }
        return super.findRows(sql,doc_type);
    }

    public String savePrintTemp(PrintTemp printTemp) throws Exception {
        String autoId = super.update("insert into t8_print_temp(id, temp_type, temp_name, doc_type ,remark, distributor_code, t8_trutee_info_id,create_date) " +
                "VALUES($AUTOIDS{t8_print_temp},$S{tempType},$S{tempName},$S{docType},$S{remark},$S{distributorCode}, $S{t8TruteeInfoId},$S{createDate})", printTemp).getAutoId();
        return autoId;
    }

    public Integer checkPrintTemp(PrintTemp printTemp){
        Integer cont = 0;
        try {
            StringBuilder sql = new StringBuilder("select count(t.id) cont " +
                    "from t8_print_temp t " +
                    "where t.doc_type = $S{docType}  and t.temp_type = $S{tempType}  and t.temp_name = $S{tempName} ");
            if (StringUtils.isNotBlank(printTemp.getDistributorCode())) {
                sql.append("and t.distributor_code = $S{distributorCode} ");
            }
            if (StringUtils.isNotBlank(printTemp.getT8TruteeInfoId())) {
                sql.append("and t.t8_trutee_info_id = $S{t8TruteeInfoId} ");
            }
            List<SqlRow> rows = super.findRows(sql.toString()
                    , printTemp);
            if (rows != null && rows.size()>0){
                cont = rows.get(0).getInteger("cont");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return cont;
    }

    public Integer checkFlowPrintTemp(PrintTemp printTemp){
        Integer cont = 0;
        try {
            StringBuilder sql = new StringBuilder("select count(wpi.id) cont " +
                    "from t8_print_temp t left join wf_process_instance wpi on wpi.id=t.process_instance_id" +
                    " where (t.doc_type = $S{docType}  and t.temp_type = $S{tempType}  and t.temp_name = $S{tempName} ");
            if (StringUtils.isNotBlank(printTemp.getDistributorCode())) {
                sql.append("and t.distributor_code = $S{distributorCode} ");
            }
            if (StringUtils.isNotBlank(printTemp.getT8TruteeInfoId())) {
                sql.append("and t.t8_trutee_info_id = $S{t8TruteeInfoId} ");
            }
            sql.append(" and wpi.RUNNING_STATUS != '3')or( ");
            sql.append(" wpi.RUNNING_STATUS is  Null AND t.doc_type = $S{docType}  and t.temp_type = $S{tempType}  and t.temp_name = $S{tempName} ");
            if (StringUtils.isNotBlank(printTemp.getDistributorCode())) {
                sql.append(" and t.distributor_code = $S{distributorCode} ");
            }
            if (StringUtils.isNotBlank(printTemp.getT8TruteeInfoId())) {
                sql.append(" and t.t8_trutee_info_id = $S{t8TruteeInfoId} ");
            }
            sql.append(" )");
            List<SqlRow> rows = super.findRows(sql.toString()
                    , printTemp);
            if (rows != null && rows.size()>0){
                cont = rows.get(0).getInteger("cont");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return cont;
    }

    public PrintTemp getPrintTempByDocumentType(String documentType) throws Exception {
        SqlRow row = super.findRow("select t.id, " +
                "       t.temp_type, " +
                "       t.temp_name, " +
                "       t.doc_type, " +
                "       t.remark, " +
                "       t.distributor_code, " +
                "       t.t8_trutee_info_id " +
                "from t8_print_temp t " +
                "where t.temp_type = $S{documentType} ", documentType);
        PrintTemp printTemp = new PrintTemp();
        if(row!=null&&row.size()>0){
            printTemp.setId(row.getString("id"));
            printTemp.setTempType(row.getString("temp_type"));
            printTemp.setTempName(row.getString("temp_name"));
            printTemp.setDocType(row.getString("doc_type"));
            printTemp.setRemark(row.getString("remark"));
            printTemp.setDistributorCode(row.getString("distributor_code"));
            printTemp.setT8TruteeInfoId(row.getString("t8_trutee_info_id"));
        }
        return printTemp;
    }

    public PrintTemp getPrintTempByDocumentType(PrintTemp documentType) throws Exception {
        String sql = "select t.id, " +
                "       t.temp_type, " +
                "       t.temp_name, " +
                "       t.doc_type, " +
                "       t.remark " +
                "from t8_print_temp t " +
                "where t.temp_type = $S{tempType} " ;
        if (StringUtils.isNotBlank(documentType.getDistributorCode())) {
            sql = sql + " and distributor_code = $S{distributorCode}";
        }
        if (StringUtils.isNotBlank(documentType.getT8TruteeInfoId())) {
            sql = sql + " and t8_trutee_info_id = $S{t8TruteeInfoId}";
        }
        SqlRow row = super.findRow(sql, documentType);
        PrintTemp printTemp = new PrintTemp();
        if (row != null) {
            printTemp.setId(row.getString("id"));
            printTemp.setTempType(row.getString("temp_type"));
            printTemp.setTempName(row.getString("temp_name"));
            printTemp.setDocType(row.getString("doc_type"));
            printTemp.setRemark(row.getString("remark"));
        }
        return printTemp;
    }

    public PrintTemp getPrintTempById(String id) throws Exception {
        SqlRow row = super.findRow("select t.id, " +
                "       t.temp_type, " +
                "       t.temp_name, " +
                "       t.doc_type, " +
                "       t.remark, " +
                "       t.distributor_code, " +
                "       t.t8_trutee_info_id " +
                "from t8_print_temp t " +
                "where t.id = $S{id} ", id);
        PrintTemp printTemp = new PrintTemp();
        printTemp.setId(row.getString("id"));
        printTemp.setTempType(row.getString("temp_type"));
        printTemp.setTempName(row.getString("temp_name"));
        printTemp.setDocType(row.getString("doc_type"));
        printTemp.setRemark(row.getString("remark"));
        printTemp.setDistributorCode(row.getString("distributor_code"));
        printTemp.setT8TruteeInfoId(row.getString("t8_trutee_info_id"));
        return printTemp;
    }
    //根据销售商代码查询数据
    public List<SqlRow> findByDistributorCode(String distributorCode) throws Exception {

        Map<String, Object> params = new HashMap<>(1);
        params.put("distributorCode", distributorCode);
        return super.findRows("select t.id,t.distributor_code from t8_print_temp t where t.distributor_code=$S{distributorCode}", params);
    }

    public Integer updateDistributorCode(PrintTemp printTemp) throws Exception {

        String sql = " UPDATE t8_print_temp SET distributor_code = $S{distributorCode} WHERE id = $S{id}";
        return super.update(sql, printTemp).getEffect();
    }

	public Integer updatePrintTempInfo(PrintTemp printTemp)throws Exception {
		  String sql = " UPDATE t8_print_temp SET remark = $S{remark} WHERE id = $S{id}";
		  return super.update(sql, printTemp).getEffect();
	}

}
