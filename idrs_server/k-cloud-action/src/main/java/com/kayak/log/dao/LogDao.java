package com.kayak.log.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;

@Repository
public class LogDao extends ComnDao {

	public void addLog(Map<Object, Object> addParams) throws Exception {
		update("INSERT INTO sys_operation_log (userid, server, server_desc, method, method_desc, submit_old_data, submit_data, operation_date, operation_time, result, error_msg) "
				+ " VALUES ($S{userid}, $S{server}, $S{server_desc}, $S{method}, $S{method_desc}, $S{submit_old_data}, $S{submit_data}, $S{operation_date}, $S{operation_time}, $S{result}, $S{error_msg})",
				addParams);
	}

}
