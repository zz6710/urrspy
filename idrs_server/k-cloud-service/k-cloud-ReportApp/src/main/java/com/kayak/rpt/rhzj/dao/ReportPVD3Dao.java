package com.kayak.rpt.rhzj.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportPVD3;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ReportPVD3Dao extends ComnDao {

	public SqlResult<ReportPVD3> findReportPVD3s(SqlParam<ReportPVD3> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_code,pbc_assetscode,stock_type,orgno,cny,prod_amount,prod_amount_rmb,product_code FROM app_rpt_pvd3", params);
	}

	public UpdateResult addReportPVD3(SqlParam<ReportPVD3> params) throws Exception {
		return super.update("INSERT INTO app_rpt_pvd3(id,report_date,prod_code,pbc_assetscode,stock_type,orgno,cny,prod_amount,prod_amount_rmb,product_code) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCode},$S{pbcAssetscode},$S{stockType},$S{orgno},$S{cny},if($S{prodAmount}='',null,$S{prodAmount}),if($S{prodAmountRmb}='',null,$S{prodAmountRmb}),$S{productCode})",
				params.getModel());
	}

	public UpdateResult addReportPVD3(Object params) throws Exception {
		return super.update("INSERT INTO app_rpt_pvd3(id,report_date,prod_code,pbc_assetscode,stock_type,orgno,cny,prod_amount,prod_amount_rmb,product_code) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCode},$S{pbcAssetscode},$S{stockType},$S{orgno},$S{cny},if($S{prodAmount}='',null,$S{prodAmount}),if($S{prodAmountRmb}='',null,$S{prodAmountRmb}),$S{productCode})",
				params);
	}
	
	public UpdateResult updateReportPVD3(SqlParam<ReportPVD3> params) throws Exception {
		return super.update("UPDATE app_rpt_pvd3 SET report_date=$S{reportDate} ,prod_code=$S{prodCode} ,pbc_assetscode=$S{pbcAssetscode} ,stock_type=$S{stockType} ,orgno=$S{orgno} ,cny=$S{cny} ,prod_amount=if($S{prodAmount}='',null,$S{prodAmount}) ,prod_amount_rmb=if($S{prodAmountRmb}='',null,$S{prodAmountRmb}) ,product_code=$S{productCode}  WHERE id = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteReportPVD3(SqlParam<ReportPVD3> params) throws Exception {
		return super.update("DELETE FROM app_rpt_pvd3 WHERE id = $S{id}",
				params.getModel());
	}

	public UpdateResult deleteReportPVD3ByReportDate(Object params) throws Exception {
		return super.update("DELETE FROM app_rpt_pvd3 where report_date like '%$U{queryDate}%'",
				params);
	}

}
