package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.BaseFiveTypeCmpInf;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class BaseFiveTypeCmpInfDao extends ComnDao {

	public SqlResult<BaseFiveTypeCmpInf> findBaseFiveTypeCmpInfs(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return super.findRows("SELECT id,report_date,cmp_nm,creditid,five_type,crt_dt FROM base_five_type_cmp_inf", params);
	}

	public UpdateResult addBaseFiveTypeCmpInf(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return super.update("INSERT INTO base_five_type_cmp_inf(id,report_date,cmp_nm,creditid,five_type,crt_dt) VALUES($AUTOIDI{id},$S{reportDate},$S{cmpNm},$S{creditid},$S{fiveType},$S{crtDt})",
				params.getModel());
	}
	
	public UpdateResult updateBaseFiveTypeCmpInf(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return super.update("UPDATE base_five_type_cmp_inf SET report_date=$S{reportDate} ,cmp_nm=$S{cmpNm} ,creditid=$S{creditid} ,five_type=$S{fiveType} ,crt_dt=$S{crtDt}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseFiveTypeCmpInf(SqlParam<BaseFiveTypeCmpInf> params) throws Exception {
		return super.update("DELETE FROM base_five_type_cmp_inf WHERE  id=$I{id} ",
				params.getModel());
	}

	public void importBaseFiveTypeCmpInf(List<BaseFiveTypeCmpInf> baseFiveTypeCmpInfs, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				super.update("DELETE FROM base_five_type_cmp_inf WHERE report_date=$S{reportDate} and five_type=$S{fiveType} ", params);
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("INSERT INTO base_five_type_cmp_inf(report_date,cmp_nm,five_type,crt_dt) VALUES(?,?,?,?)");
				for (BaseFiveTypeCmpInf baseFiveTypeCmpInf : baseFiveTypeCmpInfs) {
					preparedStatement.setString(1, (String) params.get("reportDate"));
					preparedStatement.setString(2, baseFiveTypeCmpInf.getCmpNm());
					preparedStatement.setString(3, (String) params.get("fiveType"));
					preparedStatement.setString(4, (String) params.get("crtDt"));
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
