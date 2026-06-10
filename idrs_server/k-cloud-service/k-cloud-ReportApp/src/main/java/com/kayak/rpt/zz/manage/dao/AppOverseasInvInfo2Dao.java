package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.AppOverseasInvInfo2;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class AppOverseasInvInfo2Dao extends ComnDao {

	public SqlResult<AppOverseasInvInfo2> findAppOverseasInvInfo2s(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return super.findRows("SELECT report_date,comp_nm,prod_type,inv_asst_mkt,gro_rate FROM app_overseas_inv_info_2 order by seq_type", params);
	}

	public List<SqlRow> findAppOverseasInvInfo2s(Map<String, Object> params) throws Exception {
		return super.findRows("SELECT prod_type c1,inv_asst_mkt c2,gro_rate c3 FROM app_overseas_inv_info_2 where report_date=$S{reportDate} order by seq_type", params);
	}

	public UpdateResult addAppOverseasInvInfo2(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return super.update("INSERT INTO app_overseas_inv_info_2(report_date,comp_nm,prod_type,inv_asst_mkt,gro_rate) VALUES($S{reportDate},$S{compNm},$S{prodType},$D{invAsstMkt},$D{groRate})",
				params.getModel());
	}
	
	public UpdateResult updateAppOverseasInvInfo2(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return super.update("UPDATE app_overseas_inv_info_2 SET report_date=$S{reportDate} ,comp_nm=$S{compNm} ,prod_type=$S{prodType} ,inv_asst_mkt=$D{invAsstMkt} ,gro_rate=$D{groRate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAppOverseasInvInfo2(SqlParam<AppOverseasInvInfo2> params) throws Exception {
		return super.update("DELETE FROM app_overseas_inv_info_2 WHERE ",
				params.getModel());
	}

}
