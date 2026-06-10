package com.kayak.rpt.rhzj.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportPCD;
import org.springframework.stereotype.Repository;

@Repository
public class ReportPCDDao extends ComnDao {

	public SqlResult<ReportPCD> findReportPCDs(SqlParam<ReportPCD> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount,current_buy_amount_rmb,current_buy_vol,current_redemption_amount,current_redemption_amountrmb,current_redemption_vol,termina_prod_amount,termina_prod_amount_rmb,termina_prod_vol,termina_prod_nav,termina_prod_nav_rmb,termina_prod_nav_add,termina_prod_nav_add_rmb,prod_max_rate,prod_min_rate FROM app_rpt_pcd order by report_date,prod_code", params);
	}

	public UpdateResult addReportPCD(SqlParam<ReportPCD> params) throws Exception {
		return super.update("INSERT INTO app_rpt_pcd(id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount,current_buy_amount_rmb,current_buy_vol,current_redemption_amount,current_redemption_amountrmb,current_redemption_vol,termina_prod_amount,termina_prod_amount_rmb,termina_prod_vol,termina_prod_nav,termina_prod_nav_rmb,termina_prod_nav_add,termina_prod_nav_add_rmb,prod_max_rate,prod_min_rate) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCode},$S{peoplebankSubmitcode},$S{areaCode},$S{custType},$S{cny},if($S{currentBuyAmount}='',null,$S{currentBuyAmount}),if($S{currentBuyAmountRmb}='',null,$S{currentBuyAmountRmb}),if($S{currentBuyVol}='',null,$S{currentBuyVol}),if($S{currentRedemptionAmount}='',null,$S{currentRedemptionAmount}),if($S{currentRedemptionAmountrmb}='',null,$S{currentRedemptionAmountrmb}),if($S{currentRedemptionVol}='',null,$S{currentRedemptionVol}),if($S{terminaProdAmount}='',null,$S{terminaProdAmount}),if($S{terminaProdAmountRmb}='',null,$S{terminaProdAmountRmb}),if($S{terminaProdVol}='',null,$S{terminaProdVol}),if($S{terminaProdNav}='',null,$S{terminaProdNav}),if($S{terminaProdNavRmb}='',null,$S{terminaProdNavRmb}),if($S{terminaProdNavAdd}='',null,$S{terminaProdNavAdd}),if($S{terminaProdNavAddRmb}='',null,$S{terminaProdNavAddRmb}),if($S{prodMaxRate}='',null,$S{prodMaxRate}),if($S{prodMinRate}='',null,$S{prodMinRate}))",
				params.getModel());
	}

	public UpdateResult addReportPCD(Object params) throws Exception {
		return super.update("INSERT INTO app_rpt_pcd(id,report_date,prod_code,peoplebank_submitcode,area_code,cust_type,cny,current_buy_amount,current_buy_amount_rmb,current_buy_vol,current_redemption_amount,current_redemption_amountrmb,current_redemption_vol,termina_prod_amount,termina_prod_amount_rmb,termina_prod_vol,termina_prod_nav,termina_prod_nav_rmb,termina_prod_nav_add,termina_prod_nav_add_rmb,prod_max_rate,prod_min_rate) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCode},$S{peoplebankSubmitcode},$S{areaCode},$S{custType},$S{cny},if($S{currentBuyAmount}='',null,$S{currentBuyAmount}),if($S{currentBuyAmountRmb}='',null,$S{currentBuyAmountRmb}),if($S{currentBuyVol}='',null,$S{currentBuyVol}),if($S{currentRedemptionAmount}='',null,$S{currentRedemptionAmount}),if($S{currentRedemptionAmountrmb}='',null,$S{currentRedemptionAmountrmb}),if($S{currentRedemptionVol}='',null,$S{currentRedemptionVol}),if($S{terminaProdAmount}='',null,$S{terminaProdAmount}),if($S{terminaProdAmountRmb}='',null,$S{terminaProdAmountRmb}),if($S{terminaProdVol}='',null,$S{terminaProdVol}),if($S{terminaProdNav}='',null,$S{terminaProdNav}),if($S{terminaProdNavRmb}='',null,$S{terminaProdNavRmb}),if($S{terminaProdNavAdd}='',null,$S{terminaProdNavAdd}),if($S{terminaProdNavAddRmb}='',null,$S{terminaProdNavAddRmb}),if($S{prodMaxRate}='',null,$S{prodMaxRate}),if($S{prodMinRate}='',null,$S{prodMinRate}))",
				params);
	}
	
	public UpdateResult updateReportPCD(SqlParam<ReportPCD> params) throws Exception {
		return super.update("UPDATE app_rpt_pcd SET report_date=$S{reportDate} ,prod_code=$S{prodCode} ,peoplebank_submitcode=$S{peoplebankSubmitcode} ,area_code=$S{areaCode} ,cust_type=$S{custType} ,cny=$S{cny} ,current_buy_amount=if($S{currentBuyAmount} = '',null,$S{currentBuyAmount}) ,current_buy_amount_rmb=if($S{currentBuyAmountRmb} = '',null,$S{currentBuyAmountRmb}) ,current_buy_vol=if($S{currentBuyVol} = '',null,$S{currentBuyVol}) ,current_redemption_amount=if($S{currentRedemptionAmount}='',null,$S{currentRedemptionAmount}) ,current_redemption_amountrmb=if($S{currentRedemptionAmountrmb}='',null,$S{currentRedemptionAmountrmb}) ,current_redemption_vol=if($S{currentRedemptionVol}='',null,$S{currentRedemptionVol}) ,termina_prod_amount=if($S{terminaProdAmount}='',null,$S{terminaProdAmount}) ,termina_prod_amount_rmb=if($S{terminaProdAmountRmb}='',null,$S{terminaProdAmountRmb}) ,termina_prod_vol=if($S{terminaProdVol}='',null,$S{terminaProdVol}) ,termina_prod_nav=if($S{terminaProdNav}='',null,$S{terminaProdNav}) ,termina_prod_nav_rmb=if($S{terminaProdNavRmb}='',null,$S{terminaProdNavRmb}) ,termina_prod_nav_add=if($S{terminaProdNavAdd}='',null,$S{terminaProdNavAdd}) ,termina_prod_nav_add_rmb=if($S{terminaProdNavAddRmb}='',null,$S{terminaProdNavAddRmb}) ,prod_max_rate=if($S{prodMaxRate}='',null,$S{prodMaxRate}) ,prod_min_rate=if($S{prodMinRate}='',null,$S{prodMinRate})  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteReportPCD(SqlParam<ReportPCD> params) throws Exception {
		return super.update("DELETE FROM app_rpt_pcd WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteReportPCDByReportDate(Object params) throws Exception {
		return super.update("DELETE FROM app_rpt_pcd WHERE report_date = LAST_DAY($S{queryDate})",
				params);
	}

}
