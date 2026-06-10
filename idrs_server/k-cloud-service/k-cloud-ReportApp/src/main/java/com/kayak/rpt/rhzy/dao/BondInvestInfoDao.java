package com.kayak.rpt.rhzy.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzy.model.BondInvestInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class BondInvestInfoDao extends ComnDao {

	public SqlResult<BondInvestInfo> findBondInvestInfos(SqlParam<BondInvestInfo> params) throws Exception {
		String sql = "SELECT t.id,t.org_code,t.report_date,t.inner_org_code,t.bond_code,t.bond_trustsp_org,t.bond_cate,t.bond_credit_grade,t.cur,t.bond_balance,t.bond_balance_rmb,t.debt_reg_date,t.value_date,t.redem_date,t.coupon_rate,t.issuer_id_code,t.issuer_region_code,t.issuer_industry,t.issuer_entp_scale,t.issuer_eco_sector,t.issuer_eco_dept FROM app_bond_invest_info t WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and  t.report_date like '%" + params.getModel().getReportDate() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBondCode())) {
			sql = sql + " and  t.bond_code like '%" + params.getModel().getBondCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBondCate())) {
			sql = sql + " and  t.bond_cate = '" + params.getModel().getBondCate() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addBondInvestInfo(SqlParam<BondInvestInfo> params) throws Exception {
		return super.update("INSERT INTO app_bond_invest_info(id,org_code,report_date,inner_org_code,bond_code,bond_trustsp_org,bond_cate,bond_credit_grade,cur,bond_balance,bond_balance_rmb,debt_reg_date,value_date,redem_date,coupon_rate,issuer_id_code,issuer_region_code,issuer_industry,issuer_entp_scale,issuer_eco_sector,issuer_eco_dept) VALUES($AUTOIDI{id},$S{orgCode},$S{reportDate},$S{innerOrgCode},$S{bondCode},$S{bondTrustspOrg},$S{bondCate},$S{bondCreditGrade},$S{cur},$D{bondBalance},$D{bondBalanceRmb},$S{debtRegDate},$S{valueDate},$S{redemDate},$D{couponRate},$S{issuerIdCode},$S{issuerRegionCode},$S{issuerIndustry},$S{issuerEntpScale},$S{issuerEcoSector},$S{issuerEcoDept})",
				params.getModel());
	}
	
	public UpdateResult updateBondInvestInfo(SqlParam<BondInvestInfo> params) throws Exception {
		return super.update("UPDATE app_bond_invest_info SET org_code=$S{orgCode} ,report_date=$S{reportDate} ,inner_org_code=$S{innerOrgCode} ,bond_code=$S{bondCode} ,bond_trustsp_org=$S{bondTrustspOrg} ,bond_cate=$S{bondCate} ,bond_credit_grade=$S{bondCreditGrade} ,cur=$S{cur} ,bond_balance=$D{bondBalance} ,bond_balance_rmb=$D{bondBalanceRmb} ,debt_reg_date=$S{debtRegDate} ,value_date=$S{valueDate} ,redem_date=$S{redemDate} ,coupon_rate=$D{couponRate} ,issuer_id_code=$S{issuerIdCode} ,issuer_region_code=$S{issuerRegionCode} ,issuer_industry=$S{issuerIndustry} ,issuer_entp_scale=$S{issuerEntpScale} ,issuer_eco_sector=$S{issuerEcoSector} ,issuer_eco_dept=$S{issuerEcoDept}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBondInvestInfo(SqlParam<BondInvestInfo> params) throws Exception {
		return super.update("DELETE FROM app_bond_invest_info WHERE  id=$I{id} ",
				params.getModel());
	}

	public void deleteBondInvestInfoByDate(Object params) throws Exception {
		super.update("DELETE FROM app_bond_invest_info where report_date = $S{reportDate} ", params);
	}

	public List<SqlRow> addclcSourceZonCdDict(Map<String, Object> params) throws Exception {
		String sql = "SELECT itemkey VALUE, itemval TEXT  FROM sys_dict_item where 1 =1 ";
		sql = sql + " and dict  in ('pbc_city_area_det','pbc_country_code')  ";
		if(StringUtils.isNotBlank(params.get("TEXT").toString())) {
			sql = sql + " and (itemkey like '%$U{TEXT}%' or itemval like '%$U{TEXT}%')  ";
		}
		List<SqlRow> s = super.findRows(sql, DataSourceProperty.PUB,params);
		return s;
	}

}
