package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JCConfigDao extends ComnDao {

    private ComnDao comnDao = SysBeans.getBean("comnDao");

    /**
     * 查询所有的与它系统交互的参数配置
     * @param config_type
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryPs(String config_type) throws Exception {
        String qdeal = "  select t.id,t.config_describe,t.config_name,"+
                " t.config_code,t.config_type,t.status "+
                " from base_port_config_info t "+
                " where t.config_type = $S{config_type} and t.status ='1'";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("config_type", config_type);
        return comnDao.findRows(qdeal,params);
    }

}
