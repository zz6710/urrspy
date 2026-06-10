package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.dao.SysDictItemDao;
import com.kayak.pms.T85.model.SysDictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: SysDictItemService.java
 * 描述:   字典数据项操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月16日下午2:47:04
 */
@Service
@APIDefine(desc = "字典数据项操作服务", model = SysDictItem.class)
public class SysDictItemService {

	@Autowired
	private SysDictItemDao sysDictItemDao;

	@API(desc = "查询字典数据项", auth = APIAuth.NO)
	public SqlResult<SysDictItem> findSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		params.setMakeSql(true);
		return sysDictItemDao.findSysDictItem(params);
	}
	

	@API(desc = "查询清算类型字典数据项", auth = APIAuth.NO)
	public SqlResult<SysDictItem> findSysDictItemWithMultipleKey(SqlParam<SysDictItem> params) throws Exception {
		
		params.setMakeSql(false);
		
		String[] itemkeys = params.getModel().getItemkey().split(",");
		
		return sysDictItemDao.findSysDictItemWithMultipleKey(params,itemkeys);
	}

	@API(desc = "新增字典数据项", auth = APIAuth.NO)
	public int addSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		params.setMakeSql(true);
		return sysDictItemDao.insertSysDictItem(params);
	}

	@API(desc = "修改字典数据项", auth = APIAuth.NO)
	public int updateSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		params.setMakeSql(false);
		return sysDictItemDao.updateSysDictItem(params);
	}

	@API(desc = "删除字典数据项", auth = APIAuth.NO)
	public int deleteSysDictItem(SqlParam<SysDictItem> params) throws Exception {
		
		params.setMakeSql(false);
		return sysDictItemDao.deleteSysDictItem(params);
	}

    public String findDictValueByKey(String itemKey,String dist) throws Exception {
		return sysDictItemDao.findDictValueByKey(itemKey,dist);
    }
}
