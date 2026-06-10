package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 27/06/2017.
 */
@Data
@Alias("fieldMap")
public class FieldMap {

    private Integer id;
    private String buttonId;
    private String name;
    private String displayName;
    private String fieldType;
    private String dict;
    private String exeid;
    private String displayField;
    private String valueField;
    private String url;
    private String validateJson;

}
