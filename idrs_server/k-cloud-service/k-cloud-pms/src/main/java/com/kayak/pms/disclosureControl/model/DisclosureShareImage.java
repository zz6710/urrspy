package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @Description :
 * @Author: wangchenglin
 * @Date: 2021/12/16 14:19
 */
@Data
@GraphQLModel(fetcher = "disclosureShareImageService", table = "idb_disclosure_share_image")
public class DisclosureShareImage {

    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "信披公告id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}" ,field = "t8_disclosure_notice_id")
    private String t8DisclosureNoticeId;

    @GraphQLField(kkhtml = "KFieldText", label = "净值区间", sql = "area_section = $S{areaSection}" ,field = "area_section")
    private String areaSection;

    @GraphQLField(kkhtml = "KFieldText", label = "份额名称", sql = "share_name = $S{shareName}" ,field = "share_name")
    private String shareName;

    @GraphQLField(kkhtml = "KFieldText", label = "份额图片路径", sql = "share_image_path = $S{shareImagePath}" ,field = "share_image_path")
    private String shareImagePath;

    @GraphQLField(kkhtml = "KFieldText", label = "创建人id", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
    private String crtUserId;

    @GraphQLField(kkhtml = "KFieldText", label = "创建人姓名", sql = "crt_name = $S{crtName}" ,field = "crt_name")
    private String crtName;

    @GraphQLField(kkhtml = "KFieldText", label = "更新人id", sql = "upd_user_id = $S{updUserId}" ,field = "upd_user_id")
    private String updUserId;

    @GraphQLField(kkhtml = "KFieldText", label = "更新人姓名", sql = "upd_name = $S{updName}" ,field = "upd_name")
    private String updName;

    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;

    @GraphQLField
    private String userId;
    @GraphQLField
    private String t8ProdInfoId;
    @GraphQLField
    private String salesName;
    @GraphQLField
    private String salesCode;
}
