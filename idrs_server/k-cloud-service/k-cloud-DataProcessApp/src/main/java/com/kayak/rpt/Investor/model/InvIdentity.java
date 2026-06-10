package com.kayak.rpt.Investor.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "rptCmpService", table = "base_rpt_cmp_result")
public class InvIdentity {

	@GraphQLField(key = true, label = "ID", sql = "id = $S{id}", field = "id")
	private String id;

	@GraphQLField(label = "登记机构代码", sql = "a.bank_code = $S{bankCode}", field = "bankCode")
	private String bankCode;

	@GraphQLField(label = "该投资者是否属于本机构", sql = "a.is_belong = $S{isBelong}", field = "isBelong")
	private String isBelong;

	@GraphQLField(label = "投资者所属银行名称",  field = "issBankName")
	private String issBankName;

	@GraphQLField(label = "投资者所属银行代码", field = "issBankCode")
	private String issBankCode;

	@GraphQLField(label = "投资者境内外标识",  field = "inOutSign")
	private String inOutSign;

	@GraphQLField(label = "投资者所属国家或地区", field = "issCountry")
	private String issCountry;

	@GraphQLField(label = "数据类型",  field = "dataType")
	private String dataType;

	@GraphQLField(label = "原识别标识", field = "oriCustNo")
	private String oriCustNo;

	@GraphQLField(label = "识别标识",  field = "custNo")
	private String custNo;

	@GraphQLField(label = "投资者类别", field = "custType")
	private String custType;

	@GraphQLField(label = "个人证件类别",  field = "personalIdType")
	private String personalIdType;

	@GraphQLField(label = "机构证件类别", field = "organizationIdType")
	private String organizationIdType;

	@GraphQLField(label = "其他证件名称",  field = "otherIdName")
	private String otherIdName;

	@GraphQLField(label = "证件号码", field = "id_code")
	private String idCode;

	@GraphQLField(label = "SPV资金托管账户开户行",  field = "spvOpenBank")
	private String spvOpenBank;

	@GraphQLField(label = "其他资金托管账户开户行",  field = "otherOpenBank")
	private String otherOpenBank;

	@GraphQLField(label = "投资者名称",  field = "custName")
	private String custName;

	@GraphQLField(label = "投资者名称原码",  field = "custNameOri")
	private String custNameOri;

	@GraphQLField(label = "性别",  field = "sex")
	private String sex;

	@GraphQLField(label = "风险偏好",  field = "riskLevel")
	private String riskLevel;

	@GraphQLField(label = "手机号码",  field = "moble")
	private String moble;

	@GraphQLField(label = "固定电话",  field = "telPhone")
	private String telPhone;

	@GraphQLField(label = "电子邮箱",  field = "email")
	private String email;

	@GraphQLField(label = "备注",  field = "remark")
	private String remark;

	@GraphQLField(label = "登记流水号",  field = "registerSerno")
	private String registerSerno;

	@GraphQLField(label = "导入日期",  field = "impDate")
	private String impDate;

	@GraphQLField(label = "登记日期",  field = "registerDate")
	private String registerDate;

	@GraphQLField(label = "登记状态",  field = "registerStatus")
	private String registerStatus;

	@GraphQLField(label = "投资者登记账号",  field = "registerAcct")
	private String registerAcct;

	@GraphQLField(label = "投资者客户编号",  field = "registerCustNo")
	private String registerCustNo;

	@GraphQLField(label = "TA_ID",  field = "taId")
	private String taId;

	@GraphQLField(label = "数据日期",  field = "reportDate")
	private String reportDate;

	@GraphQLField(label = "内部识别标识",  field = "inCustNo")
	private String inCustNo;

	@GraphQLField(label = "渠道号",  field = "channelCode")
	private String channelCode;

	@GraphQLField(label = "合格投资者类别",  field = "custMark")
	private String custMark;



}
