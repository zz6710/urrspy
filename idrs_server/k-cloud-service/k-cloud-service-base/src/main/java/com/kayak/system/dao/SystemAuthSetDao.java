package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.Dict;
import com.kayak.system.model.Role;
import com.kayak.system.model.SystemAuthSet;
import org.springframework.stereotype.Repository;

@Repository
public class SystemAuthSetDao extends ComnDao {
    public SqlResult<SystemAuthSet> find(SqlParam<SystemAuthSet> params) throws Exception {
        return super.findRows("SELECT a.*,m.name as servername,r.rolename  FROM sys_auth_set a left join sys_server_method m on a.server=m.server left join sys_role r on a.roleid=r.roleid", params);
    }
    public int add(SqlParam<SystemAuthSet> params) throws Exception {
        return super.update("INSERT INTO sys_auth_set(server,roleid,remark) VALUES($S{server},$S{roleid},$S{remark})",params.getModel()).getEffect();
    }

    public int deleteByKey(SqlParam<SystemAuthSet> params) throws Exception {
        super.update("delete from sys_auth_role_check where roleid=$S{roleid} and server =$S{server}",params.getModel());
        return super.update("delete from sys_auth_set where roleid=$S{roleid} and server =$S{server}",params.getModel()).getEffect();
    }
}
