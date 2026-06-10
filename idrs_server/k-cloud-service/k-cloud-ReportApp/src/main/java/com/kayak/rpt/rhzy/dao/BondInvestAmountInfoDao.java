package com.kayak.rpt.rhzy.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzy.model.BondInvestAmountInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class BondInvestAmountInfoDao extends ComnDao {

	public SqlResult<BondInvestAmountInfo> findBondInvestAmountInfos(SqlParam<BondInvestAmountInfo> params) throws Exception {
		String sql = "SELECT t.id,t.org_code,t.report_date,t.inner_org_code,t.bond_code,t.bond_trustsp_org,t.bond_cate,t.bond_credit_grade,t.cur,t.debt_reg_date,t.value_date,t.redem_date,t.coupon_rate,t.issuer_id_code,t.issuer_region_code,t.issuer_industry,t.issuer_entp_scale,t.issuer_eco_sector,t.issuer_eco_dept,t.trade_date,t.trade_ser_no,t.trade_amount,t.trade_amount_rmb,t.trade_flag FROM app_bond_invest_amount_info t WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and  t.report_date like '%" + params.getModel().getReportDate() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBondCate())) {
			sql = sql + " and  t.bond_cate = '" + params.getModel().getBondCate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTradeSerNo())) {
			sql = sql + " and  t.trade_ser_no like '%" + params.getModel().getTradeSerNo() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTradeFlag())) {
			sql = sql + " and  t.trade_flag like '%" + params.getModel().getTradeFlag() + "%'";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addBondInvestAmountInfo(SqlParam<BondInvestAmountInfo> params) throws Exception {
		return super.update("INSERT INTO app_bond_invest_amount_info(id,org_code,report_date,inner_org_code,bond_code,bond_trustsp_org,bond_cate,bond_credit_grade,cur,debt_reg_date,value_date,redem_date,coupon_rate,issuer_id_code,issuer_region_code,issuer_industry,issuer_entp_scale,issuer_eco_sector,issuer_eco_dept,trade_date,trade_ser_no,trade_amount,trade_amount_rmb,trade_flag) VALUES($AUTOIDI{id},$S{orgCode},$S{reportDate},$S{innerOrgCode},$S{bondCode},$S{bondTrustspOrg},$S{bondCate},$S{bondCreditGrade},$S{cur},$S{debtRegDate},$S{valueDate},$S{redemDate},$D{couponRate},$S{issuerIdCode},$S{issuerRegionCode},$S{issuerIndustry},$S{issuerEntpScale},$S{issuerEcoSector},$S{issuerEcoDept},$S{tradeDate},$S{tradeSerNo},$D{tradeAmount},$D{tradeAmountRmb},$S{tradeFlag})",
				params.getModel());
	}
	
	public UpdateResult updateBondInvestAmountInfo(SqlParam<BondInvestAmountInfo> params) throws Exception {
		return super.update("UPDATE app_bond_invest_amount_info SET org_code=$S{orgCode} ,report_date=$S{reportDate} ,inner_org_code=$S{innerOrgCode} ,bond_code=$S{bondCode} ,bond_trustsp_org=$S{bondTrustspOrg} ,bond_cate=$S{bondCate} ,bond_credit_grade=$S{bondCreditGrade} ,cur=$S{cur} ,debt_reg_date=$S{debtRegDate} ,value_date=$S{valueDate} ,redem_date=$S{redemDate} ,coupon_rate=$D{couponRate} ,issuer_id_code=$S{issuerIdCode} ,issuer_region_code=$S{issuerRegionCode} ,issuer_industry=$S{issuerIndustry} ,issuer_entp_scale=$S{issuerEntpScale} ,issuer_eco_sector=$S{issuerEcoSector} ,issuer_eco_dept=$S{issuerEcoDept} ,trade_date=$S{tradeDate} ,trade_ser_no=$S{tradeSerNo} ,trade_amount=$D{tradeAmount} ,trade_amount_rmb=$D{tradeAmountRmb} ,trade_flag=$S{tradeFlag}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBondInvestAmountInfo(SqlParam<BondInvestAmountInfo> params) throws Exception {
		return super.update("DELETE FROM app_bond_invest_amount_info WHERE  id=$I{id} ",
				params.getModel());
	}

	public void deleteBondInvestAmountInfoByDate(Object params) throws Exception {
		super.update("DELETE FROM app_bond_invest_amount_info where report_date = $S{reportDate} ", params);
	}

}
