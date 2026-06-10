package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.FormDesignDao;
import com.kayak.pms.opFlow.engine.entity.Form;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 26/05/2017.
 */
@Service
@Transactional
public class FormDesignService {
    @Autowired
    FormDesignDao formDesignDao;

    public void saveForm(Form Form) {
        formDesignDao.saveForm(Form);
    }

    public void removeForm(String FormName) {
        formDesignDao.removeForm(FormName);
    }

    public Form getForm(String FormName) {
        return formDesignDao.getForm(FormName);
    }

    public BootstrapTableVO<Form> listForm(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<Form> allForm = formDesignDao.listForm(queryCriteria);
//        PageInfo page = new PageInfo<Form>(allForm);
        return new BootstrapTableVO<>(allForm, allForm.size());
    }

    public void updateForm(Form Form) {
        formDesignDao.updateForm(Form);
    }

    public List<Form> listSelect2() {
        return formDesignDao.listForm(new HashMap<String, Object>());
    }

    public List<String> listProcessInstanceIds(Map<String,Object> params) {
        return formDesignDao.listProcessInstanceIds(params);
    }
}
