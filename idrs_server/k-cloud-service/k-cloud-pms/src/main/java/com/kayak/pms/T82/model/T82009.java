package com.kayak.pms.T82.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t82009Service", table = "sys_param")
public class T82009 {

    /**
     * 数据键
     */
    @GraphQLField(   label = "数据键", sql = "itemkey = $S{itemkey}", field = "itemkey")
    private String itemkey;

    /**
     * 数据值
     */
    @GraphQLField( label = "数据键", sql = "itemval = $S{itemval}", field = "itemval")
    private String itemval;


    /**
     * 参数ID
     */
    @GraphQLField(  label = "参数ID", sql = "paraid = $S{paraid}", field = "paraid")
    private String paraid;

    /**
     * 参数值
     */
    @GraphQLField( label = "参数值", sql = "paravalue = $S{paravalue}", field = "paravalue")
    private String paravalue;


    @GraphQLField( label = "参数值", sql = "paravalue_text = $S{paravalueText}", field = "paravalueText")
    private String paravalueText;

    /**
     * 参数名称
     */
    @GraphQLField(kkhtml = "KFieldText", label = "参数名称", sql = "a.paraname like '%$U{paraname}%' ", field = "paraname")
    private String paraname;

    /**
     * 转换的数据字典
     */
    @GraphQLField( label = "转换的数据字典", sql = "dict = $S{dict}", field = "dict")
    private String dict;

    /**
     * 获取数据的graphql
     */
    @GraphQLField( label = "获取数据的graphql", sql = "graphql = $S{graphql}", field = "graphql")
    private String graphql;

    /**
     * 字段类型
     */
    @GraphQLField( label = "字段类型", sql = "fieldtype = $S{fieldtype}", field = "fieldtype")
    private String fieldtype;

    /**
     * 是否显示：1-显示  其他-隐藏
     */
    @GraphQLField( label = "是否显示：1-显示  其他-隐藏", sql = "isdisplay = $S{isdisplay}", field = "isdisplay")
    private String isdisplay;


    @GraphQLField( label = "", sql = "moduleid = $S{moduleid}" ,field = "moduleid")
    private String moduleid;


    @GraphQLField( label = "", sql = "groupparaid = $S{groupparaid}" ,field = "groupparaid")
    private String groupparaid;

    @GraphQLField( label = "", sql = "functype = $S{functype}" ,field = "functype")
    private String functype;
    @GraphQLField( label = "", sql = "confoption = $S{confoption}" ,field = "confoption")
    private String confoption;

    @GraphQLField( label = "", sql = "action = $S{action}" ,field = "action")
    private String action;


    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }
    public String getParaid() {
        return paraid;
    }

    public void setParaid(String paraid) {
        this.paraid = paraid;
    }
    public String getParavalue() {
        return paravalue;
    }

    public void setParavalue(String paravalue) {
        this.paravalue = paravalue;
    }
    public String getParaname() {
        return paraname;
    }

    public void setParaname(String paraname) {
        this.paraname = paraname;
    }
    public String getGroupparaid() {
        return groupparaid;
    }

    public void setGroupparaid(String groupparaid) {
        this.groupparaid = groupparaid;
    }
    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }
    public String getFunctype() {
        return functype;
    }

    public void setFunctype(String functype) {
        this.functype = functype;
    }
    public String getConfoption() {
        return confoption;
    }

    public void setConfoption(String confoption) {
        this.confoption = confoption;
    }
    public String getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(String isdisplay) {
        this.isdisplay = isdisplay;
    }

    public String getGraphql() {
        return graphql;
    }

    public void setGraphql(String graphql) {
        this.graphql = graphql;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }


    public String getItemkey() {
        return itemkey;
    }

    public void setItemkey(String itemkey) {
        this.itemkey = itemkey;
    }

    public String getItemval() {
        return itemval;
    }

    public void setItemval(String itemval) {
        this.itemval = itemval;
    }

    public String getParavalueText() {
        return paravalueText;
    }

    public void setParavalueText(String paravalueText) {
        this.paravalueText = paravalueText;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
