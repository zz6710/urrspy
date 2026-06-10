package com.kayak.auth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;

@Repository
public class ServerDao extends ComnDao {

	public List<SqlRow> findServerModels() throws Exception {
		return super.findRows(
				"SELECT model_name,app_name,model_full_name,model_field,server_name FROM sys_server_model");
	}

	public List<SqlRow> findServers() throws Exception {
		return super.findRows(
				"SELECT server,upper,app_name,name,server_desc,model_name FROM sys_server_method WHERE need_auth = 1");
	}

	public List<SqlRow> findAuthOpChecks() throws Exception {
		return super.findRows(
				"SELECT id, server,field,fieldtype,logic,value,opjoin,descript FROM sys_auth_op_check");
	}

	public List<SqlRow> findAuthRoleChecks() throws Exception {
		return super.findRows(
				"SELECT id,roleid, server,field,fieldtype,logic,value,opjoin,descript FROM sys_auth_role_check");
	}


}
