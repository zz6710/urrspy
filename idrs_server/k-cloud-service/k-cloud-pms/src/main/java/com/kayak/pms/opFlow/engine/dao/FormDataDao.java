package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.entity.FormData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by daniel on 20/06/2017.
 */
@Repository("formDataDao")
public class FormDataDao extends ComnDao {

    public void saveFormData(FormData formdata) throws Exception {
        String sql = "INSERT INTO opf_form_data(id, field_name, field_value, process_instance_id, form_id, task_id)" +
                " VALUES ($S{id}, $S{fieldName}, $S{fieldValue}, $S{processInstanceId}, $S{formId}, $S{taskId})";
        super.update(sql, formdata);
    }

    public void saveByFind(String taskId, String oldTaskId) {
        String sql = "INSERT INTO opf_form_data(id, field_name, field_value, process_instance_id, form_id, task_id)" +
                " SELECT '"+ StringHelper.getPrimaryKey() +"', field_name, field_value, process_instance_id, form_id, '"+taskId+"' FROM opf_form_data WHERE task_id='"+oldTaskId+"'";
    }

    public void delete(FormData formdata) throws Exception {
        String sql = "DELETE FROM opf_form_data where process_instance_id=$S{processInstanceId} AND form_id=$S{formId} AND task_id=$S{taskId}";
        super.update(sql, formdata);
    }

    public List<FormData> listLatestFormData(String processInstanceId) {
        return null;
    }


    public void updateDynamicFormData(FormData formdata) {}

    public void insertDynamicFormData(FormData formData) {}
}
