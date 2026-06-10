package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.ProdStateRgInfoErr;
import com.kayak.rpt.zz.errorInfo.model.ProdTransRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ProdStateRgInfoErrDao extends ComnDao {

	public SqlResult<ProdStateRgInfoErr> findProdStateRgInfoErrs(SqlParam<ProdStateRgInfoErr> params) throws Exception {
		String sql = "SELECT PROD_CODE,BANK_CODE_DESC,PROD_REG_ENC_DESC,TOT_ASSETS_DESC,VALDATE_DESC,DETAILS_DESC,CREATE_DATE,THEORY_REPORT_START_DATE,THEORY_REPORT_END_DATE,REGISTER_SERNO,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,ID " +
				"FROM app_prod_state_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}
}
