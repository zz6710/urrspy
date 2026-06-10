package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.DataSupplementModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DataSupplementDao extends ComnDao {

	public SqlResult<DataSupplementModel> findDataSupplements(SqlParam<DataSupplementModel> params) throws Exception {
		String sql=" SELECT id,report_date,ROUND(balance_assets, 2) balance_assets,ROUND(financial_assets, 2) financial_assets,remark,crt_date,crt_time,upd_date,upd_time " +
				" FROM dwd_data_supplement " +
				" where 1=1 " ;
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql += " and report_date = DATE($S{reportDate})";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addDataSupplement(SqlParam<DataSupplementModel> params) throws Exception {
		return super.update("INSERT INTO dwd_data_supplement(report_date,balance_assets,financial_assets,remark,crt_date,crt_time) VALUES($S{reportDate},$S{balanceAssets},$S{financialAssets},$S{remark},$S{crtDate},$S{crtTime})",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateDataSupplement(SqlParam<DataSupplementModel> params) throws Exception {
		return super.update("UPDATE dwd_data_supplement SET report_date=$S{reportDate} ,balance_assets=$S{balanceAssets} ,financial_assets=$S{financialAssets},remark=$S{remark},upd_date=$S{updDate},upd_time=$S{updTime}  WHERE  id=$S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteDataSupplement(SqlParam<DataSupplementModel> params) throws Exception {
		return super.update("DELETE FROM dwd_data_supplement WHERE  id=$S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public SqlResult<DataSupplementModel> findOnlyDataSupplements(SqlParam<DataSupplementModel> params) throws Exception {
		DataSupplementModel o = params.getModel();
		String sql = "select * from  dwd_data_supplement  where 1 = 1 and report_date = DATE($S{reportDate}) ";
		if (StringUtils.isNotBlank(o.getId())){
			sql += " and id != $S{id}";
		}
		return super.findRows(sql, params);
	}
}
