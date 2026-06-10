package com.kayak.system.service;

import com.kayak.aspect.annotations.APIAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.system.dao.DictDao;
import com.kayak.system.model.Dict;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@APIDefine(desc = "字典服务", model = Dict.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class DictService {

	private final DictDao dictDao;

	@API(desc = "查询字典列表",auth = APIAuth.YES)
	public SqlResult<Dict> find1(SqlParam<Dict> params) throws Exception {
		return find(params);
	}

	@API(desc = "查询字典列表",auth = APIAuth.NO)
	public SqlResult<Dict> find(SqlParam<Dict> params) throws Exception {
		params.setMakeSql(true);
		return dictDao.findDict(params);
	}

	@API(desc = "修改字典",auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String edit(SqlParam<Dict> params) throws Exception {
		if (dictDao.editDict(params) < 1) {
			throw new PromptException("修改失败");
		}
		return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}

	@API(desc = "添加字典", operation = APIOperation.INSTER)
	public String add(SqlParam<Dict> params) throws Exception {
		if(dictDao.findDictOnly(params).getRows().size()>0){
			return RequestSupport.updateReturnJson(false, "添加字典失败,字典已经存在", null).toString();
		}
		if (dictDao.addDict(params) < 1) {
			throw new PromptException("添加失败");
		}
		
		return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
	}

	@API(desc = "删除字典", params = "dict", operation = APIOperation.DELETE)
	public String delete(SqlParam<Dict> params) throws Exception {
		dictDao.deleteDict(params);
		// 刷新缓存
		CacheUtil.freshenDict(params.getModel().getDict());

		return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
	}

	@API(desc="唯一性检查", auth = APIAuth.NO)
	public SqlResult<Dict> findDict(SqlParam<Dict> params) throws Exception{
		params.setMakeSql(true);
		return dictDao.findDictOnly(params);
	}

	@API(desc="唯一性检查", auth = APIAuth.NO)
	public SqlResult<Dict> update(SqlParam<Dict> params) throws Exception{
//		params.setMakeSql(true);
//		return dictDao.findDictOnly(params);
		return null;
	}
}
