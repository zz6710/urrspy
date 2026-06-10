package com.kayak.graphql.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UpdateFieldDao extends ComnDao {

	public void updateField(Map map) throws Exception {

		super.update(
				"INSERT INTO t8_batch_data(id,batch_type,sub_params,crt_date,crt_time,remark,crt_user) " +
						"VALUES($AUTOIDS{t8_batch_data},$S{batchType},$S{subParams},$S{crtDate},$S{crtTime},$S{remark},$S{crtUser})", map);

	}

	public List<Map<String, Object>> findBatchSearchField(String modelName) throws Exception {
		List<SqlRow> list = super.findRows(
				"SELECT id, method_name, batch_type, batch_name, remark FROM t8_batch_config", Tools.makeParams().put("modelName", modelName).build());

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (list != null && list.size() > 0) {
			for (SqlRow sqlRow : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", sqlRow.getString("id"));
				map.put("methodName", sqlRow.getString("method_name"));
				map.put("batchType", sqlRow.getString("batch_type"));
				map.put("batchName", sqlRow.getString("batch_name"));
				map.put("remark", sqlRow.getString("remark"));

				result.add(map);
			}
		}

		return result;

	}

}
