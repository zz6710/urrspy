package com.kayak.graphql.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import org.springframework.stereotype.Repository;

@Repository
public class GridFieldDao extends ComnDao {

    public SqlRow get(String userid, String modelName) throws Exception {
        return super.findRow(
                "SELECT userid, model_name, grid_fields FROM sys_grid_field WHERE userid = $S{userid} AND model_name = $S{modelName}",
                Tools.makeParams().put("userid", userid).put("modelName", modelName).build());
    }

    public void update(String userid, String modelName, String gridFields) throws Exception {
        doTrans(() -> {
            super.update("DELETE FROM sys_grid_field WHERE userid = $S{userid} AND model_name = $S{modelName}",
                    Tools.makeParams().put("userid", userid).put("modelName", modelName).build());
            super.update(
                    "INSERT INTO sys_grid_field(userid,model_name,grid_fields) VALUES($S{userid},$S{model_name},$S{gridFields})",
                    Tools.makeParams().put("userid", userid).put("model_name", modelName)
                            .put("gridFields", gridFields).build());
        });
    }

}
