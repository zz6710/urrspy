package com.kayak.rpt.rhzj.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzj.dao.ReportPCDDao;
import com.kayak.rpt.rhzj.model.ReportPCD;
import com.kayak.rpt.rhzj.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品募集信息服务", model = ReportPCD.class)
public class ReportPCDService {

    @Autowired
    private ReportPCDDao reportPCDDao;

    @API(desc = "查询产品募集信息信息", auth = APIAuth.YES)
    public SqlResult<ReportPCD> findReportPCDs(SqlParam<ReportPCD> params) throws Exception {
        params.setMakeSql(true);
        return reportPCDDao.findReportPCDs(params);
    }

    @API(desc = "添加产品募集信息", params = "id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount,current_buy_amount_rmb,current_buy_vol,current_redemption_amount,current_redemption_amountrmb,current_redemption_vol,termina_prod_amount,termina_prod_amount_rmb,termina_prod_vol,termina_prod_nav,termina_prod_nav_rmb,termina_prod_nav_add,termina_prod_nav_add_rmb,prod_max_rate,prod_min_rate", auth = APIAuth.NO)
    public int addReportPCD(SqlParam<ReportPCD> params) throws Exception {
        return reportPCDDao.addReportPCD(params).getEffect();
    }

    @API(desc = "修改产品募集信息", params = "id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount,current_buy_amount_rmb,current_buy_vol,current_redemption_amount,current_redemption_amountrmb,current_redemption_vol,termina_prod_amount,termina_prod_amount_rmb,termina_prod_vol,termina_prod_nav,termina_prod_nav_rmb,termina_prod_nav_add,termina_prod_nav_add_rmb,prod_max_rate,prod_min_rate", auth = APIAuth.NO)
    public int updateReportPCD(SqlParam<ReportPCD> params) throws Exception {
        return reportPCDDao.updateReportPCD(params).getEffect();
    }

    @API(desc = "删除产品募集信息", params = "id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount,current_buy_amount_rmb,current_buy_vol,current_redemption_amount,current_redemption_amountrmb,current_redemption_vol,termina_prod_amount,termina_prod_amount_rmb,termina_prod_vol,termina_prod_nav,termina_prod_nav_rmb,termina_prod_nav_add,termina_prod_nav_add_rmb,prod_max_rate,prod_min_rate", auth = APIAuth.NO)
    public int deleteReportPCD(SqlParam<ReportPCD> params) throws Exception {
        return reportPCDDao.deleteReportPCD(params).getEffect();
    }

    public void importReportPCDData(List<ReportPCD> reportPCDS) {
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("queryDate", reportPCDS.get(0).getReportDate().replace("-", ""));
            reportPCDDao.deleteReportPCDByReportDate(params);
            for (ReportPCD reportPCD : reportPCDS) {
                Map<String, Object> map = MapUtil.toMap(reportPCD);
                map.put("reportDate", reportPCD.getReportDate().replace("-", ""));
                map.put("custType", StringUtils.isNotBlank(reportPCD.getCustType()) ? reportPCD.getCustType().split("-")[0] : null);
                reportPCDDao.addReportPCD(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
