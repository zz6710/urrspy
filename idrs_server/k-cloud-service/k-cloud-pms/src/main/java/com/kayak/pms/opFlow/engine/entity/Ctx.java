package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 27/06/2017.
 */
@Data
@Alias("ctx")
public class Ctx {

    private String id;
    private String buttonId;
    private String name;
    private String displayName;
    private String json;

}
