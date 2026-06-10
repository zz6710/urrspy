package com.kayak.cache.impl;

import com.kayak.cache.Cache;
import com.kayak.core.sql.SqlRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("cache")
@ConditionalOnExpression("${kayak.cache:'memory'}.equals('memory')")
public class MemoryCache extends Cache {
	private static final Logger log = LoggerFactory.getLogger(MemoryCache.class);

	private Map<String, Object> dictCache = new ConcurrentHashMap<String, Object>();				// 数据字典缓存
	private Map<String, String> paramsCache = new ConcurrentHashMap<String, String>();			// 系统参数缓存
	private Map<String, Object> flowCache = new ConcurrentHashMap<>();				// 工作流缓存

	@SuppressWarnings("unchecked")
	@Override
	public List<SqlRow> getDict(String dict) {
		if (!dictCache.containsKey(dict)) {
			super.reloadDictCache(dict);
		}

		if (dictCache.containsKey(dict)) {
			return (List<SqlRow>) dictCache.get(dict);
		}
		return null;
	}

	@Override
	public String getDictItem(String dict, String key) {
		List<SqlRow> items = getDict(dict);

		if (items != null) {
			for (SqlRow item : items) {
				if (item.getString("itemkey").equals(key)) {
					return item.getString("itemval");
				}
			}
		}

		return null;
	}

	@Override
	public String getDictItemKey(String dict, String val) {
		List<SqlRow> items = getDict(dict);

		if (items != null) {
			for (SqlRow item : items) {
				if (item.getString("itemval").equals(val)) {
					return item.getString("itemkey");
				}
			}
		}

		return null;
	}

	@Override
	public void deleteDict(String dict) {
		if (dict != null) {
			dictCache.remove(dict);
		}
	}

	@Override
	public void delDictItem(String dict, String key) {
		List<SqlRow> items = getDict(dict);

		if (items == null) {
			return;
		}

		ListIterator<SqlRow> itemsIterator = items.listIterator();
		while (itemsIterator.hasNext()) {
			if (itemsIterator.next().getString("itemkey").equals(key)) {
				itemsIterator.remove();
				return;
			}
		}
	}

	@Override
	public String getSystemParam(String key) {
		//if (!paramsCache.containsKey(key)) {
			super.reloadSystemParamsCache(key);
		//}

		if (paramsCache.containsKey(key)) {
			return paramsCache.get(key);
		}
		return null;
	}

	@Override
	public String getAllSystemParam() {
		return null;
	}

	@Override
	public <T> void setDictCache(String key, T data) {
		dictCache.put(key, data);
	}

	@Override
	public void setParamsCache(String key, String data) {
		paramsCache.put(key, data);
	}

	@Override
	public void clearSystemParam() {
		paramsCache.clear();
	}

	@Override
	public void setFlowCache(List data) {
		flowCache.clear();
		if (CollectionUtils.isEmpty(data)) {
			return;
		}
		for (Object o : data) {
			flowCache.put(((SqlRow)o).getString("server"), o);
		}
		log.info(" 完成工作流业务关联配置缓存更新 ");
	}

	/**
	 * 获取工作流业务配置
	 *
	 * @param server
	 * @return
	 */
	@Override
	public Object getFlowConfig(String server) {
		return flowCache.get(server);
	}

}
