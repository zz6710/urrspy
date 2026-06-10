package com.kayak.pms.opFlow.engine.busi;

import com.alibaba.fastjson.JSON;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.dao.TaskBusinessFormLogDao;
import com.kayak.pms.opFlow.engine.entity.Approval;
import com.kayak.pms.opFlow.engine.entity.BusinessFieldUpdateContent;
import com.kayak.pms.opFlow.engine.entity.TaskPassBusinessFormLog;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务数据修改日志记录
 */
public class SaveTaskUpdateBusinessLogService {

    private final TaskBusinessFormLogDao taskBusinessFormLogDao;

    private final BusinessDataManager businessDataManager;

    public SaveTaskUpdateBusinessLogService() {
        this.businessDataManager = SysBeans.getBean("businessDataManager");
        this.taskBusinessFormLogDao = SysBeans.getBean("taskBusinessFormLogDao");
    }

    public void saveLog(Approval approval, BuildUpdateDataLog buildUpdateData) throws Exception {

        String processInstanceId = approval.getProcessInstanceId();
        Map<String, Object> latestBusinessData = businessDataManager.getLatestData(processInstanceId);
        // 获取此次更新的数据
        List<UpdateDataElement> updateDataElements = buildUpdateData.buildUpdateData();

        //  拼装最新日志
        List<BusinessFieldUpdateContent> businessFieldUpdateContents = buildLatestDataLog(latestBusinessData,
                updateDataElements);

        Date now = new Date();
        taskBusinessFormLogDao.add(TaskPassBusinessFormLog.builder()
                .taskId(approval.getTaskId())
                .taskDisplayName(approval.getTaskDisplayName())
                .optUserName((String) SysUtil.getUserInfo().get(SysUtil.USERNAME))
                .processInstanceId(processInstanceId)
                .createDate(Tools.dt2Date1(now))
                .createTime(Tools.dt2Time1(now))
                .data(JSON.toJSONString(businessFieldUpdateContents))
                .id(StringHelper.getPrimaryKey()).build()
        );
    }

    private List<BusinessFieldUpdateContent> buildLatestDataLog(Map<String, Object> latestBusinessData, List<UpdateDataElement> updateDataElements) {
        List<BusinessFieldUpdateContent> businessFieldUpdateContents = new ArrayList<>(updateDataElements.size());
        for (UpdateDataElement updateDataElement : updateDataElements) {
            businessFieldUpdateContents.add(
                    BusinessFieldUpdateContent.builder()
                            .after(updateDataElement.getValue())
                            .before(latestBusinessData.get(updateDataElement.getName()).toString())
                            .label(updateDataElement.getDisplayName())
                            .name(updateDataElement.getName()).build());

        }
        return businessFieldUpdateContents;
    }


    @Data
    public static class UpdateDataElement {

        public UpdateDataElement(String name, String value, String displayName) {
            this.name = name;
            this.value = value;
            this.displayName = displayName;
        }

        /**
         * 参数名称
         */
        private String name;

        /**
         * 参数值
         */
        private String value;

        /**
         * 显示名称
         */
        private String displayName;
    }
}
