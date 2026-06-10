package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsZyShcommonCust;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsZyShcommonCustDao extends ComnDao {

	public SqlResult<DwsZyShcommonCust> findDwsZyShcommonCusts(SqlParam<DwsZyShcommonCust> params) throws Exception {
		String sql = "SELECT id,cust_name,registernumber,s_info_org_code,s_info_oth_code,s_info_oth_type,cust_number,ne_ind_code,ne_ind_type,s_relevance,act_dt,deal_date FROM dws_zy_shcommon_cust where 1=1 ";
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
			sql += " and act_dt like '" + params.getModel().getActDt() + "%'";
		}
		if (Tools.isNotEmpty(params.getModel().getCustName())) {
			sql += " and cust_name like '%" + params.getModel().getCustName() + "%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addDwsZyShcommonCust(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return super.update("INSERT INTO dws_zy_shcommon_cust(id,cust_name,registernumber,s_info_org_code,s_info_oth_code,s_info_oth_type,cust_number,ne_ind_code,ne_ind_type,s_relevance,act_dt,deal_date) VALUES($AUTOIDI{id},$S{custName},$S{registernumber},$S{sInfoOrgCode},$S{sInfoOthCode},$S{sInfoOthType},$S{custNumber},$S{neIndCode},$S{neIndType},$S{sRelevance},$S{actDt},$S{dealDate})",
				params.getModel());
	}

	public UpdateResult updateDwsZyShcommonCust(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return super.update("UPDATE dws_zy_shcommon_cust SET cust_name=$S{custName} ,registernumber=$S{registernumber} ,s_info_org_code=$S{sInfoOrgCode} ,s_info_oth_code=$S{sInfoOthCode} ,s_info_oth_type=$S{sInfoOthType} ,cust_number=$S{custNumber} ,ne_ind_code=$S{neIndCode} ,ne_ind_type=$S{neIndType} ,s_relevance=$S{sRelevance} ,act_dt=$S{actDt} ,deal_date=$S{dealDate}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsZyShcommonCust(SqlParam<DwsZyShcommonCust> params) throws Exception {
		return super.update("DELETE FROM dws_zy_shcommon_cust WHERE  id=$I{id} ",
				params.getModel());
	}

}