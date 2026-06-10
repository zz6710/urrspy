package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.dao.ReportXmlSqlDao;
import com.kayak.report.model.ReportXmlSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报表条件配置服务", model = ReportXmlSql.class)
public class ReportXmlSqlService {

    @Autowired
    private ReportXmlSqlDao reportXmlSqlDao;

    @API(desc = "查询数据源关系", auth = APIAuth.YES)
    public SqlResult<ReportXmlSql> findXmlSqlInfo(SqlParam<ReportXmlSql> params) throws Exception {
        return reportXmlSqlDao.findXmlSqlInfo(params);
    }

}
