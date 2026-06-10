package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.*;

@Repository
public class DealValuePortDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;
    /**
     * 获取接口信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getAllPortInfo(Map<String, Object> params) throws Exception {
        List<Map<String,Object>> portList = new ArrayList<>();
        params.put("portState","1");
        String sql ="select k.id, " +
                "k.port_code, " +
                "k.xml_node_info, " +
                "k.port_name, " +
                "k.port_exeid, " +
                "k.port_address," +
                "k.port_state," +
                "k.file_type," +
                "k.slice_flag," +
                "split_flag," +
                "k.port_table, " +
                "k.port_type, " +
                "k.port_dir, " +
                "k.separator, " +
                "k.skip_rows, " +
                "k.has_end_separator, " +
                "k.charset, " +
                "k.synch_type, " +
                "k.skip_no_file, " +
                "k.wait_flag " +
                "from base_port_manage k " +
                "where 1=1 ";
        if(Tools.isNotEmpty((String) params.get("portState"))){
            sql = sql + " and k.port_state = '"+params.get("portState")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("portType"))){
            sql = sql + " and k.port_type = '"+params.get("portType")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("portDir"))){
            sql = sql + " and k.port_dir = '"+params.get("portDir")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("portName"))){
            sql = sql + " and k.port_name = '"+params.get("portName")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("pId"))){
            sql = sql + " and k.pid = '"+params.get("pId")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("portCode"))){
            sql = sql + " and k.port_code = '"+params.get("portCode")+"'";
        }
        List<SqlRow> it = comnDao.findRows(sql, DataSourceProperty.PUB,params);
        for (SqlRow sqlRow:it) {
            Map<String,Object> tparam = new HashMap<String,Object>();
            tparam.put("port_code",sqlRow.getString("port_code"));
            tparam.put("xml_node_info",sqlRow.getString("xml_node_info"));
            tparam.put("port_name",sqlRow.getString("port_name"));
            tparam.put("port_exeid",sqlRow.getString("port_exeid"));
            tparam.put("port_address",sqlRow.getString("port_address"));
            tparam.put("file_type",sqlRow.getString("file_type"));
            tparam.put("split_flag",sqlRow.getString("split_flag"));
            tparam.put("slice_flag",sqlRow.getString("slice_flag"));
            tparam.put("port_table",sqlRow.getString("port_table"));
            tparam.put("port_type",sqlRow.getString("port_type"));
            tparam.put("separator",sqlRow.getString("separator"));
            tparam.put("port_dir",sqlRow.getString("port_dir"));
            tparam.put("deal_date",params.get("dealDate").toString());//当前执行数据日期
            tparam.put("last_date", DateUtil.geBeforeDay(params.get("dealDate").toString()));//当前执行数据日期前一自然日
            tparam.put("next_date",DateUtil.getAfterDay(params.get("dealDate").toString()));//当前执行数据日期后一自然日
            tparam.put("skip_rows",sqlRow.getString("skip_rows"));//跳过行数
            tparam.put("has_end_separator",sqlRow.getString("has_end_separator"));//行末尾是否带多余分隔符
            tparam.put("charset",sqlRow.getString("charset"));//字符集
            tparam.put("synch_type",sqlRow.getString("synch_type"));//同步类型
            tparam.put("skip_no_file",sqlRow.getString("skip_no_file"));//文件不存在是否跳过
            tparam.put("wait_flag",sqlRow.getString("wait_flag"));//无文件等待标志
            tparam.put("separator_val",getItemval(sqlRow.getString("separator")));//文件分隔符text
            portList.add(tparam);
        }
        return portList;
    }

    private String getItemval(String key) throws Exception {
        String area = "";
        String querySql = "select itemval from sys_dict_item bem where dict ='separator' and itemkey = '"+key+"' " ;
        SqlRow sqlRow = comnDao.findRow(querySql,null);
        return sqlRow.getString("itemval");
    }
    /**
     * 记录估值文件log
     * @param params
     * @throws Exception
     */
    public void createFileLog(Map<String, Object> params) throws Exception{
        comnDao.update("insert into base_port_file_log (sequence,"+
                " port_code,\n" +
                " port_name,\n" +
                " port_type,\n" +
                " port_dir,\n" +
                " file_state,\n" +
                " exec_message,\n" +
                " deal_user_id,\n" +
                " deal_date,\n" +
                " crt_date,\n" +
                " crt_time ) \n" +
                " values ("+
                " $S{sequence},\n" +
                " $S{port_code},\n" +
                " $S{port_name},\n" +
                " $S{port_type},\n" +
                " $S{port_dir},\n" +
                " $S{file_state},\n" +
                " '处理中',\n" +
                " $S{deal_user_id},\n" +
                " $S{deal_date},\n" +
                " DATE_FORMAT(NOW(), '%Y%m%d'),\n" +
                " DATE_FORMAT(NOW(), '%H%i%s')\n" +
                " )", params);
    }

    /**
     * 查询接口字段全部信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryFieldList(Map<String,Object> params) throws Exception{
        return comnDao.findRows("select t.id,\n" +
                " t.port_code,\n" +
                " t.file_field_code,\n" +
                " t.field_code,\n" +
                " t.field_name,\n" +
                " t.field_type,\n" +
                " t.field_length,\n" +
                " t.field_dights,\n" +
                " t.field_seq\n" +
                " from base_port_field_manage t\n" +
                " where t.port_code = $S{port_code}\n" +
                " order by t.field_seq", params);
    }

    /**
     * 查询接口字段全部信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryFieldListNoDealDate(Map<String,Object> params) throws Exception{
        return comnDao.findRows("select t.id,\n" +
                " t.port_code,\n" +
                " t.field_code,\n" +
                " t.field_name,\n" +
                " t.field_type,\n" +
                " t.field_length,\n" +
                " t.field_dights,\n" +
                " t.field_seq\n" +
                " from base_port_field_manage t\n" +
                " where t.port_code = $S{port_code} and field_code not in('DEAL_DATE','deal_date') \n" +
                " order by t.field_seq", params);
    }

    /**
     * 查询接口所有字段名称(只查询字段名称)
     * @param params
     * @return
     * @throws Exception
     */
    public List<String> queryAllFieldCode(Map<String,Object> params) throws Exception{
        List<SqlRow> rs = this.queryFieldList(params);
        List<String> fieldList = new ArrayList<>();
        for (SqlRow sqlRow : rs) {
            fieldList.add(sqlRow.get("field_code").toString());
        }
        return  fieldList;
    }

    /**
     * 返回接口内容
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> getSystemExeId(Map<String,Object> params)throws Exception{
        String str = "select t.exeid,t.sqlid,t.sqlstr from t8_sql_info t where 1=1\n";
        if(Tools.isNotEmpty((String) params.get("exeid"))){
            str = str + " and t.exeid = '"+params.get("exeid")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("sqlid"))){
            str = str + " and t.sqlid = '"+params.get("sqlid")+"'";
        }
        if(Tools.isNotEmpty((String) params.get("desc"))){
            str = str + " and t.desc = '"+params.get("desc")+"'";
        }
        return comnDao.findRows(str, params);

    }
    /**
     * 获取exeid
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryPortContent(String sql,Map<String,Object> params)throws Exception{
        return comnDao.findRows(sql, params);

    }

    /**
     * 更新估值文件log
     * @param params
     * @throws Exception
     */
    public void updateFileLog(Map<String, Object> params) throws Exception{
        comnDao.update("update base_port_file_log\n" +
                " set file_state = $S{fileState},\n" +
                " exec_message = $S{message},\n" +
                " total_num = $S{totalNum},\n" +
                " deal_user_id = $S{deal_user_id},\n" +
                " upd_date = DATE_FORMAT(NOW(), '%Y%m%d'),\n" +
                " upd_time = DATE_FORMAT(NOW(), '%H%i%s')\n" +
                " where sequence = $S{sequence}", params);
    }

    public void deleteFromDate(String sql, Map<String, Object> params) throws Exception{
        comnDao.update(sql, params);
    }
    public void insertFormData(String sql) throws Exception{
        comnDao.update(sql);
    }

    public SqlRow getSeparator(String itemkey) throws Exception {
        return comnDao.findRow("select itemval from sys_dict_item where dict = 'separator' and itemkey = $S{itemkey}",DataSourceProperty.PUB, itemkey);
    }

    public List<SqlRow> getDealDateList(Map<String, Object> params) throws Exception {

        return comnDao.findRows("select a.day as dealDate from (select curdate() - interval (a.a+10*b.a) + (100*c.a) + (1000*d.a) Day as day from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as d) a where a.day between '"+params.get("startDate")+"' and '"+params.get("endDate")+"'  order by a.dealDate", DataSourceProperty.PUB,params);
    }


    public List<SqlRow> getXmlDateList(Map<String, Object> params) throws Exception {

        return comnDao.findRows("select *  from  rms_stg_xml_config where   data_table_name =  $S{table} ", DataSourceProperty.PUB,params);
    }

    public List<SqlRow> findSelectCondition(Map<String, Object> params) throws Exception {
        String sql = "select GROUP_CONCAT(t.natural_key) natural_key,t1.remind_field ,t1.remind_type,t1.related_report,t1.task_group,t1.field_type,t1.out_dict  from rem_datanatural_key t " +
                "left join rem_sourcedata_config t1 on t.table_name = t1.table_name " +
                "where t.table_name = $S{port_table} group by t1.remind_field ,t1.remind_type,t1.related_report,t1.task_group,t1.field_type,t1.out_dict";
        return comnDao.findRows(sql,DataSourceProperty.PUB,params);
    }

    public List<SqlRow> findOldData(Map<String, Object> params) throws Exception {
        String sql = "select t.id newid,tt.id oldid,";
        String natural_keys_sql = "";
        String remind_fields_sql = "";
        String special_fields_sql = "";
        String insqlsearch = "";
        String wheresql = "";
        List<String> natural_keys = (List<String>) params.get("natural_keys");
        List<String> remind_fields = (List<String>) params.get("remind_fields");
        List<String> special_fields = (List<String>) params.get("special_fields");
        for (int i = 0; i < natural_keys.size(); i++) {
            natural_keys_sql+="t."+natural_keys.get(i)+",";
            insqlsearch+="t."+natural_keys.get(i)+",";
            wheresql+="t."+natural_keys.get(i)+"=tt."+natural_keys.get(i)+" and ";
        }
        natural_keys_sql = natural_keys_sql.substring(0,natural_keys_sql.length()-1);
        insqlsearch = insqlsearch.substring(0,insqlsearch.length()-1);
        wheresql = wheresql.substring(0,wheresql.length()-4);
        for (int i = 0; i < remind_fields.size(); i++) {
            remind_fields_sql+=",t."+remind_fields.get(i)+" newdata"+i+",tt."+remind_fields.get(i)+" olddata"+i;
            insqlsearch+=",t."+remind_fields.get(i);
        }
        for (int i = 0; i < special_fields.size(); i++) {
            if(i==special_fields.size()-1){
                special_fields_sql+="t."+special_fields.get(i)+" specnew"+i+",tt."+special_fields.get(i)+" specold"+i;
                insqlsearch+="t."+special_fields.get(i);
            }else{
                special_fields_sql+=",t."+special_fields.get(i)+" specnew"+i+",tt."+special_fields.get(i)+" specold"+i+",";
                insqlsearch+=",t."+special_fields.get(i)+",";
            }
        }
        sql+=natural_keys_sql;
        sql+=remind_fields_sql;
        sql+=special_fields_sql;
        if(params.get("port_table").toString().equals("rms_stg_pms_cpdm")){
            sql += ",t.mjksrq";
        }
        sql += " from $U{port_table} t left join (select t.id,"+ insqlsearch +" from $U{port_table} t where t.is_effective = '1') tt on "+wheresql+" where t.is_effective = '2' order by tt.id asc,t.id desc";
        return comnDao.findRows(sql,DataSourceProperty.PUB,params);
    }




    public List<SqlRow> findComType(Map<String, Object> params) throws Exception {
        String table  =  params.get("port_table").toString();
        String sql = "select  t.comparison_rules  from  rem_sourcedata_config t  where t.TABLE_NAME  =  '"+table+"' ";
        return comnDao.findRows(sql,DataSourceProperty.PUB,params);
    }

    public void updateComOldData(Map<String, Object> params) throws Exception {
        String table  =  params.get("port_table").toString();
        String dealDate  =  params.get("deal_date").toString();
        String sql = "update  "+table+"  set   is_effective = '0'  where  is_effective = '2'   and  deal_date =  '"+dealDate+"'   ";
        comnDao.update(sql);
    }



    public List<SqlRow> findOldDataByConcat(Map<String, Object> params) throws Exception {
        String sql = "select t.id newid,tt.id oldid,";
        String concat = "CONCAT(";
        // 关联主键
        List<String> natural_keys = (List<String>) params.get("natural_keys");
        // 对比字段
        List<String> remind_fields = (List<String>) params.get("remind_fields");
        List<String> special_fields = (List<String>) params.get("special_fields");


        // 关联主键
        String natural_keys_sql = "";
        // 对比字段
        String remind_fields_sql = "";
        String special_fields_sql = "";
        String insqlsearch = "";
        String wheresql = "";

        for (int i = 0; i < natural_keys.size(); i++) {
            natural_keys_sql+="tt."+natural_keys.get(i)+",";
            insqlsearch+="t."+natural_keys.get(i)+",";
            wheresql+="t."+natural_keys.get(i)+"=tt."+natural_keys.get(i)+" and ";
        }
        natural_keys_sql = natural_keys_sql.substring(0,natural_keys_sql.length()-1);
        insqlsearch = insqlsearch.substring(0,insqlsearch.length()-1);
        wheresql = wheresql.substring(0,wheresql.length()-4);


        for (int i = 0; i < remind_fields.size() ; i++) {
            if(i == remind_fields.size()-1){
                concat = concat +  "ifnull(t." + remind_fields.get(i)+",'')" +")  ";
            }else {
                concat = concat +  "ifnull(t." + remind_fields.get(i) +",''),',',";
            }
        }
        sql = sql + natural_keys_sql +", t.sourcedata as newdata,  tt.sourcedata as olddata  from  (  select   group_concat(id) as id,  "+insqlsearch+" , group_concat(    " +  concat + "   separator ';')  as sourcedata from $U{port_table} t  where t.is_effective = '2'  group by " + insqlsearch + " ) t  left join (  select  group_concat(id) as id, ";
        sql =sql  + insqlsearch + ", group_concat( " +  concat + "   separator ';')  as sourcedata from $U{port_table} t  where t.is_effective = '1'  group by " + insqlsearch + " )tt on " + wheresql;
        sql = sql + "  order by tt.id asc,t.id desc";
        return comnDao.findRows(sql,DataSourceProperty.PUB,params);
    }




    public void deleteSourceChgInfo(Map<String, Object> params) throws Exception {
        comnDao.update("delete from rem_sourcedata_chginfo where tables = $S{port_table} and status = '0'", params);
    }

    public void addSourceChgInfo(Map<String, Object> dataSourcechg) throws Exception {
        String addflag = dataSourcechg.get("addflag").toString();
        String insert = "INSERT INTO rem_sourcedata_chginfo (NATURAL_KEYS,OLDID,NEWID,CHANGE_FIELD, TABLES, TABLE_NAME, STATUS, CRT_DT,deal_date,port_type" ;
        String values = "VALUES($S{natural_keys},$S{oldid}, $S{newid}, $S{change_field}, $S{tables}, $S{table_name}, $S{status}, DATE_FORMAT(NOW(), '%Y%m%d'),$S{deal_date},$S{port_type},$S{field_old}, $S{field_new})";
        switch (addflag) {
            case "var" :
                insert += ",FIELD_OLD_VAR,FIELD_NEW_VAR)";
                break;
            case "int" :
                insert += ",FIELD_OLD_INT,FIELD_NEW_INT)";
                break;
            case "db" :
                insert += ",FIELD_OLD_DB,FIELD_NEW_DB)";
                break;
            case "dec" :
                insert += ",FIELD_OLD_DEC,FIELD_NEW_DEC)";
                break;
            case "dt" :
                insert += ",FIELD_OLD_DT,FIELD_NEW_DT)";
                break;
            case "tm" :
                insert += ",FIELD_OLD_TM,FIELD_NEW_TM)";
                break;
            case "dts" :
                insert += ",FIELD_OLD_DTS,FIELD_NEW_DTS)";
                break;
            case "bool" :
                insert += ",FIELD_OLD_BOOL,FIELD_NEW_BOOL)";
                break;
            case "blob" :
                insert += ",FIELD_OLD_BLOB,FIELD_NEW_BLOB)";
                break;
            default :
                insert += ",FIELD_OLD,FIELD_NEW)";
        }
        comnDao.update(insert+values, dataSourcechg);
    }

    public boolean isSubmit(Map<String, Object> params, Map<String, Object> tableMapping, SqlRow rows, List<Map<String, Object>> oldDataListForNK) throws Exception {
        boolean flag = false;
        List<SqlRow> resultrow;
        List<String> related_repos = (List<String>) params.get("related_repos");
        String mjksrq = StringUtil.isBlank(rows.getString("mjksrq"))?"":rows.getString("mjksrq");
        for(String related_repo:related_repos){
            String sql = "select register_status from "+related_repo+" where 1=1";
            if(related_repo.equals("app_prod_regist_filing_info")) {
                sql += " and theory_report_start_date <= '" + mjksrq + "' and theory_report_end_date >= '" + mjksrq + "'";
                sql += " and "+tableMapping.get("app_prod_regist_filing_info")+" = '"+oldDataListForNK.get(0).get("cpdm")+"'";
            }else if(related_repo.equals("app_prod_issuance_regist_info")){
                sql += " and theory_report_start_date <= '" + mjksrq + "' and theory_report_end_date >= '" + mjksrq + "'";
                sql += " and "+tableMapping.get("app_prod_issuance_regist_info")+" = '"+oldDataListForNK.get(0).get("cpdm")+"'";
            }else{
                sql += " and imp_date = '"+params.get("deal_date").toString()+"'";
//                for (int i = 0; i < oldDataListForNK.size(); i++) {
//                    Map<String, Object> oldDataMap = oldDataListForNK.get(i);
//                    Set<String> keyset = oldDataMap.keySet();
//                    for (String key : keyset){
//                        sql += " and "+key+" = '"+oldDataMap.get(key).toString()+"'";
//                    }
//                }
            }
            resultrow = comnDao.findRows(sql, DataSourceProperty.PUB, (Object) null);
            if(resultrow.size()>0){
                List<SqlRow> sqlresult = (List<SqlRow>) comnDao.findRows(sql, DataSourceProperty.PUB, null);
                for (SqlRow row : sqlresult){
                    if (!row.getString("register_status").equals("3")){
                        flag = true;
                        break;
                    }
                }
            }
            if(flag){
                break;
            }
        }
        return flag;
    }

    public void updateOldData(Map<String, Object> is_effectives,String updateeffectives, String whereid) throws Exception {
        String sql = "update $U{port_table} set is_effective = '"+updateeffectives+"' where id = '"+whereid+"'";
        comnDao.update(sql, is_effectives);
    }

    public void updateOldData2(Map<String, Object> is_effectives, List<Map<String,Object>> oldDataforNK) throws Exception {
        String sql = "update $U{port_table} set is_effective = $S{updateeffectives} where is_effective = $S{whereeffectives}";
        for (Map<String,Object> maprow : oldDataforNK){
            for (String key : maprow.keySet()){
                sql += " and "+key+" = '"+maprow.get(key)+"'";
            }
        }
        comnDao.update(sql, is_effectives);
    }

    public void upSpecialField(Map<String, Object> dataSourcechg) throws Exception {
        String sql = "update $U{tables} set cpzt = $S{cpzt},dqrq = ifnull(DATE_FORMAT($S{dqrq},'%Y-%m-%d'),null),sjdqrq = DATE_FORMAT($S{sjdqrq},'%Y-%m-%d') where id = $S{dealid}";
        comnDao.update(sql, dataSourcechg);
    }


    public static void main(String[] args) {





        String sql = "select t.id newid,tt.id oldid,";
        String concat = "CONCAT(";
        // 关联主键
        List<String> natural_keys = new ArrayList<>();
        natural_keys.add("cpdm");


        // 对比字段
        List<String> remind_fields =  new ArrayList<>();
        remind_fields.add("TZZC");
        remind_fields.add("PZBL");

        List<String> special_fields =  new ArrayList<>();


        // 关联主键
        String natural_keys_sql = "";
        // 对比字段
        String remind_fields_sql = "";
        String special_fields_sql = "";
        String insqlsearch = "";
        String wheresql = "";

        for (int i = 0; i < natural_keys.size(); i++) {
            natural_keys_sql+="tt."+natural_keys.get(i)+",";
            insqlsearch+="t."+natural_keys.get(i)+",";
            wheresql+="t."+natural_keys.get(i)+"=tt."+natural_keys.get(i)+" and ";
        }
        natural_keys_sql = natural_keys_sql.substring(0,natural_keys_sql.length()-1);
        insqlsearch = insqlsearch.substring(0,insqlsearch.length()-1);
        wheresql = wheresql.substring(0,wheresql.length()-4);


        for (int i = 0; i < remind_fields.size() ; i++) {
            if(i == remind_fields.size()-1){
                concat = concat +  "t." + remind_fields.get(i) +")  ";
            }else {
                concat = concat +  "t." + remind_fields.get(i) +",',',";
            }
        }
        sql = sql + natural_keys_sql +", t.sourcedata as newdata,  tt.sourcedata as olddata  from  (  select   group_concat(id) as id,  "+insqlsearch+" , group_concat(    " +  concat + "   separator ';')  as sourcedata from $U{port_table} t  where t.is_effective = '2'  group by " + insqlsearch + " ) t  left join (  select  group_concat(id) as id, ";
        sql =sql  + insqlsearch + ", group_concat( " +  concat + "   separator ';')  as sourcedata from $U{port_table} t  where t.is_effective = '1'  group by " + insqlsearch + " )tt on " + wheresql;
        sql = sql + "  order by tt.id asc,t.id desc";
        System.out.println(sql);

    }


}
