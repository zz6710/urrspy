package com.kayak.auth.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.util.Tools;

@Repository
public class ServerDao extends ComnDao {

	public void addServer(List<Map<String, Object>> params, String app_name) throws Exception {
		doTrans(() -> {
			super.update("DELETE FROM sys_server_method WHERE app_name = $S{app_name}",
					Tools.makeParams().put("app_name", app_name).build());

			for (Map<String, Object> param : params) {
				super.update(
						"INSERT INTO sys_server_method(server, upper, app_name, name, model_name, type, need_auth, server_desc, server_params, operation) " +
								" VALUES( $S{server}, $S{uper_server}, $S{app_name}, $S{name}, $S{model_name}, $S{type}, $S{auth}, $S{desc}, $S{params}, $S{operation})",
						param);
			}
		});
	}

	public void addGraphQLModel(String appName, List<Map<String, Object>> params) throws Exception {
		doTrans(() -> {
			super.update("DELETE FROM sys_server_model WHERE app_name = $S{app_name}",
					Tools.makeParams().put("app_name", appName).build());
			for (Map<String, Object> param : params) {
				super.update(
						"INSERT INTO sys_server_model(model_name, app_name, model_full_name, model_field, server_name, is_encrypt, encrypt_field, model_keys, model_label, model_table) VALUES( $S{model_name}, $S{app_name}, $S{model_full_name}, $S{model_field}, $S{server_name}, $S{is_encrypt}, $S{encrypt_field}, $S{model_keys}, $S{model_label}, $S{model_table})",
						param);
			}
		});
	}

}
