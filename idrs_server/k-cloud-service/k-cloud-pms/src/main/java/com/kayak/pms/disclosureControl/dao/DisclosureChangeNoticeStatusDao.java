package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.DisclosureChangeNoticeStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DisclosureChangeNoticeStatusDao extends ComnDao {

	public SqlResult<DisclosureChangeNoticeStatus> findDisclosureChangeNoticeStatus(SqlParam<DisclosureChangeNoticeStatus> params) throws Exception {
		String sql = "SELECT  tdn.disclosure_type, tdn.disclosure_son_type, tdn.prod_code, pb.prod_nm prod_name, tdnv.id, tdnv.t8_disclosure_notice_id, tdnv.disclosure_status_ahead, tdnv.disclosure_status_after, tdnv.change_reason, tdnv.crt_date, tdnv.crt_time, tdnv.crt_user_id,tdnv.crt_user_name,dnv.notice_version,tdn.notice_title  FROM idb_disclosure_notice_status_record tdnv  LEFT JOIN idb_disclosure_notice tdn  ON tdnv.t8_disclosure_notice_id = tdn.id  LEFT JOIN idb_disclosure_notice_version dnv on  tdnv.notice_version_id = dnv.id  LEFT JOIN APP_PRD_BAS_INF pb  ON pb.prod_cd = tdn.prod_code WHERE 1 = 1  " ;

		if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
			sql = sql + " and tdn.disclosure_type = " + params.getModel().getDisclosureType() + " ";
		}
		if(Tools.isNotEmpty(params.getModel().getDisclosureSonType())){
			sql = sql + " and tdn.disclosure_son_type = " + params.getModel().getDisclosureSonType() + " ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
			sql = sql + " and tdn.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if(Tools.isNotEmpty(params.getModel().getProdName())){
			sql = sql + " and tdn.prod_name like '%" + params.getModel().getProdName() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql = sql + " and  tdnv.crt_date >= '" + params.getModel().getBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql = sql + " and  tdnv.crt_date <= '" + params.getModel().getQueryDate() + "'";
		}
		sql= sql+" ORDER BY prod_code, tdnv.id desc ";
		return super.findRows(sql, DataSourceProperty.IDB, params);
	}

}
