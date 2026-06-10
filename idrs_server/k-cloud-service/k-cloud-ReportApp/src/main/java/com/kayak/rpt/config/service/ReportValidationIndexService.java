package com.kayak.rpt.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.config.dao.ReportDataValidationIndexDao;
import com.kayak.rpt.config.model.ReportValidationIndexModel;
import com.kayak.rpt.validate.model.ReportValidationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "报送数据校验指标", model = ReportValidationIndexModel.class)
public class ReportValidationIndexService {
    
    @Autowired
    private ReportDataValidationIndexDao reportDataValidationIndexDao;

    @API(desc = "报送数据校验指标配置信息查询", auth = APIAuth.NO)
    public SqlResult<ReportValidationIndexModel> findReportValidationIndexConfigInformation(SqlParam<ReportValidationIndexModel> params) throws Exception {
        return reportDataValidationIndexDao.queryReportValidationIndexConfig(params);
    }

    @API(desc = "删除报送数据指标校验", auth = APIAuth.NO)
    public String deleteReportDataValidationIndex(SqlParam<ReportValidationIndexModel> params) throws Exception {
        reportDataValidationIndexDao.deleteReportDataIndex(params.getParams());
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "获取报表数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getReportTableDict(SqlParam<ReportValidationIndexModel> param) throws Exception {
        SqlResult<SqlRow> result = new SqlResult<SqlRow>();
        List<SqlRow> sqlRowSqlResult  = reportDataValidationIndexDao.getReportTableDict();
        result.setRows(sqlRowSqlResult);
        return result;
    }

    /**
     * 校验指标停用
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "停用", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String stopIndexStatus(SqlParam<ReportValidationIndexModel> params) throws Exception {
        boolean result = reportDataValidationIndexDao.stopIndexStatus(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "停用成功" : "停用失败", null).toString();
    }

    /**
     * 校验指标生效
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "启用", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String recoverIndexStatus(SqlParam<ReportValidationIndexModel> params) throws Exception {
        boolean result = reportDataValidationIndexDao.recoverIndexStatus(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "启用成功" : "启用失败", null).toString();
    }

    /**
     * 修改校验指标信息
     * @param params
     * @throws Exception
     */
    public void updateReportValidationIndexMethod(Map<String, Object> params) throws Exception {
        reportDataValidationIndexDao.updateReportDataIndex(params);
    }

    /**
     * 新增校验指标信息
     * @param params
     * @throws Exception
     */
    public void addReportValidationIndexMethod(Map<String, Object> params) throws Exception {
        reportDataValidationIndexDao.addReportDataIndex(params);
    }

    public List<SqlRow> findReportValidationIndexConfigInformationByIndex(Map<String, Object> params) throws Exception {
        return reportDataValidationIndexDao.queryReportValidationIndexConfigByIndex(params);
    }
}
