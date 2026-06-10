package com.kayak.rpt.zz.manage.dao;

import com.kayak.rpt.zz.manage.model.OdsSubProdBaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class OdsSubProdBaseInfoDao extends ComnDao {

	public SqlResult<OdsSubProdBaseInfo> findOdsSubProdBaseInfos(SqlParam<OdsSubProdBaseInfo> params) throws Exception {
		String sql = "SELECT * " +
				"       FROM (SELECT prod_code,prod_name," +
				"                    case when mother_fund_flag = '0' then prod_status " +
				"                         when mother_fund_flag = '2' and run_stat = '5' then '04' " +
				"                         when mother_fund_flag = '2' and run_stat = '7' then '05' " +
				"                         when mother_fund_flag = '2' and run_stat = '9' then '06' " +
				"                         when mother_fund_flag = '2' and run_stat = '10' then '07' end run_stat," +
				"                    mother_fund_code,prod_type,subscr_sd_earliest,subscr_ed_latest,establish_date,real_end_date,perfm_benchm_type,perfm_benchm_upper,perfm_benchm_lower,yjbjjzsm_pj,is_esg,is_inclusive,is_pension,pen_inv_prod_f,per_pen_inv_prod_f " +
				"               FROM ods_prod_base_info " +
				"              WHERE mother_fund_flag IN ('0', '2')) t " +
				"      WHERE 1 = 1 ";
		if (StringUtils.isNotEmpty(params.getModel().getMdDateRangeStart())) {
			sql += "     AND subscr_sd_earliest BETWEEN $S{mdDateRangeStart} AND $S{mdDateRangeEnd} ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getSdDateRangeStart())) {
			sql += "     AND establish_date BETWEEN $S{sdDateRangeStart} AND $S{sdDateRangeEnd} ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getEdDateRangeStart())) {
			sql += "     AND real_end_date BETWEEN $S{edDateRangeStart} AND $S{edDateRangeEnd} ";
		}
		return super.findRows(sql, params);
	}

}
