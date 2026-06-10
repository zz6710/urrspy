package com.kayak.rpt.rhzj.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.rhzj.dao.ReportPVDDao;
import com.kayak.rpt.rhzj.model.ReportPVD;
import com.kayak.rpt.rhzj.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "资产负债信息服务", model = ReportPVD.class)
public class ReportPVDService {

    @Autowired
    private ReportPVDDao reportPVDDao;

    private static final Logger log = LoggerFactory.getLogger(ReportPVDService.class);

    @Autowired
    private ComnDao comnDao;

    @API(desc = "查询资产负债信息信息", auth = APIAuth.YES)
    public SqlResult<ReportPVD> findReportPVDs(SqlParam<ReportPVD> params) throws Exception {
        params.setMakeSql(true);
        return reportPVDDao.findReportPVDs(params);
    }

    @API(desc = "添加资产负债信息", params = "id,report_date,prod_code,pbc_assetscode,data_type,cny,end_amount,end_amount_rmb", auth = APIAuth.NO)
    public int addReportPVD(SqlParam<ReportPVD> params) throws Exception {
        return reportPVDDao.addReportPVD(params).getEffect();
    }

    @API(desc = "修改资产负债信息", params = "id,report_date,prod_code,pbc_assetscode,data_type,cny,end_amount,end_amount_rmb", auth = APIAuth.NO)
    public int updateReportPVD(SqlParam<ReportPVD> params) throws Exception {
        return reportPVDDao.updateReportPVD(params).getEffect();
    }

    @API(desc = "删除资产负债信息", params = "id,report_date,prod_code,pbc_assetscode,data_type,cny,end_amount,end_amount_rmb", auth = APIAuth.NO)
    public int deleteReportPVD(SqlParam<ReportPVD> params) throws Exception {
        return reportPVDDao.deleteReportPVD(params).getEffect();
    }

    public SqlResult<ReportPVD> validateReportPVDsAmount(SqlParam<ReportPVD> params) throws Exception {
        return reportPVDDao.validateReportPVDsAmount(params);
    }

    public SqlResult<SqlRow> validateReportPVDsSum(SqlParam<ReportPVD> params) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("reportDate", params.getModel().getReportDate());
        List<SqlRow> sqlRows = reportPVDDao.validateReportPVDsSum(param);
        SqlResult<SqlRow> sqlResult = new SqlResult<>();
        sqlResult.setRows(sqlRows);
        return sqlResult;
    }

    public void importReportPVDData(List<ReportPVD> reportPVDS) {
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("queryDate", reportPVDS.get(0).getReportDate().replace("-", ""));
            reportPVDDao.deleteReportPVDByReportDate(params);
            for (ReportPVD reportPVD : reportPVDS) {
                Map<String, Object> map = MapUtil.toMap(reportPVD);
                //处理日期格式
                map.put("reportDate", reportPVD.getReportDate().replace("-", ""));
                map.put("dataType", StringUtils.isNotBlank(reportPVD.getDataType()) ? reportPVD.getDataType().split("-")[0] : StringUtils.EMPTY);
                reportPVDDao.addReportPVD(map);
            }
        } catch (Exception e) {
            log.error("导入资产负债信息异常：",e);
            throw new RuntimeException();
        }
    }

}
