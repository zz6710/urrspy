package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.RoleMenu;
import org.springframework.stereotype.Repository;

@Repository
public class RoleMenuDao extends ComnDao {

    public SqlResult<RoleMenu> find(SqlParam<RoleMenu> params) throws Exception {
        return super.findRows("SELECT * FROM sys_role_menu", params);
    }
}
