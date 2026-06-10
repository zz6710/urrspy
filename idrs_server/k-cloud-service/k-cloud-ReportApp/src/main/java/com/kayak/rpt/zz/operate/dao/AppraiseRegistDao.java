package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.AppraiseRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class AppraiseRegistDao extends ComnDao {

	public SqlResult<AppraiseRegist> findAppraiseRegists(SqlParam<AppraiseRegist> params) throws Exception {
		String sql = "SELECT id, bank_code, asset_code, valuation_date, unit_debt_net, unit_debt_full, details, register_serno, imp_date, register_date, register_status, summit_user, create_date, create_time, op_type FROM app_appraise_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getValuationStartDate())) {
			sql += " and DATE(valuation_date) >= DATE($S{valuationStartDate}) and DATE(valuation_date) <= DATE($S{valuationEndDate})";
		}
		return super.findRows(sql, DataSourceProperty.PUB,  params);
	}

	public UpdateResult addAppraiseRegist(AppraiseRegist appraiseRegist) throws Exception {
		return super.update("INSERT INTO app_appraise_regist_remark(bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type) VALUES($S{bankCode},$S{assetCode},$S{valuationDate},$D{unitDebtNet},$D{unitDebtFull},$S{details},$S{registerSerno},date_format(CURTIME(),'%H%i%s'),$S{registerDate},$S{registerStatus},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType})",
				DataSourceProperty.PUB, appraiseRegist);
	}
	
	public UpdateResult updateAppraiseRegist(SqlParam<AppraiseRegist> params) throws Exception {
		return super.update("UPDATE app_appraise_regist_remark SET bank_code=$S{bankCode} ,asset_code=$S{assetCode} ,valuation_date=$S{valuationDate} ,unit_debt_net=$D{unitDebtNet} ,unit_debt_full=$D{unitDebtFull} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType}  WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteAppraiseRegist(SqlParam<AppraiseRegist> params) throws Exception {
		return super.update("DELETE FROM app_appraise_regist_remark WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}

}
