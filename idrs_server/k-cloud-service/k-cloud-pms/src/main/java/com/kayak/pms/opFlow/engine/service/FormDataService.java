package com.kayak.pms.opFlow.engine.service;

import com.alibaba.fastjson.JSON;
import com.kayak.helper.JsonHelper;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.constant.ProcessType;
import com.kayak.pms.opFlow.engine.dao.FormDataDao;
import com.kayak.pms.opFlow.engine.dao.ProcessInstanceDao;
import com.kayak.pms.opFlow.engine.entity.Approval;
import com.kayak.pms.opFlow.engine.entity.FormData;
import com.kayak.pms.opFlow.engine.entity.ModifiedData;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 22/08/2017.
 */
@Service("formDataService")
@Transactional
public class FormDataService {
    @Autowired
    FormDataDao formDataDao;

    @Autowired
    ProcessInstanceDao processInstanceDao;

    @Autowired
    BusinessExtendService businessExtendService;

    public FormDataDao getFormDataDao() {
        return this.formDataDao;
    }


    public void saveFormData(String submitParams, String processInstanceId) throws Exception {
        Map<String, Object> map = JsonHelper.fromJson(submitParams, Map.class);
        if (map == null) {
            return;
        }
        for (Map.Entry<String, Object> fieldValues : map.entrySet()) {
            Object fieldValue = fieldValues.getValue();
            if (fieldValues.getValue() instanceof List) {//如果是数组。则进行强制转换
                fieldValue = StringHelper.getStringByArray((List) fieldValues.getValue());
            }
            formDataDao.saveFormData(new FormData(StringHelper.getPrimaryKey(), fieldValues.getKey(), (String) fieldValue, processInstanceId));
        }
    }

    public void saveFormData(Map<String, Object> map, String processInstanceId, String taskId) throws Exception {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, Object> fieldValues : map.entrySet()) {
            // 表单id
            String formId = fieldValues.getKey();
            FormData deleteData = new FormData();
            deleteData.setFormId(formId);
            deleteData.setTaskId(taskId);
            deleteData.setProcessInstanceId(processInstanceId);
            formDataDao.delete(deleteData);
            Object formDataObj = fieldValues.getValue();
            // 每个表单下面的每个参数作为一行记录
            Map<String, Object> formData = new HashMap<>();
            // 列表数据处理成map
            if (formDataObj instanceof List) {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("rows", formDataObj);
                formData = hashMap;
            } else if (formDataObj instanceof Map) {
                formData = (Map<String, Object>) formDataObj;
            }
            for (Map.Entry<String, Object> entry : formData.entrySet()) {
                Object fieldValue = entry.getValue();
                if (fieldValue instanceof List) {//如果是数组。则进行强制转换
                    fieldValue = StringHelper.getStringByArray((List) fieldValue);
                }
                if (fieldValue instanceof Integer) {//如果是整形，则转换为字符串
                    fieldValue = fieldValue.toString();
                }
                FormData formData1 = new FormData(StringHelper.getPrimaryKey(), entry.getKey(), String.valueOf(fieldValue), processInstanceId, formId, taskId);
                formDataDao.saveFormData(formData1);
            }
        }
    }

    public List<FormData> listLatestFormData(String processInstanceId) {
        return formDataDao.listLatestFormData(processInstanceId);
    }

    public void batchUpdateDynamicFormData(Approval approval) {
        try {
            String processInstanceId = approval.getProcessInstanceId();
            List<ModifiedData> modifiedDatas = JSON.parseArray(approval.getModifiedData(), ModifiedData.class);

            List<FormData> latestFormData = listLatestFormData(processInstanceId);
            List<String> fieldNames = new ArrayList<>();
            for (int i = 0; i < latestFormData.size(); i++) {
                fieldNames.add(latestFormData.get(i).getFieldName());
            }

            for (ModifiedData item : modifiedDatas) {
                String fieldName = item.getName();
                if (fieldNames.contains(fieldName)) {//有更新, 没有插入
                    formDataDao.updateDynamicFormData((new FormData(fieldName, item.getAfterValue(), processInstanceId)));//更新真实值
                } else {
                    formDataDao.insertDynamicFormData((new FormData(StringHelper.getPrimaryKey(), fieldName, item.getAfterValue(), processInstanceId)));
                }

            }

            ProcessInstance processInstance = processInstanceDao.getProcessInstanceById(processInstanceId);
            if (ProcessType.PASSIVE.equals(processInstance.getType())) {
                businessExtendService.saveFormDataUpdateLog(approval, modifiedDatas);
            }
        } catch (Exception e) {
            throw new WorkflowException("更新表单数据失败", e);
        }
    }
}
