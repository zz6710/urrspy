package com.kayak.auth.dao;

import com.kayak.auth.dao.model.WfBusiExtend;
import com.kayak.base.dao.ComnDao;
import org.springframework.stereotype.Repository;

@Repository
public class WorkflowDao extends ComnDao {

    public WfBusiExtend findWfBusiExtend(String processInstanceId) throws Exception {
        return super.findRow(WfBusiExtend.class,
                "SELECT * FROM wf_busi_extend WHERE process_instance_id = $S{processInstanceId}",
                0, processInstanceId);
    }
}
