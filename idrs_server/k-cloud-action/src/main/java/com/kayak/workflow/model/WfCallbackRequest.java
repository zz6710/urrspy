package com.kayak.workflow.model;

import java.util.Map;

public class WfCallbackRequest {

    private Map<String, Object> latestSubmitParams;

    private ProcessInstance processInstance;

    public Map<String, Object> getLatestSubmitParams() {
        return latestSubmitParams;
    }

    public void setLatestSubmitParams(Map<String, Object> latestSubmitParams) {
        this.latestSubmitParams = latestSubmitParams;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public static class ProcessInstance {

        /**
         * 主键ID
         */
        private String id;
        /**
         * 流程定义ID
         */
        private String processId;

        /**
         * 流程实例创建者ID
         */
        private String creator;
        /**
         * 流程实例创建时间
         */
        private String createDate;
        private String createTime;

        /**
         * 流程实例完成时间
         */
        private String finishDate;
        private String finishTime;

        /**
         * 流程实例为子流程时，该字段标识父流程哪个节点模型启动的子流程
         */
        private String parentNodeName;

        /**
         * 1--正在运行 0--已结束 2--拒绝
         */
        private String runningStatus;

        /**
         * 表单url, 用来表示jsp在webapp下面的路径,用于和formUrl相结合就能生成完整表单
         */
        private String formUrl;

        //表单结构数据, 用于生成表单,可以和formData,用于和formUrl相结合就能生成完整表单
        private String formId;

        private String controllerClass;

        private String controllerMethod;

        private String submitParams;

        private String processDisplayName;

        private String originalData;

        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }


        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }


        public String getFormUrl() {
            return formUrl;
        }

        public void setFormUrl(String formUrl) {
            this.formUrl = formUrl;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public String getCreator() {
            return creator;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getFinishDate() {
            return finishDate;
        }

        public void setFinishDate(String finishDate) {
            this.finishDate = finishDate;
        }

        public String getParentNodeName() {
            return parentNodeName;
        }

        public void setParentNodeName(String parentNodeName) {
            this.parentNodeName = parentNodeName;
        }

        public String getControllerClass() {
            return controllerClass;
        }

        public void setControllerClass(String controllerClass) {
            this.controllerClass = controllerClass;
        }

        public String getControllerMethod() {
            return controllerMethod;
        }

        public void setControllerMethod(String controllerMethod) {
            this.controllerMethod = controllerMethod;
        }

        public String getSubmitParams() {
            return submitParams;
        }

        public void setSubmitParams(String submitParams) {
            this.submitParams = submitParams;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        public String getRunningStatus() {
            return runningStatus;
        }

        public void setRunningStatus(String runningStatus) {
            this.runningStatus = runningStatus;
        }

        public String getProcessDisplayName() {
            return processDisplayName;
        }

        public void setProcessDisplayName(String processDisplayName) {
            this.processDisplayName = processDisplayName;
        }

        public String getOriginalData() {
            return originalData;
        }

        public void setOriginalData(String originalData) {
            this.originalData = originalData;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
