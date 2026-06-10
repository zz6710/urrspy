package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.ButtonProcessMappingDao;
import com.kayak.pms.opFlow.engine.entity.ButtonProcessMapping;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 20/06/2017.
 */
@Service
@Transactional
public class ButtonProcessMappingService {
    @Autowired
    ButtonProcessMappingDao buttonProcessMappingDao;

    public void save(ButtonProcessMapping buttonProcessMapping) {
        buttonProcessMappingDao.save(buttonProcessMapping);
    }

    public void update(ButtonProcessMapping buttonProcessMapping) {
        buttonProcessMappingDao.update(buttonProcessMapping);
    }

    public void remove(String id) {
        buttonProcessMappingDao.remove(id);
    }

    public ButtonProcessMapping get(String id) {
        return buttonProcessMappingDao.get(id);
    }

    public BootstrapTableVO<ButtonProcessMapping> list(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<ButtonProcessMapping> allProcessInstance = buttonProcessMappingDao.list(queryCriteria);
//        PageInfo page = new PageInfo<ButtonProcessMapping>(allProcessInstance);
        return new BootstrapTableVO<ButtonProcessMapping>(allProcessInstance, allProcessInstance.size());
    }
}
