package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.SumButtomAssetAft;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class SumButtomAssetDaoAft extends ComnDao {

	public SqlResult<SumButtomAssetAft> findSumButtomAssetAfts(SqlParam<SumButtomAssetAft> params) throws Exception {
		String sql = "SELECT id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code as icode,i_name_c1 as inamec1,i_name_c2 as inamec2,a_type as atype,m_type as mtype,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,pen_inv_f,format(inv_val_rate_pen,10) as inv_val_rate_pen,per_pen_inv_f,format(inv_val_rate_per_pen,10) as inv_val_rate_per_pen,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date,is_real_setate,is_csh_mng,format(inv_val_rate_csh,10) as inv_val_rate_csh,hk_inv,qdii_inv FROM dwd_sum_buttom_asset where (ASSET_TYPE <> '02' or ASSET_TYPE is null)";
		if (StringUtils.isNotBlank(params.getModel().getInputDate())) {
			sql += " and input_date = '" + params.getModel().getInputDate() + "' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getIcode())) {
			sql += " and i_code like '%" + params.getModel().getIcode() + "%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getBottomCode())) {
			sql += " and bottom_code like '%" + params.getModel().getBottomCode() + "%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getG06Type())) {
			sql += " and g06_type = '" + params.getModel().getG06Type() + "' ";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addSumButtomAssetAft(SqlParam<SumButtomAssetAft> params) throws Exception {
		return super.update("INSERT INTO dwd_sum_buttom_asset(id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,pen_inv_f,inv_val_rate_pen,per_pen_inv_f,inv_val_rate_per_pen,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date) VALUES($AUTOIDI{id},$S{comcode},$S{bottomCode},$S{assetType},$D{amount},$D{cost},$S{currency},$S{inputDate},$S{itemId},$S{itemName},$S{importDate},$S{icode},$S{inameC1},$S{inameC2},$S{atype},$S{mtype},$S{assetCode},$S{orgLevel},$D{netValue},$S{zzReportType},$S{g06Type},$S{ratLevel},$S{isPppPart},$S{isMktBtsRlt},$S{isGovFncPart},$S{isFncStk},$S{isFncBnd},$S{isFncScdBnd},$S{isFncTsfBnd},$S{isOthBnkTls},$S{isGovSpcBnd},$D{exchangeRate},$D{invValRate},$D{penInvF} ,$D{invValRatePen} ,$D{perPenInvF} ,$D{invValRatePerPen} ,$D{nonGrtRate},$D{nonGrtAmt},$S{tradePlace},$S{isPublic},$S{manageMode},$S{mark},$S{dataInsrDt},$S{dealDate})",
				params.getModel());
	}
	
	public UpdateResult updateSumButtomAssetAft(SqlParam<SumButtomAssetAft> params) throws Exception {
		return super.update("UPDATE dwd_sum_buttom_asset SET comcode=$S{comcode} ,bottom_code=$S{bottomCode} ,asset_type=$S{assetType} ,amount=$D{amount} ,cost=$D{cost} ,currency=$S{currency} ,input_date=$S{inputDate} ,item_id=$S{itemId} ,item_name=$S{itemName} ,import_date=$S{importDate} ,i_code=$S{icode} ,i_name_c1=$S{inameC1} ,i_name_c2=$S{inameC2} ,a_type=$S{atype} ,m_type=$S{mtype} ,asset_code=$S{assetCode} ,org_level=$S{orgLevel} ,net_value=$D{netValue} ,zz_report_type=$S{zzReportType} ,g06_type=$S{g06Type} ,rat_level=$S{ratLevel} ,is_ppp_part=$S{isPppPart} ,is_mkt_bts_rlt=$S{isMktBtsRlt} ,is_gov_fnc_part=$S{isGovFncPart} ,is_fnc_stk=$S{isFncStk} ,is_fnc_bnd=$S{isFncBnd} ,is_fnc_scd_bnd=$S{isFncScdBnd} ,is_fnc_tsf_bnd=$S{isFncTsfBnd} ,is_oth_bnk_tls=$S{isOthBnkTls} ,is_gov_spc_bnd=$S{isGovSpcBnd} ,exchange_rate=$D{exchangeRate} ,inv_val_rate=$D{invValRate} ,pen_inv_f=$D{penInvF} ,inv_val_rate_pen=$D{invValRatePen} ,per_pen_inv_f=$D{perPenInvF} ,inv_val_rate_per_pen=$D{invValRatePerPen} ,non_grt_rate=$D{nonGrtRate} ,non_grt_amt=$D{nonGrtAmt} ,trade_place=$S{tradePlace} ,is_public=$S{isPublic} ,manage_mode=$S{manageMode} ,mark=$S{mark} ,data_insr_dt=$S{dataInsrDt} ,deal_date=$S{dealDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteSumButtomAssetAft(SqlParam<SumButtomAssetAft> params) throws Exception {
		return super.update("DELETE FROM dwd_sum_buttom_asset WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteSumButtomAssetAftByInputDate(SumButtomAssetAft params) throws Exception {
		return super.update("DELETE FROM dwd_sum_buttom_asset WHERE input_date = '"+params.getInputDate()+"'");
	}

}
