package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 27/06/2017.
 */
@Data
@Alias("envItem")
public class EnvItem {

    private String id;
    private String key;
    private String value;

}
