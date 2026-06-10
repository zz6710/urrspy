package com.kayak.rpt.zz.historyInfo.service.comm;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ZtDao extends ComnDao {

    public List<SqlRow> queryFieldList(Map<String,Object> params) throws Exception {
        return findRows("select t.id,\n" +
                " bpm.port_table,\n" +
                " t.port_code,\n" +
                " t.file_field_code,\n" +
                " t.field_code,\n" +
                " t.field_name,\n" +
                " t.field_type,\n" +
                " t.field_length,\n" +
                " t.field_dights,\n" +
                " t.field_seq\n" +
                " from base_port_field_manage t join base_port_manage bpm on bpm.port_code=t.port_code\n" +
                " where t.port_code = $S{port_code}\n" +
                " order by t.field_seq", params);
    }

    /**
     * 获取接口信息
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String,Object> getPortInfo(String port_code) throws Exception {
        Map<String,Object> tparam = new HashMap<String,Object>();
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
                "k.skip_no_file " +
                "from base_port_manage k " +
                "where 1=1 ";
        if(Tools.isNotEmpty(port_code)){
            sql = sql + " and k.port_code = '"+port_code+"'";
        }
        List<SqlRow> it = super.findRows(sql, DataSourceProperty.PUB);
        for (SqlRow sqlRow:it) {
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
            tparam.put("skip_rows",sqlRow.getString("skip_rows"));//跳过行数
            tparam.put("has_end_separator",sqlRow.getString("has_end_separator"));//行末尾是否带多余分隔符
            tparam.put("charset",sqlRow.getString("charset"));//字符集
            tparam.put("synch_type",sqlRow.getString("synch_type"));//同步类型
            tparam.put("skip_no_file",sqlRow.getString("skip_no_file"));//文件不存在是否跳过
        }
        return tparam;
    }

    public void clearTable (String tableName) throws Exception {
        update("truncate table " + tableName);
    }

    public void deleteTable (String tableName, String order_id) throws Exception {
        update("delete from " + tableName + " where order_id = '" + order_id + "' ");
    }

}
