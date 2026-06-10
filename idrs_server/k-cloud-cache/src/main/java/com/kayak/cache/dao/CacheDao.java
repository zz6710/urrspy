package com.kayak.cache.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CacheDao extends ComnDao {

	public List<SqlRow> findDictItems(String dict) throws Exception {
		Map<String, Object> params = new HashMap<>(1);
		params.put("dict", dict);
		return super.findRows("SELECT * FROM sys_dict_item WHERE dict = $S{dict} order by itemorder desc", params);
	}

	public List<SqlRow> findSystemParam(String paraid) throws Exception {
		Map<String, Object> params = new HashMap<>(1);
		params.put("paraid", paraid);
		return super.findRows("SELECT * FROM sys_param WHERE paraid = $S{paraid}", params);
	}

	/**
	 * 查询工作流业务配置
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findFlowConfig() throws Exception {
		Map<String, Object> params = new HashMap<>(1);
		return super.findRows("SELECT * FROM flow_busi_config", params);
	}

	/**
	 * 查询工作流交易配置
	 * @param transCode
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findFlowConfigByTrans(String transCode) throws Exception {
		Map<String, Object> params = new HashMap<>(1);
		return super.findRows("SELECT * FROM wf_trans_config ", params);
	}

}
