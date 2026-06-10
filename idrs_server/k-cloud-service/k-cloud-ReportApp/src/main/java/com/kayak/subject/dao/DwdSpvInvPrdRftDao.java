package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.DwdSpvInvPrdRft;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwdSpvInvPrdRftDao extends ComnDao {

	public SqlResult<DwdSpvInvPrdRft> findDwdSpvInvPrdRfts(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.findRows("SELECT spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time FROM dwd_spv_inv_prd_rft", params);
	}
	public SqlResult<DwdSpvInvPrdRft> findDwdSpvInvPrdRftsSpvCode(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.findRows("SELECT spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time FROM dwd_spv_inv_prd_rft where spv_code = $S{spvCode}", params);
	}
	public SqlResult<DwdSpvInvPrdRft> findDwdSpvInvPrdRftsAmpsCode(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.findRows("SELECT spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time FROM dwd_spv_inv_prd_rft where amps_code = $S{ampsCode}", params);
	}
	public SqlResult<DwdSpvInvPrdRft> findDwdSpvInvPrdRftsAmpsCodeBySpvCode(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.findRows("SELECT spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time FROM dwd_spv_inv_prd_rft where amps_code = $S{ampsCode} AND spv_code != $S{spvCode}", params);
	}

	public UpdateResult addDwdSpvInvPrdRft(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.update("INSERT INTO dwd_spv_inv_prd_rft(spv_code,amps_code,spv_type,data_from,create_date,create_time,update_date,update_time) VALUES($S{spvCode},$S{ampsCode},$S{spvType},$S{dataFrom},$S{createDate},$S{createTime},$S{updateDate},$S{updateTime})",
				params.getModel());
	}
	
	public UpdateResult updateDwdSpvInvPrdRft(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.update("UPDATE dwd_spv_inv_prd_rft SET amps_code=$S{ampsCode} ,spv_type=$S{spvType} ,data_from=$S{dataFrom} ,create_date=$S{createDate} ,create_time=$S{createTime} ,update_date=$S{updateDate} ,update_time=$S{updateTime}  WHERE  spv_code=$S{spvCode} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwdSpvInvPrdRft(SqlParam<DwdSpvInvPrdRft> params) throws Exception {
		return super.update("DELETE FROM dwd_spv_inv_prd_rft WHERE  spv_code=$S{spvCode} ",
				params.getModel());
	}

}
