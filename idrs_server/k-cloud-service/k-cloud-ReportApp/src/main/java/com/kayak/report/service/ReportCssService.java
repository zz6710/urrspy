package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.report.dao.ReportCssDao;
import com.kayak.report.model.ReportCss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报表样式服务", model = ReportCss.class)
public class ReportCssService {

    @Autowired
    private ReportCssDao reportCssDao;

    @API(desc = "获取页面查询条件", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<ReportCss> findHtmlInfo(SqlParam<ReportCss> params) throws Exception {
        return reportCssDao.findHtmlInfo(params);
    }

    @API(desc = "查询页面相关控件", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<ReportCss> findReportCss(SqlParam<ReportCss> params) throws Exception {
        return reportCssDao.findReportCss(params);
    }

    @API(desc = "查询属性控件列表", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<ReportCss> findById(SqlParam<ReportCss> params) throws Exception {
        return reportCssDao.findById(params);
    }

    @API(desc = "添加页面相关控件", auth = APIAuth.NO)
    public String addReportCss(SqlParam<ReportCss> params) throws Exception {
        reportCssDao.addReportCss(params);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

}
