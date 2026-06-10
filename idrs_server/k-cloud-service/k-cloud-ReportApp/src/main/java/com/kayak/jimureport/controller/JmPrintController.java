package com.kayak.jimureport.controller;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;

import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.jimureport.report.dao.JmPrintDao;
import org.apache.commons.collections4.MapUtils;
import org.json.JSONArray;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/JmPrint")
@Scope("prototype")
public class JmPrintController extends BaseController {

    @Resource(name = "jmPrintDao")
    private JmPrintDao jmPrintDao;


    Map<String, Object> params = RequestSupport.getParameters();

    private static Map<String, Object> PrintParams = null;

    private static Map<String, Object> EditParams = null;

    @RequestMapping(value = "/jmPrintAPI", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject jmPrintApi(HttpServletRequest request) throws Exception {
        /*获取默认值*/
        JmReportController jmReportController = new JmReportController();
        jmReportController.getDeFaultParam(jmPrintDao.findJmReportSql(params));
        /*获取查询条件*/
        params = jmReportController.fun(request);
        JSONObject jsonObject = new JSONObject();
        if (params.containsKey("isView")) {
            if ("true".equals(params.get("isView"))) {
                params.put("anniu_id", PrintParams.get("anniu_id"));
                params.put("id", PrintParams.get("id"));
                params.put("t8deal_id", PrintParams.get("t8deal_id"));
                params.put("sys_user_loginname", PrintParams.get("loginuser"));
            } else {
                params.put("anniu_id", EditParams.get("botton_ids"));
                params.put("id", EditParams.get("id"));
                params.put("t8deal_id", "100487");
                params.put("sys_user_loginname", EditParams.get("loginuser"));
            }
        }
        /*获取查询SQL*/
        SqlRow sql = jmPrintDao.getJmPrintSql(params).get(0);
        String str = sql.getString("sql");
        params.put("newSql", str);
        List<SqlRow> sqlResult = jmPrintDao.sqlQuery(str, params);
        jsonObject.put("data", sqlResult);
        return jsonObject;
    }

    /*保存SQL接口*/
    @ResponseBody
    @RequestMapping(value = "/getComParams.json")
    public String getComParams() {
        try {
            JSONObject jsonObject = new JSONObject();
            PrintParams = RequestSupport.getParameters();
            SqlRow count = jmPrintDao.getJmPrintCount(params);
            if (!count.isEmpty()) {
                JSONArray rowArr = new JSONArray();
                jsonObject.put("rows", rowArr);
                jsonObject.put("count", count.getString("count"));
                jsonObject.put("results", 0);
            }
            return jsonObject.toString();
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getEditParams.json")
    public String getEditParams() {
        try {
            JSONObject jsonObject = new JSONObject();
            EditParams = RequestSupport.getParameters();
            funEditParams(EditParams, jsonObject);
            return jsonObject.toString();
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    public void funEditParams(Map<String, Object> map, JSONObject jsonObject) {
        if (MapUtils.isNotEmpty(map)) {
            JSONArray rowArr = new JSONArray();
            jsonObject.put("rows", rowArr);
            jsonObject.put("results", 0);
        }
    }

    /*查询SQL保存接口*/
    @ResponseBody
    @RequestMapping(value = "/jmPrint.json")
    public Object jmPrint() {
        try {
            params = RequestSupport.getParameters();
            String sql = params.get("sql").toString();
            params.put("sql", sql);
            SqlRow count = jmPrintDao.getJmPrintCount(params);
            if (!count.isEmpty()) {
                params.put("count", count.getString("count"));
            }
            setJmPrint(params);
            return updateSuccess("SQL保存成功");
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setJmPrint(Map<String, Object> params)
            throws Exception {
        try {
            jmPrintDao.insertJmPrintSql(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    /*保存SQL接口*/
    @ResponseBody
    @RequestMapping(value = "/updateJmPrint.json")
    public Object updateJmPrint() {
        JmReportController jmReportController = new JmReportController();
        jmReportController.getSql();
        try {
            updateJmPrint(params);
            return updateSuccess("SQL保存成功");
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateJmPrint(Map<String, Object> params) throws Exception {
        try {
            jmPrintDao.updateJmPrintSql(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/deleteJmPrint.json")
    public Object deleteJmPrint() {
        try {
            jmPrintDao.deleteJmPrintSql(params);
            return updateSuccess("SQL删除成功");
        } catch (Exception e) {
            return updateFailure("SQL删除失败");
        }
    }
}
