package com.kayak.rpt.zz.manage.dao;



import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ZonClcInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ZonClcInfoDao extends ComnDao {

	public SqlResult<ZonClcInfo> findZonClcInfos(SqlParam<ZonClcInfo> params) throws Exception {
		return super.findRows("SELECT prod_code,zon_clc,zon_clc_amt FROM app_zon_clc_info",DataSourceProperty.PUB, params);
	}

	public UpdateResult addZonClcInfo(SqlParam<ZonClcInfo> params) throws Exception {
		return super.update("INSERT INTO app_zon_clc_info(prod_code,zon_clc,zon_clc_amt) VALUES($S{prodCode},$S{zonClc},$D{zonClcAmt})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateZonClcInfo(SqlParam<ZonClcInfo> params) throws Exception {
		return super.update("UPDATE app_zon_clc_info SET  zon_clc_amt=$D{zonClcAmt}  WHERE prod_code=$S{prodCode} and zon_clc=$S{zonClc}",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteZonClcInfo(SqlParam<ZonClcInfo> params) throws Exception {
		return super.update("DELETE FROM app_zon_clc_info WHERE prod_code=$S{prodCode} and zon_clc=$S{zonClc}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportZonClcInfo(ZonClcInfo z) throws Exception {
		return super.update("INSERT INTO app_zon_clc_info(prod_code,zon_clc,zon_clc_amt) VALUES($S{prodCode},$S{zonClc},$D{zonClcAmt})",
				DataSourceProperty.PUB,z);
	}
}
