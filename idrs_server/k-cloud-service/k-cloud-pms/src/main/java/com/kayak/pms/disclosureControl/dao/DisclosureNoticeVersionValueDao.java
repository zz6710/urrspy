package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeProcess;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersionValue;
import com.kayak.pms.global.constants.IsSysvalue;
import org.springframework.stereotype.Repository;

@Repository
public class DisclosureNoticeVersionValueDao extends ComnDao {

	public SqlResult<DisclosureNoticeVersionValue> findDisclosureNoticeVersionValues(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return super.findRows("SELECT id,t8_disclosure_notice_id,t8_disclosure_version_id,t8_disclosure_notice_version_id,prod_code,data_date,column_key,column_value,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark FROM idb_disclosure_notice_version_value", params);
	}

	public UpdateResult addDisclosureNoticeVersionValue(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_version_value(id,t8_disclosure_notice_id,t8_disclosure_version_id,t8_disclosure_notice_version_id,prod_code,data_date,column_key,column_value,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark) VALUES($AUTOIDS{id},$S{t8DisclosureNoticeId},$S{disclosureModVersionId},$S{t8DisclosureNoticeVersionId},$S{prodCode},$S{dataDate},$S{columnKey},$S{columnValue},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{remark})",
				params.getModel());
	}
	
	public UpdateResult updateDisclosureNoticeVersionValue(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_version_value SET t8_disclosure_notice_id=$S{t8DisclosureNoticeId} ,t8_disclosure_version_id=$S{t8DisclosureVersionId} ,t8_disclosure_notice_version_id=$S{t8DisclosureNoticeVersionId} ,prod_code=$S{prodCode} ,data_date=$S{dataDate} ,column_key=$S{columnKey} ,column_value=$S{columnValue} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ,remark=$S{remark}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDisclosureNoticeVersionValue(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice_version_value WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：根据公告id查询存在未补录完成数据的数量
	 * 作者：rennannan
	 * 日期：20210926
	 * @param
	 * @return
	 * @throws Exception
	 */
	public SqlResult<DisclosureNoticeVersionValue> findNotFinishProcessList(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		String sql = " select id from  idb_disclosure_notice_value " +
				" where t8_disclosure_notice_id = $S{t8DisclosureNoticeId} AND t8_disclosure_version_id = $S{t8DisclosureVersionId}" +
				" AND is_sysvalue = '"+ IsSysvalue.hand.getItemKey() +"' AND (column_value = '' OR column_value IS NULL) ";
		return super.findRows(sql, DataSourceProperty.IDB, params);
	}

}
