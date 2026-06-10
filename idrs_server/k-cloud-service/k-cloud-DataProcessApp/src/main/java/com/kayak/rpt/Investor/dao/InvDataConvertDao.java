package com.kayak.rpt.Investor.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InvDataConvertDao extends ComnDao{

    /**
     * 获取投资者身份信息List
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getInvIdentityList(Map<String, Object> params) throws Exception {
        List<Map<String, String>> invList = new ArrayList<>();
        List<SqlRow> invResult = super.findRows(ExeQuery.queryExeId("INVRAREEQ01"), DataSourceProperty.PUB, params);
        for(SqlRow sqlRow : invResult) {
            Map<String, String> pMap = new HashMap<>();
            pMap.put("custName", sqlRow.getString("cust_name"));
            pMap.put("custNameOri", sqlRow.getString("cust_name_ori"));
            pMap.put("taId", sqlRow.getString("ta_id"));
            pMap.put("report_date", sqlRow.getString("report_date"));
            invList.add(pMap);
        }
        return invList;
    }

    /**
     * 更新异常投资者生僻字名称
     * @param params
     * @throws Exception
     */
    public void doUpdateInvName (Map<String, String> params) throws Exception {
        super.update(ExeQuery.queryExeId("INVRAREEU02"), params);
        super.update(ExeQuery.queryExeId("INVRAREEU03"), params);
    }

}
