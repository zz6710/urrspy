package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.AppOverseasInvInfo1;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class AppOverseasInvInfo1Dao extends ComnDao {

	public SqlResult<AppOverseasInvInfo1> findAppOverseasInvInfo1s(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return super.findRows("SELECT report_date,comp_nm,fund_type,inv_asst_mkt,gro_rate FROM app_overseas_inv_info_1", params);
	}

	public List<SqlRow> findAppOverseasInvInfo1s(Map<String, Object> params) throws Exception {
		return super.findRows("SELECT inv_asst_mkt c2,gro_rate c3 FROM app_overseas_inv_info_1 where report_date=$S{reportDate}", params);
	}

	public UpdateResult addAppOverseasInvInfo1(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return super.update("INSERT INTO app_overseas_inv_info_1(report_date,comp_nm,fund_type,inv_asst_mkt,gro_rate) VALUES($S{reportDate},$S{compNm},$S{fundType},$D{invAsstMkt},$D{groRate})",
				params.getModel());
	}
	
	public UpdateResult updateAppOverseasInvInfo1(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return super.update("UPDATE app_overseas_inv_info_1 SET report_date=$S{reportDate} ,comp_nm=$S{compNm} ,fund_type=$S{fundType} ,inv_asst_mkt=$D{invAsstMkt} ,gro_rate=$D{groRate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteAppOverseasInvInfo1(SqlParam<AppOverseasInvInfo1> params) throws Exception {
		return super.update("DELETE FROM app_overseas_inv_info_1 WHERE ",
				params.getModel());
	}

}
