package com.kayak.system.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.system.dao.DictDao;
import com.kayak.system.model.Dict;
import com.kayak.system.model.DictItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@APIDefine(desc = "字典项服务", model = DictItem.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DictItemService {

	private final DictDao dictDao;

	@API(desc = "查询字典项列表" ,auth = APIAuth.NO)
	public SqlResult<DictItem> find(SqlParam<DictItem> params) throws Exception {
		params.setMakeSql(true);
		return dictDao.findDictItem(params);
	}
	@API(desc = "查询字典项列表(数据字典界面)" ,auth = APIAuth.NO)
	public SqlResult<DictItem> findWithoutParam(SqlParam<DictItem> params) throws Exception {
		params.setMakeSql(false);
		return dictDao.findDictItem(params);
	}

	@API(desc = "查询所有的省份除台,澳,香,其他" ,auth = APIAuth.NO)
	public SqlResult<SqlRow> fidAllArea(SqlParam<DictItem> params) throws Exception {
		return SqlResult.build(dictDao.fidAllArea(params));
	}

	@API(desc = "添加数据字典子项", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String add(SqlParam<DictItem> params) throws Exception {
		if(dictDao.findDictItemOnly(params).getRows().size()>0){
			return RequestSupport.updateReturnJson(false, "添加数字字典失败,数字字典已经存在", null).toString();
		}
		int count = dictDao.addDictItem(params);
		if (count < 1) {
			throw new PromptException("添加失败");
		}

		// 刷新缓存
		CacheUtil.freshenDict(params.getModel().getDict());
		return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
	}

	@API(desc = "修改字典子项", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public String edit(SqlParam<DictItem> params) throws Exception {
		int count = dictDao.editDictItem(params);
		if (count < 1) {
			throw new PromptException("修改失败");
		}

		// 刷新缓存
		CacheUtil.freshenDict(params.getModel().getDict());
		return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}

	@API(desc = "删除字典子项", auth = APIAuth.YES,operation = APIOperation.DELETE)
	public String delete(SqlParam<DictItem> params) throws Exception {
		dictDao.deleteDictItem(params);

		// 刷新缓存
		CacheUtil.freshenDict(params.getModel().getDict());
		return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
	}

	@API(desc="检查唯一性", auth = APIAuth.NO)
	public SqlResult<DictItem> findDictItemOnly(SqlParam<DictItem> param)throws Exception{
		param.setMakeSql(false);
		return dictDao.findDictItemOnly(param);
	}

	/**
	 * 获取所有的数据字典,转换成json返回到前台
	 * @return
	 */
	public String getAllDict() throws Exception {
		HashMap<String, List<HashMap<String,String>>> dictMap = new HashMap<>();
		//从数据库中查询所有的数据字典
		List<SqlRow> allDict = dictDao.findAllDict();
		//将数据字典存储到map集合中
		if (!CollectionUtils.isEmpty(allDict)) {
			allDict.forEach(item -> {
				String dict = item.getString("dict");
				List<HashMap<String, String>> list = dictMap.get(dict);
				HashMap<String,String> hashMap = new HashMap<>();
				//判断保存数据字典的list是否为空
				if (CollectionUtils.isEmpty(list)) {
					List<HashMap<String,String>> mapList = new ArrayList<>();
					hashMap.put("itemKey", item.getString("itemKey"));
					hashMap.put("itemval", item.getString("itemval"));
					hashMap.put("dict", dict);
					mapList.add(hashMap);
					dictMap.put(dict, mapList);

				} else {
					hashMap.put("itemKey", item.getString("itemKey"));
					hashMap.put("itemval", item.getString("itemval"));
					hashMap.put("dict", dict);
					list.add(hashMap);
				}
			});
		}
		return JSONObject.toJSONString(dictMap);
	}
}
