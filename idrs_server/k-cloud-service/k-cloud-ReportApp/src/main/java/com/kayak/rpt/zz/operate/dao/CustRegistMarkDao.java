package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.CustRegistMark;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class CustRegistMarkDao extends ComnDao {

	public SqlResult<CustRegistMark> findCustRegistMarks(SqlParam<CustRegistMark> params) throws Exception {
		String sql = "SELECT id, SUMMIT_USER, CREATE_DATE, CREATE_TIME, BANK_CODE, DATA_TYPE, CUST_NO, ORI_CUST_NO, IS_BELONG, ISS_BANK_NAME, ISS_BANK_CODE, IN_OUT_SIGN, ISS_COUNTRY, CUST_TYPE, PERSONAL_ID_TYPE, ORGANIZATION_ID_TYPE, OTHER_ID_NAME, ID_CODE, SPV_OPEN_BANK, OTHER_OPEN_BANK, CUST_NAME, SEX, RISK_LEVEL, MOBLE, TEL_PHONE, EMAIL, REGISTER_DATE, REGISTER_SERNO, REMARK, IMP_DATE, REGISTER_STATUS, OP_TYPE,report_date,ta_id FROM app_cust_register_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addCustRegistMark(CustRegistMark custRegistMark) throws Exception {
		return super.update("INSERT INTO app_cust_register_remark(summit_user,create_date,create_time,bank_code,data_type,cust_no,ori_cust_no,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,register_date,register_serno,remark,imp_date,register_status,op_type,report_date,ta_id) VALUES($S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{bankCode},$S{dataType},$S{custNo},$S{oriCustNo},$S{isBelong},$S{issBankName},$S{issBankCode},$S{inOutSign},$S{issCountry},$S{custType},$S{personalIdType},$S{organizationIdType},$S{otherIdName},$S{idCode},$S{spvOpenBank},$S{otherOpenBank},$S{custName},$S{sex},$S{riskLevel},$S{moble},$S{telPhone},$S{email},$S{registerDate},$S{registerSerno},$S{remark},$S{impDate},$S{registerStatus},$S{opType},$S{reportDate},$S{taId})",
				DataSourceProperty.PUB,custRegistMark);
	}
	
	public UpdateResult updateCustRegistMark(SqlParam<CustRegistMark> params) throws Exception {
		return super.update("UPDATE app_cust_register_remark SET summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,bank_code=$S{bankCode} ,data_type=$S{dataType} ,cust_no=$S{custNo} ,ori_cust_no=$S{oriCustNo} ,is_belong=$S{isBelong} ,iss_bank_name=$S{issBankName} ,iss_bank_code=$S{issBankCode} ,in_out_sign=$S{inOutSign} ,iss_country=$S{issCountry} ,cust_type=$S{custType} ,personal_id_type=$S{personalIdType} ,organization_id_type=$S{organizationIdType} ,other_id_name=$S{otherIdName} ,id_code=$S{idCode} ,spv_open_bank=$S{spvOpenBank} ,other_open_bank=$S{otherOpenBank} ,cust_name=$S{custName} ,sex=$S{sex} ,risk_level=$S{riskLevel} ,moble=$S{moble} ,tel_phone=$S{telPhone} ,email=$S{email} ,register_date=$S{registerDate} ,register_serno=$S{registerSerno} ,remark=$S{remark} ,imp_date=$S{impDate} ,register_status=$S{registerStatus} ,op_type=$S{opType}  WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteCustRegistMark(SqlParam<CustRegistMark> params) throws Exception {
		return super.update("DELETE FROM app_cust_register_remark WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportCustRegistMark(CustRegistMark custRegistMark) throws Exception {
		return super.update("INSERT INTO app_cust_register_remark(summit_user,create_date,create_time,bank_code,data_type,cust_no,ori_cust_no,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,register_date,register_serno,remark,imp_date,register_status,op_type,report_date,ta_id) VALUES($S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{bankCode},$S{dataType},$S{custNo},$S{oriCustNo},$S{isBelong},$S{issBankName},$S{issBankCode},$S{inOutSign},$S{issCountry},$S{custType},$S{personalIdType},$S{organizationIdType},$S{otherIdName},$S{idCode},$S{spvOpenBank},$S{otherOpenBank},$S{custName},$S{sex},$S{riskLevel},$S{moble},$S{telPhone},$S{email},$S{registerDate},$S{registerSerno},$S{remark},date_format(CURDATE(),'%Y%m%d'),$S{registerStatus},$S{opType}, $S{reportDate},$S{taId})",
				DataSourceProperty.PUB,custRegistMark);
	}
}
