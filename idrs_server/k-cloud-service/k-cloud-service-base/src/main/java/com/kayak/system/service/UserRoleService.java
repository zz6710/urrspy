package com.kayak.system.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.UserRoleDao;
import com.kayak.system.model.DictItem;
import com.kayak.system.model.UserRole;

import lombok.RequiredArgsConstructor;

/**
 * @author yinwanxiong
 * @date 2020/4/9 16:25
 * @description
 */

@Service
@APIDefine(desc = "用户角色关联服务", model = UserRole.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRoleService {

	private final UserRoleDao userRoleDao;

	@API(desc = "查询用户角色", auth = APIAuth.NO)
	public SqlResult<UserRole> find(SqlParam<UserRole> params) throws Exception {
		UserRole userRole = params.getModel();
		List<String> roles = userRoleDao.getRoleIdsByUserId(userRole.getUserid());

		String roleIds = "";
		for (String role : roles) {
			if (Tools.strIsEmpty(roleIds)) {
				roleIds = role;
			} else {
				roleIds += "," + role;
			}
		}

		List<UserRole> userRoles = new ArrayList<UserRole>();

		userRole.setRoleid(roleIds);
		userRoles.add(userRole);
		return SqlResult.build(userRoles, 1);

	}

	@API(desc = "角色设置", auth = APIAuth.YES ,operation = APIOperation.UPDATE)
	public String update(SqlParam<UserRole> params) throws Exception {
		UserRole userRole = params.getModel();
		// 更新后的用户角色
		String newRoles = userRole.getRoleids();
		List<String> newRoleList = null;
		if (!Tools.strIsEmpty(newRoles)) {
			newRoleList = Arrays.asList(newRoles.split(","));
		}
		userRoleDao.update(userRole.getUserid(), newRoleList);
		return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}
}
