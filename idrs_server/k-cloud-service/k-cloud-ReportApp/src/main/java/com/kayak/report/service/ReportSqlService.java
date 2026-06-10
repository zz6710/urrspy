package com.kayak.report.service;

import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.dao.ReportSqlDao;
import com.kayak.report.model.ReportSql;

@Service
@APIDefine(desc = "报表Sql服务", model = ReportSql.class)
public class ReportSqlService {

    @Autowired
    private ReportSqlDao reportSqlDao;

    @API(desc = "查询报表Sql信息", auth = APIAuth.NO)
    public SqlResult<ReportSql> findReportSqls(SqlParam<ReportSql> params) throws Exception {
        params.setMakeSql(true);
        return reportSqlDao.findReportSqls(params);
    }

    @API(desc = "添加报表Sql", auth = APIAuth.NO)
    public String addReportSql(SqlParam<ReportSql> params) throws Exception {
        reportSqlDao.addReportSql(params);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

}
