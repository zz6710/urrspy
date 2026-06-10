package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.system.model.SystemAuthOpCheck;
import com.kayak.system.model.SystemAuthRoleCheck;
import org.springframework.stereotype.Repository;

@Repository
public class SystemAuthRoleCheckDao extends ComnDao {

    public SqlResult<SystemAuthRoleCheck> find(SqlParam<SystemAuthRoleCheck> params) throws Exception {
        return super.findRows("SELECT id,roleid,server,field,fieldtype,fieldname,logic,value,opjoin,descript FROM sys_auth_role_check", params);
    }

    public int add(SqlParam<SystemAuthRoleCheck> params) throws Exception {
        return super.update("INSERT INTO sys_auth_role_check(id,roleid,server,field,fieldtype,fieldname,logic,value,opjoin,descript) VALUES($AUTOIDS{id},$S{roleid},$S{server},$S{field},$S{fieldtype},$S{fieldname},$S{logic},$S{value},$S{opjoin},$S{descript})",params.getModel()).getEffect();
    }

    public int deleteById(SqlParam<SystemAuthRoleCheck> params) throws Exception {
        return super.update("delete from sys_auth_role_check where id=$S{id}",params.getModel()).getEffect();
    }
}
