package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.historyInfo.model.CustRegisterInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class CustRegisterInfohDao extends ComnDao {

	public SqlResult<CustRegisterInfoh> findCustRegisterInfohs(SqlParam<CustRegisterInfoh> params) throws Exception {
		String sql = "SELECT id, bank_code, is_belong, iss_bank_name, iss_bank_code, in_out_sign, iss_country, data_type, ori_cust_no, cust_no, " +
				"            cust_type, personal_id_type, organization_id_type, other_id_name, id_code, spv_open_bank, other_open_bank, cust_name, sex, " +
				"            risk_level, moble, tel_phone, email, remark, register_serno, imp_date, register_date, register_status, register_acct, register_cust_no, " +
				"            create_date, theory_report_start_date, theory_report_end_date, report_date " +
				"       FROM app_cust_register_info_h " +
				"      where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getReportDate())) {
			sql += " and report_date = $S{reportDate} ";
		}
		if (Strings.isNotBlank(params.getModel().getPersonalIdType())) {
			sql += " and personal_id_type in (" + SysUtil.inStr(params.getModel().getPersonalIdType()) + ") ";
		}
		if (Strings.isNotBlank(params.getModel().getOrganizationIdType())) {
			sql += " and organization_id_type in (" + SysUtil.inStr(params.getModel().getOrganizationIdType()) + ") ";
		}
		if (Strings.isNotBlank(params.getModel().getDataType())) {
			sql += " and data_type in (" + SysUtil.inStr(params.getModel().getDataType()) + ") ";
		}
		if (Strings.isNotBlank(params.getModel().getCustNo())) {
			sql += " and cust_no like '%" + params.getModel().getCustNo() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getCustType())) {
			sql += " and cust_type in (" + SysUtil.inStr(params.getModel().getCustType()) + ") ";
		}
		if (Strings.isNotBlank(params.getModel().getIdCode())) {
			sql += " and id_code like '%" + params.getModel().getIdCode() + "%' ";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addCustRegisterInfoh(SqlParam<CustRegisterInfoh> params) throws Exception {
		return super.update("INSERT INTO app_cust_register_info_h(bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no) VALUES($S{bankCode},$S{isBelong},$S{issBankName},$S{issBankCode},$S{inOutSign},$S{issCountry},$S{dataType},$S{oriCustNo},$S{custNo},$S{custType},$S{personalIdType},$S{organizationIdType},$S{otherIdName},$S{idCode},$S{spvOpenBank},$S{otherOpenBank},$S{custName},$S{sex},$S{riskLevel},$S{moble},$S{telPhone},$S{email},$S{remark},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{registerAcct},$S{registerCustNo})",
				params.getModel());
	}
	
	public UpdateResult updateCustRegisterInfoh(SqlParam<CustRegisterInfoh> params) throws Exception {
		return super.update("UPDATE app_cust_register_info_h SET bank_code=$S{bankCode} ,is_belong=$S{isBelong} ,iss_bank_name=$S{issBankName} ,iss_bank_code=$S{issBankCode} ,in_out_sign=$S{inOutSign} ,iss_country=$S{issCountry} ,data_type=$S{dataType} ,ori_cust_no=$S{oriCustNo} ,cust_no=$S{custNo} ,cust_type=$S{custType} ,personal_id_type=$S{personalIdType} ,organization_id_type=$S{organizationIdType} ,other_id_name=$S{otherIdName} ,id_code=$S{idCode} ,spv_open_bank=$S{spvOpenBank} ,other_open_bank=$S{otherOpenBank} ,cust_name=$S{custName} ,sex=$S{sex} ,risk_level=$S{riskLevel} ,moble=$S{moble} ,tel_phone=$S{telPhone} ,email=$S{email} ,remark=$S{remark} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,register_acct=$S{registerAcct} ,register_cust_no=$S{registerCustNo}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteCustRegisterInfoh(SqlParam<CustRegisterInfoh> params) throws Exception {
		return super.update("DELETE FROM app_cust_register_info_h WHERE ",
				params.getModel());
	}

}
