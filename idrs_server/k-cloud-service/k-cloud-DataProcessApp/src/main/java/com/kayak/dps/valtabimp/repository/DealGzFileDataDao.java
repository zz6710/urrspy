package com.kayak.dps.valtabimp.repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DealGzFileDataDao extends ComnDao {

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
                "k.port_name, " +
                "k.port_exeid, " +
                "k.port_address," +
                "k.port_state," +
                "k.file_type," +
                "split_flag," +
                "k.port_table, " +
                "k.port_type, " +
                "k.port_dir, " +
                "k.separator, " +
                "k.skip_rows, " +
                "k.has_end_separator " +
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
        List<SqlRow> it = comnDao.findRows(sql, DataSourceProperty.PUB,params);
        for (SqlRow sqlRow:it) {
            Map<String,Object> tparam = new HashMap<String,Object>();
            tparam.put("port_code",sqlRow.getString("port_code"));
            tparam.put("port_name",sqlRow.getString("port_name"));
            tparam.put("port_exeid",sqlRow.getString("port_exeid"));
            tparam.put("port_address",sqlRow.getString("port_address"));
            tparam.put("file_type",sqlRow.getString("file_type"));
            tparam.put("split_flag",sqlRow.getString("split_flag"));
            tparam.put("port_table",sqlRow.getString("port_table"));
            tparam.put("port_type",sqlRow.getString("port_type"));
            tparam.put("separator",sqlRow.getString("separator"));
            tparam.put("port_dir",sqlRow.getString("port_dir"));
            tparam.put("deal_date",params.get("dealDate").toString());
            tparam.put("skip_rows",sqlRow.getString("skip_rows"));//跳过行数
            tparam.put("has_end_separator",sqlRow.getString("has_end_separator"));//行末尾是否带多余分隔符
            portList.add(tparam);
        }
        return portList;
    }

    /**
     * 记录估值文件log
     * @param params
     * @throws Exception
     */
    public void createFileLog(Map<String, Object> params) throws Exception{
        comnDao.update("insert into base_port_file_log (\n" +
                " id,\n" +
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
                " values (\n" +
                " $S{id},\n" +
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
                " t.field_code,\n" +
                " t.field_name,\n" +
                " t.field_type,\n" +
                " t.field_length,\n" +
                " t.field_dights,\n" +
                " t.field_seq\n" +
                " from base_port_field_manage t\n" +
                " where t.port_code = $S{port_table}\n" +
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
                " where id = $S{id}", params);
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
}
