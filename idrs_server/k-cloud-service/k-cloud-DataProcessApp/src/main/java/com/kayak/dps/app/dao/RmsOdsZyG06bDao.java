package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.RmsOdsZyG06b;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class RmsOdsZyG06bDao extends ComnDao {

	public SqlResult<RmsOdsZyG06b> findRmsOdsZyG06bs(SqlParam<RmsOdsZyG06b> params) throws Exception {
		return super.findRows("SELECT id,data_date,b09,b10,b11,b12 FROM rms_ods_zy_g06b_i", params);
	}

	public UpdateResult addRmsOdsZyG06b(SqlParam<RmsOdsZyG06b> params) throws Exception {
		return super.update("INSERT INTO rms_ods_zy_g06b_i(id,data_date,b09,b10,b11,b12) VALUES($AUTOIDI{id},$S{dataDate},$S{b09},$S{b10},$S{b11},$S{b12})",
				params.getModel());
	}
	
	public UpdateResult updateRmsOdsZyG06b(SqlParam<RmsOdsZyG06b> params) throws Exception {
		return super.update("UPDATE rms_ods_zy_g06b_i SET data_date=$S{dataDate} ,b09=$S{b09} ,b10=$S{b10} ,b11=$S{b11} ,b12=$S{b12}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteRmsOdsZyG06b(SqlParam<RmsOdsZyG06b> params) throws Exception {
		return super.update("DELETE FROM rms_ods_zy_g06b_i WHERE  id=$I{id} ",
				params.getModel());
	}

	/**
	 * @methodName countBydataDate
	 * @description 根据日期查询数量
	 * @param params 参数
	 * @return int
	 */
	public int countByDataDate(SqlParam<RmsOdsZyG06b> params) throws Exception {
		return super.findRows("select count(1) c from rms_ods_zy_g06b_i where data_date = $S{dataDate}", params.getModel()).get(0).getInteger("c");
	}
}
