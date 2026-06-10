package com.kayak.dps.app.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "baseExMapService", table = "base_ex_map")
public class BaseExMapModel {
    //模块ID
    @GraphQLField(label = "模块ID", sql = "MODULEID = $S{moduleId}", field = "MODULEID")
    private String moduleid;
    //本地字典名
    @GraphQLField(label = "本地字典名", sql = "DICT = $S{dict}", field = "DICT")
    private String dict;
    //本地字典中文名
    @GraphQLField(label = "本地字典中文名", sql = "DICTNAME = $S{dictName}", field = "DICTNAME")
    private String dictname;
    //本地字典值
    @GraphQLField(label = "本地字典值", sql = "SYS_VALUE = $S{sysValue}", field = "SYS_VALUE")
    private String sysValue;
    //外部字典值
    @GraphQLField(label = "外部字典值", sql = "OUT_VALUE = $S{outValue}", field = "OUT_VALUE")
    private String outValue;
    //备注
    @GraphQLField(label = "备注", sql = "REMARK = $S{remark}", field = "REMARK")
    private String remark;
    @GraphQLField(label = "id", sql = "id = $S{id}", field = "id")
    private Integer id;
    @GraphQLField(label = "itemkey", field = "itemkey")
    private String itemkey;
    @GraphQLField(label = "itemval", field = "itemval")
    private String itemval;
}
