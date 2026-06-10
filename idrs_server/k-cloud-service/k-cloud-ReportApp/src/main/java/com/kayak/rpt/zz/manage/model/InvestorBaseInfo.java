package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "investorBaseMaintainService",table = "ods_cust_base_inf")
public class InvestorBaseInfo {

    @GraphQLField(key = true , label = "ID" ,field = "id")
    private String id;

    @GraphQLField(label = "登记银行代码" ,field = "bank_code")
    private String bankCode;

    @GraphQLField(label = "识别标识" ,field = "cust_no")
    private String custNo;

    @GraphQLField(label = "内部识别标识" ,field = "inner_cust_no")
    private String innerCustNo;

    @GraphQLField(label = "原识别标识" ,field = "ori_cust_no")
    private String oriCustNo;

    @GraphQLField(label = "投资者是否属于本行" ,field = "is_belong")
    private String isBelong;

    @GraphQLField(label = "投资者所属银行名称" ,field = "iss_bank_name")
    private String issBankName;

    @GraphQLField(label = "投资者所属银行代码" ,field = "iss_bank_code")
    private String issBankCode;

    @GraphQLField(label = "投资者境内外标识" ,field = "in_out_sign")
    private String inOutSign;

    @GraphQLField(label = "投资者所属国家或地区" ,field = "iss_country")
    private String issCountry;

    @GraphQLField(label = "投资者类别" ,field = "cust_type")
    private String custType;

    @GraphQLField(label = "个人证件类别" ,field = "personal_id_type")
    private String personalIdType;

    @GraphQLField(label = "机构证件类别" ,field = "organization_id_type")
    private String organizationIdType;

    @GraphQLField(label = "其他证件名称" ,field = "other_id_name")
    private String otherIdName;

    @GraphQLField(label = "证件号码" ,field = "id_code")
    private String idCode;

    @GraphQLField(label = "SPV资金托管账户开户行" ,field = "spv_open_bank")
    private String spvOpenBank;

    @GraphQLField(label = "其他资金托管账户开户行" ,field = "other_open_bank")
    private String otherOpenBank;

    @GraphQLField(label = "投资者名称" ,field = "cust_name")
    private String custName;

    @GraphQLField(label = "性别" ,field = "sex")
    private String sex;

    @GraphQLField(label = "风险偏好" ,field = "risk_level")
    private String riskLevel;

    @GraphQLField(label = "手机号码" ,field = "moble")
    private String moble;

    @GraphQLField(label = "固定电话" ,field = "tel_phone")
    private String telPhone;

    @GraphQLField(label = "电子邮箱" ,field = "email")
    private String email;

    @GraphQLField(label = "TA_ID" ,field = "ta_id")
    private String taId;

    @GraphQLField(label = "渠道号" ,field = "channel_code")
    private String channelCode;

    @GraphQLField(label = "合格投资者类别" ,field = "cust_mark")
    private String custMark;

    @GraphQLField(label = "备注" ,field = "remark")
    private String remark;

    @GraphQLField(label = "处理时间" ,field = "deal_date")
    private String dealDate;

    @GraphQLField(label = "创建人" ,field = "crt_user")
    private String crtUser;

    @GraphQLField(label = "修改人" ,field = "upd_user")
    private String updUser;

    @GraphQLField(label = "创建时间" ,field = "crt_dt")
    private String crtDt;

    @GraphQLField(label = "修改时间" ,field = "upd_dt")
    private String updDt;

    @GraphQLField(label = "数据类型" ,field = "data_type")
    private String dataType;

    @GraphQLField(label = "开始时间" ,field = "strt_dt")
    private String strtDt;

    @GraphQLField(label = "结束时间" ,field = "end_dt")
    private String endDt;



    //脱敏数据字段置于末端
    @GraphQLField(kkhtml = "KFieldText", field = "moble_display")
    private String mobleDisplay;
    @GraphQLField(kkhtml = "KFieldText", field = "id_code_display")
    private String idCodeDisplay;
    @GraphQLField(kkhtml = "KFieldText", field = "cust_name_display")
    private String custNameDisplay;
    @GraphQLField(kkhtml = "KFieldText", field = "tel_phone_display")
    private String telPhoneDisplay;
    @GraphQLField(kkhtml = "KFieldText", field = "email_display")
    private String emailDisplay;

}
