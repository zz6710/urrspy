package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.system.model.ServerModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class ServerModelDao extends ComnDao {

    public ServerModel get(String modelName) throws Exception {
        return super.findRow(ServerModel.class, "SELECT * FROM sys_server_model WHERE model_name = $S{modelName}", 0, modelName);
    }

    public List<SqlRow> getAppNames() throws Exception {
        return super.findRows("SELECT app_name FROM sys_server_model GROUP BY app_name");
    }

    public List<SqlRow> getModelNames(String appName) throws Exception {
        String sql = "";
        if (!StringUtils.isEmpty(appName)) {
            sql = "WHERE app_name='" + appName + "'";
        }
        return super.findRows("SELECT model_name FROM sys_server_model " + sql);
    }
}
