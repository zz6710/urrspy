package com.kayak.model.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.model.model.Model;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @author: beacon
 * @Date: 2020/3/31   3:42 下午
 * @Description
 */
@Repository
public class ModelDao extends ComnDao {

    public SqlRow findMode(SqlParam<Model> params) throws Exception {
        return super.findRow("SELECT * FROM sys_server_model ",params);
    }

}
