package com.kayak.rpt.validate.service;

import com.alibaba.druid.util.StringUtils;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.validate.dao.ReportDataValidationDao;
import com.kayak.rpt.validate.model.ReportValidationModel;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "报送数据校验", model = ReportValidationModel.class)
public class ReportDataValidateResService {
    
    @Autowired
    private ReportDataValidationDao reportDataValidationDao;

    @API(desc = "报送数据校验结果查询", auth = APIAuth.YES)
    public SqlResult<ReportValidationModel> findReportValidationResultInformation(SqlParam<ReportValidationModel> params) throws Exception {
        //将计算的数据日期进行覆盖
        if (!StringUtils.isEmpty(params.getModel().getReportTable())) {
            params.getModel().setDealDate(reportDataValidationDao.checkDataDate(params));
        }
        return reportDataValidationDao.queryReportValidationResult(params);
    }

    @API(desc = "获取所有需要校验的报表信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getAllCheckingReport(SqlParam<ReportValidationModel> params) throws Exception {
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        List<SqlRow> reportTableList = reportDataValidationDao.getCheckTableList();
        sqlRowSqlResult.setResults(reportTableList.size());
        sqlRowSqlResult.setRows(reportTableList);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }

    @API(desc = "报送数据校验结果查询", auth = APIAuth.YES)
    public String doCheckReportIndex(SqlParam<ReportValidationModel> params) throws Exception {
        Map<String, Object> report_params = RequestSupport.getParameters();



        Map<String, Object> map = new HashMap<>();
        return RequestSupport.updateReturnJson(true, "操作成功!", map).toString();
    }

}
