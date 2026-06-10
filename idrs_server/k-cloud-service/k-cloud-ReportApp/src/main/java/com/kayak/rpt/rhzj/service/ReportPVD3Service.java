package com.kayak.rpt.rhzj.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzj.dao.ReportPVD3Dao;
import com.kayak.rpt.rhzj.model.ReportPVD3;
import com.kayak.rpt.rhzj.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "人行资金信托PVD3服务", model = ReportPVD3.class)
public class ReportPVD3Service {

	@Autowired
	private ReportPVD3Dao reportPVD3Dao;

	private static final Logger log = LoggerFactory.getLogger(ReportPVD3Service.class);

	@API(desc = "查询人行资金信托PVD3信息", auth = APIAuth.YES)
	public SqlResult<ReportPVD3> findReportPVD3s(SqlParam<ReportPVD3> params) throws Exception {
		params.setMakeSql(true);
		return reportPVD3Dao.findReportPVD3s(params);
	}

	@API(desc = "添加人行资金信托PVD3", params = "id,report_date,prod_code,pbc_assetscode,stock_type,orgno,cny,prod_amount,prod_amount_rmb,product_code", auth = APIAuth.NO)
	public int addReportPVD3(SqlParam<ReportPVD3> params) throws Exception {
		return reportPVD3Dao.addReportPVD3(params).getEffect();
	}
	
	@API(desc = "修改人行资金信托PVD3", params = "id,report_date,prod_code,pbc_assetscode,stock_type,orgno,cny,prod_amount,prod_amount_rmb,product_code", auth = APIAuth.NO)
	public int updateReportPVD3(SqlParam<ReportPVD3> params) throws Exception {
		return reportPVD3Dao.updateReportPVD3(params).getEffect();
	}
	
	@API(desc = "删除人行资金信托PVD3", params = "id,report_date,prod_code,pbc_assetscode,stock_type,orgno,cny,prod_amount,prod_amount_rmb,product_code", auth = APIAuth.NO)
	public int deleteReportPVD3(SqlParam<ReportPVD3> params) throws Exception {
		return reportPVD3Dao.deleteReportPVD3(params).getEffect();
	}

	public void importReportPVD3Data(List<ReportPVD3> reportPVD3s) {
		Map<String, Object> params = new HashMap<>();
		try {
			if (reportPVD3s.size() > 0) {
				params.put("queryDate", reportPVD3s.get(0).getReportDate().replace("-","").substring(0,6));
				reportPVD3Dao.deleteReportPVD3ByReportDate(params);
			}
			for (ReportPVD3  reportPVD3 : reportPVD3s){
				Map<String, Object> map = MapUtil.toMap( reportPVD3);
				map.put("stockType",  reportPVD3.getStockType().split("-")[0]);
				map.put("reportDate",  reportPVD3.getReportDate().replace("-",""));
				reportPVD3Dao.addReportPVD3(map);
			}
		} catch (Exception e) {
			log.error("导入股权及其他资产异常：",e);
			throw new RuntimeException();
		}
	}

}
