package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.BaseCfetsBondInf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class BaseCfetsBondInfDao extends ComnDao {

	public SqlResult<BaseCfetsBondInf> findBaseCfetsBondInfs(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return super.findRows("SELECT id,report_date,index_nm,bond_code,bond_name,index_weight,crt_dt FROM base_cfets_bond_inf", params);
	}

	public UpdateResult addBaseCfetsBondInf(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return super.update("INSERT INTO base_cfets_bond_inf(id,report_date,index_nm,bond_code,bond_name,index_weight,crt_dt) VALUES($AUTOIDI{id},$S{reportDate},$S{indexNm},$S{bondCode},$S{bondName},$D{indexWeight},$S{crtDt})",
				params.getModel());
	}
	
	public UpdateResult updateBaseCfetsBondInf(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return super.update("UPDATE base_cfets_bond_inf SET report_date=$S{reportDate} ,index_nm=$S{indexNm} ,bond_code=$S{bondCode} ,bond_name=$S{bondName} ,index_weight=$D{indexWeight} ,crt_dt=$S{crtDt}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseCfetsBondInf(SqlParam<BaseCfetsBondInf> params) throws Exception {
		return super.update("DELETE FROM base_cfets_bond_inf WHERE  id=$I{id} ",
				params.getModel());
	}

	public void importBaseCfetsBondInf(List<BaseCfetsBondInf> baseCfetsBondInfs, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				super.update("DELETE FROM base_cfets_bond_inf WHERE report_date=$S{reportDate} ", params);
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("INSERT INTO base_cfets_bond_inf(report_date,index_nm,bond_code,bond_name,index_weight,crt_dt) VALUES(?,?,?,?,?,?)");
				for (BaseCfetsBondInf baseCfetsBondInf : baseCfetsBondInfs) {
					preparedStatement.setString(1, (String) params.get("reportDate"));
					preparedStatement.setString(2, baseCfetsBondInf.getIndexNm());
					preparedStatement.setString(3, baseCfetsBondInf.getBondCode());
					preparedStatement.setString(4, baseCfetsBondInf.getBondName());
					String indexWeight = baseCfetsBondInf.getIndexWeight();
					preparedStatement.setBigDecimal(5, StringUtils.isEmpty(indexWeight) ? null : new BigDecimal(indexWeight));
					preparedStatement.setString(6, (String) params.get("crtDt"));
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} catch (Exception e) {
				throw e;
			} finally {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		});
	}

}
