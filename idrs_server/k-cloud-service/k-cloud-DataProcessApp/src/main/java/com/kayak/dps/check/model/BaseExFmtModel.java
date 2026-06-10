package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "baseExFmtService",table = "base_ex_fmt")
public class BaseExFmtModel {

    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;
    @GraphQLField(label = "包格式代码" ,field = "exfmtid")
    private String exfmtid;
    @GraphQLField(label = "时点配置" ,field = "itmmem")
    private String itmmem;
    @GraphQLField(label = "长度" ,field = "itmprc")
    private String itmprc;
    @GraphQLField(label = "小数位数" ,field = "itmscl")
    private String itmscl;
    @GraphQLField(label = "对应本系统字段" ,field = "fld")
    private String fld;
    @GraphQLField(label = "是否主键" ,field = "fldpk")
    private String fldpk;
    @GraphQLField(label = "顺序号" ,field = "sn")
    private String sn;
    @GraphQLField(label = "接口数据项类型" ,field = "itmtp")
    private String itmtp;
    @GraphQLField(label = "数据项描述" ,field = "itmdsc")
    private String itmdsc;
    @GraphQLField(label = "数据字典" ,field = "itmdic")
    private String itmdic;
    @GraphQLField(label = "分级数据项父节点" ,field = "itmup")
    private String itmup;
    @GraphQLField(label = "包数据项名称" ,field = "itmnm")
    private String itmnm;
}
