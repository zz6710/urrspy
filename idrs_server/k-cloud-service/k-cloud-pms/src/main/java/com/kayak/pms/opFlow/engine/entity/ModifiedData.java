package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;

/**
 * Created by daniel on 22/08/2017.
 */
@Data
public class ModifiedData {

    private String name;
    private String beforeValue;
    private String beforeValueText;
    private String afterValue;
    private String afterValueText;
    private String fieldLabel;

}
