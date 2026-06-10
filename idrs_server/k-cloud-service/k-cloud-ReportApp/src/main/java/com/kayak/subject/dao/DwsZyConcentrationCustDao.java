package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsZyConcentrationCust;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsZyConcentrationCustDao extends ComnDao {

	public SqlResult<DwsZyConcentrationCust> findDwsZyConcentrationCusts(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		String sql = "SELECT id,xh,case when s_type not in ('0606','0607') then cust_name else null end cust_name,s_type as stype,act_dt,deal_date,case when s_type='0606' then cust_name else null end indu_name ,case when s_type='0607' then cust_name else null end region_name FROM dws_zy_concentration_cust where 1=1 ";
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
			sql += " and act_dt like '" + params.getModel().getActDt() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getStype())) {
			sql += " and s_type like '%" + params.getModel().getStype() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getCustName())) {
			sql += " and s_type not in ('0606','0607') and cust_name like '%" + params.getModel().getCustName() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getInduName())) {
			sql += " and s_type = '0606' and cust_name like '%" + params.getModel().getInduName() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getRegionName())) {
			sql += " and s_type = '0607' and cust_name like '%" + params.getModel().getRegionName() + "%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addDwsZyConcentrationCust(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return super.update("INSERT INTO dws_zy_concentration_cust(id,xh,cust_name,s_type,act_dt,deal_date,indu_name,region_name) VALUES($AUTOIDI{id},$I{xh},$S{custName},$S{sType},$S{actDt},$S{dealDate},$S{induName},$S{regionName})",
				params.getModel());
	}

	public UpdateResult updateDwsZyConcentrationCust(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return super.update("UPDATE dws_zy_concentration_cust SET xh=$I{xh} ,cust_name=$S{custName} ,s_type=$S{sType} ,act_dt=$S{actDt} ,deal_date=$S{dealDate} ,indu_name=$S{induName} ,region_name=$S{regionName}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsZyConcentrationCust(SqlParam<DwsZyConcentrationCust> params) throws Exception {
		return super.update("DELETE FROM dws_zy_concentration_cust WHERE  id=$I{id} ",
				params.getModel());
	}

}
