package com.kayak.jimureport.report.dao;


import com.kayak.base.dao.ComnDao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Repository(value = "xmlDao")
public class XmlDao extends ComnDao {

/*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> setXml(Map<String, Object> params)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PreparedSqlStatement st, prst;
        try {
            prst = new PreparedSqlStatement(
                    "delete from sys_report_XML where for_table = $S{for_table}",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();
            st = new PreparedSqlStatement(
                    "insert into sys_report_XML(id,for_table,xml,inputuser,crt_date,crt_time,upd_date,upd_time)"
                            + " values($AUTOID{sys_report_XML_id},$S{for_table},$c{xml_info},$S{sys_user_loginname},$S{SYSDATE},$s{SYSTIME},null,null)",
                    getJdbcTemplate());
            st.autoSetParams(params);
            st.executeUpdate();
            map.put("success", "success");
            return map;
        } catch (Exception e) {

            throw new Exception("xml数据插入到数据库报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> setTreeXml(Map<String, Object> params)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PreparedSqlStatement st, prst, d1, p1;
        try {
            d1 = new PreparedSqlStatement(
                    "delete from sys_report_XML_sql where for_table = $S{for_table}",
                    getJdbcTemplate());
            d1.autoSetParams(params);
            d1.executeUpdate();

            p1 = new PreparedSqlStatement(
                    "insert into sys_report_XML_sql(id,for_table,exeid,inputuser,crt_date,crt_time,upd_date,upd_time)"
                            + " values($AUTOID{sys_report_XML_sql},$S{for_table},$S{xml_exeid},$S{sys_user_loginname},$S{SYSDATE},$s{SYSTIME},null,null)",
                    getJdbcTemplate());
            p1.autoSetParams(params);
            p1.executeUpdate();

            prst = new PreparedSqlStatement(
                    "delete from sys_report_XML where for_table = $S{for_table}",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();
            st = new PreparedSqlStatement(
                    "insert into sys_report_XML(id,for_table,xml,table_sort,inputuser,crt_date,crt_time,upd_date,upd_time)"
                            + " values($AUTOID{sys_report_XML_id},$S{for_table},$c{xml_info},$S{table_sort},$S{sys_user_loginname},$S{SYSDATE},$s{SYSTIME},null,null)",
                    getJdbcTemplate());
            st.autoSetParams(params);
            st.executeUpdate();
            map.put("success", "success");
            return map;
        } catch (Exception e) {

            throw new Exception("xml数据插入到数据库报错");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setSql(Map<String, Object> params)
            throws Exception {
        PreparedSqlStatement st, prst;
        try {
            //"insert into sys_report_xml_sql values((select nvl(max(id),0)+1 from sys_report_xml_sql),$S{for_table},$S{ds},$S{sql_exeid},$S{sys_user_loginname},$U{SYSDATE},$U{SYSTIME},null,null)",
            prst = new PreparedSqlStatement(
                    "insert into sys_report_xml_sql values($AUTOID{id},$S{for_table},$S{ds},$S{sql_exeid},$S{sys_user_loginname},$U{SYSDATE},$U{SYSTIME},null,null)",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();

            st = new PreparedSqlStatement(
                    "insert into sys_report_sql (id,exeid,sqlid,report_sql,inputuser,crt_date,crt_time,upd_date,upd_time) values"
                            + "($AUTOID{sys_report_sql_id},$S{sql_exeid},$S{sql_id},$c{report_sql},$S{sys_user_loginname},$U{SYSDATE},$U{SYSTIME},null,null)",
                    getJdbcTemplate());
            st.autoSetParams(params);
            st.executeUpdate();
        } catch (Exception e) {
            throw new Exception("sql数据插入到数据库报错");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete_Sql(Map<String, Object> params)
            throws KPromptException {
        PreparedSqlStatement prst;
        try {
            prst = new PreparedSqlStatement(
                    "delete from sys_report_sql s where s.exeid in (select xs.exeid from sys_report_xml_sql xs where xs.for_table=$S{for_table})",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();
            prst = new PreparedSqlStatement(
                    "delete from sys_report_xml_sql where for_table = $S{for_table}",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();
        } catch (Exception e) {

            throw new KPromptException("sql数据插入到数据库报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete_XML_CSS(String for_table)
            throws KPromptException {
        PreparedSqlStatement st, prst;
        try {
            prst = new PreparedSqlStatement(
                    "delete from sys_report_xml_css where for_table ='" + for_table + "'",
                    getJdbcTemplate());
            prst.executeUpdate();
            //删除css数据
            st = new PreparedSqlStatement(
                    "delete from sys_report_css where id in (select sys_report_css_id from sys_report_xml_css where for_table = '" + for_table + "')", getJdbcTemplate());
            st.executeUpdate();
        } catch (Exception e) {

            throw new KPromptException("sql数据删除报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addSql(Map<String, Object> params)
            throws KPromptException {
        PreparedSqlStatement st;
        try {
            //插入css数据
            st = new PreparedSqlStatement(
                    "insert into sys_report_css" +
                            "(id," +
                            " css_id," +
                            " css_name," +
                            " css_type," +
                            " css_class," +
                            " label_discrible," +
                            " crt_date," +
                            " crt_time," +
                            " upd_time," +
                            " upd_date," +
                            " inputuser," +
                            " data_min_length," +
                            " data_max_length," +
                            " data_data," +
                            " data_exeid," +
                            " data_dict," +
                            " data_value_field," +
                            " data_display_field," +
                            " data_workday," +
                            " data_date_format," +
                            " data_value," +
                            " data_allowblank," +
                            " data_min_value," +
                            " data_max_value," +
                            " ligroupid,validatetype,data_sqlinfo,data_on_change)" +
                            "values" +
                            " ($AUTOID{sys_report_css_id}," +
                            "  $S{css_id}," +
                            "  $S{css_name}," +
                            "  $S{css_type}," +
                            "  $S{css_class}," +
                            "  $S{label_discrible}," +
                            "  $U{SYSDATE}," +
                            "  $U{SYSTIME}," +
                            "  null," +
                            "  null," +
                            "  $S{sys_user_loginname}," +
                            "  $S{data_min_length}," +
                            "  $S{data_max_length}," +
                            "  $S{data_data}," +
                            "  $S{data_exeid}," +
                            "  $S{data_dict}," +
                            "  $S{data_value_field}," +
                            "  $S{data_display_field}," +
                            "  $S{data_workday}," +
                            "  $S{data_date_format}," +
                            "  $S{data_value}," +
                            "  $S{data_allowblank}," +
                            "  $S{data_min_value}," +
                            "  $S{data_max_value}," +
                            "  $S{ligroupid}," +
                            "  $s{validatetype}," +
                            "  $c{data_sqlinfo},$s{data_on_change})",
                    getJdbcTemplate());
            st.autoSetParams(params);
            st.executeUpdate();

            //新增xml_css关系
            st = new PreparedSqlStatement(
                    "insert into sys_report_xml_css (id,sys_report_css_id,for_table,inputuser,crt_date,crt_time,upd_date,upd_time)" +
                            "values($AUTOID{sys_report_xml_css_id},$S{sys_report_css_id},$S{for_table},$S{sys_user_loginname},$U{SYSDATE},$U{SYSTIME},null,null)", getJdbcTemplate());
            st.autoSetParams(params);
            st.executeUpdate();
        } catch (Exception e) {

            throw new KPromptException("sql数据插入到数据库报错");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> getXml(Map<String, Object> params)
            throws Exception {
        Map<String, Object> str_Map = new HashMap<String, Object>();
        String xml_info = "";
        String exeid = "";
        String sql = "select x.id,x.for_table,x.xml,s.exeid from sys_report_XML x left join sys_report_xml_sql s on x.for_table=s.for_table where x.for_table = $S{for_table}";
        try {
            SqlResult sr = SysBeans.getComnDao().sqlQuery(sql, "dssys", params);
            if (sr.next()) {
                xml_info = sr.getString("xml");
                exeid = sr.getString("exeid");
            } else {
                throw new Exception("报表ID没有对应的xml文件");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        str_Map.put("xml_info", xml_info);
        str_Map.put("exeid", exeid);
        return str_Map;
    }

    //create by wanggq 20181113 start
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> getFreeFormXml(Map<String, Object> params) throws Exception {
        Map<String, Object> str_Map = new HashMap<String, Object>();
        String xml_info[] = null;
        String exeid[] = null;
        String sql = "select t.xml,t.exeid from sys_report_xml t where t.for_table = $S{for_table} order by t.exeid";
        try {
            SqlResult sr = SysBeans.getComnDao().sqlQuery(sql, "dssys", params);
            if (sr != null && sr.getCount() > 0) {
                xml_info = new String[(int) sr.getCount()];
                exeid = new String[(int) sr.getCount()];

                int i = 0;
                while (sr.next()) {
                    xml_info[i] = sr.getString("xml");
                    exeid[i] = sr.getString("exeid");
                    i++;
                }
            } else {
                throw new Exception("报表ID没有对应的xml文件");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        str_Map.put("xml_info", xml_info);
        str_Map.put("exeid", exeid);
        return str_Map;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteFreeFormXml(Map<String, Object> params) throws KPromptException {
        PreparedSqlStatement prst;
        try {
            prst = new PreparedSqlStatement(
                    "delete from sys_report_xml where for_table = $S{for_table}",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();
        } catch (Exception e) {

            throw new KPromptException("xml数据删除报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setFreeFormXml(Map<String, Object> params) throws KPromptException {
        PreparedSqlStatement prst;
        try {
            //"insert into sys_report_xml values((select nvl(max(id),0)+1 from sys_report_xml),$S{for_table},$S{xml},$S{table_sort},$S{sys_user_loginname},$U{SYSDATE},$U{SYSTIME},null,null,$S{exeid})",
            prst = new PreparedSqlStatement(
                    "insert into sys_report_xml values($AUTOID{id},$S{for_table},$S{xml},$S{table_sort},$S{sys_user_loginname},$U{SYSDATE},$U{SYSTIME},null,null,$S{exeid})",
                    getJdbcTemplate());
            prst.autoSetParams(params);
            prst.executeUpdate();

        } catch (Exception e) {

            throw new KPromptException("xml数据插入到数据库报错");
        }
    }
    //create by wanggq 20181113 end

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String get_mobility_xml(Map<String, Object> params)
            throws Exception {
        String xml_info = "";
        String sql = "select s.sum_content xml from t8_mobility_sum s where s.id=$s{id}";
        try {
            SqlResult sr = SysBeans.getComnDao().sqlQuery(sql, "dssys", params);
            if (sr.next()) {
                xml_info = sr.getString("xml");
            } else {
                throw new Exception("报表ID没有对应的xml文件");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return xml_info;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> getTreeXml(Map<String, Object> params)
            throws KPromptException {
        Map<String, Object> map = new HashMap<String, Object>();
        String xml_info = "", table_sort = "", xml_exeid = "";
        String sql = "select id,for_table,xml,table_sort from sys_report_XML where for_table = $S{for_table}";
        String sql1 = "select for_table,exeid from sys_report_XML_sql where for_table = $S{for_table}";
        try {
            SqlResult sr = SysBeans.getComnDao().sqlQuery(sql, "dssys", params);
            SqlResult sr1 = SysBeans.getComnDao().sqlQuery(sql1, "dssys", params);
            if (sr.next()) {
                xml_info = sr.getString("xml");
                table_sort = sr.getString("table_sort");
            }
            if (sr1.next()) {
                xml_exeid = sr1.getString("exeid");
            }
            map.put("xml_exeid", xml_exeid);
            map.put("xml_info", xml_info);
            map.put("table_sort", table_sort);
        } catch (Exception e) {

        }
        return map;
    }
*/

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setJmReProSql(Map<String, Object> params)
            throws Exception {
        try {
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat f = new SimpleDateFormat(strDateFormat);
            String time = f.format(new Date());
            params.put("time", time);
            super.update("insert into JIMU_SQLDICT (id,REPORT_SQL,menuid,sqltype,upttime,JIMU_REPORT_ID) values "
                    + "($s{id},$s{sql},$s{menuid},'1',$s{time},$s{count}) ", params);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setQuerySql(Map<String, Object> params) throws Exception {
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat f = new SimpleDateFormat(strDateFormat);
        String time = f.format(new Date());
        params.put("time", time);
        try {
            super.update("insert into T8_QUERY_SQL (menuid, query_sql ,upttime,query_name) values"
                       + " ($s{menuid},$s{sql},$S{time},$S{query_name})", params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void uptQuerySql(Map<String, Object> params) throws Exception {
        try {
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat f = new SimpleDateFormat(strDateFormat);
            String time = f.format(new Date());
            params.put("time", time);
            super.update("update JIMU_SQLDICT set REPORT_SQL=$s{sql},upttime=$s{time} where menuid=$S{menuid}", params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateQuery(Map<String, Object> params) throws Exception {
        try {
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat f = new SimpleDateFormat(strDateFormat);
            String time = f.format(new Date());
            params.put("time", time);
            super.update("update T8_QUERY_SQL set " +
                     " query_sql=$S{sql},upttime=$S{time},query_name=$S{query_name}" +
                     " where menuid=$S{menuid}", params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }


}
