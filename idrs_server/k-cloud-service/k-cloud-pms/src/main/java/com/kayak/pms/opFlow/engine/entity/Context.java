package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 21/05/2017.
 */
@Data
@Alias("context")
public class Context {

    private String id;
    private String name;
    private String displayName;
    private String mergedJson;
    private String entityIds;

}
