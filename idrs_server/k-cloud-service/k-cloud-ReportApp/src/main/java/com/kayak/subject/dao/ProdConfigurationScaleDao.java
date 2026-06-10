package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.ProdConfigurationScale;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdConfigurationScaleDao extends ComnDao {

	public SqlResult<ProdConfigurationScale> findProdConfigurationScales(SqlParam<ProdConfigurationScale> params) throws Exception {
		String sql = "SELECT id,i_code as icode,investmonamount,investownamount,investcountamount,concat(investmonrate) as investmonrate,concat(investownrate) as investownrate,concat(investcountrate) as investcountrate,act_dt,deal_date FROM dws_prod_configuration_scale where 1=1 ";
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
			sql += " and act_dt like '" + params.getModel().getActDt() +"%'";
		}
		if (Tools.isNotEmpty(params.getModel().getIcode())) {
			sql += " and i_code like '%" + params.getModel().getIcode() +"%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addProdConfigurationScale(SqlParam<ProdConfigurationScale> params) throws Exception {
		return super.update("INSERT INTO dws_prod_configuration_scale(id,i_code,investmonamount,investownamount,investcountamount,investmonrate,investownrate,investcountrate,act_dt,deal_date) VALUES($AUTOIDI{id},$S{iCode},$D{investmonamount},$D{investownamount},$D{investcountamount},$D{investmonrate},$D{investownrate},$D{investcountrate},$S{actDt},$S{dealDate})",
				params.getModel());
	}
	
	public UpdateResult updateProdConfigurationScale(SqlParam<ProdConfigurationScale> params) throws Exception {
		return super.update("UPDATE dws_prod_configuration_scale SET i_code=$S{iCode} ,investmonamount=$D{investmonamount} ,investownamount=$D{investownamount} ,investcountamount=$D{investcountamount} ,investmonrate=$D{investmonrate} ,investownrate=$D{investownrate} ,investcountrate=$D{investcountrate} ,act_dt=$S{actDt} ,deal_date=$S{dealDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteProdConfigurationScale(SqlParam<ProdConfigurationScale> params) throws Exception {
		return super.update("DELETE FROM dws_prod_configuration_scale WHERE  id=$I{id} ",
				params.getModel());
	}

}
