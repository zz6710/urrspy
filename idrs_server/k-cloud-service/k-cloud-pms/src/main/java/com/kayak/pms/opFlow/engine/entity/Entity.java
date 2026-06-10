package com.kayak.pms.opFlow.engine.entity;

import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 21/05/2017.
 */
@Alias("entity")
public class Entity {

    private String id;
    private String name;
    private String displayName;
    private String json;

}
