package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.AssetRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRegistInfohDao extends ComnDao {

	public SqlResult<AssetRegistInfoh> findAssetRegistInfohs(SqlParam<AssetRegistInfoh> params) throws Exception {
		String sql = "SELECT * FROM app_asset_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getReportStartDate())) {
			sql += " and DATE(report_date) >= DATE($S{reportStartDate}) and DATE(report_date) <= DATE($S{reportEndDate})";
		}
		return super.findRows(sql, params);
	}

}
