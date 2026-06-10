package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "dictService", table = "sys_dict")
public class Dict {

    /**
     * 字典标识
     */
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "字典标识", sql = "dict like '%$U{dict}%'", field = "dict")
    private String dict;

    /**
     * 字典名称
     */
    @GraphQLField(kkhtml = "KFieldText", label = "字典名称", sql = "dictname like '%$U{dictname}%'", field = "dictname")
    private String dictname;

    private String groupdict;
    
}
