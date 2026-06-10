package com.kayak.dps.app.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.cache.util.RedisUtils;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.FundNavInfoModel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FundNavInfoModelDao extends ComnDao {

	public SqlResult<FundNavInfoModel> findFundNavInfoModels(SqlParam<FundNavInfoModel> params) throws Exception {
		String sql1 = "SELECT * FROM dwd_ast_fnd_nav_inf d1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql1 += " and DATE(stop_dt) >= DATE($S{startDate}) and DATE(stop_dt) <= DATE($S{endDate})";
		}
		SqlResult<FundNavInfoModel> s1 = super.findRows(sql1, DataSourceProperty.PUB, params);
		String sql2 = "SELECT scr_id,scr_cd,trx_mkt,STOP_DT,TEN_THSD_SHR_ERN,RCT_7D_ANL_YLD,crt_dt,upd_dt FROM DWD_AST_CCY_MKT_FND_ERN_INF d1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql2 += " and DATE(stop_dt) >= DATE($S{startDate}) and DATE(stop_dt) <= DATE($S{endDate})";
		}
		SqlResult<FundNavInfoModel> s2 = super.findRows(sql2, DataSourceProperty.PUB, params);
		String sql3 = "SELECT scr_id,scr_cd,trx_mkt,TRX_DT as STOP_DT,CLS_PRC,crt_dt,upd_dt FROM DWD_AST_LST_FND_QUO_INF d1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql3 += " and DATE(TRX_DT) >= DATE($S{startDate}) and DATE(TRX_DT) <= DATE($S{endDate})";
		}
		SqlResult<FundNavInfoModel> s3 = super.findRows(sql3, DataSourceProperty.PUB, params);
		List<FundNavInfoModel> s1Rows = s1.getRows();
		List<FundNavInfoModel> s2Rows = s2.getRows();
		List<FundNavInfoModel> s3Rows = s3.getRows();
		List<FundNavInfoModel> all = new ArrayList<>();
		all.addAll(s1Rows);
		all.addAll(s2Rows);
		all.addAll(s3Rows);
		SqlResult<FundNavInfoModel> result = new SqlResult<FundNavInfoModel>();
		result.setRows(all);
		return result;
	}


	public SqlResult<FundNavInfoModel> findFundNavInfoModelsByScrCd(SqlParam<FundNavInfoModel> params) throws Exception {
		String sql1 = "SELECT * FROM dwd_ast_fnd_nav_inf d1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql1 += " and DATE(stop_dt) >= DATE($S{startDate}) and DATE(stop_dt) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getScrCd())) {
			sql1 += " and SCR_CD = $S{scrCd}";
		}
		SqlResult<FundNavInfoModel> s1 = super.findRows(sql1, DataSourceProperty.PUB, params);
		String sql2 = "SELECT scr_id,scr_cd,trx_mkt,STOP_DT,TEN_THSD_SHR_ERN,RCT_7D_ANL_YLD,crt_dt,upd_dt FROM DWD_AST_CCY_MKT_FND_ERN_INF d1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql2 += " and DATE(stop_dt) >= DATE($S{startDate}) and DATE(stop_dt) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getScrCd())) {
			sql2 += " and SCR_CD = $S{scrCd}";
		}
		SqlResult<FundNavInfoModel> s2 = super.findRows(sql2, DataSourceProperty.PUB, params);
		String sql3 = "SELECT scr_id,scr_cd,trx_mkt,TRX_DT as STOP_DT,CLS_PRC,crt_dt,upd_dt FROM DWD_AST_LST_FND_QUO_INF d1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql3 += " and DATE(TRX_DT) >= DATE($S{startDate}) and DATE(TRX_DT) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getScrCd())) {
			sql3 += " and SCR_CD = $S{scrCd}";
		}
		SqlResult<FundNavInfoModel> s3 = super.findRows(sql3, DataSourceProperty.PUB, params);
		List<FundNavInfoModel> s1Rows = s1.getRows();
		List<FundNavInfoModel> s2Rows = s2.getRows();
		List<FundNavInfoModel> s3Rows = s3.getRows();
		List<FundNavInfoModel> all = new ArrayList<>();
		all.addAll(s1Rows);
		all.addAll(s2Rows);
		all.addAll(s3Rows);
		SqlResult<FundNavInfoModel> result = new SqlResult<FundNavInfoModel>();
		result.setRows(all);
		return result;
	}

	// 插入场外基金
	public UpdateResult addFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("INSERT INTO dwd_ast_fnd_nav_inf(scr_id,scr_cd,trx_mkt,ntc_dt,stop_dt,unt_nav,crt_dt,upd_dt) VALUES($S{scrId},$S{scrCd},$S{trxMkt},$S{ntcDt},$S{stopDt},$D{untNav},date_format(SYSDATE(),'yyyyMMdd'),date_format(SYSDATE(),'yyyyMMdd'))",
				DataSourceProperty.PUB, params.getModel());
	}

	// 更新场外基金
	public UpdateResult updateFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("UPDATE dwd_ast_fnd_nav_inf SET scr_id=$S{scrId} ,scr_cd=$S{scrCd} ,trx_mkt=$S{trxMkt} ,ntc_dt=$S{ntcDt} ,stop_dt=$S{stopDt} ,upd_dt=date_format(SYSDATE(),'yyyyMMdd')  WHERE  scr_cd=$S{scrCd} and trx_mkt=$S{trxMkt} and stop_dt=$S{stopDt}",
				DataSourceProperty.PUB, params.getModel());
	}

	// 删除
	public UpdateResult deleteFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("DELETE FROM dwd_ast_fnd_nav_inf WHERE  scr_cd=$S{scrCd} and stop_dt=$S{stopDt} and trx_mkt=$S{trxMkt}" ,
				DataSourceProperty.PUB, params.getModel());
	}


	// 新增货币型基金
	public UpdateResult addToMoneyMarketFund(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("INSERT INTO DWD_AST_CCY_MKT_FND_ERN_INF(scr_id,scr_cd,trx_mkt,STOP_DT,TEN_THSD_SHR_ERN,RCT_7D_ANL_YLD,NTC_DT,crt_dt,upd_dt) VALUES($S{scrId},$S{scrCd},$S{trxMkt},$S{stopDt},$S{tenThsdShrErn},$S{rct7dAnlYld},$S{ntcDt},date_format(SYSDATE(),'yyyyMMdd'),date_format(SYSDATE(),'yyyyMMdd'))",
				DataSourceProperty.PUB, params.getModel());
	}
	// 更新货币型基金
	public UpdateResult updateToMoneyMarketFund(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("UPDATE DWD_AST_CCY_MKT_FND_ERN_INF SET scr_id=$S{scrId} ,scr_cd=$S{scrCd} ,trx_mkt=$S{trxMkt} , STOP_DT=$S{stopDt} ,upd_dt=date_format(SYSDATE(),'yyyyMMdd'),TEN_THSD_SHR_ERN=$S{tenThsdShrErn} ,RCT_7D_ANL_YLD=$S{rct7dAnlYld},NTC_DT=$S{ntcDt} WHERE   scr_cd=$S{scrCd} and trx_mkt=$S{trxMkt} and stop_dt=$S{stopDt}  ",
				DataSourceProperty.PUB, params.getModel());
	}
	// 删除货币型基金
	public UpdateResult deleteToMoneyMarketFund(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("DELETE FROM DWD_AST_CCY_MKT_FND_ERN_INF WHERE  scr_cd=$S{scrCd} and trx_mkt=$S{trxMkt} and stop_dt=$S{stopDt}" ,
				DataSourceProperty.PUB, params.getModel());
	}
	// 新增上市基金
	public UpdateResult addToPublicFund(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("INSERT INTO DWD_AST_LST_FND_QUO_INF(scr_id,scr_cd,trx_mkt,TRX_DT,CLS_PRC,crt_dt,upd_dt) VALUES($S{scrId},$S{scrCd},$S{trxMkt},$S{stopDt},$S{clsPrc},date_format(SYSDATE(),'yyyyMMdd'),date_format(SYSDATE(),'yyyyMMdd'))",
				DataSourceProperty.PUB, params.getModel());
	}

	// 更新上市基金
	public UpdateResult updateToPublicFund(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("UPDATE DWD_AST_LST_FND_QUO_INF SET scr_id=$S{scrId} ,scr_cd=$S{scrCd} ,trx_mkt=$S{trxMkt},CLS_PRC=$S{clsPrc} , TRX_DT=$S{stopDt} ,upd_dt=date_format(SYSDATE(),'yyyyMMdd'),CLS_PRC=$S{clsPrc}  WHERE   scr_cd=$S{scrCd} and trx_mkt=$S{trxMkt} and  TRX_DT=$S{stopDt}  ",
				DataSourceProperty.PUB, params.getModel());
	}

	// 删除
	public UpdateResult deleteToPublicFund(SqlParam<FundNavInfoModel> params) throws Exception {
		return super.update("DELETE FROM DWD_AST_LST_FND_QUO_INF WHERE  scr_cd=$S{scrCd} and trx_mkt=$S{trxMkt} and  TRX_DT=$S{stopDt} " ,
				DataSourceProperty.PUB, params.getModel());
	}

	public SqlRow checkToMoneyMarketFund(SqlParam<FundNavInfoModel> params) throws Exception {
		String sql = "select count(1) con from DWD_AST_CCY_MKT_FND_ERN_INF where scr_id=$S{scrId} and STOP_DT=$S{stopDt}";

		return super.findRow(sql,DataSourceProperty.PUB,  params.getModel());
	}

	public SqlRow checkToPublicFund(SqlParam<FundNavInfoModel> params) throws Exception {
		String sql = "select count(1) con from DWD_AST_LST_FND_QUO_INF where scr_id=$S{scrId} and TRX_DT=$S{stopDt}";
		return super.findRow(sql,DataSourceProperty.PUB,  params.getModel());
	}

	public SqlRow checkFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		String sql = "select count(1) con from dwd_ast_fnd_nav_inf where scr_id=$S{scrId} and STOP_DT=$S{stopDt}";
		return super.findRow(sql,DataSourceProperty.PUB,  params.getModel());
	}
}
