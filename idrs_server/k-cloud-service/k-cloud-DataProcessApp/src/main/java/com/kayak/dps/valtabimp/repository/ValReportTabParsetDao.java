package com.kayak.dps.valtabimp.repository;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.valtabimp.model.ValReportTabParset;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ValReportTabParsetDao extends ComnDao {

	public SqlResult<ValReportTabParset> findValReportTabParsets(SqlParam<ValReportTabParset> params) throws Exception {
		ValReportTabParset v = params.getModel();
		String sql = "SELECT t1.id,t1.t8_val_reporttab_id,t1.param_type,t1.order_num,t1.param_code,t1.param_name,t1.param_data_type," +
				"t1.param_value,t1.param_condition,t1.note,t1.inputuser,t1.crt_date,t1.crt_time,t2.reporttab_name " +
				"FROM base_fa_reporttab_parset t1 " +
				"left join base_fa_reporttab t2 on t1.t8_val_reporttab_id = t2.id " +
				"where 1 = 1 ";
		if (StringUtils.isNotBlank(v.getT8ValReporttabId())){
			sql += " and t1.t8_val_reporttab_id = $S{t8ValReporttabId}" ;
		}
		if (StringUtils.isNotBlank(v.getId())){
			sql += " and t1.id = $S{Id}" ;
		}
		sql += " order by t1.id desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addValReportTabParset(SqlParam<ValReportTabParset> params) throws Exception {
		return super.update("INSERT INTO base_fa_reporttab_parset(t8_val_reporttab_id,param_type,order_num,param_code,param_name,param_data_type,param_value,param_condition,note,inputuser,crt_date,crt_time) VALUES($I{t8ValReporttabId},$I{paramType},$I{orderNum},$S{paramCode},$S{paramName},$S{paramDataType},$S{paramValue},$S{paramCondition},$S{note},$S{inputuser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'))",
				params.getModel());
	}
	
	public UpdateResult updateValReportTabParset(SqlParam<ValReportTabParset> params) throws Exception {
		return super.update("UPDATE base_fa_reporttab_parset SET t8_val_reporttab_id=$I{t8ValReporttabId} ,param_type=$I{paramType} ,order_num=$I{orderNum} ,param_code=$S{paramCode} ,param_name=$S{paramName} ,param_data_type=$S{paramDataType} ,param_value=$S{paramValue} ,param_condition=$S{paramCondition} ,note=$S{note} ,inputuser=$S{inputuser} ,crt_date=date_format(CURDATE(),'%Y%m%d') ,crt_time=date_format(CURTIME(),'%H%i%s')  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteValReportTabParset(SqlParam<ValReportTabParset> params) throws Exception {
		return super.update("DELETE FROM base_fa_reporttab_parset WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteValReportTabParset(String t8ValReporttabId) throws Exception {
		return super.update("DELETE FROM base_fa_reporttab_parset WHERE  t8_val_reporttab_id = $S{t8ValReporttabId} ",t8ValReporttabId);
	}

}
