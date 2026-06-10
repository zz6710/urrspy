package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.report.dao.ReportConditionDao;
import com.kayak.report.model.ReportCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报表控件类型服务", model = ReportCondition.class)
public class ReportConditionService {

    @Autowired
    private ReportConditionDao reportConditionDao;

    @API(desc = "查询报表菜单", auth = APIAuth.YES)
    public SqlResult<ReportCondition> findReportMenu(SqlParam<ReportCondition> params) throws Exception {
        params.setMakeSql(true);
        return reportConditionDao.findReportMenu(params);
    }

    @API(desc = "新增报表菜单", auth = APIAuth.YES)
    public String addReportMenu(SqlParam<ReportCondition> params) throws Exception {
        if(reportConditionDao.checkPrimary(params)){
            throw new PromptException("菜单ID重复！");
        }
        reportConditionDao.addReportMenu(params);
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "修改报表菜单", auth = APIAuth.YES)
    public String updateReportMenu(SqlParam<ReportCondition> params) throws Exception {
        reportConditionDao.updateReportMenu(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

}
