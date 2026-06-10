package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.system.model.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinwanxiong
 * @date 2020/4/9 18:08
 * @description
 */

@Repository
public class RoleDao extends ComnDao {

    public Role findRoleById(String roleid) throws Exception {
        // 左关联本表是为了带出父角色信息
        return super.findRow(Role.class,"SELECT r.*,p.rolename AS parent_role_name FROM sys_role r " +
                " LEFT JOIN sys_role p ON r.parentroleid = p.roleid WHERE r.roleid = '" + roleid + "'",0,null);
    }

    public SqlResult<Role> findRoleList(SqlParam<Role> params) throws Exception {
    	params.setMakeSql(true);
        return super.findRows("select * from sys_role where roleid not in (select roleid  from sys_user_role sur where userid ='admin')",0, params);
    }

    public List<Role> findAll() throws Exception {
        // 左关联本表是为了带出父角色信息
        return super.findRows(Role.class,"SELECT r.*,p.rolename AS parent_role_name FROM sys_role r " +
                " LEFT JOIN sys_role p ON r.parentroleid = p.roleid ",0,null);
    }

    public Role findRoleByRoleId(String roleid) throws Exception {

        return super.findRow(Role.class,"SELECT r.* FROM sys_role r WHERE r.roleid = '" + roleid + "'",0,null);
    }

    public List<Role> findRoleByParentId(String parentId) throws Exception {
        // 左关联本表是为了带出父角色信息
        return super.findRows(Role.class,"SELECT r.*,p.rolename AS parent_role_name FROM sys_role r " +
                " LEFT JOIN sys_role p ON r.parentroleid = p.roleid WHERE r.parentroleid = '" + parentId + "'",0,null);
    }

    public Role findRoleByRoleName(String roleName) throws Exception {
        return super.findRow(Role.class,"SELECT r.*,p.rolename AS parent_role_name FROM sys_role r " +
                " LEFT JOIN sys_role p ON r.parentroleid = p.roleid WHERE r.rolename = '" + roleName + "'",0,null);
    }

    public int findCountRole(SqlParam<Role> params) throws Exception {
        return super.findRow("select count(*) as count FROM sys_role WHERE rolename = $S{rolename} and roleid != $S{roleid}",params.getModel()).getInteger("count");
    }

    public int deleteRole(String roleId) throws Exception {
        return super.update("DELETE FROM sys_role WHERE roleid = '" + roleId + "'").getEffect();
    }

    public int addRole(SqlParam<Role> params) throws Exception {
        return super.update("INSERT INTO sys_role(roleid,parentroleid,rolename,descript) VALUES($AUTOIDS{roleid},$S{parentroleid},$S{rolename},$S{descript})",params.getModel()).getEffect();
    }

    public int updateRole(SqlParam<Role> params) throws Exception {
        return super.update("UPDATE sys_role SET parentroleid=$S{parentroleid},rolename=$S{rolename},descript=$S{descript} WHERE roleid=$S{roleid}",params.getModel()).getEffect();
    }

    public int updateRoleDoc(SqlParam<Role> params) throws Exception {
        String sql = "UPDATE sys_role SET doc_types=$S{docTypes},doc_re_types = $S{docReTypes} WHERE roleid=$S{roleid}";
        return super.update(sql,params.getModel()).getEffect();
    }
    public List<SqlRow> findRoles() throws Exception {
        // 左关联本表是为了带出父角色信息
        return super.findRows("SELECT r.roleid value,r.parentroleid,r.rolename label,r.descript,p.rolename AS parent_role_name FROM sys_role r " +
                " LEFT JOIN sys_role p ON r.parentroleid = p.roleid ",0);
    }

    public List<SqlRow> findAllUser(SqlParam<Role> params) throws Exception {
        String sql = "SELECT DISTINCT sur.userid value,su.username label FROM sys_user_role sur \n" +
                "LEFT JOIN sys_user su ON sur.userid = su.userid where 1=1";
        if (StringUtils.isNotBlank(params.getModel().getRoleid())) {
            sql += " and sur.roleid = '" + params.getModel().getRoleid() + "'";
        }
        return super.findRows(sql,0);
    }

    public List<SqlRow> findRoleUser(String roleId) throws Exception {
        // 左关联本表是为了带出父角色信息
        return super.findRows("SELECT r.roleid value,r.parentroleid,r.rolename label,r.descript,p.rolename AS parent_role_name FROM sys_role r " +
                " LEFT JOIN sys_role p ON r.parentroleid = p.roleid ",0);
    }

    public SqlResult<Role> findParents(SqlParam<Role> params)throws Exception {
        params.setLimit(30);
        String sql = "SELECT r.roleid,r.rolename from sys_role r LEFT JOIN sys_role t on r.parentroleid =t.roleid ";
        return super.findRows(sql, 0, params);
    }
	
	public SqlResult<SqlRow> findRowsBySelect(SqlParam params) throws Exception {
        return super.findRows("SELECT roleid, rolename FROM sys_role ", params);
    }

    public List<SqlRow> findRowsByByIds(List<String> roleids) throws Exception {
        return super.findRows("SELECT * FROM sys_role where roleid IN "  + roleids.stream().collect(Collectors.joining("','","('", "')")));
    }
}
