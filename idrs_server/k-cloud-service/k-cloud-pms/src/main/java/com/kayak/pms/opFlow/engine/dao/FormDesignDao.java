package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Form;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Repository
public class FormDesignDao extends ComnDao {
    public void saveForm(Form Form) {}

    public void removeForm(String id) {}

    public Form getForm(String id) {
        return null;
    }

    public List<Form> listForm(Map<String, Object> queryCriteria) {
        return null;
    }

    public void updateForm(Form Form) {}

    public List<String> listProcessInstanceIds(Map<String, Object> params) {
        return null;
    }

}
