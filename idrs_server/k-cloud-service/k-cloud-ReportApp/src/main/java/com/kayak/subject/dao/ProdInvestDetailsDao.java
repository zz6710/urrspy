package com.kayak.subject.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.ProdInvestDetails;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class ProdInvestDetailsDao extends ComnDao {

	public SqlResult<ProdInvestDetails> findProdInvestDetailss(SqlParam<ProdInvestDetails> params) throws Exception {
		String sql = "SELECT id,product_code,i_code as icode,asset_third_type,i_name_c1 as inamec1,i_name_c2 as inamec2,bottom_code,item_name,comcode,cost,amount,inv_val_rate_csh,currency,orderfreemanage,org_classific,g06_type,zxb_second_type,zxb_first_type,zxb_third_type,concat(prodmonrate) prodmonrate,concat(pordownrate) pordownrate,mycompnyamount,investmonamount,investownamount,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,inv_val_rate,concat(non_grt_rate) non_grt_rate,non_grt_amt,mark,asset_end_date,asset_term_pj,bg_date,over_day,concat(investmonrate) investmonrate,concat(investownrate) investownrate,prod_type,act_dt,deal_date,vcintfund,govintfund FROM dws_prod_invest_details where 1=1 ";
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
			sql += " and act_dt like '" + params.getModel().getActDt() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getProductCode())) {
			sql += " and product_code like '%" + params.getModel().getProductCode() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getIcode())) {
			sql += " and i_code like '%" + params.getModel().getIcode() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getBottomCode())) {
			sql += " and bottom_code like '%" + params.getModel().getBottomCode() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getOrgClassific())) {
			sql += " and org_classific like '%" + params.getModel().getOrgClassific() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getG06Type())) {
			sql += " and g06_type = '" + params.getModel().getG06Type() +"'";
		}
		if (Tools.isNotEmpty(params.getModel().getZxbFirstType())) {
			sql += " and zxb_first_type = '" + params.getModel().getZxbFirstType() +"'";
		}
		if (Tools.isNotEmpty(params.getModel().getZxbSecondType())) {
			sql += " and zxb_second_type = '" + params.getModel().getZxbSecondType() +"'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addProdInvestDetails(SqlParam<ProdInvestDetails> params) throws Exception {
		return super.update("INSERT INTO dws_prod_invest_details(id,product_code,i_code,asset_third_type,i_name_c1,i_name_c2,bottom_code,item_name,comcode,cost,amount,inv_val_rate_csh,currency,orderfreemanage,org_classific,g06_type,zxb_second_type,zxb_first_type,zxb_third_type,prodmonrate,pordownrate,mycompnyamount,investmonamount,investownamount,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,inv_val_rate,non_grt_rate,non_grt_amt,mark,asset_end_date,asset_term_pj,bg_date,over_day,investmonrate,investownrate,prod_type,act_dt,deal_date,vcintfund,govintfund) VALUES($AUTOIDI{id},$S{productCode},$S{iCode},$S{assetThirdType},$S{inamec1},$S{inamec2},$S{bottomCode},$S{itemName},$S{comcode},$D{cost},$D{amount},$D{invValRateCsh},$S{currency},$S{orderfreemanage},$S{orgClassific},$S{g06Type},$S{zxbSecondType},$S{zxbFirstType},$S{zxbThirdType},$D{prodmonrate},$D{pordownrate},$D{mycompnyamount},$D{investmonamount},$D{investownamount},$S{ratLevel},$S{isPppPart},$S{isMktBtsRlt},$S{isGovFncPart},$S{isFncStk},$S{isFncBnd},$S{isFncScdBnd},$S{isFncTsfBnd},$S{isOthBnkTls},$S{isGovSpcBnd},$D{invValRate},$D{nonGrtRate},$D{nonGrtAmt},$S{mark},$S{assetEndDate},$S{assetTermPj},$S{bgDate},$S{overDay},$D{investmonrate},$D{investownrate},$S{prodType},$S{actDt},$S{dealDate},$S{vcintfund},$S{govintfund})",
				params.getModel());
	}
	
	public UpdateResult updateProdInvestDetails(SqlParam<ProdInvestDetails> params) throws Exception {
		return super.update("UPDATE dws_prod_invest_details SET product_code=$S{productCode} ,i_code=$S{icode} ,asset_third_type=$S{assetThirdType} ,i_name_c1=$S{inamec1} ,i_name_c2=$S{inamec2} ,bottom_code=$S{bottomCode} ,item_name=$S{itemName} ,comcode=$S{comcode} ,cost=$D{cost} ,amount=$D{amount} ,inv_val_rate_csh=$D{invValRateCsh} ,currency=$S{currency} ,orderfreemanage=$S{orderfreemanage} ,org_classific=$S{orgClassific} ,g06_type=$S{g06Type} ,zxb_second_type=$S{zxbSecondType} ,zxb_first_type=$S{zxbFirstType} ,zxb_third_type=$S{zxbThirdType} ,prodmonrate=$D{prodmonrate} ,pordownrate=$D{pordownrate} ,mycompnyamount=$D{mycompnyamount} ,investmonamount=$D{investmonamount} ,investownamount=$D{investownamount} ,rat_level=$S{ratLevel} ,is_ppp_part=$S{isPppPart} ,is_mkt_bts_rlt=$S{isMktBtsRlt} ,is_gov_fnc_part=$S{isGovFncPart} ,is_fnc_stk=$S{isFncStk} ,is_fnc_bnd=$S{isFncBnd} ,is_fnc_scd_bnd=$S{isFncScdBnd} ,is_fnc_tsf_bnd=$S{isFncTsfBnd} ,is_oth_bnk_tls=$S{isOthBnkTls} ,is_gov_spc_bnd=$S{isGovSpcBnd} ,inv_val_rate=$D{invValRate} ,non_grt_rate=$D{nonGrtRate} ,non_grt_amt=$D{nonGrtAmt} ,mark=$S{mark} ,asset_end_date=$S{assetEndDate} ,asset_term_pj=$S{assetTermPj} ,bg_date=$S{bgDate} ,over_day=$S{overDay} ,investmonrate=$D{investmonrate} ,investownrate=$D{investownrate} ,prod_type=$S{prodType} ,act_dt=$S{actDt} ,deal_date=$S{dealDate} ,data_status='02' ,vcintfund=$S{vcintfund} ,govintfund=$S{govintfund}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteProdInvestDetails(SqlParam<ProdInvestDetails> params) throws Exception {
		return super.update("DELETE FROM dws_prod_invest_details WHERE  id=$I{id} ",
				params.getModel());
	}

	/**
	 * Rpt服务数据库分布式锁查询方法
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> queryTaskStatus(Map<String, Object> params) throws Exception {
		String sqlstr = "select paravalue from sys_param where paraid = $S{paraid} ";
		return super.findRows(sqlstr, params);
	}

}
