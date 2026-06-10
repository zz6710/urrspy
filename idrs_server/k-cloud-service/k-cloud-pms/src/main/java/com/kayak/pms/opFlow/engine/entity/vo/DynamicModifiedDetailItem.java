package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;

/**
 * Created by daniel on 10/08/2017.
 */
@Data
public class DynamicModifiedDetailItem {
    private String beforeModified;
    private String afterModified;
    private String fieldName;

}
