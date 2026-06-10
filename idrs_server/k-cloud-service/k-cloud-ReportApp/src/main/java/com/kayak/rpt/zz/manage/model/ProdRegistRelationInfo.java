package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodRegistRelationInfoService",table = "app_prod_reg_relation")
public class ProdRegistRelationInfo {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "理财登记编码", sql = "reg_code = $S{regCode}" ,field = "reg_code")
   private String regCode;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "file_name = $S{fileName}" ,field = "file_name")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "日期", sql = "workdate = $S{workdate}" ,field = "workdate")
   private String workdate;
   @GraphQLField(kkhtml = "KFieldText", label = "日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "新增时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
    private String updTime;

}