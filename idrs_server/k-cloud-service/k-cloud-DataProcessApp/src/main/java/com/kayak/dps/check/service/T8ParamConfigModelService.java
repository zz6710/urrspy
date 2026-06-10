package com.kayak.dps.check.service;

import com.kayak.dps.app.utils.AESUtils;
import com.kayak.dps.check.dao.T8ParamConfigModelDao;
import com.kayak.dps.check.model.T8ParamConfigModel;
import com.kayak.dps.check.model.T8SQLConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import java.util.*;


@Service
@APIDefine(desc = "接口参数配置服务", model = T8ParamConfigModel.class)
public class T8ParamConfigModelService {

	@Autowired
	private T8ParamConfigModelDao t8ParamConfigModelDao;

	@API(desc = "查询接口参数配置信息", auth = APIAuth.YES)
	public SqlResult<T8ParamConfigModel> findT8ParamConfigModels(SqlParam<T8ParamConfigModel> params) throws Exception {
		params.setMakeSql(true);
		SqlResult<T8ParamConfigModel> result  = t8ParamConfigModelDao.findT8ParamConfigModels(params);
		List<T8ParamConfigModel> list  = result.getRows();
		SqlResult<T8ParamConfigModel> sqlResult = new SqlResult<>();
		for (T8ParamConfigModel configModel:list) {
			//对密码字段进行AES解密
			if("USERNAMES".equals(configModel.getConfigName())||"PASSWORD".equals(configModel.getConfigName())){
				String real_config_code = configModel.getRealConfigCode();
				if(StringUtils.isNotEmpty(real_config_code)) {
					try {
						configModel.setRealConfigCode(AESUtils.AESDecrypted(real_config_code));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		sqlResult.setRows(list);
		sqlResult.setResults(result.getResults());
		return sqlResult;
	}

	@API(desc = "添加接口参数配置", params = "id,config_describe,config_name,config_code,config_type,status", auth = APIAuth.YES)
	public int addT8ParamConfigModel(SqlParam<T8ParamConfigModel> params) throws Exception {
		return t8ParamConfigModelDao.addT8ParamConfigModel(params).getEffect();
	}
	
	@API(desc = "修改接口参数配置", params = "id,config_describe,config_name,config_code,config_type,status", auth = APIAuth.YES)
	public int updateT8ParamConfigModel(SqlParam<T8ParamConfigModel> params) throws Exception {
		return t8ParamConfigModelDao.updateT8ParamConfigModel(params).getEffect();
	}
	
	@API(desc = "删除接口参数配置", params = "id,config_describe,config_name,config_code,config_type,status", auth = APIAuth.YES)
	public int deleteT8ParamConfigModel(SqlParam<T8ParamConfigModel> params) throws Exception {
		return t8ParamConfigModelDao.deleteT8ParamConfigModel(params).getEffect();
	}

}
