package com.kayak.rpt.rhzj.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzj.dao.ReportPIEDao;
import com.kayak.rpt.rhzj.model.ReportPIE;
import com.kayak.rpt.rhzj.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品终止信息服务", model = ReportPIE.class)
public class ReportPIEService {

    @Autowired
    private ReportPIEDao reportPIEDao;

    private static final Logger log = LoggerFactory.getLogger(ReportPIEService.class);

    @API(desc = "查询产品终止信息信息", auth = APIAuth.YES)
    public SqlResult<ReportPIE> findReportPIEs(SqlParam<ReportPIE> params) throws Exception {
        params.setMakeSql(true);
        return reportPIEDao.findReportPIEs(params);
    }

    @API(desc = "添加产品终止信息", params = "id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real,cny,org_ern,org_ern_rmb,cust_ern,cust_ern_rmb,cust_ern_yld", auth = APIAuth.NO)
    public int addReportPIE(SqlParam<ReportPIE> params) throws Exception {
        return reportPIEDao.addReportPIE(params).getEffect();
    }

    @API(desc = "修改产品终止信息", params = "id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real,cny,org_ern,org_ern_rmb,cust_ern,cust_ern_rmb,cust_ern_yld", auth = APIAuth.NO)
    public int updateReportPIE(SqlParam<ReportPIE> params) throws Exception {
        return reportPIEDao.updateReportPIE(params).getEffect();
    }

    @API(desc = "删除产品终止信息", params = "id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real,cny,org_ern,org_ern_rmb,cust_ern,cust_ern_rmb,cust_ern_yld", auth = APIAuth.NO)
    public int deleteReportPIE(SqlParam<ReportPIE> params) throws Exception {
        return reportPIEDao.deleteReportPIE(params).getEffect();
    }

    public void importReportPIEData(List<ReportPIE> reportPIES, Map<String, Object> params) {
        try {
            reportPIEDao.deleteReportPIEForReportDate(params);
            for (ReportPIE reportPIE : reportPIES) {
                Map<String, Object> map = MapUtil.toMap(reportPIE);
                map.put("endDateReal", reportPIE.getEndDateReal().replace("-", ""));
                reportPIEDao.addReportPIE(map);
            }
        } catch (Exception e) {
            log.error("导入产品终止信息报错：", e);
            throw new RuntimeException();
        }
    }

}
