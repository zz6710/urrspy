package com.kayak.pms.opFlow.engine.utils;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.system.SysBeans;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.service.FormDataService;
import com.kayak.pms.opFlow.engine.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BusinessDataUtil {

    @Autowired
    FormDataService formDataService;

    @Autowired
    ProcessService processService;

    public Map<String, Object> getBusinessData(Map<String, Object> submitParams) throws Exception {
        ComnDao comnDao = SysBeans.getBean("comnDao");
        String busiTableName = (String) submitParams.get(ProcessInstanceConstant.BUSI_TABLE_NAME);

        String busiTablePrimaryKey = (String) submitParams.get(ProcessInstanceConstant.BUSI_TABLE_PrimaryKey);
        String[] busiTablePrimaryKeys = busiTablePrimaryKey.split(",");

        StringBuilder sql = new StringBuilder("SELECT * FROM " + busiTableName + " WHERE 1 = 1 ");
        for (String tablePrimaryKey : busiTablePrimaryKeys) {
            sql.append(" AND ");
            sql.append(tablePrimaryKey);
            sql.append(" = '");
            sql.append(submitParams.get(tablePrimaryKey));
            sql.append("' ");
        }

        return comnDao.findRows(sql.toString()).get(0);
    }

    public void updateBusinessData(String processInstanceId, Map<String, Object> submitParams) throws Exception {
        Map<String, Object> formData = new HashMap<>(submitParams);
        ComnDao comnDao = SysBeans.getBean("comnDao");
        String busiTableName = (String) formData.get(ProcessInstanceConstant.BUSI_TABLE_NAME);

        String busiTablePrimaryKey = (String) formData.get(ProcessInstanceConstant.BUSI_TABLE_PrimaryKey);
        String[] busiTablePrimaryKeys = busiTablePrimaryKey.split(",");

        StringBuilder sql = new StringBuilder("UPDATE " + busiTableName + " SET " +
                " process_instance_id=$S{process_instance_id}, process_status=" + ProcessInstanceConstant.RUNNING +
                " WHERE 1 = 1 ");
        for (String tablePrimaryKey : busiTablePrimaryKeys) {
            sql.append(" AND ");
            sql.append(tablePrimaryKey);
            sql.append(" = '");
            sql.append(formData.get(tablePrimaryKey));
            sql.append("' ");
        }

        formData.put("process_instance_id", processInstanceId);
        comnDao.update(sql.toString(), formData);
    }

}
