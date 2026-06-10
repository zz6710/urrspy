package com.kayak.rpt.reportMenu.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportMenuManageService",table = "sys_report_condition,sys_menu")
public class ReportMenuModel {
    @GraphQLField(key = true , label = "模块id" ,field = "moduleid")
    private String moduleId;

    @GraphQLField(label = "菜单id" ,field = "menuid")
    private String menuId;

    @GraphQLField(label = "菜单名称" ,field = "menuname")
    private String menuName;

    @GraphQLField(label = "上级菜单id" ,field = "upperid")
    private String upperId;

    @GraphQLField(label = "页面URL" ,field = "url")
    private String url;

    @GraphQLField(label = "菜单图片样式" ,field = "iconcls")
    private String iconCls;

    @GraphQLField(label = "菜单图片文件" ,field = "icon")
    private String icon;

    @GraphQLField(label = "加载顺序" ,field = "loadorder")
    private String loadOrder;

    @GraphQLField(label = "交易状态" ,field = "status")
    private String status;//N--展示,P-隐藏

    @GraphQLField(label = "页面配置ID" ,field = "pageid")
    private String pageId;

    @GraphQLField(label = "快捷调用代码" ,field = "fastcode")
    private String fastCode;

    @GraphQLField(label = "输入类型" ,field = "functype")
    private String funcType;//‘text’-文本输入，‘int’-整数，‘number’-数字，‘select’-下拉单选，‘mselect'-下拉多选，‘date’-日期，‘time'-时间

    @GraphQLField(label = "备注" ,field = "remark")
    private String remark;

    @GraphQLField(label = "菜单类型" ,field = "menutype")
    private String menuType;//0-管理台菜单，1-柜台菜单

    @GraphQLField(label = "报表报送模板路径" ,field = "reporturl")
    private String reportUrl;

    @GraphQLField(label = "控件类型" ,field = "obj_type")
    private String objType;//0-TreeList，1-Report

    @GraphQLField(label = "导入报表时初始化的SQL" ,field = "init_sql")
    private String initSql;

}
