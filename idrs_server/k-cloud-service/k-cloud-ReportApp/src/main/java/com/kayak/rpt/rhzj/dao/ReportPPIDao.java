package com.kayak.rpt.rhzj.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportPPI;
import org.springframework.stereotype.Repository;

@Repository
public class ReportPPIDao extends ComnDao {

	public SqlResult<ReportPPI> findReportPPIs(SqlParam<ReportPPI> params) throws Exception {
		return super.findRows("SELECT prod_code,orgno,peoplebank_submitcode,prod_variety,prod_name,prod_brand,prod_times,coll_mod,oper_mod,run_mod,prod_type,busi_mod,safe_rate,safe_capit,max_rate,min_rate,subs_bdate,subs_edate,term_flag,redeem_flag,prod_credit_flag,bord_trusti_code,overs_trusti_nation,overs_trusti_name,establish_date,end_date,pbc_assetscode,issu_ccy,return_ccy,income_ccy,invest_object,prod_credit_org,prod_credit_mod,end_date_real,rate_real,income_type,cooperation_mode,grading_flag,entrested_obligation,transfer_flag,orgno_flag,cash_type,cross_border_finan FROM app_rpt_ppi order by establish_date", params);
	}

	public UpdateResult addReportPPI(SqlParam<ReportPPI> params) throws Exception {
		return super.update("INSERT INTO app_rpt_ppi(prod_code,orgno,peoplebank_submitcode,prod_variety,prod_name,prod_brand,prod_times,coll_mod,oper_mod,run_mod,prod_type,busi_mod,safe_rate,safe_capit,max_rate,min_rate,subs_bdate,subs_edate,term_flag,redeem_flag,prod_credit_flag,bord_trusti_code,overs_trusti_nation,overs_trusti_name,establish_date,end_date,pbc_assetscode,issu_ccy,return_ccy,income_ccy,invest_object,prod_credit_org,prod_credit_mod,end_date_real,rate_real,income_type,cooperation_mode,grading_flag,entrested_obligation,transfer_flag,orgno_flag,cash_type,cross_border_finan) VALUES($S{prodCode},$S{orgno},$S{peoplebankSubmitcode},$S{prodVariety},$S{prodName},$S{prodBrand},$S{prodTimes},$S{collMod},$S{operMod},$S{runMod},$S{prodType},$S{busiMod},$S{safeRate},$S{safeCapit},if($S{maxRate}='',null,$S{maxRate}),if($S{minRate}='',null,$S{minRate}),$S{subsBdate},$S{subsEdate},$S{termFlag},$S{redeemFlag},$S{prodCreditFlag},$S{bordTrustiCode},$S{oversTrustiNation},$S{oversTrustiName},$S{establishDate},$S{endDate},$S{pbcAssetscode},$S{issuCcy},$S{returnCcy},$S{incomeCcy},$S{investObject},$S{prodCreditOrg},$S{prodCreditMod},$S{endDateReal},if($S{rateReal}='',null,$S{rateReal}),$S{incomeType},$S{cooperationMode},$S{gradingFlag},$S{entrestedObligation},$S{transferFlag},$S{orgnoFlag},$S{cashType},$S{crossBorderFinan})",
				params.getModel());
	}

