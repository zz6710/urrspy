package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author lw
 */
@Data
@GraphQLModel(fetcher="prodAccountInfoService",table="ods_prod_account_info")
public class ProdAccountInfo {
   	@GraphQLField(label="ID", sql="id=$S{id}",field="id")
	private String id;
	@GraphQLField(label="账号", sql="account_code=$S{accountCode}",field="account_code")
	private String accountCode;
	@GraphQLField(label="账户名称", sql="account_name=$S{accountName}",field="account_name")
	private String accountName;
	@GraphQLField(label="'托管行名称'", sql="trustee_name_sub =$S{trusteeNameSub}' ",field="trustee_name_sub")
	private String trusteeNameSub;
	@GraphQLField(label="'总行名称'", sql="trustee_name=$S{trusteeName}",field="trustee_name")
	private String trusteeName;
   	@GraphQLField(label="账户类型", sql="account_type=$S{accountType}",field="account_type",kkhtmlExt="{\"data-dict\": \"t8_account_type\"}")
	private String accountType;
   	@GraphQLField(label="产品代码", sql="prod_code=$S{prodCode}",field="prod_code")
	private String prodCode;
   	@GraphQLField(label="销售商代码", sql="distributor_code=$S{distributorCode}",field="distributor_code")
	private String distributorCode;
	@GraphQLField(label="销售商名称", sql="seller_name=$S{sellerName}",field="seller_name")
	private String sellerName;
   	@GraphQLField(label="大额行号", sql="bank_acc_num=$S{bankAccNum}",field="bank_acc_num")
	private String bankAccNum;
   	@GraphQLField(label="邮箱", sql="email=$S{email}",field="email")
	private String email;
   	@GraphQLField(label="传真", sql="faxno=$S{faxno}",field="faxno")
	private String faxno;
   	@GraphQLField(label="联系人", sql="call_person=$S{callPerson}",field="call_person")
	private String callPerson;
   	@GraphQLField(label="联系电话", sql="telphone_no=$S{telphoneNo}",field="telphone_no")
	private String telphoneNo;
   	@GraphQLField(label="地址", sql="address=$S{address}",field="address")
	private String address;
   	@GraphQLField(label="开户时间", sql="acc_crt_date=$S{accCrtDate}",field="acc_crt_date")
	private String accCrtDate;
   	@GraphQLField(label="备注", sql="note=$S{note}",field="note}")
	private String note;
	@GraphQLField(label="开户行账户地址", sql="open_bank_addr=$S{openBankAddr}",field="open_bank_addr}")
	private String openBankAddr;
	@GraphQLField(label="账号开户行", sql="account_acnt_bank=$S{accountAcntBank}",field="account_acnt_bank}")
	private String accountAcntBank;
	@GraphQLField(label="开户行账户所在省", sql="account_province=$S{accountProvince}",field="account_province}")
	private String accountProvince;
	@GraphQLField(label="开户行账户所在城市", sql="account_city=$S{accountCity}",field="account_city}")
	private String accountCity;
	@GraphQLField(label="处理日期", sql="deal_date=$S{dealDate}",field="deal_date}")
	private String dealDate;
   	@GraphQLField(label="创建日期", sql="crt_date=$S{crtDate}",field="crt_date")
	private String crtDate;
   	@GraphQLField(label="创建时间", sql="crt_time=$S{crtTime}",field="crt_time")
	private String crtTime;
   	@GraphQLField(label="更新日期", sql="upd_date=$S{updDate}",field="upd_date")
	private String updDate;
   	@GraphQLField(label="更新时间", sql="upd_time=$S{updTime}",field="upd_time")
	private String updTime;
	@GraphQLField
	private String prodCd;
	@GraphQLField
	private String prodNm;
	@GraphQLField(label="更新时间", sql="check_inon=$S{checkInon}",field="check_inon")
	private String checkInon;
}
