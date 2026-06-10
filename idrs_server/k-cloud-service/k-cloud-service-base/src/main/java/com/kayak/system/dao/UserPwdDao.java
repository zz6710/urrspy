package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.system.model.User;
import com.kayak.system.model.UserInfo;

import org.springframework.stereotype.Repository;

@Repository
public class UserPwdDao extends ComnDao {


	
	public int setPwdsetdate(SqlParam<UserInfo> params) throws Exception {

		return super.update(
				"UPDATE sys_user SET passwd = $S{passwd},pwdsetdate=$S{pwdsetdate} WHERE passwd = $S{oldPwd}",
				params.getModel()).getEffect();
	}
	
	public int updateUserInfo(SqlParam<UserInfo> params) throws Exception {

		return super.update(
				"UPDATE sys_user SET username=$S{username},passwd=$S{passwd},orgno=$S{orgno},deptno=$S{deptno},mobileno=$S{mobileno},jobno=$S{jobno},email=$S{email},sex=$S{sex},pwdsetdate=$S{pwdsetdate} WHERE userid=$S{userid}",
				params.getModel()).getEffect();
	}
	
}
