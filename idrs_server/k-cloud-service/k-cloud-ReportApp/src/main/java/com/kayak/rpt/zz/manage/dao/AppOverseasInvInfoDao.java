package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.zz.manage.model.AppOverseasInvInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class AppOverseasInvInfoDao extends ComnDao {

	public SqlResult<AppOverseasInvInfo> findAppOverseasInvInfos(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return super.findRows("SELECT report_date,num,comp_nm,fund_sour_type,fund_sour_over,prod_cd,prod_reg_enc,prod_org_nm,prod_org_seas,inv_type,prod_ccy,f_inv_tm as finv_tm,inv_type_f,inv_type_s,over_sove_bd,fx_type,inv_seas_f,inv_seas_s,inv_seas_t,inv_asst_f,inv_asst_s,inv_asst_t,qdii_fund,inv_asst_nm,tran_count,inv_asst_cost,inv_asst_mkt,bd_rmai_type,risk_envn,risk_pj_amt,depr_rdy_amt,depr_amt,note FROM app_overseas_inv_info", params);
	}

	public List<SqlRow> findAppOverseasInvInfos(Map<String, Object> params) throws Exception {
		return super.findRows("SELECT num c0,comp_nm c1,fund_sour_type c2,fund_sour_over c3,prod_cd c4,prod_reg_enc c5,prod_org_nm c6,prod_org_seas c7,inv_type c8,prod_ccy c9,f_inv_tm as c10,inv_type_f c11,inv_type_s c12,over_sove_bd c13,fx_type c14,inv_seas_f c15,inv_seas_s c16,inv_seas_t c17,inv_asst_f c18,inv_asst_s c19,inv_asst_t c20,qdii_fund c21,inv_asst_nm c22,tran_count c23,inv_asst_cost c24,inv_asst_mkt c25,bd_rmai_type c26,risk_envn c27,risk_pj_amt c28,depr_rdy_amt c29,depr_amt c30,note c31 FROM app_overseas_inv_info where report_date=$S{reportDate}", params);
	}

	public UpdateResult addAppOverseasInvInfo(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return super.update("INSERT INTO app_overseas_inv_info(report_date,num,comp_nm,fund_sour_type,fund_sour_over,prod_cd,prod_reg_enc,prod_org_nm,prod_org_seas,inv_type,prod_ccy,f_inv_tm,inv_type_f,inv_type_s,over_sove_bd,fx_type,inv_seas_f,inv_seas_s,inv_seas_t,inv_asst_f,inv_asst_s,inv_asst_t,qdii_fund,inv_asst_nm,tran_count,inv_asst_cost,inv_asst_mkt,bd_rmai_type,risk_envn,risk_pj_amt,depr_rdy_amt,depr_amt,note) VALUES($S{reportDate},$S{num},$S{compNm},$S{fundSourType},$S{fundSourOver},$S{prodCd},$S{prodRegEnc},$S{prodOrgNm},$S{prodOrgSeas},$S{invType},$S{prodCcy},$S{finvTm},$S{invTypeF},$S{invTypeS},$S{overSoveBd},$S{fxType},$S{invSeasF},$S{invSeasS},$S{invSeasT},$S{invAsstF},$S{invAsstS},$S{invAsstT},$S{qdiiFund},$S{invAsstNm},$S{tranCount},$S{invAsstCost},$D{invAsstMkt},$S{bdRmaiType},$S{riskEnvn},$D{riskPjAmt},$D{deprRdyAmt},$D{deprAmt},$S{note})",
				params.getModel());
	}
	
	public UpdateResult updateAppOverseasInvInfo(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return super.update("UPDATE app_overseas_inv_info SET report_date=$S{reportDate} ,num=$S{num} ,comp_nm=$S{compNm} ,fund_sour_type=$S{fundSourType} ,fund_sour_over=$S{fundSourOver} ,prod_cd=$S{prodCd} ,prod_reg_enc=$S{prodRegEnc} ,prod_org_nm=$S{prodOrgNm} ,prod_org_seas=$S{prodOrgSeas} ,inv_type=$S{invType} ,prod_ccy=$S{prodCcy} ,f_inv_tm=$S{finvTm} ,inv_type_f=$S{invTypeF} ,inv_type_s=$S{invTypeS} ,over_sove_bd=$S{overSoveBd} ,fx_type=$S{fxType} ,inv_seas_f=$S{invSeasF} ,inv_seas_s=$S{invSeasS} ,inv_seas_t=$S{invSeasT} ,inv_asst_f=$S{invAsstF} ,inv_asst_s=$S{invAsstS} ,inv_asst_t=$S{invAsstT} ,qdii_fund=$S{qdiiFund} ,inv_asst_nm=$S{invAsstNm} ,tran_count=$S{tranCount} ,inv_asst_cost=$S{invAsstCost} ,inv_asst_mkt=$D{invAsstMkt} ,bd_rmai_type=$S{bdRmaiType} ,risk_envn=$S{riskEnvn} ,risk_pj_amt=$D{riskPjAmt} ,depr_rdy_amt=$D{deprRdyAmt} ,depr_amt=$D{deprAmt} ,note=$S{note}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAppOverseasInvInfo(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return super.update("DELETE FROM app_overseas_inv_info WHERE ",
				params.getModel());
	}

}
