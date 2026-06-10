package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.RoleServer;
import org.springframework.stereotype.Repository;

@Repository
public class RoleServerDao extends ComnDao {

    public SqlResult<RoleServer> find(SqlParam<RoleServer> params) throws Exception {
        return super.findRows("SELECT * FROM sys_role_server", params);
    }
}
