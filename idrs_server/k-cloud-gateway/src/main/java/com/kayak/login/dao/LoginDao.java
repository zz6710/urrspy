package com.kayak.login.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.system.constants.ServerMethodType;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;

@Repository
public class LoginDao extends ComnDao {

	public SqlRow findUser(String loginname) throws Exception {
		return super.findRow("SELECT * FROM sys_user WHERE loginname = $S{loginname}", loginname);
	}

	public SqlRow findUserByPhone(String mobileno) throws Exception {
		return super.findRow("SELECT * FROM sys_user WHERE mobileno = $S{mobileno}", mobileno);
	}

	public SqlRow findUserByUserid(String userid) throws Exception {
		return super.findRow("SELECT * FROM sys_user WHERE userid = $S{userid}", userid);
	}

	public void updateUserLogin(String loginname, String lastlogintime, String lastloginstation) throws Exception {
		super.update(
				"UPDATE sys_user SET pwderrtimes=0, lastlogintime=$S{lastlogintime}, lastloginstation=$S{lastloginstation} WHERE loginname = $N{loginname}",
				Tools.makeParams().put("loginname", loginname).put("lastlogintime", lastlogintime)
						.put("lastloginstation", lastloginstation).build());
	}

	public void updateUserLock(String loginname, int pwderrtimes, String pwderrlockdt) throws Exception {
		super.update(
				"UPDATE sys_user SET pwderrtimes=$I{pwderrtimes}, pwderrlockdt=$S{pwderrlockdt} WHERE loginname = $S{loginname}",
				Tools.makeParams().put("loginname", loginname).put("pwderrtimes", pwderrtimes)
						.put("pwderrlockdt", pwderrlockdt).build());
	}

	public void updateUserLockTime(String loginname) throws Exception {
		super.update("UPDATE sys_user SET pwderrtimes = pwderrtimes +1 WHERE loginname = $S{loginname}",
				Tools.makeParams().put("loginname", loginname).build());
	}

	public void resetUserLock(int pwderrtimes, String loginname) throws Exception {
		super.update("UPDATE sys_user SET pwderrtimes = $I{pwderrtimes}, pwderrlockdt = '' WHERE loginname = $S{loginname}",
				Tools.makeParams().put("pwderrtimes", pwderrtimes).put("loginname", loginname).build());
	}

	public List<SqlRow> findRoleServers(String userid) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("userid", userid);
		return super.findRows("SELECT DISTINCT t1.server,t1.need_auth, t1.model_name FROM sys_server_method t1 join sys_role_server t2 on (t1.server = t2.server)\n" +
						"   join sys_user_role t3 on (t2.roleid = t3.roleid) where t3.userid = $S{userid} " +
						"   union " +
						"  SELECT server,need_auth, model_name FROM sys_server_method where need_auth='0'",
				map);
	}

	public List<SqlRow> findPublicServers() throws Exception {
		return super.findRows("SELECT server, model_name FROM sys_server_method WHERE need_auth != '1' AND type='" + ServerMethodType.CHILD +"'");
	}

	public List<SqlRow> findRoleMenu(String userid) throws Exception {
		return super.findRows("SELECT m.* FROM sys_menu m " +
				" JOIN sys_role_menu rm ON m.menuid = rm.menuid " +
				" JOIN sys_user_role ur ON ur.roleid = rm.roleid " +
				" WHERE ur.userid = $S{userid}", userid);
	}

	public List<SqlRow> findUserRoles(String userid) throws Exception {
		return super.findRows("SELECT roleid FROM sys_user_role WHERE userid = $S{userid}", DataSourceProperty.PUB, userid);
	}
	
	public List<SqlRow>  getUncheckUser()throws Exception {
		return super.findRows("SELECT paravalue from  sys_param where paraid ='80000080010' ");
	}
	
	public void resetPwd(String userName,String password) throws Exception{
		String currentDate =  Tools.getCurrentDate();
		super.update("update sys_user set passwd = '"+password+"',pwdsetdate='"+currentDate+"', is_initial_pwd='1' where loginname = '"+userName+"'");	
		
	}
	
	public void addLog(Map<String, Object> addParams) throws Exception {
		update("INSERT INTO sys_operation_log ( userid, server, server_desc, method, method_desc, submit_old_data, submit_data, operation_date, operation_time, result, error_msg) "
				+ " VALUES ( $S{userid}, $S{server}, $S{server_desc}, $S{method}, $S{method_desc}, $S{submit_old_data}, $S{submit_data}, $S{operation_date}, $S{operation_time}, $S{result}, $S{error_msg})",
				addParams);
	}

}
