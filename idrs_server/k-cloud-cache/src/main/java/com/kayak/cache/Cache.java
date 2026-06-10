package com.kayak.cache;

import com.kayak.cache.dao.CacheDao;
import com.kayak.core.sql.SqlRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class Cache {

	private static final Logger log = LoggerFactory.getLogger(Cache.class);

	@Autowired
	private CacheDao cacheDao;

	protected String dictKey = "dict";

	public abstract List<SqlRow> getDict(String dict);

	public abstract String getDictItem(String dict, String key);

	public abstract String getDictItemKey(String dict, String val);

	public abstract void deleteDict(String dict);

	public abstract void delDictItem(String dict, String key);

	public abstract String getSystemParam(String key);

	public abstract String getAllSystemParam();

	public abstract void clearSystemParam();

	public void reloadDictCache(String dict) {
		try {
			List<SqlRow> data = cacheDao.findDictItems(dict);
			setDictCache(dict, data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	public void reloadSystemParamsCache(String key) {
		try {
			List<SqlRow> data = cacheDao.findSystemParam(key);
			if (CollectionUtils.isEmpty(data)) {
				return;
			}
			setParamsCache(key, data.get(0).getString("paravalue"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	};

	public abstract <T> void setDictCache(String key, T data);

	public abstract void setParamsCache(String key, String data);


	/**
	 * ��ʼ������������
	 */
	public void initFlowCache(){
		try {
			List<SqlRow> data = cacheDao.findFlowConfig();
			this.setFlowCache(data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("���¹�����ҵ��������û����쳣", e);
		}
	}

	/**
	 * ���ù�����ҵ�����û���
	 * @param data
	 */
	public abstract void setFlowCache(List data);

	/**
	 * ��ȡ������ҵ������
	 * @param server
	 * @return
	 */
	public abstract Object getFlowConfig(String server);


}
