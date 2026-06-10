package com.kayak.dps.ods.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.ods.dao.JCConfigDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JCConfigService {

    private JCConfigDao jcConfigDao;

    /**
     * 读取配置参数
     * @param
     * @throws Exception
     */
    public Map<String,Object> getConfigInfo(String type) throws Exception{
        jcConfigDao = new JCConfigDao();
        List<SqlRow> rs = jcConfigDao.queryPs(type);
        Map<String,Object> map = new HashMap<String,Object>();
        for (SqlRow sqlRow : rs) {
            map.put(sqlRow.getString("config_name").toUpperCase(),sqlRow.getString("config_code"));
        }
        return map;
    }
}
