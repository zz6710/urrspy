package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;

/**
 * Created by daniel on 03/06/2017.
 */
@Data
public class FormConfVO {
    private String dynamicFormId;
    private String formId;
    private String formUrl;
    private String taskName;

    public FormConfVO(String formId, String formUrl) {
        this.formId = formId;
        this.formUrl = formUrl;
    }

    public FormConfVO(String formId, String formUrl, String dynamicFormId) {
        this.formId = formId;
        this.formUrl = formUrl;
        this.dynamicFormId = dynamicFormId;
    }

    public FormConfVO() {
    }
}
