package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "documentAttachmentService",table = "t8_prod_document_attachment")
public class DocumentAttachment {

  @GraphQLField(field = "id")
  private String id;
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
  @GraphQLField(field = "inputuser")
  private String inputuser;
  @GraphQLField(field = "crt_date")
  private String crtDate;
  @GraphQLField(field = "crt_time")
  private String crtTime;
  @GraphQLField(field = "updateuser")
  private String updateuser;
  @GraphQLField(field = "upd_date")
  private String updDate;
  @GraphQLField(field = "upd_time")
  private String updTime;
  @GraphQLField(field = "distributor_code")
  private String distributorCode;
  @GraphQLField(field = "t8_trutee_info_id")
  private String t8TruteeInfoId;
  @GraphQLField
  private String documentType;
  @GraphQLField
  private String approvalAdvice;


}
