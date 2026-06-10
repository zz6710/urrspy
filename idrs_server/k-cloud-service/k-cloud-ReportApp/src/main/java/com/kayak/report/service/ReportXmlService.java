package com.kayak.report.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.report.dao.ReportXmlDao;
import com.kayak.report.dao.ReportXmlSqlDao;
import com.kayak.report.model.ReportXml;
import com.kayak.report.model.ReportXmlSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "报表XML文件", model = ReportXml.class)
public class ReportXmlService {

    @Autowired
    private DaoService daoService;

    @Autowired
    private ReportXmlDao reportXmlDao;

    @Autowired
    private ReportXmlSqlDao reportXmlSqlDao;

    @API(desc = "查询XML信息", auth = APIAuth.YES)
    public SqlResult<ReportXml> findXmlInfo(SqlParam<ReportXml> params) throws Exception {
        return reportXmlDao.findXmlInfo(params);
    }

    @API(desc = "查询TreeList列排序", auth = APIAuth.YES)
    public SqlResult<ReportXml> findTreeListTableSort(SqlParam<ReportXml> params) throws Exception {
        return reportXmlDao.findTreeListTableSort(params);
    }

    @API(desc = "查询报表XML文件信息", auth = APIAuth.YES)
    public String findReportXmls(SqlParam<ReportXml> params) throws Exception {
        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("forTable", params.getModel().getForTable());
        SqlRow reportXmls = reportXmlDao.findReportXmls(new FetcherData<>(mapParams, ReportXml.class));
        SqlRow reportXmlSqls = reportXmlSqlDao.findReportXmlSqls(new FetcherData<>(mapParams, ReportXmlSql.class));
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("tableSort", reportXmls.getString("table_sort"));
        json.put("xml", reportXmls.getString("xml"));
        json.put("exeid", reportXmlSqls.getString("exeid"));
        return json.toString();
    }

    @API(desc = "添加TressList的XML文件", auth = APIAuth.NO)
    public String addTreeListXml(SqlParam<ReportXml> params) throws Exception {
        String loginname = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_loginname"));
        Map<String, Object> mapParams = params.getParams();
        mapParams.put("inputuser", loginname);
        mapParams.put("crtDate", Tools.dt2Date1(new Date()));
        mapParams.put("crtTime", Tools.dt2Time1(new Date()));
        FetcherData<ReportXmlSql> reportXmlSqlFetcherData = new FetcherData<>(mapParams, ReportXmlSql.class);
        FetcherData<ReportXml> reportXmlFetcherData = new FetcherData<>(mapParams, ReportXml.class);
        daoService.doTrans(() -> {
            reportXmlSqlDao.deleteReportXmlSql(reportXmlSqlFetcherData);
            reportXmlSqlDao.insertXmlSql(reportXmlSqlFetcherData);
            reportXmlDao.deleteReportXml(reportXmlFetcherData);
            reportXmlDao.insertXml(reportXmlFetcherData);
        });
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

    @API(desc = "添加Report的XML文件", auth = APIAuth.NO)
    public String addReportXml(SqlParam<ReportXml> params) throws Exception {
        String loginname = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_loginname"));
        JSONArray exeids = JSON.parseArray(params.getModel().getExeid());
        JSONArray dss = JSON.parseArray(params.getModel().getDs());
        Map<String, Object> mapParams = params.getParams();

        mapParams.put("inputuser", loginname);
        mapParams.put("crtDate", Tools.dt2Date1(new Date()));
        mapParams.put("crtTime", Tools.dt2Time1(new Date()));
        FetcherData<ReportXml> reportXmlFetcherData = new FetcherData<>(mapParams, ReportXml.class);
        daoService.doTrans(() -> {
            reportXmlSqlDao.deleteReportXmlSql(new FetcherData<>(mapParams, ReportXmlSql.class));
            for (int i = 0; i < dss.size(); i++) {
                if(dss.get(i) == null || dss.get(i).equals(""))
                    continue;
                mapParams.put("exeid", exeids.get(i));
                mapParams.put("ds", dss.get(i));
                reportXmlSqlDao.insertXmlSql(new FetcherData<>(mapParams, ReportXmlSql.class));
            }
            reportXmlDao.deleteReportXml(reportXmlFetcherData);
            reportXmlDao.insertXml(reportXmlFetcherData);
        });
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

}
