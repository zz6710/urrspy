package com.kayak.base.dao;

import com.kayak.base.dao.sql.SqlConfig;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysBeans;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import java.util.Map;

public abstract class XmlSqlSupportDao extends BaseDao {

    private XmlSqlOperate xmlSqlDao;

    public <T> SqlResult<T> findRows(SqlParam<T> param, String action) throws Exception {
        if(xmlSqlDao == null){
            xmlSqlDao = SysBeans.getBean("xmlSqlDao");
        }
        return xmlSqlDao.query(param, action);
    }

    public <T> SqlResult<T> findRows(SqlParam<T> param, SqlConfig sqlConfig) throws Exception {
        if(xmlSqlDao == null){
            xmlSqlDao = SysBeans.getBean("xmlSqlDao");
        }
        return xmlSqlDao.query(param, sqlConfig);
    }

    public SqlResult<Map> findRows(Map<String, Object> param, SqlConfig sqlConfig) throws Exception {
        if(xmlSqlDao == null){
            xmlSqlDao = SysBeans.getBean("xmlSqlDao");
        }
        return xmlSqlDao.query(param, sqlConfig);
    }

    public <T> UpdateResult update(SqlParam<T> param, String action) throws Exception {
        if (xmlSqlDao == null) {
            xmlSqlDao = SysBeans.getBean("xmlSqlDao");
        }
        return getUpdateResult(xmlSqlDao.update(param, action));
    }

    public UpdateResult update(Map<String, Object> param, SqlConfig sqlConfig) throws Exception {
        if (xmlSqlDao == null) {
            xmlSqlDao = SysBeans.getBean("xmlSqlDao");
        }
        return getUpdateResult(xmlSqlDao.update(param, sqlConfig));
    }

    public <T> UpdateResult update(SqlParam<T> param, SqlConfig sqlConfig) throws Exception {
        if (xmlSqlDao == null) {
            xmlSqlDao = SysBeans.getBean("xmlSqlDao");
        }
        return getUpdateResult(xmlSqlDao.update(param, sqlConfig));
    }

    private UpdateResult getUpdateResult(String result) {
        UpdateResult updateResult = new UpdateResult();
        JSONObject returnData = new JSONObject(result);
        if (!ObjectUtils.isEmpty(returnData.get("returndata"))) {
            JSONObject resultData = returnData.getJSONObject("returndata");
            if (resultData.has("autoId")) {
                updateResult.setAutoId(resultData.getString("autoId"));
            }
            if (resultData.has("effect")) {
                updateResult.setEffect(resultData.getInt("effect"));
            }
        }
        return updateResult;
    }
}
