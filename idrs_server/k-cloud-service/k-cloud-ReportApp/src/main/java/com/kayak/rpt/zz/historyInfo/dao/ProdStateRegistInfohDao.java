package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.ProdStateRegistInfoh;
import com.kayak.rpt.zz.historyInfo.model.ProdTransRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ProdStateRegistInfohDao extends ComnDao {

	public SqlResult<ProdStateRegistInfoh> findProdStateRegistInfohs(SqlParam<ProdStateRegistInfoh> params) throws Exception {
		String sql = "SELECT PROD_CODE,BANK_CODE,PROD_REG_ENC,TOT_ASSETS,RATE,VALDATE,DETAILS,CREATE_DATE,THEORY_REPORT_START_DATE,THEORY_REPORT_END_DATE,REGISTER_SERNO,IMP_DATE,REGISTER_DATE,REGISTER_STATUS,ID FROM app_prod_state_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

}
