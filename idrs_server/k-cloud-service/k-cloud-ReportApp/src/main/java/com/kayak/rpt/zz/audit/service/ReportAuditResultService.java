package com.kayak.rpt.zz.audit.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.zz.audit.model.ReportAuditResult;
import com.kayak.rpt.zz.dao.ReportAuditResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ty
 * @since 2023-05-18 11:22:11
 */
@Service
@APIDefine(desc = "报送数据复核结果", model = ReportAuditResult.class)
public class ReportAuditResultService {
    @Autowired
    private ReportAuditResultDao reportAuditResultDao;
    @API(desc = "添加估值信息错误", params = "bank_code_desc,asset_code_desc,valuation_date_desc,unit_debt_net_desc,unit_debt_full_desc,details_desc,register_serno,imp_date", auth = APIAuth.NO)
    public int updateAuditResult(SqlParam<ReportAuditResult> params) throws Exception {
        return reportAuditResultDao.updateAuditResult(params).getEffect();
    }
    public int updateAuditResult(ReportAuditResult reportAuditResult) throws Exception {
        return reportAuditResultDao.updateAuditResult(reportAuditResult).getEffect();
    }

    public int getIndexStatus(ReportAuditResult reportAuditResult) throws Exception {
        return reportAuditResultDao.getIndexStatus(reportAuditResult);
    }

    public int getCheckTable(ReportAuditResult reportAuditResult) throws Exception {
        return reportAuditResultDao.getCheckTable(reportAuditResult);
    }
}
