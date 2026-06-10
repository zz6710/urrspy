package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ProdRegistRelationInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdRegistRelationInfoDao extends ComnDao {

	public SqlResult<ProdRegistRelationInfo> findProdRegistRelationInfos(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		String sql = "SELECT id,prod_code,reg_code,file_name,workdate,report_date,remark,crt_time,upd_time FROM app_prod_reg_relation where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and prod_code ='" + params.getModel().getProdCode() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegCode())) {
			sql = sql + " and  reg_code ='" + params.getModel().getRegCode() + "'";
		}
		sql = sql+" order by id desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addProdRegistRelationInfo(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		return super.update("INSERT INTO app_prod_reg_relation(prod_code,reg_code,workdate,remark) VALUES($AUTOIDS{prodCode},$S{regCode},$S{workdate},$S{remark})",
				params.getModel());
	}
	
	public UpdateResult updateProdRegistRelationInfo(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		return super.update("UPDATE app_prod_reg_relation SET reg_code=$S{regCode} ,workdate=$S{workdate} ,remark=$S{remark}  WHERE  prod_code=$S{prodCode} ",
				params.getModel());
	}
	
	public UpdateResult deleteProdRegistRelationInfo(SqlParam<ProdRegistRelationInfo> params) throws Exception {
		return super.update("DELETE FROM app_prod_reg_relation WHERE  prod_code=$S{prodCode} ",
				params.getModel());
	}

}
