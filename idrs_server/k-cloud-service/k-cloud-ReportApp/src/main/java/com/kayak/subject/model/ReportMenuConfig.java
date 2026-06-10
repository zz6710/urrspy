package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@GraphQLModel(fetcher = "reportMenuConfigService",table = "base_report_menu_config")
@Data
public class ReportMenuConfig {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "菜单id", sql = "menu_id = $S{menuId}" ,field = "menu_id")
   private String menuId;
   @GraphQLField(kkhtml = "KFieldText", label = "菜单名称", sql = "menu_name = $S{menuName}" ,field = "menu_name")
   private String menuName;
   @GraphQLField(kkhtml = "KFieldText", label = "按钮名称", sql = "button_name = $S{buttonName}" ,field = "button_name")
   private String buttonName;
   @GraphQLField(kkhtml = "KFieldText", label = "任务id", sql = "task_id = $S{taskId}" ,field = "task_id")
   private String taskId;
   @GraphQLField(kkhtml = "KFieldText", label = "任务名称", sql = "task_name = $S{taskName}" ,field = "task_name")
   private String taskName;
   @GraphQLField(kkhtml = "KFieldText", label = "任务频率 0 每日 1 每周 2 每月 3 每季 4 每年", sql = "task_type = $S{taskType}" ,field = "task_type")
   private String taskType;
   @GraphQLField(kkhtml = "KFieldText", label = "报表类型 0 中间表 1 一维表 2 二维表", sql = "report_type = $S{reportType}" ,field = "report_type")
   private String reportType;
   @GraphQLField(kkhtml = "KFieldText", label = "报表参数值", sql = "report_code = $S{reportCode}" ,field = "report_code")
   private String reportCode;
   @GraphQLField(kkhtml = "KFieldText", label = "报表替换值", sql = "report_value = $S{reportValue}" ,field = "report_value")
   private String reportValue;
   @GraphQLField(kkhtml = "KFieldText", label = "是否启用 0 否 1 是", sql = "is_use = $S{isUse}" ,field = "is_use")
   private String isUse;
   @GraphQLField(kkhtml = "KFieldText", label = "是否前端提示任务名称 0 否 1 是", sql = "is_show = $S{isShow}" ,field = "is_show")
   private String isShow;
   @GraphQLField(kkhtml = "KFieldText", label = "排序值", sql = "sort = $S{sort}" ,field = "sort")
   private String sort;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;

   private String name;
   private List children;

}