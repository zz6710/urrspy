package com.kayak.rpt.rhzj.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportPIE;
import org.springframework.stereotype.Repository;

@Repository
public class ReportPIEDao extends ComnDao {

	public SqlResult<ReportPIE> findReportPIEs(SqlParam<ReportPIE> params) throws Exception {
		return super.findRows("SELECT id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real,cny,org_ern,org_ern_rmb,cust_ern,cust_ern_rmb,cust_ern_yld FROM app_rpt_pie", params);
	}

	public UpdateResult addReportPIE(SqlParam<ReportPIE> params) throws Exception {
		return super.update("INSERT INTO app_rpt_pie(id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real,cny,org_ern,org_ern_rmb,cust_ern,cust_ern_rmb,cust_ern_yld) VALUES($AUTOIDI{id},$S{prodCode},$S{pbcAssetscode},$S{peoplebankSubmitcode},$S{endDateReal},$S{cny},if($S{orgErn}='',null,$S{orgErn}),if($S{orgErnRmb}='',null,$S{orgErnRmb}),if($S{custErn}='',null,$S{custErn}),if($S{custErnRmb}='',null,$S{custErnRmb}),if($S{custErnYld}='',null,$S{custErnYld}))",
				params.getModel());
	}

	public UpdateResult addReportPIE(Object params) throws Exception {
		return super.update("INSERT INTO app_rpt_pie(id,prod_code,pbc_assetscode,peoplebank_submitcode,end_date_real,cny,org_ern,org_ern_rmb,cust_ern,cust_ern_rmb,cust_ern_yld) VALUES($AUTOIDI{id},$S{prodCode},$S{pbcAssetscode},$S{peoplebankSubmitcode},$S{endDateReal},$S{cny},if($S{orgErn}='',null,$S{orgErn}),if($S{orgErnRmb}='',null,$S{orgErnRmb}),if($S{custErn}='',null,$S{custErn}),if($S{custErnRmb}='',null,$S{custErnRmb}),if($S{custErnYld}='',null,$S{custErnYld}))",
				params);
	}
	
	public UpdateResult updateReportPIE(SqlParam<ReportPIE> params) throws Exception {
		return super.update("UPDATE app_rpt_pie SET prod_code=$S{prodCode} ,pbc_assetscode=$S{pbcAssetscode} ,peoplebank_submitcode=$S{peoplebankSubmitcode} ,end_date_real=$S{endDateReal} ,cny=$S{cny} ,org_ern=if($S{orgErn}='',null,$S{orgErn}) ,org_ern_rmb=if($S{orgErnRmb}='',null,$S{orgErnRmb}) ,cust_ern=if($S{custErn}='',null,$S{custErn}) ,cust_ern_rmb=if($S{custErnRmb}='',null,$S{custErnRmb}) ,cust_ern_yld=if($S{custErnYld}='',null,$S{custErnYld})  WHERE id=$S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteReportPIE(SqlParam<ReportPIE> params) throws Exception {
		return super.update("DELETE FROM app_rpt_pie WHERE  id=$S{id} ",
				params.getModel());
	}
	public UpdateResult deleteReportPIEForReportDate(Object params) throws Exception {
		return super.update("DELETE FROM app_rpt_pie where end_date_real between $S{beginDate} and $S{queryDate} ",
				params);
	}



}
