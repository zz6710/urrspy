package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.util.Tools;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinwanxiong
 * @date 2020/4/10 13:30
 * @description
 */

@Repository
public class UserRoleDao extends ComnDao {

	public List<String> getRoleIdsByUserId(String userId) throws Exception {
		return super.findRows(String.class, "SELECT roleid FROM sys_user_role WHERE userid = $S{userid}", DataSourceProperty.PUB, userId);
	}

	public void update(String userid, List<String> roleIds) throws Exception {
		doTrans(() -> {
			super.update("DELETE FROM sys_user_role WHERE userid = $S{userid}", userid);
			if (roleIds != null && roleIds.size() > 0) {
				for (String roleid : roleIds) {
					super.update("INSERT INTO sys_user_role (userid,roleid) VALUES ($S{userid},$S{roleid})",
							Tools.makeParams().put("userid", userid).put("roleid", roleid).build());
				}
			}
		});

	}
}
