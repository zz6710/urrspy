package com.kayak.pms.prod.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * zhangchangsi
 * creat by 2020/1/20
 */
@Data
@GraphQLModel(fetcher = "creativitySeminarService",table = "t8_creativity_seminar")
public class CreativitySeminar {
  @GraphQLField(field = "id")
  private String id;
  @GraphQLField(field = "seminar_name",sql = "seminar_name like '%$U{seminarName}%' ")
  private String seminarName;
  @GraphQLField(field = "seminar_date")
  private String seminarDate;
  @GraphQLField(field = "seminar_time")
  private String seminarTime;
  @GraphQLField(field = "seminar_initiator")
  private String seminarInitiator;
  @GraphQLField(field = "seminar_addr")
  private String seminarAddr;
  @GraphQLField(field = "participant")
  private String participant;
  @GraphQLField(field = "inputuser")
  private String inputuser;
  @GraphQLField(field = "updateuser")
  private String updateuser;
  @GraphQLField(field = "seminar_status")
  private String seminarStatus;
  @GraphQLField(field = "crt_date")
  private String crtDate;
  @GraphQLField(field = "crt_time")
  private String crtTime;
  @GraphQLField(field = "upd_date")
  private String updDate;
  @GraphQLField(field = "upd_time")
  private String updTime;




  //用于删除附件
  @GraphQLField(field = "prod_code")
  private String prodCode;
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
}
