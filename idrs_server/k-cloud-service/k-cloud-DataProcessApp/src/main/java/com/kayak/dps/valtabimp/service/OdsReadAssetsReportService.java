package com.kayak.dps.valtabimp.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.valtabimp.model.OdsReadAssetsReport;
import com.kayak.dps.valtabimp.repository.OdsReadAssetsReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "解析资产配置服务", model = OdsReadAssetsReport.class)
public class OdsReadAssetsReportService {

    @Autowired
    private OdsReadAssetsReportDao odsReadAssetsReportDao;
    
    @API(desc = "查询", auth = APIAuth.NO)
    public SqlResult<OdsReadAssetsReport> findOdsReadAssetsReports(SqlParam<OdsReadAssetsReport> params) throws Exception {
        params.setMakeSql(false);
        return odsReadAssetsReportDao.findOdsReadAssetsReports(params);
    }

    @API(desc = "添加", auth = APIAuth.NO)
    public int addOdsReadAssetsReport(SqlParam<OdsReadAssetsReport> params) throws Exception {
        return odsReadAssetsReportDao.addOdsReadAssetsReport(params).getEffect();
    }

    @API(desc = "修改",  auth = APIAuth.NO)
    public int updateOdsReadAssetsReport(SqlParam<OdsReadAssetsReport> params) throws Exception {
        return odsReadAssetsReportDao.updateOdsReadAssetsReport(params).getEffect();
    }

    @API(desc = "删除", auth = APIAuth.NO)
    public int deleteOdsReadAssetsReport(SqlParam<OdsReadAssetsReport> params) throws Exception {
        return odsReadAssetsReportDao.deleteOdsReadAssetsReport(params).getEffect();
    }

    @API(desc = "唯一校验", auth = APIAuth.NO)
    public SqlResult<OdsReadAssetsReport> findOnlyOdsReadAssetsReports(SqlParam<OdsReadAssetsReport> params) throws Exception {
        params.setMakeSql(false);
        return odsReadAssetsReportDao.findOnlyOdsReadAssetsReports(params);
    }
}
