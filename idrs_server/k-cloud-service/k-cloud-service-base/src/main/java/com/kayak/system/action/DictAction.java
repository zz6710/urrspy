package com.kayak.system.action;

import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.system.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictAction extends BaseController {

	@Autowired
	private DictItemService dictItemService;

	/**
	 * 获取数据字典JSON结构结果集
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/base/dict/{dict}.json")
	public @ResponseBody String dictItemsJson(@PathVariable String dict) throws Exception {
		if (Tools.strIsEmpty(dict)) {
			throw new PromptException("dict不能为空");
		}

		List<SqlRow> dicts = CacheUtil.getDict(dict);

		return updateSuccess(dicts);
	}

	/**
	 * 获取所有的数据字典,转换成json返回到前台
	 * @throws Exception
	 */
	@RequestMapping(value = "/base/getAllDict.json")
	public @ResponseBody String getAllDict() throws Exception {
		String allDict = dictItemService.getAllDict();
		System.out.println(allDict);
		return allDict;
	}

}
