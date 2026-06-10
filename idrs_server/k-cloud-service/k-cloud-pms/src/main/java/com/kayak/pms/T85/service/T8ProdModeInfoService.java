package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T85.dao.T8ProdModeInfoDao;
import com.kayak.pms.T85.model.T8ProdModeInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名: T8ProdModeInfoService.java
 * 描述:  产品模型
 * 创建人: zengzt
 * 创建时间:2020年6月4日下午2:01:16
 */
@Service
@APIDefine(desc = "产品模型服务", model = T8ProdModeInfo.class)
public class T8ProdModeInfoService {

	@Autowired
	private T8ProdModeInfoDao t8ProdModeInfoDao;

	@API(desc = "查询产品模型信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8ProdModeInfo> findT8ProdModeInfos(SqlParam<T8ProdModeInfo> params) throws Exception {

		//不需要自动拼接sql
		params.setMakeSql(false);
		return t8ProdModeInfoDao.findT8ProdModeInfos(params);
	}

	@API(desc = "查询产品模型信息2", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8ProdModeInfo> findT8ProdModeInfosByCode(SqlParam<T8ProdModeInfo> params) throws Exception {

		//不需要自动拼接sql
		params.setMakeSql(false);
		return t8ProdModeInfoDao.findT8ProdModeInfosByCode(params);
	}

	@API(desc = "修改产品模型信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public String updateT8ProdModeInfo(SqlParam<T8ProdModeInfo> params) throws Exception {
		//不需要自动拼接sql
		params.setMakeSql(false);
		if(t8ProdModeInfoDao.checkProdModeName(params,true)){
			return RequestSupport.updateReturnJson(false, "产品模型名称重复，请重新输入！", null).toString();
		}
		t8ProdModeInfoDao.updateT8ProdModeInfo(params);
		return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}


	@API(desc = "添加产品模型信息", auth = APIAuth.YES,operation = APIOperation.INSTER)
	public String insertT8ProdModeInfo(SqlParam<T8ProdModeInfo> params) throws Exception {
		//不需要自动拼接sql
		params.setMakeSql(false);
		if(t8ProdModeInfoDao.checkProdModeName(params,false)){
			return RequestSupport.updateReturnJson(false, "产品模型名称重复，请重新输入！", null).toString();
		}
		t8ProdModeInfoDao.insertT8ProdModeInfo(params);
		return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
	}


	@API(desc = "删除产品模型信息", auth = APIAuth.YES ,operation = APIOperation.DELETE)
	public String deleteT8ProdModeInfo(SqlParam<T8ProdModeInfo> params) throws Exception {
		//删除前先校验是否有产品使用了该模型
		List<T8ProdModeInfo> prodModeInfoList = t8ProdModeInfoDao.checkTaProdWithMode(params);
		if(prodModeInfoList != null && prodModeInfoList.size() > 0 && StringUtils.isNotBlank(prodModeInfoList.get(0).getProdCode())){
			//throw new PromptException("已经有["+ prodModeInfoList.get(0).getProdCode() +"]等产品配置为该模型，不允许删除");
			String[] prodCodes = prodModeInfoList.get(0).getProdCode().split(",");
			if(prodCodes.length>4){
				String attr = prodCodes[0]+","+prodCodes[1]+","+prodCodes[2]+","+prodCodes[3]+","+prodCodes[4]+"...";
				return RequestSupport.updateReturnJson(false, "已经有["+ attr +"]等产品配置为该模型，不允许删除", null).toString();
			}else{
				return RequestSupport.updateReturnJson(false, "已经有["+ prodModeInfoList.get(0).getProdCode() +"]等产品配置为该模型，不允许删除", null).toString();
			}

		}

		params.setMakeSql(false);
		//删除产品清算任务配置和产品形态
		//t8ProdModeInfoDao.deleteT8ProdModeInfo(params);

		return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
	}

}