	public UpdateResult addReportPPI(Object params) throws Exception {
		return super.update("INSERT INTO app_rpt_ppi(prod_code,orgno,peoplebank_submitcode,prod_variety,prod_name,prod_brand,prod_times,coll_mod,oper_mod,run_mod,prod_type,busi_mod,safe_rate,safe_capit,max_rate,min_rate,subs_bdate,subs_edate,term_flag,redeem_flag,prod_credit_flag,bord_trusti_code,overs_trusti_nation,overs_trusti_name,establish_date,end_date,pbc_assetscode,issu_ccy,return_ccy,income_ccy,invest_object,prod_credit_org,prod_credit_mod,end_date_real,rate_real,income_type,cooperation_mode,grading_flag,entrested_obligation,transfer_flag,orgno_flag,cash_type,cross_border_finan) VALUES($S{prodCode},$S{orgno},$S{peoplebankSubmitcode},$S{prodVariety},$S{prodName},$S{prodBrand},$S{prodTimes},$S{collMod},$S{operMod},$S{runMod},$S{prodType},$S{busiMod},$S{safeRate},$S{safeCapit},if($S{maxRate}='',null,$S{maxRate}),if($S{minRate}='',null,$S{minRate}),$S{subsBdate},$S{subsEdate},$S{termFlag},$S{redeemFlag},$S{prodCreditFlag},$S{bordTrustiCode},$S{oversTrustiNation},$S{oversTrustiName},$S{establishDate},$S{endDate},$S{pbcAssetscode},$S{issuCcy},$S{returnCcy},$S{incomeCcy},$S{investObject},$S{prodCreditOrg},$S{prodCreditMod},$S{endDateReal},if($S{rateReal}='',null,$S{rateReal}),$S{incomeType},$S{cooperationMode},$S{gradingFlag},$S{entrestedObligation},$S{transferFlag},$S{orgnoFlag},$S{cashType},$S{crossBorderFinan})",
				params);
	}

	
	public UpdateResult updateReportPPI(SqlParam<ReportPPI> params) throws Exception {
		return super.update("UPDATE app_rpt_ppi SET orgno=$S{orgno} ,peoplebank_submitcode=$S{peoplebankSubmitcode} ,prod_variety=$S{prodVariety} ,prod_name=$S{prodName} ,prod_brand=$S{prodBrand} ,prod_times=$S{prodTimes} ,coll_mod=$S{collMod} ,oper_mod=$S{operMod} ,run_mod=$S{runMod} ,prod_type=$S{prodType} ,busi_mod=$S{busiMod} ,safe_rate=$S{safeRate} ,safe_capit=$S{safeCapit} ,max_rate=$S{maxRate} ,min_rate=$S{minRate} ,subs_bdate=$S{subsBdate} ,subs_edate=$S{subsEdate} ,term_flag=$S{termFlag} ,redeem_flag=$S{redeemFlag} ,prod_credit_flag=$S{prodCreditFlag} ,bord_trusti_code=$S{bordTrustiCode} ,overs_trusti_nation=$S{oversTrustiNation} ,overs_trusti_name=$S{oversTrustiName} ,establish_date=$S{establishDate} ,end_date=$S{endDate} ,pbc_assetscode=$S{pbcAssetscode} ,issu_ccy=$S{issuCcy} ,return_ccy=$S{returnCcy} ,income_ccy=$S{incomeCcy} ,invest_object=$S{investObject} ,prod_credit_org=$S{prodCreditOrg} ,prod_credit_mod=$S{prodCreditMod} ,end_date_real=$S{endDateReal} ,rate_real=if($S{rateReal}='',null,$S{rateReal}) ,income_type=$S{incomeType} ,cooperation_mode=$S{cooperationMode} ,grading_flag=$S{gradingFlag} ,entrested_obligation=$S{entrestedObligation} ,transfer_flag=$S{transferFlag} ,orgno_flag=$S{orgnoFlag} ,cash_type=$S{cashType} ,cross_border_finan=$S{crossBorderFinan}  WHERE  prod_code = $S{prodCode}",
				params.getModel());
	}
	
	public UpdateResult deleteReportPPI(SqlParam<ReportPPI> params) throws Exception {
		return super.update("DELETE FROM app_rpt_ppi WHERE prod_code = $S{prodCode}",
				params.getModel());
	}

	public UpdateResult deleteReportPPIForReportDate(Object params) throws Exception {
		return super.update("DELETE from app_rpt_ppi where establish_date between $S{beginDate} and $S{queryDate}",
				params);
	}

}
