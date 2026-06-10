package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsMonthInvRaiseService",table = "dws_month_inv_raise")
@Data
public class DwsMonthInvRaise {

   @ExcelProperty(value = "ID")
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "人行产品代码", sql = "prdc_cd_pbc = $S{prdcCdPbc}" ,field = "prdc_cd_pbc")
   private String prdcCdPbc;
   @ExcelProperty(value = "内部产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "内部产品代码", sql = "prdc_cd = $S{prdcCd}" ,field = "prdc_cd")
   private String prdcCd;
   @ExcelProperty(value = "所属地区")
   @GraphQLField(kkhtml = "KFieldText", label = "所属地区", sql = "zon_cd = $S{zonCd}" ,field = "zon_cd")
   private String zonCd;
   @ExcelProperty(value = "客户类型")
   @GraphQLField(kkhtml = "KFieldText", label = "客户类型", sql = "inv_typ = $S{invTyp}" ,field = "inv_typ")
   private String invTyp;
   @ExcelProperty(value = "客户类型原始码值")
   @GraphQLField(kkhtml = "KFieldText", label = "客户类型原始码值", sql = "orgn_inv_type = $S{orgnInvType}" ,field = "orgn_inv_type")
   private String orgnInvType;
   @ExcelProperty(value = "业务种类")
   @GraphQLField(kkhtml = "KFieldText", label = "业务类型", sql = "busi_type = $S{busiType}" ,field = "busi_type")
   private String busiType;
   @ExcelProperty(value = "金额")
   @GraphQLField(kkhtml = "KFieldText", label = "金额(元)", sql = "hold_amt = $S{holdAmt}" ,field = "hold_amt")
   private String holdAmt;
   @ExcelProperty(value = "份额")
   @GraphQLField(kkhtml = "KFieldText", label = "份额(元)", sql = "hold_vol = $S{holdVol}" ,field = "hold_vol")
   private String holdVol;
   @ExcelProperty(value = "创建日期")
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @ExcelProperty(value = "创建时间")
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_tm = $S{crtTm}" ,field = "crt_tm")
   private String crtTm;

}