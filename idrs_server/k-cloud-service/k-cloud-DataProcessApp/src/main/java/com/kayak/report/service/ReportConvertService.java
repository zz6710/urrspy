package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.dao.ReportConvertDao;
import com.kayak.report.model.ReportConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报表模板转换服务", model = ReportConvert.class)
public class ReportConvertService {

    @Autowired
    private ReportConvertDao reportConvertDao;

    @API(desc = "获取报表转换操作日志列表", auth = APIAuth.YES)
    public SqlResult<ReportConvert> findReportConvert(SqlParam<ReportConvert> params) throws Exception {
        return reportConvertDao.findReportConvert(params);
    }

    @API(desc = "报表模板列表", auth = APIAuth.YES)
    public SqlResult<ReportConvert> findReportTemplateList(SqlParam<ReportConvert> params) throws Exception {
        return reportConvertDao.findReportTemplateList(params);
    }

}
