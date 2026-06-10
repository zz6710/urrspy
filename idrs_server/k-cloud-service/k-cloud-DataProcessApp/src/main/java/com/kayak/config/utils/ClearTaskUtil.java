package com.kayak.config.utils;

import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.model.KbatchGroupInfo;

import java.util.*;

/**
 * 清算任务工具类
 */
public class ClearTaskUtil {

    /**
     * 拼接前置清算组字符串(taskGroup:*|taskGroup:*)为pre_task_id所用
     *
     * @param taClearGroups 清算组集合
     * @return
     * @throws Exception
     */
    public static Map<String, String> getPreTaskGroup(List<KbatchGroupInfo> taClearGroups) throws Exception {
        // 1.查询所有产品型清算组,并以task_group为key,组装 pre_task_group为value
        Map<String, String> preTaskGroupMap = new HashMap<>();
        for (KbatchGroupInfo row : taClearGroups) {
            String preTaskGroup = row.getPreTaskGroup();
            if (null != preTaskGroup && !"".equals(preTaskGroup)) {
                StringBuilder value = new StringBuilder();
                for (String taskgroup : preTaskGroup.split(",")) {
                    if (!"".equals(value.toString())) {
                        value.append(GlobalConstants.GROUP_TASK_REGEX);
                    }
                    value.append(taskgroup);
                    value.append(GlobalConstants.TASK_GROUP_PRE_FLAG);
                }
                preTaskGroupMap.put(row.getTaskGroup(), value.toString());
            }
        }
        return preTaskGroupMap;
    }

    /**
     * 1. 去除前置任务字段pre_task_id中前置清算组的字符,如 taskGroup1:*|taskGroup2:*|#:taskId1|#:taskId2 返回的结果为: #:taskId1|#:taskId2
     * 2. 判断pre_task_id是否改变,如果无改变直接返回null
     *
     * @param preTaskGroup
     * @param preTaskId
     * @return
     */
    public static String minusTaskGroupPreFlag(String preTaskId, String preTaskGroup) {
        //用set装在taskGroup,并去重
        Set<String> countSet = new HashSet<>();
        //如果数据库查出来的preTaskId为null,判断preTaskGroup来决定是否参与修改数据库
        if (preTaskId == null || "".equals(preTaskId)) {
            if (null == preTaskGroup || "".equals(preTaskGroup)) {
                return null;
            } else {
                return preTaskGroup;
            }
        }

        String[] taskIds = preTaskId.split("[" + GlobalConstants.GROUP_TASK_REGEX + "]");
        String   result  = (null == preTaskGroup || "".equals(preTaskGroup)) ? "" : (preTaskGroup + GlobalConstants.GROUP_TASK_REGEX);
        int      count   = 0;
        for (String taskId : taskIds) {
            if (taskId.indexOf(":*") == -1) {
                // #:taskId 和 taskGroup:taskId 情况
                result += taskId + GlobalConstants.GROUP_TASK_REGEX;
            } else {
                countSet.add(taskId);
                if (preTaskGroup.indexOf(taskId) != -1) {
                    count++;
                }
            }
        }
        //如果以"|"结尾,需截掉"|"
        if (result.endsWith("|")) {
            result = result.substring(0, result.length() - 1);
        }

        //判断preTaskId里的taskGroup是否和需要修改的taskGroup一样
        if (null != preTaskGroup && preTaskGroup.split("[" + GlobalConstants.GROUP_TASK_REGEX + "]").length == countSet.size() && count == countSet.size()) {
            return null;
        }
        return result;
    }


}
