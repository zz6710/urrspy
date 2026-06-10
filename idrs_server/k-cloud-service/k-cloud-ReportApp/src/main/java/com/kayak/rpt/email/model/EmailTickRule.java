package com.kayak.rpt.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "emailTickRuleService",table = "email_tick_rule")
@Data
public class EmailTickRule {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "规则名称", sql = "rule_name = $S{ruleName}" ,field = "rule_name")
   private String ruleName;
   @GraphQLField(kkhtml = "KFieldText", label = "业务数据表", sql = "biz_table = $S{bizTable}" ,field = "biz_table")
   private String bizTable;
   @GraphQLField(kkhtml = "KFieldText", label = "业务数据表名称", sql = "biz_name = $S{bizName}" ,field = "biz_name")
   private String bizName;
   @GraphQLField(kkhtml = "KFieldText", label = "邮件模板id", sql = "template_id = $S{templateId}" ,field = "template_id")
   private String templateId;
   @GraphQLField(kkhtml = "KFieldText", label = "邮件模板名称", sql = "template_name = $S{templateName}" ,field = "template_name")
   private String templateName;
   @GraphQLField(kkhtml = "KFieldText", label = "规则单位 月M/周W/日D", sql = "rule_unit = $S{ruleUnit}" ,field = "rule_unit")
   private String ruleUnit;
   @GraphQLField(kkhtml = "KFieldText", label = "间隔时间 支持月M/周W/日D", sql = "rule_seprate = $S{ruleSeprate}" ,field = "rule_seprate")
   private String ruleSeprate;
   @GraphQLField(kkhtml = "KFieldText", label = "发送频次 支持每月-几号/每周-周几/每日-选择日时，频次不填", sql = "rule_tick = $S{ruleTick}" ,field = "rule_tick")
   private String ruleTick;
   @GraphQLField(kkhtml = "KFieldText", label = "启用状态，0-未启用，1-已启用", sql = "rule_status = $S{ruleStatus}" ,field = "rule_status")
   private String ruleStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;

}