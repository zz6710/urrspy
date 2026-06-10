package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.CustVolRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class CustVolRgInfoErrDao extends ComnDao {

	public SqlResult<CustVolRgInfoErr> findCustVolRgInfos(SqlParam<CustVolRgInfoErr> params) throws Exception {
		String sql = "SELECT bank_code_desc, prod_code_desc, cust_no_desc, hold_date_desc, cur_desc, hold_vol_desc, hold_amt_desc, convert_rmb_desc, imp_date, register_serno, id, create_date, theory_report_start_date, theory_report_end_date, report_date\n" +
				"FROM app_cust_vol_register_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addCustVolRgInfo(SqlParam<CustVolRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_cust_vol_register_info_erdesc(bank_code_desc,prod_code_desc,cust_no_desc,hold_date_desc,cur_desc,hold_vol_desc,hold_amt_desc,convert_rmb_desc,imp_date,register_serno) VALUES($S{bankCodeDesc},$S{prodCodeDesc},$S{custNoDesc},$S{holdDateDesc},$S{curDesc},$S{holdVolDesc},$S{holdAmtDesc},$S{convertRmbDesc},$S{impDate},$S{registerSerno})",
				params.getModel());
	}
	
	public UpdateResult updateCustVolRgInfo(SqlParam<CustVolRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_cust_vol_register_info_erdesc SET bank_code_desc=$S{bankCodeDesc} ,prod_code_desc=$S{prodCodeDesc} ,cust_no_desc=$S{custNoDesc} ,hold_date_desc=$S{holdDateDesc} ,cur_desc=$S{curDesc} ,hold_vol_desc=$S{holdVolDesc} ,hold_amt_desc=$S{holdAmtDesc} ,convert_rmb_desc=$S{convertRmbDesc} ,imp_date=$S{impDate} ,register_serno=$S{registerSerno}   WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteCustVolRgInfo(SqlParam<CustVolRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_cust_vol_register_info_erdesc WHERE ",
				params.getModel());
	}

}
