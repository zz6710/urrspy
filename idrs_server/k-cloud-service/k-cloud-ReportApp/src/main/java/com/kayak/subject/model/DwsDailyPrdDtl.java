package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsDailyPrdDtlService",table = "dws_daily_prd_dtl")
@Data
public class DwsDailyPrdDtl {

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
   @ExcelProperty(value = "认购金额(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "认购金额(元)", sql = "ssp_amt = $S{sspAmt}" ,field = "ssp_amt")
   private String sspAmt;
   @ExcelProperty(value = "申购金额(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "申购金额(元)", sql = "ssb_amt = $S{ssbAmt}" ,field = "ssb_amt")
   private String ssbAmt;
   @ExcelProperty(value = "赎回金额(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "赎回金额(元)", sql = "rdm_amt = $S{rdmAmt}" ,field = "rdm_amt")
   private String rdmAmt;
   @ExcelProperty(value = "到期金额(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "到期金额(元)", sql = "exp_amt = $S{expAmt}" ,field = "exp_amt")
   private String expAmt;
   @ExcelProperty(value = "强减金额(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "强减金额(元)", sql = "sbt_amt_f = $S{sbtAmtF}" ,field = "sbt_amt_f")
   private String sbtAmtF;
   @ExcelProperty(value = "当日客户端收益总额(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "当日客户端收益总额(元)", sql = "inv_yld_amt_dly = $S{invYldAmtDly}" ,field = "inv_yld_amt_dly")
   private String invYldAmtDly;
   @ExcelProperty(value = "赎回收益(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "赎回收益(元)", sql = "rdm_yld_amt = $S{rdmYldAmt}" ,field = "rdm_yld_amt")
   private String rdmYldAmt;
   @ExcelProperty(value = "到期收益(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "到期收益(元)", sql = "exp_yld_amt = $S{expYldAmt}" ,field = "exp_yld_amt")
   private String expYldAmt;
   @ExcelProperty(value = "份额强减收益(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "份额强减收益(元)", sql = "shr_sbt_yld_amt_f = $S{shrSbtYldAmtF}" ,field = "shr_sbt_yld_amt_f")
   private String shrSbtYldAmtF;
   @ExcelProperty(value = "现金分红(元)")
   @GraphQLField(kkhtml = "KFieldText", label = "现金分红(元)", sql = "csh_dvd = $S{cshDvd}" ,field = "csh_dvd")
   private String cshDvd;
   @ExcelProperty(value = "红利转投份额")
   @GraphQLField(kkhtml = "KFieldText", label = "红利转投份额", sql = "shr_rvt_dvd = $S{shrRvtDvd}" ,field = "shr_rvt_dvd")
   private String shrRvtDvd;
   @ExcelProperty(value = "创建日期")
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @ExcelProperty(value = "创建时间")
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_tm = $S{crtTm}" ,field = "crt_tm")
   private String crtTm;


}