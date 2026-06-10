package com.kayak.dps.valtabimp.repository;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValFunctionFactory {
	
	@Autowired
	private ComnDao comnDao;
	
	
	
	/**
	 * 获取债券贴现债的久期
	 *
	 * @return
	 */
	public String GetBondcode(String bond_name ) {
		String bond_code ="";

		String sql = "select bond_code from t8_bond_info "
				+ "where bond_name=$S{bond_name} ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bond_name", bond_name);

		try {
			List<SqlRow> sqlRows = comnDao.findRows(sql, params);
			for (SqlRow rs : sqlRows) {
				bond_code = rs.getString("bond_code");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return bond_code;
	}


	/**
	 * 获取债券贴现债的久期
	 *
	 * @return
	 */
	public String GetBondmarket(String bond_name ) {
		String market_code ="";

		String sql = "select market_code from  "
				+ "where bond_name=$S{bond_name} ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bond_name", bond_name);

		try {
			List<SqlRow> sqlRows = comnDao.findRows(sql, params);
			for (SqlRow rs : sqlRows) {
				market_code = rs.getString("market_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return market_code;
	}
	
	
	
	/**
	 * 获取非标的资产品�?
	 *
	 * @return
	 */
	public String GetAssetAdtype(String ftool_code ) {
		String t8_sys_adtype_id ="";

		String sql = "select t.t8_sys_adtype_id from T8_ASSDEPT_INFO t where t.ftool_code=$S{ftool_code}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ftool_code", ftool_code);

		try {
			List<SqlRow> sqlRows = comnDao.findRows(sql, params);
			for (SqlRow rs : sqlRows) {
				t8_sys_adtype_id = rs.getString("t8_sys_adtype_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t8_sys_adtype_id;
	}
	
	/**
	 * 获取产品ID
	 *
	 * @return
	 */
	public String GetProdbaseid(String prod_code ) {
		String t8_prod_base_id ="";

		String sql = "select t.id from ods_amng_prod_base t where t.prod_code=$S{prod_code}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prod_code", prod_code);

		try {
			List<SqlRow> sqlRows = comnDao.findRows(sql, params);
			for (SqlRow rs : sqlRows) {
				t8_prod_base_id = rs.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t8_prod_base_id;
	}
	
	/**
	 * 获取投组ID
	 *
	 * @return
	 */
	public String GetPortfolid(String port_code ) {
		String t8_id ="";

		String sql = "select t.id from T8_SYS_PORTFOLIO t where t.port_code=$S{port_code}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("port_code", port_code);

		try {
			List<SqlRow> sqlRows = comnDao.findRows(sql, params);
			for (SqlRow rs : sqlRows) {
				t8_id = rs.getString("id");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return t8_id;
	}
}
