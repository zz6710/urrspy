package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdScheduleService",table = "t8_prod_schedule")
public class T8ProdSchedule {
  @GraphQLField(field = "id")
  private String id;
  @GraphQLField(field = "prod_code")
  private String prodCode;
  @GraphQLField(field = "sell_month")
  private String sellMonth;
  @GraphQLField(field = "prod_status")
  private String prodStatus;
  @GraphQLField(field = "current_progress")
  private String currentProgress;
  @GraphQLField(field = "prod_position")
  private String prodPosition;
  @GraphQLField(field = "apply_start_date")
  private String applyStartDate;
  @GraphQLField(field = "apply_end_date")
  private String applyEndDate;
  @GraphQLField(field = "establish_open_date")
  private String establishOpenDate;
  @GraphQLField(field = "close_start_date")
  private String closeStartDate;
  @GraphQLField(field = "close_dnd_date")
  private String closeEndDate;
  @GraphQLField(field = "perf_method_explain")
  private String perfMethodExplain;
  @GraphQLField(field = "current_quota")
  private String currentQuota;
  @GraphQLField(field = "current_scale")
  private String currentScale;
  @GraphQLField(field = "invest_manage_name")
  private String investManageName;
  @GraphQLField(field = "prod_manage_name")
  private String prodManageName;
  @GraphQLField(field = "distributor_code")
  private String distributorCode;
  @GraphQLField(field = "issue_date")
  private String issueDate;
  @GraphQLField(field = "process_instance_id")
  private String processInstanceId;
  @GraphQLField(field = "process_status")
  private String processStatus;
  @GraphQLField(field = "crt_date")
  private String crtDate;
  @GraphQLField(field = "crt_time")
  private String crtTime;
  @GraphQLField(field = "crt_user")
  private String crtUser;
  @GraphQLField(field = "upd_date")
  private String updDate;
  @GraphQLField(field = "upd_time")
  private String updTime;
  @GraphQLField(field = "upd_user")
  private String updUser;
  @GraphQLField
  private String prodName;
  @GraphQLField
  private String dataType;
  @GraphQLField
  private String queryStartDate;
  @GraphQLField
  private String queryEndDate;
  @GraphQLField
  private String distributorName;
  @GraphQLField
  private String prodScheduleStatus;
  @GraphQLField(label = "是否代码回收")
  private String isRecycleCode;
  
  @GraphQLField
  private String pgmno;
  @GraphQLField
  private String establishDate;
  @GraphQLField
  private String openStartDate;
  @GraphQLField
  private String openEndDate;
  @GraphQLField
  private String endDate;
  @GraphQLField
  private String liquidate;
  @GraphQLField
  private String cycleOpenTerm; 
  @GraphQLField
  private String cycleOpenType;  
  @GraphQLField
  private String  orderOpenDays;
  @GraphQLField
  private String  postponeRule;
  
  @GraphQLField
  private String  applyEndTime;
  @GraphQLField
  private String  redemStartTime;
  @GraphQLField
  private String  redemEndTime;
  

}
