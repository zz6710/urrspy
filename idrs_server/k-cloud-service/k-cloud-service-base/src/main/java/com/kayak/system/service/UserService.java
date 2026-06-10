package com.kayak.system.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.dao.OrgDao;
import com.kayak.system.dao.UserDao;
import com.kayak.system.dao.UserRoleDao;
import com.kayak.system.model.Org;
import com.kayak.system.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
	@APIDefine(desc = "用户服务", model = User.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService extends ComnDao {

	private final UserDao userDao;

	private final UserRoleDao userRoleDao;

	private final OrgDao orgDao;


	@API(desc = "根据工号查询用户信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<User> findUserByLoginName(SqlParam<User> params) throws Exception {
		return userDao.findUserByLoginName(params);
	}

	@API(desc = "查询用户信息权限",auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<User> findUsers1(SqlParam<User> params) throws Exception {
		return findUsers(params);
	}

	@API(desc = "查询用户信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<User> findUsers(SqlParam<User> params) throws Exception {
		SqlParam<User> sqlParam=new FetcherData<>(params.getParams(),User.class);
		String parentOrgno = params.getModel().getParentOrgno();
		if (Tools.isNotBlank(parentOrgno)) {
			Org org = orgDao.get(parentOrgno);
			if (org != null) {
				params.getModel().setOrgid(org.getOrgid());
			}
		}

		//params.setMakeSql(true);
		SqlResult<User> users = userDao.findUsers(params);
		for (User user : users.getRows()) {
			List<String> roleIds = userRoleDao.getRoleIdsByUserId(user.getUserid());
			String roleIdStr = String.join(",", roleIds);
			user.setRoleids(roleIdStr);
		}

		//设置脱敏
		users.setDesensitized(true);
		return users;
	}

	@API(desc = "查询启用用户信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<User> findUsersWithQY(SqlParam<User> params) throws Exception {
		SqlParam<User> sqlParam=new FetcherData<>(params.getParams(),User.class);
		String parentOrgno = params.getModel().getParentOrgno();
		if (Tools.isNotBlank(parentOrgno)) {
			Org org = orgDao.get(parentOrgno);
			if (org != null) {
				params.getModel().setOrgid(org.getOrgid());
			}
		}

		//params.setMakeSql(true);
		SqlResult<User> users = userDao.findUsersWithQY(params);
		for (User user : users.getRows()) {
			List<String> roleIds = userRoleDao.getRoleIdsByUserId(user.getUserid());
			String roleIdStr = String.join(",", roleIds);
			user.setRoleids(roleIdStr);
		}

		//设置脱敏
		users.setDesensitized(true);
		return users;
	}

	@API(desc = "查询所有用户的名称和ID",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<User> findUserNameAndId(SqlParam<User> params) throws Exception {
		SqlParam<User> sqlParam=new FetcherData<>(params.getParams(),User.class);
		return userDao.findUserNameAndId(params);
	}

	@API(desc = "查询当前登录用户信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<User> findUsersForLogin(SqlParam<User> params) throws Exception {
		SqlParam<User> sqlParam=new FetcherData<>(params.getParams(),User.class);
		params.getModel().setUserid((String) SysUtil.getSysUserParamValue("sys_user_userid"));
		SqlResult<User> users = userDao.findUsers2(params);
		//设置脱敏
		users.setDesensitized(true);
		return users;
	}

	@API(desc = "添加用户", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addUser(SqlParam<User> params) throws Exception {
		Integer count = userDao.getUsersCount(params);
		if(count>0){
			return RequestSupport.updateReturnJson(false, "添加失败,工号已存在!", null).toString();
		}else {

			params.getModel().setPwdsetdate(Tools.getCurrentDate());
			boolean result = userDao.addUser(params) > 0;
			return RequestSupport.updateReturnJson(result, result ? "添加成功" : "添加失败", null).toString();
		}

	}

	@API(desc = "修改用户", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateUser(SqlParam<User> params) throws Exception {
		DaoUtil.doTrans(()->{
			userDao.updateUser(params);
			//userDao.updateCustInfoTel(params);
		});
		
		return RequestSupport.updateReturnJson(true ,"修改成功", null).toString();
	}

	@API(desc = "修改用户", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String updateUserNoAuth(SqlParam<User> params) throws Exception {
		params.getModel().setPwdsetdate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN));
		boolean result = userDao.updateUser1(params) > 0;
		return RequestSupport.updateReturnJson(result, result ? "修改成功" : "修改失败", null).toString();
	}

	@API(desc = "停用用户", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String stopUse(SqlParam<User> params) throws Exception {
		boolean result = userDao.stopUse(params) > 0;
		return RequestSupport.updateReturnJson(result, result ? "停用成功" : "停用失败", null).toString();
	}

	@API(desc = "启用用户", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String recoverUse(SqlParam<User> params) throws Exception {
		boolean result = userDao.recoverUse(params) > 0;
		return RequestSupport.updateReturnJson(result, result ? "启用成功" : "启用失败", null).toString();
	}

	@API(desc = "重置密码", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String resetPwd(SqlParam<User> params) throws Exception {
		params.getModel().setPwdsetdate(Tools.getCurrentDate());
		boolean result = userDao.resetPwd(params) > 0;
		return RequestSupport.updateReturnJson(result, result ? "重置密码成功" : "重置密码失败", null).toString();
	}

	@API(desc = "修改密码", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String resetPwdCheckOldPwd(SqlParam<User> params) throws Exception {
		params.getModel().setPwdsetdate(Tools.getCurrentDate());
		boolean result = userDao.setPwdsetdate(params) > 0;
		return RequestSupport.updateReturnJson(result, result ? "修改密码成功" : "修改密码失败", null).toString();
	}

	@API(desc = "查询user所有数据", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public SqlResult<User> getUser(SqlParam<User> params) throws Exception {
		params.setMakeSql(true);
		return userDao.getUsers(params);
	}


	@API(desc = "查询user所有数据", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public SqlResult<User> getOtherUser(SqlParam<User> params) throws Exception {
		//params.setMakeSql(true);
		String userId = (String)SysUtil.getSysUserParamValue("sys_user_userid");
		params.getModel().setUserid(userId);
		return userDao.getOtherUser(params);
	}

	@API(desc = "查询全部user数据", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public SqlResult<SqlRow> getAllUser(SqlParam<User> params) throws Exception {
		SqlResult<SqlRow> result = new SqlResult<SqlRow>();
		List<SqlRow> users = userDao.getAllUsers(params);
		result.setRows(users);
		return result;
	}

	@API(desc = "查询指定角色用户数据", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public SqlResult<SqlRow> getRoleUser(SqlParam<User> params) throws Exception {
		SqlResult<SqlRow> result = new SqlResult<SqlRow>();
		List<SqlRow> users = userDao.getRoleUser(params);
		result.setRows(users);
		return result;
	}

	@API(desc = "反向查询所有user数据", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public SqlResult<User> getRavUser(SqlParam<User> params) throws Exception {
		return userDao.getRevUsers(params);
	}

	@API(desc = "反向查询所有user数据", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public SqlResult<User> getRevUser(SqlParam<User> params) throws Exception {
		return userDao.getRevUsers(params);
	}

	@API(desc = "根据角色查询用户", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<User> getUserByRoleId(SqlParam<User> params) throws Exception {
		return userDao.getUserByRoleId(params);
	}

	@API(desc = "根据角色查询用户2", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<User> getUserByRoleId2(SqlParam<User> params) throws Exception {
		return userDao.getUserByRoleId2(params);
	}
	@API(desc = "根据角色查询用户2", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<User> getUserByRoleName(SqlParam<User> params) throws Exception {
		return userDao.getUserByRoleName(params);
	}
	
	@API(desc = "查询估值核算岗", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<User> getUserOfGZ(SqlParam<User> params) throws Exception {
		return userDao.getUserOfGZ(params);
	}


}
