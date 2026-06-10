package com.kayak.login.service;

import java.util.List;
import java.util.Map;

import com.kayak.core.sql.SqlRow;

public interface LoginService {

	public SqlRow findUser(String loginUser) throws Exception;

	public void resetUserLock(int pwderrtimes, String loginUser) throws Exception;

	public boolean checkPassword(SqlRow user, String password) throws Exception;

	public void updateUserLock(String loginname, int pwderrtimes, String pwderrlockdt) throws Exception;
	
	public void resetPwd(String loginname, String password) throws Exception;
	
	public void updateUserLockTime(String loginname) throws Exception;

	public List<String> getUncheckUser() throws Exception;
	
	public void addLog(Map param) throws Exception ;

}
