package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsPrdSlrFeeDtlService",table = "dws_prd_slr_fee_dtl")
@Data
public class DwsPrdSlrFeeDtl {

   @ExcelProperty(value = "ID")
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prdc_cd = $S{prdcCd}" ,field = "prdc_cd")
   private String prdcCd;
   @ExcelProperty(value = "产品名称")
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prdc_nm = $S{prdcNm}" ,field = "prdc_nm")
   private String prdcNm;
   @ExcelProperty(value = "母产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "母产品代码", sql = "mother_prdc_cd = $S{motherPrdcCd}" ,field = "mother_prdc_cd")
   private String motherPrdcCd;
   @ExcelProperty(value = "费用类型")
   @GraphQLField(kkhtml = "KFieldText", label = "费用类型", sql = "fee_type = $S{feeType}" ,field = "fee_type")
   private String feeType;
   @ExcelProperty(value = "费用(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "费用(元)", sql = "fee_amt = $S{feeAmt}" ,field = "fee_amt")
   private String feeAmt;
   @ExcelProperty(value = "销售商代码")
   @GraphQLField(kkhtml = "KFieldText", label = "销售商代码", sql = "slr_cd = $S{slrCd}" ,field = "slr_cd")
   private String slrCd;
   @ExcelProperty(value = "销售商名称")
   @GraphQLField(kkhtml = "KFieldText", label = "销售商名称", sql = "slr_nm = $S{slrNm}" ,field = "slr_nm")
   private String slrNm;
   @ExcelProperty(value = "创建日期")
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @ExcelProperty(value = "创建时间")
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_tm = $S{crtTm}" ,field = "crt_tm")
   private String crtTm;

}