package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Attachment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 16/06/2017.
 */
@Repository
public class AttachmentDao extends ComnDao {

    public List<Attachment> listAttachmentsByProcessInstanceId(String processInstanceId) {
        return null;
    }

    public void save(Attachment attachment) {}

    public Attachment get(String id) {
        return null;
    }

    public Attachment remove(String id) {
        return null;
    }

    public void updateApprovalIds(Map<String, Object> map) {}

    public List<Attachment> listByIds(List<String> ids) {
        return null;
    }
}
