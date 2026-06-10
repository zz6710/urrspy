package com.kayak.system.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "systemParamService", table = "sys_param")
public class SystemParam {

    /* 模块ID */
    @GraphQLField( label = "模块ID", sql = "moduleid LIKE '%$U{moduleid}%'" ,field = "moduleid")
    private String moduleid;

    /*参数ID*/
    @GraphQLField(key = true, label = "参数ID",sql = "paraid = $S{paraid}", field = "paraid")
    private String paraid;

    /*参数值*/
    @GraphQLField(kkhtml = "KFieldText", label = "参数值", kkhtmlDefault = true, sql = "paravalue LIKE '%$U{paravalue}'", field = "paravalue")
    private String paravalue;

    /*参数名称*/
    @GraphQLField(kkhtml = "KFieldText", label = "参数名称", kkhtmlDefault = true, sql = "paraname LIKE '%$U{paraname}%'", field = "paraname")
    private String paraname;

    /*分组ID*/
    @GraphQLField(label = "分组ID", sql = "groupparaid LIKE '%$U{groupparaid}%'", field = "groupparaid")
    private String groupparaid;

    /*转换的数据字典*/
    @GraphQLField( label = "转换的数据字典", sql = "dict = $S{dict}", field = "dict")
    private String dict;

    private String dictValue;

    /* 获取数据的graphql */
    @GraphQLField( label = "获取数据的graphql", sql = "graphql = $S{graphql}", field = "graphql")
    private String graphql;

    @GraphQLField( label = "", sql = "functype = $S{functype}" ,field = "functype")
    private String functype;

    /* 控件配置，即前端组件属性配置 */
    @GraphQLField( label = "", sql = "confoption = $S{confoption}" ,field = "confoption")
    private String confoption;

    @GraphQLField( label = "参数值", sql = "paravalue_text = $S{paravalueText}", field = "paravalueText")
    private String paravalueText;

    /**
     * 数据键
     */
    @GraphQLField(   label = "数据键", sql = "itemkey = $S{itemkey}", field = "itemkey")
    private String itemkey;

    /**
     * 数据值
     */
    @GraphQLField( label = "数据值", sql = "itemval = $S{itemval}", field = "itemval")
    private String itemval;

    /* 是否显示：1-显示  其他-隐藏 */
    @GraphQLField( label = "是否显示：1-显示  其他-隐藏", sql = "isdisplay = $S{isdisplay}", field = "isdisplay")
    private String isdisplay;

}
