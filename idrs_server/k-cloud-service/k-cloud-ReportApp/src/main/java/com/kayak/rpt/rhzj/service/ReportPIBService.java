package com.kayak.rpt.rhzj.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzj.dao.ReportPIBDao;
import com.kayak.rpt.rhzj.model.ReportPIB;
import com.kayak.rpt.rhzj.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品起始募集信息服务", model = ReportPIB.class)
public class ReportPIBService {

    @Autowired
    private ReportPIBDao reportPIBDao;

    private static final Logger log = LoggerFactory.getLogger(ReportPIBService.class);

    @API(desc = "查询产品起始募集信息信息", auth = APIAuth.YES)
    public SqlResult<ReportPIB> findReportPIBs(SqlParam<ReportPIB> params) throws Exception {
        params.setMakeSql(true);
        return reportPIBDao.findReportPIBs(params);
    }

    @API(desc = "添加产品起始募集信息", params = "id,peoplebank_submitcode,report_date,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol", auth = APIAuth.NO)
    public int addReportPIB(SqlParam<ReportPIB> params) throws Exception {
        return reportPIBDao.addReportPIB(params).getEffect();
    }

    @API(desc = "修改产品起始募集信息", params = "id,peoplebank_submitcode,report_date,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol", auth = APIAuth.NO)
    public int updateReportPIB(SqlParam<ReportPIB> params) throws Exception {
        return reportPIBDao.updateReportPIB(params).getEffect();
    }

    @API(desc = "删除产品起始募集信息", params = "id,peoplebank_submitcode,report_date,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol", auth = APIAuth.NO)
    public int deleteReportPIB(SqlParam<ReportPIB> params) throws Exception {
        return reportPIBDao.deleteReportPIB(params).getEffect();
    }

    @API(desc = "根据日期查询产品起始募集信息", auth = APIAuth.YES)
    public SqlResult<ReportPIB> findReportPIBsByReportDate(SqlParam<ReportPIB> params) throws Exception {
        return reportPIBDao.findReportPIBsByReportDate(params);
    }

    public void importReportPIBData(List<ReportPIB> reportPIBS, Map<String,Object> params) {
        try {
            reportPIBDao.deleteReportPIBByReportDate(params);
            for (ReportPIB reportPIB : reportPIBS){
                Map<String, Object> map = MapUtil.toMap(reportPIB);
                map.put("reportDate",reportPIB.getReportDate().replace("-",""));
                map.put("custType", StringUtils.isNotBlank(reportPIB.getCustType()) ? reportPIB.getCustType().split("-")[0] : null);
                reportPIBDao.addReportPIB(map);
            }
        } catch (Exception e) {
            log.error("导入起始募集信息异常：",e);
            throw new RuntimeException();
        }
    }


}
