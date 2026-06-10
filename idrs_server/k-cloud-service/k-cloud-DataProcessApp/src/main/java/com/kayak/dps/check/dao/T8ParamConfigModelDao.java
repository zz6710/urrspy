package com.kayak.dps.check.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.utils.AESUtils;
import com.kayak.dps.check.model.T8ParamConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class T8ParamConfigModelDao extends ComnDao {

	public SqlResult<T8ParamConfigModel> findT8ParamConfigModels(SqlParam<T8ParamConfigModel> params) throws Exception {
		return super.findRows("SELECT id,config_describe,config_name,(case when config_name in ('USERNAMES','PASSWORD') then '********' else config_code end) config_code,config_code real_config_code,config_type,status FROM base_port_config_info", params);
	}

	public UpdateResult addT8ParamConfigModel(SqlParam<T8ParamConfigModel> params) throws Exception {
		//对密码字段进行AES加密
		if("USERNAMES".equals(params.getModel().getConfigName())||"PASSWORD".equals(params.getModel().getConfigName())){
			String real_config_code = params.getModel().getRealConfigCode();
			if(StringUtils.isNotEmpty(real_config_code)) {
				try {
					params.getModel().setRealConfigCode(AESUtils.AESEncrypted(real_config_code));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.update("INSERT INTO base_port_config_info(id,config_describe,config_name,config_code,config_type,status) VALUES($AUTOIDI{id},$S{configDescribe},$S{configName},$S{realConfigCode},$S{configType},$S{status})",
				params.getModel());
	}
	
	public UpdateResult updateT8ParamConfigModel(SqlParam<T8ParamConfigModel> params) throws Exception {
		//对密码字段进行AES加密
		if("USERNAMES".equals(params.getModel().getConfigName())||"PASSWORD".equals(params.getModel().getConfigName())){
			String real_config_code = params.getModel().getRealConfigCode();
			if(StringUtils.isNotEmpty(real_config_code)) {
				try {
					params.getModel().setRealConfigCode(AESUtils.AESEncrypted(real_config_code));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.update("UPDATE base_port_config_info SET config_describe=$S{configDescribe} ,config_name=$S{configName} ,config_code=$S{realConfigCode} ,config_type=$S{configType} ,status=$S{status}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteT8ParamConfigModel(SqlParam<T8ParamConfigModel> params) throws Exception {
		return super.update("DELETE FROM base_port_config_info WHERE  id=$I{id} ",
				params.getModel());
	}

}
