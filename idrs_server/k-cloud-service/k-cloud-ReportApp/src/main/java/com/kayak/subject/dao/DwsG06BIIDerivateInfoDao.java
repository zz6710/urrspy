package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsG06BIIDerivateInfo;
import org.springframework.stereotype.Repository;


@Repository
public class DwsG06BIIDerivateInfoDao extends ComnDao {

	public SqlResult<DwsG06BIIDerivateInfo> findDwsG06BIIDerivateInfos(SqlParam<DwsG06BIIDerivateInfo> params) throws Exception {
	    String sql = "select id, i_code as icode, i_name_c1 as inamec1, asset_code, g06_type, inv_manager, der_bus_typ, if_stand_der, data_info, net_value, coef, inv_value, report_date \n" +
				"       from dws_g06b_der_info gd \n" +
				"      where 1 = 1 ";
		if (Tools.isNotEmpty(params.getModel().getReportDate())) {
			sql += " and report_date like '" + params.getModel().getReportDate() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getIcode())) {
			sql += " and i_code like '%" + params.getModel().getIcode() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getAssetCode())) {
			sql += " and asset_code like '%" + params.getModel().getAssetCode() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getG06Type())) {
			sql += " and g06_type = '" + params.getModel().getG06Type() + "'";
		}
		sql += " order by report_date desc";
        return super.findRows(sql, params);
	}

	public UpdateResult updateDwsG06BIIDerivateInfo(SqlParam<DwsG06BIIDerivateInfo> params) throws Exception {
		return super.update("update dws_g06b_der_info \n" +
						"           set i_code = $S{icode}, \n" +
						"               i_name_c1 = $S{inamec1}, \n" +
						"               asset_code = $S{assetCode}, \n" +
						"               g06_type = $S{g06Type}, \n" +
						"               inv_manager = $S{invManager}, \n" +
						"               der_bus_typ = $S{derBusTyp}, \n" +
						"               if_stand_der = $S{ifStandDer}, \n" +
						"               data_info = $S{dataInfo}, \n" +
						"               net_value = $D{netValue}, \n" +
						"               coef = $S{coef}, \n" +
						"               inv_value = $D{invValue} \n" +
						"         where id = $S{id}",
				params.getModel());
	}
	
	public UpdateResult deleteDwsG06BIIDerivateInfo(SqlParam<DwsG06BIIDerivateInfo> params) throws Exception {
		return super.update("DELETE FROM dws_g06b_der_info WHERE id = $S{id}",
				params.getModel());
	}


}
