package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureRegularAssetService", table = "idb_disclosure_regular_asset")
public class DisclosureRegularAsset {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品定期报告数据表id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}", field = "t8_disclosure_notice_id")
    private String t8DisclosureNoticeId;
    @GraphQLField(kkhtml = "KFieldText", label = "资产种类", sql = "assets_type = $S{assetsType}", field = "assets_type")
    private String assetsType;
    @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "amount = $S{amount}", field = "amount")
    private String amount;
    @GraphQLField(kkhtml = "KFieldText", label = "占产品总资产比例", sql = "asset_ratio = $S{assetRatio}", field = "asset_ratio")
    private String assetRatio;
    @GraphQLField(kkhtml = "KFieldText", label = "序号", sql = "row_numbers = $S{rowNumbers}", field = "row_numbers")
    private String rowNumbers;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField
    private String index;

    @GraphQLField
    private String dataDate;

    @GraphQLField
    private String prodCode;

    @GraphQLField
    private String status;

    @GraphQLField
    private String isCommit;

}