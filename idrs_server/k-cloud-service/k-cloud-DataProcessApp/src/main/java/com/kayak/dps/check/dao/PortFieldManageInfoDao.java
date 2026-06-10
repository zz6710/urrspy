package com.kayak.dps.check.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.check.model.PortFieldManageInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class PortFieldManageInfoDao extends ComnDao {

	public SqlResult<PortFieldManageInfo> findPortFieldManageInfos(SqlParam<PortFieldManageInfo> params) throws Exception {
		return super.findRows("SELECT id,port_code,field_code,field_name,field_type,field_length,field_dights,field_seq,file_field_code,inputuser,crt_date,crt_time,upd_date,upd_time FROM base_port_field_manage",DataSourceProperty.PUB, params);
	}

	public UpdateResult addPortFieldManageInfo(SqlParam<PortFieldManageInfo> params) throws Exception {
		return super.update("INSERT INTO base_port_field_manage(id,port_code,field_code,field_name,field_type,field_length,field_dights,field_seq,file_field_code,inputuser,crt_date,crt_time,upd_date,upd_time) VALUES((SELECT TBM.id FROM(SELECT MAX(ID)+1 id  FROM base_port_field_manage) TBM),$S{portCode},$S{fieldCode},$S{fieldName},$S{fieldType},$D{fieldLength},$D{fieldDights},$I{fieldSeq},$S{fileFieldCode},$S{inputuser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'))",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updatePortFieldManageInfo(SqlParam<PortFieldManageInfo> params) throws Exception {
		return super.update("UPDATE base_port_field_manage SET port_code=$S{portCode} ,field_code=$S{fieldCode} ,field_name=$S{fieldName} ,field_type=$S{fieldType} ,field_length=$I{fieldLength} ,field_dights=$D{fieldDights} ,field_seq=$I{fieldSeq}  ,file_field_code=$S{fileFieldCode} ,inputuser=$S{inputuser} ,upd_date= date_format(CURDATE(),'%Y%m%d') ,upd_time=date_format(CURTIME(),'%H%i%s')  WHERE  id=$I{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult deletePortFieldManageInfo(SqlParam<PortFieldManageInfo> params) throws Exception {
		return super.update("DELETE FROM base_port_field_manage WHERE  id=$I{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

}
