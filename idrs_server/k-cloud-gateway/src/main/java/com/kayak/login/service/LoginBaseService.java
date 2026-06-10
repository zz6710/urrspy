package com.kayak.login.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.login.dao.LoginDao;

public class LoginBaseService implements LoginService {

	@Autowired
	private LoginDao loginDao;


	@Override
	public SqlRow findUser(String loginUser) throws Exception {
		return loginDao.findUser(loginUser);
	}

	@Override
	public void resetUserLock(int pwderrtimes, String loginUser) throws Exception {
		loginDao.resetUserLock(pwderrtimes, loginUser);
	}

	@Override
	public boolean checkPassword(SqlRow user, String password) throws Exception {
		return user.getString("passwd").equals(password);
	}

	@Override
	public void updateUserLock(String loginUser, int pwderrtimes, String pwderrlockdt) throws Exception {
		loginDao.updateUserLock(loginUser, pwderrtimes, Tools.getStringFromDate("yyyyMMdd HHmmss", new Date()));
	}

	@Override
	public void updateUserLockTime(String loginUser) throws Exception {
		loginDao.updateUserLockTime(loginUser);
	}


	@Override
	public List<String> getUncheckUser() throws Exception {
		List<String> users = null;
		List<SqlRow> sqlRows = loginDao.getUncheckUser();
		if(!CollectionUtils.isEmpty(sqlRows)) {
			users = Arrays.asList(sqlRows.get(0).get("paravalue").toString().split(",")) ;
		}
		return users;
	}

	@Override
	public void resetPwd(String loginname, String password) throws Exception {
		
		loginDao.resetPwd(loginname, password);
	}
	
	@Override
	public void addLog(Map param) throws Exception {
		loginDao.addLog(param);
		
	}

}
