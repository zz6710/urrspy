package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 26/05/2017.
 */
@Data
@Alias("form")
public class Form {

    private String id;
    private String name;
    private String displayName;
    private String contextId;
    private String createDate;
    private String createTime;
    private String json;

}
