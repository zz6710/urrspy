package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetManagePlanInfoService",table = "dwd_ast_mng_plan_val_inf")
public class AssetManagePlanInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "估值日期")
   private String valDt;
   @GraphQLField(kkhtml = "KFieldText", label = "单位估值", sql = "unt_val = $S{untVal}" ,field = "unt_val")
   private String untVal;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;

    //证券名称
    @GraphQLField(label = "证券名称")
    private String scrNm;
    //证券简称
    @GraphQLField(label = "证券简称")
    private String scrShtNm;
    @GraphQLField(kkhtml = "KFieldText", label = "开始日期")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "结束日期")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
    private String dealDate;


    public String getScrId() {
        return scrId;
    }

    public void setScrId(String scrId) {
        this.scrId = scrId;
    }
  	public String getScrCd() {
        return scrCd;
    }

    public void setScrCd(String scrCd) {
        this.scrCd = scrCd;
    }
  	public String getValDt() {
        return valDt;
    }

    public void setValDt(String valDt) {
        this.valDt = valDt;
    }
  	public String getUntVal() {
        return untVal;
    }

    public void setUntVal(String untVal) {
        this.untVal = untVal;
    }
  	public String getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(String crtDt) {
        this.crtDt = crtDt;
    }
  	public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

}