package com.kayak.pms.opFlow.engine.service;

import com.kayak.helper.JsonHelper;
import com.kayak.helper.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by daniel on 21/09/2017.
 */
@Service
@Transactional
public class DynamicQueryCondition {
    @Autowired
    private FormDesignService formDesignService;

    public void handleDynamicQueryCondition(Map<String, Object> queryCriteria) {
        String dynamicQueryConditions = (String) queryCriteria.get("dynamicQueryConditions");
        if (StringHelper.isEmpty(dynamicQueryConditions)) {
            return;
        }

        Map<String, Object> dynamicQueryConditionMap = JsonHelper.fromJson(dynamicQueryConditions, Map.class);
        if (dynamicQueryConditionMap != null && dynamicQueryConditionMap.size() != 0) {
            /**
             * 思路:(不能一条SQL实现，要实现一个流程实例的数据必须在一行)
             * 每个条件单独查询一次，就可以获得processInstanceId
             * 同时满足条件的processInstanceId即可
             */
            Set<String> retainSet = new HashSet<String>();
            boolean flag = false;
            for (Map.Entry<String, Object> entry : dynamicQueryConditionMap.entrySet()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("fieldName", entry.getKey());
                map.put("fieldValue", entry.getValue());
                List<String> processInstanceIds = formDesignService.listProcessInstanceIds(map);
                if (!flag) {
                    retainSet.addAll(processInstanceIds);
                    flag = true;
                } else {
                    retainSet.retainAll(processInstanceIds);
                }
            }
            List<String> processInstanceIds = (List<String>) queryCriteria.get("processInstanceIds");

            if (processInstanceIds != null) {
                retainSet.retainAll(processInstanceIds);
            }
            queryCriteria.put("processInstanceIds", new ArrayList<String>(retainSet));
        }
    }
}
