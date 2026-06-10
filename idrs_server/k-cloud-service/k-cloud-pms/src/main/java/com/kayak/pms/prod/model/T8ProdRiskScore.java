package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodRiskScoreService",table = "t8_prod_risk_score")
public class T8ProdRiskScore {
  @GraphQLField(field = "id")
  private String id;

  @GraphQLField(field = "t8_prod_info_id")
  private String t8ProdInfoId;

  @GraphQLField(field = "prodCode",label="产品代码",kkhtmlDefault = true)
  private String prodCode;
  
  @GraphQLField(field = "risk_score",label="产品风险评分",kkhtmlDefault = true)
  private String riskScore;

  @GraphQLField(field = "prod_risk_level",label="产品风险评级",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"risklevel\"}")
  private String prodRiskLevel;

  @GraphQLField(field = "prod_risk_level",label="产品风险星级",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"t8_risk_score_status\"}")
  private String riskLevel;

  @GraphQLField(field = "is_confirm",label="是否确认",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"t8_is_confirm\"}")
  private String isConfirm;

  @GraphQLField(field = "risk_score_status",label="风险评分状态",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"t8_risk_score_status\"}")
  private String riskScoreStatus;

  @GraphQLField(field = "inputuser")
  private String inputuser;

  @GraphQLField(field = "updateuser")
  private String updateuser;

  @GraphQLField(field = "crt_date")
  private String crtDate;

  @GraphQLField(field = "crt_time")
  private String crtTime;

  @GraphQLField(field = "upd_date")
  private String updDate;

  @GraphQLField(field = "upd_time")
  private String updTime;

  @GraphQLField(field = "process_instance_id")
  private String processInstanceId;

  @GraphQLField(field = "approval_status")
  private String approvalStatus;


  //用于附件删除
  @GraphQLField(field = "parentId")
  private String parentId;
  @GraphQLField(field = "prod_document_id")
  private String prodDocumentId;
  @GraphQLField(field = "path")
  private String path;
  @GraphQLField(field = "file_name")
  private String fileName;
  @GraphQLField(field = "attachment_type")
  private String attachmentType;
  @GraphQLField(field = "distributor_code")
  private String distributorCode;
  @GraphQLField(field = "t8_trutee_info_id")
  private String t8TruteeInfoId;
  @GraphQLField
  private String documentType;
  @GraphQLField
  private String prodName;
  @GraphQLField
  private String isRecycleCode;
}
