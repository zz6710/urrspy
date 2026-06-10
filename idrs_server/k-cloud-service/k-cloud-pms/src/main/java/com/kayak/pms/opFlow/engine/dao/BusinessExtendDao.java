package com.kayak.pms.opFlow.engine.dao;

import com.kayak.pms.opFlow.engine.model.WfBusiExtend;
import org.springframework.stereotype.Repository;

/**
 * @author libo
 */
@Repository
public class BusinessExtendDao {

    /**
     * 根据工作流实例id修改业务拓展表流程状态
     * @param wfBusiExtend
     * @return
     */
    public int updateByProcessInstanceId(WfBusiExtend wfBusiExtend) {
        return 0;
    }

    /**
     * 删除拓展表数据
     * @param processInstanceId
     * @return
     */
    public int deleteByProcessInstanceId(String processInstanceId) {
        return 0;
    }
}
