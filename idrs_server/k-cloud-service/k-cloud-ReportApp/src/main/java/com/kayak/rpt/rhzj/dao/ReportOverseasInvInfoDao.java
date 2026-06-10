package com.kayak.rpt.rhzj.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportOverseasInvInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ReportOverseasInvInfoDao extends ComnDao {

	public SqlResult<ReportOverseasInvInfo> findReportOverseasInvInfos(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_cd,prod_reg_enc,hold_type,f_asst_cd as fasst_cd,asset_third_type,f_asst_nm as fasst_nm,f_amount as famount,org_classific,itm_cd,itm_nm,d_amount damount,new_classific,f_inv_tm as finv_tm,asst_type,asst_amount,asst_zon,mang_zon,depr_rdy_amt,depr_amt,fx_type,bd_rmai_type,rmai_day,risk_envn,risk_pj_amt FROM dws_overseas_inv_info", params);
	}

	public UpdateResult addReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return super.update("INSERT INTO dws_overseas_inv_info(report_date,prod_cd,prod_reg_enc,hold_type,f_asst_cd,asset_third_type,f_asst_nm,f_amount,org_classific,itm_cd,itm_nm,d_amount,new_classific,f_inv_tm,asst_type,asst_amount,asst_zon,mang_zon,depr_rdy_amt,depr_amt,fx_type,bd_rmai_type,rmai_day,risk_envn,risk_pj_amt) VALUES($S{reportDate},$S{prodCd},$S{prodRegEnc},$S{holdType},$S{fasstCd},$S{assetThirdType},$S{fasstNm},$D{famount},$S{orgClassific},$S{itmCd},$S{itmNm},$D{damount},$S{newClassific},$S{finvTm},$S{asstType},$D{asstAmount},$S{asstZon},$S{mangZon},$S{deprRdyAmt},$S{deprAmt},$S{fxType},$S{bdRmaiType},$S{rmaiDay},$S{riskEnvn},$D{riskPjAmt})",
				params.getModel());
	}
	
	public UpdateResult updateReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return super.update("UPDATE dws_overseas_inv_info SET report_date=$S{reportDate} ,prod_cd=$S{prodCd} ,prod_reg_enc=$S{prodRegEnc} ,hold_type=$S{holdType} ,f_asst_cd=$S{fasstCd} ,asset_third_type=$S{assetThirdType} ,f_asst_nm=$S{fasstNm} ,f_amount=$D{famount} ,org_classific=$S{orgClassific} ,itm_cd=$S{itmCd} ,itm_nm=$S{itmNm} ,d_amount=$D{damount} ,new_classific=$S{newClassific} ,f_inv_tm=$S{finvTm} ,asst_type=$S{asstType} ,asst_amount=$D{asstAmount} ,asst_zon=$S{asstZon} ,mang_zon=$S{mangZon} ,depr_rdy_amt=$S{deprRdyAmt} ,depr_amt=$S{deprAmt} ,fx_type=$S{fxType} ,bd_rmai_type=$S{bdRmaiType} ,rmai_day=$S{rmaiDay} ,risk_envn=$S{riskEnvn} ,risk_pj_amt=$D{riskPjAmt}  WHERE id=$S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return super.update("DELETE FROM dws_overseas_inv_info WHERE id=$S{id}",
				params.getModel());
	}

}
