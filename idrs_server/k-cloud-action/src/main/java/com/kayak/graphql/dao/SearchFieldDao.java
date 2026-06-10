package com.kayak.graphql.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;

@Repository
public class SearchFieldDao extends ComnDao {

    public SqlRow findSearchField(String userid, String modelName) throws Exception {
        return super.findRow(
                "SELECT userid, model_name, search_fields FROM sys_search_field WHERE userid = $S{userid} AND model_name = $S{modelName}",
                Tools.makeParams().put("userid", userid).put("modelName", modelName).build());
    }

    public List<SqlRow> findCustomSearchField(String modelName) throws Exception {
        return super.findRows(
                "SELECT model_name, model_field, model_field_label, input_html, input_config, is_default FROM sys_server_model_field WHERE model_name = $S{modelName}",
                Tools.makeParams().put("modelName", modelName).build());
    }

    public void updateSearchField(String userid, String modelName, String searchFields) throws Exception {
        doTrans(() -> {
            super.update("DELETE FROM sys_search_field WHERE userid = $S{userid} AND model_name = $S{modelName}",
                    Tools.makeParams().put("userid", userid).put("modelName", modelName).build());
            super.update(
                    "INSERT INTO sys_search_field(userid,model_name,search_fields) VALUES($S{userid},$S{model_name},$S{searchFields})",
                    Tools.makeParams().put("userid", userid).put("model_name", modelName)
                            .put("searchFields", searchFields).build());
        });
    }

}
