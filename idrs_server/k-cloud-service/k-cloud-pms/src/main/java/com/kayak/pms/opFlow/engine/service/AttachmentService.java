package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.AttachmentDao;
import com.kayak.pms.opFlow.engine.entity.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daniel on 16/06/2017.
 */
@Service
@Transactional
public class AttachmentService {
    @Autowired
    AttachmentDao attachmentDao;

    public List<Attachment> listAttachmentsByProcessInstanceId(String taskId) {
        return attachmentDao.listAttachmentsByProcessInstanceId(taskId);
    }

    public void save(Attachment attachment) {
        attachmentDao.save(attachment);
    }

    public Attachment get(String id) {
        return attachmentDao.get(id);
    }

    public void remove(String id) {
        attachmentDao.remove(id);
    }

    public List<Attachment> listByIds(List<String> ids) {
        return attachmentDao.listByIds(ids);
    }
}
