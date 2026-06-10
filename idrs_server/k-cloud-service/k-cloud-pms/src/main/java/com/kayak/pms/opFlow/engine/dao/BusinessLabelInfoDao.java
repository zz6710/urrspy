package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.BusinessLabelInfo;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessLabelInfoDao extends ComnDao {

    public int add(BusinessLabelInfo model) throws Exception {
        String sql = "INSERT INTO wf_busi_label_info (id, process_instance_id, data)" +
                " VALUES ($S{id}, $S{processInstanceId}, $S{data})";
        return super.update(sql, model).getEffect();
    }

    public BusinessLabelInfo get(BusinessLabelInfo model) throws Exception {
        String sql = "SELECT * FROM wf_busi_label_info" +
                " WHERE process_instance_id = $S{processInstanceId}";
        return super.findRow(BusinessLabelInfo.class, sql, 0, model);
    }

    public BusinessLabelInfo getParams(BusinessLabelInfo model) throws Exception {
        String sql = "SELECT * FROM wf_submit_params" +
                " WHERE process_instance_id = $S{processInstanceId}";
        return super.findRow(BusinessLabelInfo.class, sql, 0, model);
    }
}
