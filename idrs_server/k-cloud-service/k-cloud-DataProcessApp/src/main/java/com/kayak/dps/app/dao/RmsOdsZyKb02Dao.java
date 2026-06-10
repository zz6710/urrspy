package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.RmsOdsZyKb02;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class RmsOdsZyKb02Dao extends ComnDao {

	public SqlResult<RmsOdsZyKb02> findRmsOdsZyKb02s(SqlParam<RmsOdsZyKb02> params) throws Exception {
		return super.findRows("SELECT id,data_date,d01 FROM rms_ods_zy_kb02", params);
	}

	public UpdateResult addRmsOdsZyKb02(SqlParam<RmsOdsZyKb02> params) throws Exception {
		return super.update("INSERT INTO rms_ods_zy_kb02(id,data_date,d01) VALUES($AUTOIDI{id},$S{dataDate},$S{d01})",
				params.getModel());
	}
	
	public UpdateResult updateRmsOdsZyKb02(SqlParam<RmsOdsZyKb02> params) throws Exception {
		return super.update("UPDATE rms_ods_zy_kb02 SET data_date=$S{dataDate} ,d01=$S{d01}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteRmsOdsZyKb02(SqlParam<RmsOdsZyKb02> params) throws Exception {
		return super.update("DELETE FROM rms_ods_zy_kb02 WHERE  id=$I{id} ",
				params.getModel());
	}

}
