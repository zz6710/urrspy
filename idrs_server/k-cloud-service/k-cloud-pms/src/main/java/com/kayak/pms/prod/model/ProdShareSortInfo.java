package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodShareSortInfoService",table = "t8_prod_share_sort_info")
public class ProdShareSortInfo {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
   private String t8ProdInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "份额分类id", sql = "t8_prod_share_sort_id = $S{t8ProdShareSortId}" ,field = "t8_prod_share_sort_id")
   private String t8ProdShareSortId;
   @GraphQLField(kkhtml = "KFieldText", label = "发生事件", sql = "occur_event = $S{occurEvent}" ,field = "occur_event")
   private String occurEvent;
   @GraphQLField(kkhtml = "KFieldText", label = "发生日期", sql = "occur_date = $S{occurDate}" ,field = "occur_date")
   private String occurDate;
   @GraphQLField(kkhtml = "KFieldText", label = "认申购份额", sql = "subs_vol = $S{subsVol}" ,field = "subs_vol")
   private String subsVol;
   @GraphQLField(kkhtml = "KFieldText", label = "认申购金额", sql = "subs_amt = $S{subsAmt}" ,field = "subs_amt")
   private String subsAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "赎回份额", sql = "redeem_vol = $S{redeemVol}" ,field = "redeem_vol")
   private String redeemVol;
   @GraphQLField(kkhtml = "KFieldText", label = "赎回金额", sql = "redeem_amt = $S{redeemAmt}" ,field = "redeem_amt")
   private String redeemAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "费用类型", sql = "fee_type = $S{feeType}" ,field = "fee_type")
   private String feeType;
   @GraphQLField(kkhtml = "KFieldText", label = "费用金额", sql = "fee_money = $S{feeMoney}" ,field = "fee_money")
   private String feeMoney;
   @GraphQLField(kkhtml = "KFieldText", label = "代销总额", sql = "proxy_sum_money = $S{proxySumMoney}" ,field = "proxy_sum_money")
   private String proxySumMoney;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "提交人员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "修改人员", sql = "updateuser = $S{updateuser}" ,field = "updateuser")
   private String updateuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upt_date = $S{uptDate}", field = "upt_date")
   private String uptDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upt_time = $S{uptTime}", field = "upt_time")
   private String uptTime;
   @GraphQLField
   private String querStartDate;
   @GraphQLField
   private String querEndDate;
   @GraphQLField(label = "份额名称", field = "real_share_name")
   private String realShareName;
   @GraphQLField(label = "份额代码", field = "share_name")
   private String shareName;
}