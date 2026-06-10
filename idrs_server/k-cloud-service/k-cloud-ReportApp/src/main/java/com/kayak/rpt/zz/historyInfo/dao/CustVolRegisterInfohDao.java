package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.historyInfo.model.CustVolRegisterInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CustVolRegisterInfohDao extends ComnDao {

	public SqlResult<CustVolRegisterInfoh> findCustVolRegisterInfohs(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		String sql = "SELECT id, bank_code, prod_code, cust_no, hold_date, cur, hold_vol, hold_amt, convert_rmb, imp_date, register_date, register_status, " +
				"            register_serno, create_date, theory_report_start_date, theory_report_end_date, report_date " +
				"       FROM app_cust_vol_register_info_h " +
				"      where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getReportDate())) {
			sql += " and hold_date = $S{reportDate} ";
		}
		if (Strings.isNotBlank(params.getModel().getProdCode())) {
			sql += " and prod_code like '%" + params.getModel().getProdCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getCustNo())) {
			sql += " and cust_no like '%" + params.getModel().getCustNo() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getCur())) {
			sql += " and cur in (" + SysUtil.inStr(params.getModel().getCur()) + ") ";
		}
		return super.findRows(sql, params);
	}

	/**
	 * 查询投资者持有(母产品)合并前后数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<CustVolRegisterInfoh> findCustVolRegisterRemark(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		String sql = "SELECT id, bank_code, prod_code, cust_no, hold_date, cur, hold_vol, hold_amt, convert_rmb, imp_date, register_date, register_status, " +
				"            register_serno, create_date, theory_report_start_date, theory_report_end_date, report_date " +
				"       FROM app_cust_vol_register_info_record " +
				"      where order_id = '" + params.getParamsDirect().get("order_id") + "' and MRG_TYP = '" + params.getParamsDirect().get("mrg_typ") + "' ";
		sql +=  " order by hold_date desc, cust_no, prod_code ";
		return super.findRows(sql, params);
	}

	public UpdateResult addCustVolRegisterInfoh(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return super.update("INSERT INTO app_cust_vol_register_info_h(bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id) VALUES($S{bankCode},$S{prodCode},$S{custNo},$S{holdDate},$S{cur},$S{holdVol},$S{holdAmt},$S{convertRmb},$S{impDate},$S{registerDate},$S{registerStatus},$S{registerSerno},$S{id})",
				params.getModel());
	}
	
	public UpdateResult updateCustVolRegisterInfoh(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return super.update("UPDATE app_cust_vol_register_info_h " +
						"  SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,cust_no=$S{custNo} ,hold_date=$S{holdDate} ," +
						"      cur=$S{cur} ,hold_vol=$S{holdVol} ,hold_amt=$S{holdAmt} ,convert_rmb=$S{convertRmb} ," +
						"      imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,register_serno=$S{registerSerno}," +
						"      report_date = $S{reportDate} " +
						"WHERE id = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteCustVolRegisterInfoh(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return super.update("DELETE FROM app_cust_vol_register_info_h WHERE ",
				params.getModel());
	}

}
