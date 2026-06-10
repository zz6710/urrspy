package com.kayak.report.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
public class ReportSqlResultDao extends ComnDao {

    public final static JSONObject emptyJsonObject = new JSONObject();

    public JSONObject getExeReult(Map<String, Object> params) throws Exception {
        String sql = "SELECT report_sql FROM sys_report_sql WHERE exeid = $S{exeid}";
        JSONObject jsonData;
        String report_sql = "";
        try {
            List<SqlRow> sr = getSqlResult(sql, params);
            if (!CollectionUtils.isEmpty(sr)) {
                report_sql = sr.get(0).getString("report_sql");
            }
            List<SqlRow> rss = getSqlResult(report_sql, params);
             jsonData=getTableResults(rss);
        } catch (Exception e) {
            jsonData = emptyJsonObject;
            throw new Exception(e.getMessage());
        }
        return jsonData;
    }

    public List<SqlRow> getSqlResult(String sql, Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRowList = null;
        try {
            sqlRowList = super.findRows(sql, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
        return sqlRowList;
    }

    private JSONObject getTableResults(List<SqlRow> result) {
        if (CollectionUtils.isEmpty(result)) {
            log.info("查询结果集sResult=null");
            return null;
        }
        JSONObject jo =new JSONObject();
        JSONArray ja1=new JSONArray();
        try {
            Iterator<SqlRow> it= result.iterator();
            while (it.hasNext()){
                SqlRow sr =it.next();
              Map mp=   ListOrderedMap.listOrderedMap(sr);
                ja1.add(mp);
            }
            jo.put("rows",ja1);
            jo.put("results",result.size());
            jo.put("totalRows",result.size());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return jo;
    }
}
