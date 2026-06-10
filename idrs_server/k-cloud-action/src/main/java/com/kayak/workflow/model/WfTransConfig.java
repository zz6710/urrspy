package com.kayak.workflow.model;

import lombok.Data;

/**
 * 交易配置
 * @author xiamh
 */
@Data
public class WfTransConfig {

   /**
    * 后台交易码
    */
   private String transCode;

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

}
