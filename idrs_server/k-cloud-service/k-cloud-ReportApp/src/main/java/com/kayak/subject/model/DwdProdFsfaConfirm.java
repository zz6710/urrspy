package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwdProdFsfaConfirmService",table = "dwd_prod_fsfa_confirm")
@Data
public class DwdProdFsfaConfirm {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prdc_cd = $S{prdcCd}" ,field = "prdc_cd")
   private String prdcCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prdc_nm = $S{prdcNm}" ,field = "prdc_nm")
   private String prdcNm;
   @GraphQLField(kkhtml = "KFieldText", label = "估值日期", sql = "dt_dt = $S{dtDt}" ,field = "dt_dt")
   private String dtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "估值表状态", sql = "gzb_stt = $S{gzbStt}" ,field = "gzb_stt")
   private String gzbStt;
   @GraphQLField(kkhtml = "KFieldText", label = "导入时间", sql = "gnrt_tm = $S{gnrtTm}" ,field = "gnrt_tm")
   private String gnrtTm;
   @GraphQLField(kkhtml = "KFieldText", label = "操作员", sql = "opt_naem = $S{optNaem}" ,field = "opt_naem")
   private String optNaem;

}