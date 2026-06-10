package com.kayak.workflow.model;

import lombok.Data;

/**
 * @author libo
 */
@Data
public class WfBusinessConfig {

   /**
    * 对应sys_server_method表的server字段
    */
   private String server;

   /**
    * 工作流流程名称
    */
   private String processName;

   /**
    * 业务的数据库主键名称集合，多个主键用逗号分割
    */
   private String busKeys;

   /**
    * 业务的数据库表名
    */
   private String tableName;

   /**
    * 是否在移动端展示
    */
   private String appDisplay;
   
   //状态
   private String status;
}
