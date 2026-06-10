package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.AnnounceRole;
import org.springframework.stereotype.Repository;

@Repository
public class AnnounceRoleDao extends ComnDao {

    public SqlResult<AnnounceRole> find(SqlParam<AnnounceRole> params) throws Exception {
        return super.findRows("SELECT * FROM sys_announce_role WHERE annid = $S{annid}", params);
    }

}
