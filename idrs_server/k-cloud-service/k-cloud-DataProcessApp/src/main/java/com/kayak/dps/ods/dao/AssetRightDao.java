package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AssetRightModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRightDao extends ComnDao {

	public SqlResult<AssetRightModel> findAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		String sql="SELECT t1.ID,t1.ASS_NBR_EXT,t1.ORG_NBR_EXT,t3.ORG_FULL_NAME,t1.TRADE_PLACES,t1.INDUSTRY_ISSUER,t1.INVESTMENT_TYPE," +
				"	t1.SHAREHOLD,t1.IS_CHANNEL,t1.CHANNEL_CODE,t1.GG_ENTER_TYPE_SCALE,t1.GG_ENTER_TYPE_TECH,t1.TRANS_CCY," +
				"	t1.GG_ENTER_TYPE_ECONOMIC,t1.GG_PLEDGED_FINACE,t1.GG_DEBT_EQUITY_SWAP,t1.CBC_TYPE," +
				"	t1.CBC_SUB_TYPE,t1.GG_CBC_TYPE,t1.GG_CBC_SUB_TYPE " +
				"   FROM ODS_ASS_RIGHT_BAS_INF t1 " +
				"   LEFT JOIN ODS_ORG_INFO t3 ON t3.ORG_NBR_EXT=t1.ORG_NBR_EXT"  +
				"	WHERE 1=1 " ;
		if (StringUtils.isNotBlank(params.getModel().getAssNbrExt())) {
			sql += " AND t1.ASS_NBR_EXT like '%$U{assNbrExt}%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getOrgNbrExt())) {
			sql += " AND t1.ORG_NBR_EXT like '%$U{orgNbrExt}%' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getIndustryIssuer())) {
			sql += " AND t1.INDUSTRY_ISSUER = $S{industryIssuer}";
		}
		return super.findRows(sql, params);
	}


	public UpdateResult addAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		return super.update("INSERT INTO ods_ass_right_bas_inf (ID, ASS_NBR_EXT, ORG_NBR_EXT, TRADE_PLACES, " +
						"INDUSTRY_ISSUER, INVESTMENT_TYPE, SHAREHOLD, IS_CHANNEL, CHANNEL_CODE, GG_ENTER_TYPE_SCALE, " +
						"GG_ENTER_TYPE_TECH, GG_ENTER_TYPE_ECONOMIC, GG_PLEDGED_FINACE, GG_DEBT_EQUITY_SWAP, CBC_TYPE, " +
						"CBC_SUB_TYPE, GG_CBC_TYPE, GG_CBC_SUB_TYPE, CRT_DATE, UPD_DATE, DEAL_DATE) VALUES ($AUTOIDS{ods_ass_right_bas_inf}," +
						"$S{assNbrExt},$S{orgNbrExt},$S{tradePlaces},$S{industryIssuer},$S{investmentType}," +
						"$S{sharehold},$S{isChannel},$S{channelCode},$S{ggEnterTypeScale},$S{ggEnterTypeTech}," +
						"$S{ggEnterTypeEconomic},$S{ggPledgedFinace},$S{ggDebtEquitySwap},$S{cbcType},$S{cbcSubType}," +
						"$S{ggCbcType},$S{ggCbcSubType},$S{crtDate},$S{updDate},$S{dealDate})",
				params.getModel());
	}

	public UpdateResult updateAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		return super.update("UPDATE ods_ass_right_bas_inf SET ORG_NBR_EXT = $S{orgNbrExt},TRADE_PLACES= $S{tradePlaces}," +
						"INDUSTRY_ISSUER= $S{industryIssuer},INVESTMENT_TYPE=$S{investmentType},SHAREHOLD=$S{sharehold}," +
						"IS_CHANNEL=$S{isChannel},CHANNEL_CODE=$S{channelCode},GG_ENTER_TYPE_SCALE=$S{ggEnterTypeScale}," +
						"GG_ENTER_TYPE_TECH=$S{ggEnterTypeTech},GG_ENTER_TYPE_ECONOMIC=$S{ggEnterTypeEconomic}," +
						"GG_PLEDGED_FINACE=$S{ggPledgedFinace},GG_DEBT_EQUITY_SWAP=$S{ggDebtEquitySwap},CBC_TYPE=$S{cbcType}," +
						"CBC_SUB_TYPE=$S{cbcSubType},GG_CBC_TYPE=$S{ggCbcType},GG_CBC_SUB_TYPE=$S{ggCbcSubType},UPD_DATE=$S{updDate} WHERE ID = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		return super.update("DELETE FROM ods_ass_right_bas_inf WHERE ID=$S{id} ",
				params.getModel());
	}
	public int findAssetCount(SqlParam<AssetRightModel> params) throws Exception {
		String sql="SELECT count(1) cnt " +
				"   FROM ods_ass_right_bas_inf t1 "  +
				"	WHERE t1.ASS_NBR_EXT ='"+params.getParams().get("assNbrExt")+"'" ;
		return super.findRow(sql, null).getInteger("cnt");
	}

}
