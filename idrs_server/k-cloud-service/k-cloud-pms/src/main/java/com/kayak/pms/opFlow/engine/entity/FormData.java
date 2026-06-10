package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;

/**
 * Created by daniel on 09/06/2017.
 */
@Data
public class FormData {

    private String id;
    private String fieldName;
    private String fieldValue;
    private String processInstanceId;
    private String formId;
    private String taskId;

    public FormData(String id, String fieldName, String fieldValue, String processInstanceId) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.processInstanceId = processInstanceId;
    }

    public FormData(String id, String fieldName, String fieldValue, String processInstanceId, String formId) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.processInstanceId = processInstanceId;
        this.formId = formId;
    }

    public FormData(String id, String fieldName, String fieldValue, String processInstanceId, String formId, String taskId) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.processInstanceId = processInstanceId;
        this.formId = formId;
        this.taskId = taskId;
    }

    public FormData() {
    }

    public FormData(String fieldName, String fieldValue, String processInstanceId) {
        this(null, fieldName, fieldValue, processInstanceId);
    }

}
