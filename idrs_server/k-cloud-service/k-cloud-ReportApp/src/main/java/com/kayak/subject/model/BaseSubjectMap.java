package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseSubjectMapService",table = "base_subject_map")
@Data
public class BaseSubjectMap {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "报表名称")
   @GraphQLField(kkhtml = "KFieldText", label = "报表名称", sql = "report_name = $S{reportName}" ,field = "report_name")
   private String reportName;
   @ExcelProperty(value = "科目代码")
   @GraphQLField(kkhtml = "KFieldText", label = "科目代码", sql = "account_code = $S{accountCode}" ,field = "account_code")
   private String accountCode;
   @ExcelProperty(value = "资产三类编码")
   @GraphQLField(kkhtml = "KFieldText", label = "资产III类代码", sql = "asst_3_knd = $S{asst3Knd}" ,field = "asst_3_knd")
   private String asst3Knd;
   @ExcelProperty(value = "报表细类")
   @GraphQLField(kkhtml = "KFieldText", label = "报表细类", sql = "ctg_cd = $S{ctgCd}" ,field = "ctg_cd")
   private String ctgCd;
   @ExcelProperty(value = "资产代码")
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "asst_cd = $S{asstCd}" ,field = "asst_cd")
   private String asstCd;
   @ExcelProperty(value = "备注")
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;

   @ExcelIgnore
   @GraphQLField
   private String accountName;
   //资产三类名称
   @ExcelProperty(value = "资产三类名称")
   @GraphQLField
   private String asst3KndName;
   @ExcelIgnore
   @GraphQLField
   private String ctgCd1;

}