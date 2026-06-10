package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.basePublish.model
 * user:rennannan
 * date:2021/5/11 14:49
 * function:信披渠道规则实体
 */
@Data
@GraphQLModel(fetcher = "disclosureChannelRuleService", table = "idb_disclosure_channel_rule")
public class DisclosureChannelRule {
    @GraphQLField(label = "id", sql = "id=$S{id}", field = "id", key = true)
    private String id;
    @GraphQLField(label = "渠道规则名称", sql = "channel_rule_name like '%$U{channelRuleName}%'", field = "channel_rule_name")
    private String channelRuleName;
    @GraphQLField(label = "渠道名称", sql = "channel_name like '%$U{channelName}%'", field = "channel_name")
    private String channelName;
    @GraphQLField(label = "渠道规则描述", sql = "channel_rule_desc=$S{channelRuleDesc}", field = "channel_rule_desc")
    private String channelRuleDesc;
    @GraphQLField(label = "信披渠道", sql = "channel_ids=$S{channelIds}", field = "channel_ids")
    private String channelIds;
    @GraphQLField(label = "创建日期", sql = "crt_date=$S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(label = "创建时间", sql = "crt_time=$S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(label = "创建人", sql = "crt_user_id=$S{crtUserId}", field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(label = "创建人名称", sql = "crt_user_name=$S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(label = "更新日期", sql = "upd_date=$S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(label = "更新时间", sql = "upd_time=$S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(label = "更新人", sql = "upd_user_id=$S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(label = "更新人名称", sql = "upd_user_name=$S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(label = "备注", sql = "remark=$S{remark}", field = "remark")
    private String remark;
    @GraphQLField(label = "状态", sql = "status=$S{status}", field = "status")
    private String status;
    @GraphQLField
    private String noticeId;

    @GraphQLField(label = "募集方式", field = "PROD_CLC_MTH")
    private String prodClcMth;

    @GraphQLField(label = "产品系列id", field = "PROD_SER_CD")
    private String prodSerCd;
    @GraphQLField(label = "产品系列名称", field = "PROD_SER_NM")
    private String prodSerNm;
    @GraphQLField(label = "产品形态", field = "PROD_FORM")
    private String prodForm;
    @GraphQLField(label = "投资周期维度", field = "INV_PRD_DIME")
    private String invPrdDime;
    @GraphQLField(label = "投资周期长度", field = "INV_PRD_LEN")
    private String invPrdLen;
    @GraphQLField(label = "产品投资性质", field = "PROD_INV_TYP")
    private String prodInvTyp;
    @GraphQLField(label = "销售对象", field = "PROD_OBJ")
    private String prodObj;
    
    @GraphQLField
    private String prodInfoId;
    @GraphQLField(label = "信披类型", sql = "disclosure_type=$S{disclosureType}", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(label = "信披子类型", sql = "disclosure_son_type=$S{disclosureSonType}", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(label = "文件名称格式", sql = "upload_file_name_type=$S{uploadFileNameType}", field = "upload_file_name_type")
    private String uploadFileNameType;
    @GraphQLField(label = "文件格式", sql = "upload_file_type=$S{uploadFileType}", field = "upload_file_type")
    private String uploadFileType;

    @GraphQLField(label = "确认文件后缀", sql = "suffix_file_name=$S{suffixFileName}", field = "suffix_file_name")
    private String suffixFileName;

}
