package com.kayak.system.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserDao extends ComnDao {

	public SqlResult<User> findUserOrgIdByOrgNo(SqlParam<User> params) throws Exception {
		String orgno = params.getModel().getOrgno();
		return super.findRows("SELECT orgid FROM sys_org WHERE orgno = '" + orgno + "'", params);
	}

	public SqlResult<User> findUsers(SqlParam<User> params) throws Exception {
		String sql = "SELECT u.*,o.orgname,d.deptname FROM sys_user u LEFT JOIN sys_org o ON u.orgno = o.orgno left join sys_dept d ON u.deptno = d.deptno where u.userid!='admin' \n";
		if (StringUtils.isNotEmpty(params.getModel().getDeptno())) {
			sql = sql + " and u.deptno LIKE '%"+ params.getModel().getDeptno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getOrgno())) {
			sql = sql + " and u.orgno LIKE '%"+ params.getModel().getOrgno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getJobno())) {
			sql = sql + " and u.jobno LIKE '%"+ params.getModel().getJobno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getUsername())) {
			sql = sql + " and u.username LIKE '%"+ params.getModel().getUsername() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getLoginname())) {
			sql = sql + " and u.loginname LIKE '%"+ params.getModel().getLoginname() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getUserstatus())) {
			sql = sql + " and u.userstatus = $S{userstatus}";
		}
		if (StringUtils.isNotEmpty(params.getModel().getSex())) {
			sql = sql + " and u.sex = $S{sex}";
		}
		if (StringUtils.isNotEmpty(params.getModel().getMobileno())) {
			sql = sql + " and u.mobileno LIKE '%" + params.getModel().getMobileno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getEmail())) {
			sql = sql + " and u.email LIKE '%" + params.getModel().getEmail() + "%' ";
		}
		//角色id
		if (StringUtils.isNotEmpty(params.getModel().getRoleId())) {
			sql += " and exists( select 1 from sys_user_role role where role.userid = u.userid and role.roleid = $S{roleId})";
		}
		return super.findRows(sql, params);
		//return super.findRows("SELECT u.*,o.orgname,d.deptname FROM sys_user u LEFT JOIN sys_org o ON u.orgno = o.orgno left join sys_dept d ON u.deptno = d.deptno", params);
	}

	public SqlResult<User> findUsersWithQY(SqlParam<User> params) throws Exception {
		String sql = "SELECT u.*,o.orgname,d.deptname FROM sys_user u LEFT JOIN sys_org o ON u.orgno = o.orgno left join sys_dept d ON u.deptno = d.deptno where u.userstatus = 'N' \n";
		if (StringUtils.isNotEmpty(params.getModel().getDeptno())) {
			sql = sql + " and u.deptno LIKE '%"+ params.getModel().getDeptno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getOrgno())) {
			sql = sql + " and u.orgno LIKE '%"+ params.getModel().getOrgno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getJobno())) {
			sql = sql + " and u.jobno LIKE '%"+ params.getModel().getJobno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getUsername())) {
			sql = sql + " and u.username LIKE '%"+ params.getModel().getUsername() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getLoginname())) {
			sql = sql + " and u.loginname LIKE '%"+ params.getModel().getLoginname() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getSex())) {
			sql = sql + " and u.sex = $S{sex}";
		}
		if (StringUtils.isNotEmpty(params.getModel().getMobileno())) {
			sql = sql + " and u.mobileno LIKE '%" + params.getModel().getMobileno() + "%' ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getEmail())) {
			sql = sql + " and u.email LIKE '%" + params.getModel().getEmail() + "%' ";
		}
		//角色id
		if (StringUtils.isNotEmpty(params.getModel().getRoleId())) {
			sql += " and exists( select 1 from sys_user_role role where role.userid = u.userid and role.roleid = $S{roleId})";
		}
		return super.findRows(sql, params);
		//return super.findRows("SELECT u.*,o.orgname,d.deptname FROM sys_user u LEFT JOIN sys_org o ON u.orgno = o.orgno left join sys_dept d ON u.deptno = d.deptno", params);
	}

	public SqlResult<User> findUsers2(SqlParam<User> params) throws Exception {
		String sql = "SELECT u.*,o.orgname,d.deptname FROM sys_user u LEFT JOIN sys_org o ON u.orgno = o.orgno left join sys_dept d ON u.deptno = d.deptno where u.userstatus = 'N'";
		if (StringUtils.isNotEmpty(params.getModel().getUserid())) {
			sql = sql + " and userid= '"+ params.getModel().getUserid() + "' ";
		}

		return super.findRows(sql, params);
		//return super.findRows("SELECT u.*,o.orgname,d.deptname FROM sys_user u LEFT JOIN sys_org o ON u.orgno = o.orgno left join sys_dept d ON u.deptno = d.deptno", params);
	}

	public int addUser(SqlParam<User> params) throws Exception {
		return super.update(
				"INSERT INTO sys_user(userid,loginname,passwd,username,orgno,mobileno,jobno,email,deptno,sex,pwdsetdate,idtype,idno)  " +
						"VALUES ($AUTOIDS{userid},$S{loginname},$S{passwd},$S{username},(select orgid from sys_org order by orgid asc limit 1),$S{mobileno},$S{jobno},$S{email},$S{deptno},$S{sex}, $S{pwdsetdate},$S{idtype},$S{idno})",
				params.getModel()).getEffect();
	}

	public int updateUser(SqlParam<User> params) throws Exception {

		return super.update(
				"UPDATE sys_user SET username=$S{username},deptno=$S{deptno},mobileno=$S{mobileno},jobno=$S{jobno},email=$S{email},sex=$S{sex},pwdsetdate=$S{pwdsetdate},idtype=$S{idtype},idno=$S{idno} WHERE userid=$S{userid}",
				params.getModel()).getEffect();
	}

	public int updateUser1(SqlParam<User> params) throws Exception {

		return super.update(
				"UPDATE sys_user SET username=$S{username},deptno=$S{deptno},mobileno=$S{mobileno},email=$S{email},sex=$S{sex},pwdsetdate=$S{pwdsetdate} WHERE userid=$S{userid}",
				params.getModel()).getEffect();
	}


	public int updateCustInfoTel(SqlParam<User> params) throws Exception {

		return super.update(
				"UPDATE ods_amng_cust_info SET mobile=$S{mobileno},email=$S{email} WHERE jobno=$S{jobno}",
				params.getModel()).getEffect();
	}

	public int stopUse(SqlParam<User> params) throws Exception {
		return super.update(
				"UPDATE sys_user SET userstatus='D' WHERE userid=$S{userid}",
				params.getModel()).getEffect();
	}

	public int recoverUse(SqlParam<User> params) throws Exception {
		return super.update(
				"UPDATE sys_user SET userstatus='N' WHERE userid=$S{userid}",
				params.getModel()).getEffect();
	}

	public int resetPwd(SqlParam<User> params) throws Exception {

		return super.update(
				"UPDATE sys_user SET passwd = $S{passwd},pwdsetdate=$S{pwdsetdate} WHERE userid=$S{userid}",
				params.getModel()).getEffect();
	}

	public int setPwdsetdate(SqlParam<User> params) throws Exception {

		return super.update(
				"UPDATE sys_user SET passwd = $S{passwd},pwdsetdate=$S{pwdsetdate} WHERE passwd = $S{oldPwd}",
				params.getModel()).getEffect();
	}


	public SqlResult<User> getUsers(SqlParam<User> params) throws Exception {
		return super.findRows("SELECT u.* FROM sys_user u ", params);
	}

	public SqlResult<User> getOtherUser(SqlParam<User> params) throws Exception {
		return super.findRows("SELECT u.* FROM sys_user u  where u.userid != $S{userid} and u.userstatus ='N'", params);
	}

	public List<SqlRow> getAllUsers(SqlParam<User> params) throws Exception {
		return super.findRows("SELECT u.username label,u.userid value FROM sys_user u  where u.userstatus ='N' ");
	}

	public List<SqlRow> getRoleUser(SqlParam<User> params) throws Exception {
		return super.findRows("select su.username label,su.userid value from sys_user_role sur left join sys_user su on su.userid = sur.userid  where sur.roleid=$S{roleId} and su.userstatus ='N' and su.username is not null",params.getModel());
	}

	public int getUsersCount(SqlParam<User> params) throws Exception {
		return super.findRow("SELECT count(*) as count FROM sys_user where jobno=$S{jobno}", params.getModel()).getInteger("count");
	}

	public SqlResult<User> getRavUsers(SqlParam<User> params) throws Exception {
		String userid = params.getModel().getUserid();
		params.getModel().setUserid("");
		return super.findRows("SELECT userid,concat(userid,username) username FROM sys_user ", params);
		//return super.findRows("SELECT userid,concat(userid,username) FROM sys_user WHERE userid != $S{userid}", params);
	}

	public SqlResult<User> getRevUsers(SqlParam<User> params) throws Exception {
		String userid = params.getModel().getUserid();
		params.getModel().setUserid("");
		return super.findRows("SELECT userid,concat(userid,username) username FROM sys_user WHERE userid != '" + userid + "'", params);
		//return super.findRows("SELECT userid,concat(userid,username) FROM sys_user WHERE userid != $S{userid}", params);
	}

	public SqlResult<User> findUserNameAndId(SqlParam<User> params) throws Exception {
		String userid=(String) SysUtil.getSysUserParamValue("sys_user_userid");
		return super.findRows("SELECT userid,username FROM sys_user where userid != '"+userid+"'",params);
	}

	public SqlResult<User> getUserByRoleId(SqlParam<User> params) throws Exception {
		String sql = "SELECT t.userid, concat(t.userid, t.username) username " +
				"FROM sys_user t " +
				"         left join sys_user_role t1 on t.userid = t1.userid " +
				"where t1.roleid = $S{roleId}";
		if (StringUtils.isNotEmpty(params.getModel().getUserid())){
			sql += "and t.userid != $S{userid}";
		}
		return super.findRows(sql, params);
	}

	//用户名不拼接id
	public SqlResult<User> getUserByRoleId2(SqlParam<User> params) throws Exception {
		String sql = "SELECT t.userid, t.username,t.jobno " +
				"FROM sys_user t " +
				"         left join sys_user_role t1 on t.userid = t1.userid " +
				"where t1.roleid = $S{roleId} and t.userstatus='N' ";
		if (StringUtils.isNotEmpty(params.getModel().getUserid())) {
			sql += "and t.userid != $S{userid}";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<User> getUserByRoleName(SqlParam<User> params) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户id
		String sql = "SELECT t.userid, t.username,t.jobno " +
				"FROM sys_user t " +
				"         left join sys_user_role t1 on t.userid = t1.userid " +
				"where t1.roleid = $S{roleId} AND t.jobno = '"+userid+"'";
		if (StringUtils.isNotEmpty(params.getModel().getUserid())) {
			sql += "and t.userid != $S{userid}";
		}
		return super.findRows(sql, params);
	}


	//查询估值核算岗
	public SqlResult<User> getUserOfGZ(SqlParam<User> params) throws Exception {
		String sql = "SELECT distinct t.jobno,t.userid, t.username " +
				" FROM idb_disclosure_evaluate_emp e left join sys_user t " +
			    " on e.emp_no = t.jobno" ;
		return super.findRows(sql, params);
	}

	public SqlResult<User> findUserByLoginName(SqlParam<User> params) throws Exception {
		String sql="SELECT\n" +
				"\tsys_user.userid,\n" +
				"\tsys_user.loginname,\n" +
				"\tsys_user.username,\n" +
				"\tsys_user.idtype,\n" +
				"\tsys_user.idno,\n" +
				"\tsys_user.email \n" +
				"FROM\n" +
				"\tsys_user \n" +
				"WHERE\n" +
				"\tloginname = $S{loginname}";
		return super.findRows(sql,params);
	}
	
	public boolean checkOldPwd(SqlParam<User> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<User> result = super.findRows("select * from  sys_user where userid = $S{userid} and passwd = $S{oldPwd} ", params);
        return result.getRows().size() > 0 ? false : true;
    }

    public List<SqlRow> findRowsBySelect(SqlParam<User> params) throws Exception {
        String username = params.getModel().getUsername();
        String userid = params.getModel().getUserid();
        String sql = "";
        if (StringUtils.isNotEmpty(username)) {
            sql = "AND u.username LIKE '%$U{username}%'";
        }
        if (StringUtils.isNotEmpty(userid)) {
            sql = "AND u.userid = $S{userid}";
        }
        List<SqlRow> rows = super.findRows("SELECT u.* FROM sys_user u WHERE 1=1 " + sql, params.getModel());
        return rows;
    }

    public List<SqlRow> findRowsByIds(List<String> userids) throws Exception {
        return super.findRows("SELECT * FROM sys_user WHERE userid IN " + userids.stream().collect(Collectors.joining("','", "('", "')")));
    }

    public List<SqlRow> selectNamespaceAuthUser(SqlParam<User> params) throws Exception {
        String sql = "";
        if (!StringUtils.isBlank(params.getModel().getLoginname())) {
            sql = "AND su.loginname LIKE '$U{loginname}%'";
        }
        return super.findRows("SELECT su.userid,su.loginname FROM sys_user su WHERE su.userstatus='N' AND su.userid " +
                        "NOT IN (SELECT ur.userid FROM sys_user_role ur WHERE ur.roleid=0) " + sql + " LIMIT 10",
                params.getModel());
    }

    public List<SqlRow> getUserByRoleIds(Map<String, Object> sqlParam) throws Exception {
        Object roleIdsObject = sqlParam.get("roleIds");
        if (StringUtils.isEmpty((String)roleIdsObject)) {
            return Collections.emptyList();
        }
        JSONArray roleIdJSONArray = JSONUtil.parseArray(roleIdsObject);
        if (CollectionUtil.isEmpty(roleIdJSONArray)) {
            return Collections.emptyList();
        }
        List<String> roleIds = roleIdJSONArray.toList(String.class);
        return super.findRows("select distinct su.userid ,su.username,su.orgno " +
                "from sys_user su " +
                "inner join sys_user_role ur on su.userid =ur.userid " +
                "where ur.roleid in " + roleIds.stream().collect(Collectors.joining("','", "('", "')")));
    }
}

