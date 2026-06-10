package com.kayak.menu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.system.constants.UserConstants;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import org.springframework.util.CollectionUtils;

@Repository
public class MenuDao extends ComnDao {

	public List<SqlRow> findMenuRoot() throws Exception {
		Map<String, Object> params = new HashMap<>();
		String loginUserId = SysUtil.getLoginUserid();

		params.put("upperid", "ROOT");
		params.put("userid", loginUserId);

		List<SqlRow> rows = super.findRows("SELECT roleid FROM sys_user_role WHERE userid = $S{userid}", DataSourceProperty.PUB, params);
		boolean superUser = false;
		if (!CollectionUtils.isEmpty(rows)) {
			for (SqlRow row : rows) {
				if (UserConstants.SUPER_ROLE_ID.equals(row.getString("roleid"))) {
					 superUser = true;
					 break;
				}
			}
		}

		if (superUser) {
			return super.findRows(
					"SELECT m.moduleid,m.menuid,menuname,shortname,upperid,url,iconcls,icon,loadorder,status,model,remark" +
							" FROM sys_menu m" +
							" where m.menuid not in('M0110','M0111')" +
							" and m.status = 'N'" +
							" ORDER BY loadorder",
					params);
		} else {
			return super.findRows(
					"SELECT distinct m.menuid,m.moduleid,menuname,shortname,upperid,url,iconcls,icon,loadorder,status,model,remark FROM sys_menu m " +
							" JOIN sys_role_menu rm ON rm.menuid = m.menuid " +
							" JOIN sys_user_role ur ON ur.roleid = rm.roleid " +
							" WHERE ur.userid = $S{userid} AND m.status ='N' AND m.menuid not in('M0110','M0111')" +
							" and m.status = 'N'" +
							" ORDER BY loadorder ",
					params);
		}
	}

	public List<SqlRow> findMenuForDev() throws Exception {
		Map<String, Object> params = new HashMap<>();
		String loginUserId = SysUtil.getLoginUserid();

		params.put("upperid", "ROOT");
			return super.findRows(
					"SELECT m.moduleid, m.menuid, m.menuname, m.shortname, m.model, m.upperid, m.url, m.iconcls, m.loadorder, m.icon, m.`status`, m.remark, m.auth_server FROM sys_menu AS m WHERE m.`status` = 'D' ",
					params);
	}

	public SqlRow findMenuByUrl(String url) throws Exception {
		return super.findRow(
				"SELECT moduleid,menuid,menuname,shortname,upperid,url,iconcls,icon,loadorder,status,model,remark FROM sys_menu WHERE url = $S{url}",
				url);
	}

	public List<SqlRow> findComMenus() throws Exception {
		// 获取用户ID
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		return super.findRows(
				"SELECT t.menuid, t.menuname, t.shortname, t.url, t.icon FROM sys_menu t, sys_user_commenu t2 WHERE t.menuid = t2.menuid AND t2.userid = $S{userid} ORDER BY t.loadorder ",
				userid);
	}

	public void addComMenu(String menuid) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		doTrans(() -> {
			super.update("DELETE FROM  sys_user_commenu WHERE userid = $S{userid} AND menuid = $S{menuid}",
					Tools.makeParams().put("userid", userid).put("menuid", menuid).build());
			super.update("INSERT INTO sys_user_commenu(userid,menuid) VALUES($S{userid},$S{menuid})",
					Tools.makeParams().put("userid", userid).put("menuid", menuid).build());
		});

	}

	public void deleteComMenu(String menuid) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		super.update("DELETE FROM  sys_user_commenu WHERE userid = $S{userid} AND menuid = $S{menuid}",
				Tools.makeParams().put("userid", userid).put("menuid", menuid).build());
	}

}
