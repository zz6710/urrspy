package com.kayak.pms.getmodel.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * com.kayak.pms.home.dao
 * user:rennannan
 * date:2021/3/9 20:26
 * function:
 */
@Repository
public class ServerMethodDao extends ComnDao {

    /**
     * 功能：根据server 服务菜单查询实体名
     * 作者：zhanghao
     * 日期：20220322
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlRow getModelNameByserver(Map<String, Object> params) throws Exception {
        return super.findRow("select model_name from sys_server_method where server = $S{server}", params);
    }
}
