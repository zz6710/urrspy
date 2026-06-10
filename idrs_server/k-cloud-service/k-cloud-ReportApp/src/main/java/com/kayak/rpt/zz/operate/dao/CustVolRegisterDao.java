package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.CustVolRegister;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class CustVolRegisterDao extends ComnDao {

	public SqlResult<CustVolRegister> findCustVolRegisters(SqlParam<CustVolRegister> params) throws Exception {
		String sql = "SELECT SUMMIT_USER, CREATE_DATE, CREATE_TIME, BANK_CODE, PROD_CODE, CUST_NO, HOLD_DATE, CUR, HOLD_VOL, HOLD_AMT, CONVERT_RMB, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, REGISTER_SERNO, ID, OP_TYPE FROM app_cust_vol_register_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		if (Strings.isNotBlank(params.getModel().getHoldStartDate())) {
			sql += " and DATE(hold_date) >= DATE($S{holdStartDate}) and DATE(hold_date) <= DATE($S{holdEndDate})";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addCustVolRegister(CustVolRegister custVolRegister) throws Exception {
		return super.update("INSERT INTO app_cust_vol_register_remark(summit_user,create_date,create_time,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,op_type,report_date) VALUES($S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$D{convertRmb},$S{impDate},$S{registerDate},$S{registerStatus},$S{registerSerno},$S{opType},$S{reportDate})",
				DataSourceProperty.PUB,custVolRegister);
	}
	
	public UpdateResult updateCustVolRegister(SqlParam<CustVolRegister> params) throws Exception {
		return super.update("UPDATE app_cust_vol_register_remark SET summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,cust_no=$S{custNo} ,hold_date=$S{holdDate} ,cur=$S{cur} ,hold_vol=$S{holdVol} ,hold_amt=$S{holdAmt} ,convert_rmb=$D{convertRmb} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,register_serno=$S{registerSerno} ,id=$S{id} ,op_type=$S{opType}  WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteCustVolRegister(SqlParam<CustVolRegister> params) throws Exception {
		return super.update("DELETE FROM app_cust_vol_register_remark WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportCustVolRegister(CustVolRegister custVolRegister) throws Exception {
		return super.update("INSERT INTO app_cust_vol_register_remark(summit_user,create_date,create_time,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,op_type,report_date) VALUES($S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$D{convertRmb},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$S{registerSerno},$S{opType},$S{reportDate})",
				DataSourceProperty.PUB,custVolRegister);
	}
}
