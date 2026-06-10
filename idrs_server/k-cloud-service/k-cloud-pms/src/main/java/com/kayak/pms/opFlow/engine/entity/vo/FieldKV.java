package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;

/**
 * Created by daniel on 10/08/2017.
 */
@Data
public class FieldKV {
    private String fieldName;
    private String fieldValue;

    public FieldKV() {
    }

    public FieldKV(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
