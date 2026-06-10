package com.kayak.rpt.zz.manage.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "zonClcInfoService",table = "app_zon_clc_info")
public class ZonClcInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品募集区域", sql = "zon_clc = $S{zonClc}" ,field = "zon_clc")
   private String zonClc;
   @GraphQLField(kkhtml = "KFieldText", label = "区域募集金额", sql = "zon_clc_amt = $S{zonClcAmt}" ,field = "zon_clc_amt")
   private String zonClcAmt;
   
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getZonClc() {
        return zonClc;
    }

    public void setZonClc(String zonClc) {
        this.zonClc = zonClc;
    }
  	public String getZonClcAmt() {
        return zonClcAmt;
    }

    public void setZonClcAmt(String zonClcAmt) {
        this.zonClcAmt = zonClcAmt;
    }

}