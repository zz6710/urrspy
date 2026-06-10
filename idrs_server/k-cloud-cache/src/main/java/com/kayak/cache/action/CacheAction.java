package com.kayak.cache.action;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;

@RestController
public class CacheAction extends BaseController {

	@RequestMapping(value = "/dict/reload.json")
	public String dictReload() {
		try {
			Map<String, Object> params = RequestSupport.getParameters();

			String dict = Tools.obj2Str(params.get("dict"));
			log.info("清除数字字典缓存：" + dict);
			CacheUtil.deleteDict(dict);
			return updateSuccess();
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "/system/reload.json")
	public String systemReload() {
		try {
			log.info("清除系统参数缓存");
			CacheUtil.clearSystemParams();
			return updateSuccess();
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "/flow/reload.json")
	public String flowReload() {
		try {
			CacheUtil.initFlowCache();
			log.info("工作流刷新缓存成功");
			return updateSuccess();
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "/searchField/reload.json")
	public String searchFieldReload() {
		try {

			CacheUtil.customSearchFieldMap.clear();
			CacheUtil.customDefaultSearchFieldMap.clear();
			log.info("清除searchField缓存");
			return updateSuccess();
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}
	}

}
